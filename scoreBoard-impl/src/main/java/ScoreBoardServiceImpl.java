import akka.Done;
import akka.NotUsed;
import com.datastax.driver.core.Row;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;


public class ScoreBoardServiceImpl implements ScoreBoardService {

    private PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    @Inject
    public ScoreBoardServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(ScoreEntity.class);
        this.session = session;
        readSide.register(ScoreBoardProcessor.class);
    }


    @Override
    public ServiceCall<Team, Done> newTeam(String teamName) {
        return request -> {
            PersistentEntityRef<ScoreCommand> ref = persistentEntityRegistry.refFor(ScoreEntity.class, teamName);
            return ref.ask(new ScoreCommand.CreateTeam(request.coachName, request.no_Of_ODI, request.no_Of_Wins));
        };
    }

    @Override
    public ServiceCall<NotUsed, String> getWinPercentage(String teamName) {
        return request -> session.selectAll(String.format("select * from score.scoreboard where team='%s';", teamName))
                .thenCompose(list -> {
                    if (list.isEmpty())
                        return CompletableFuture.completedFuture(String.format("There is no team in our database with name %s.", teamName));
                    else {
                        return persistentEntityRegistry.refFor(ScoreEntity.class, teamName).ask(new ScoreCommand.GetWinPercent());
                    }
                });
    }

    @Override
    public ServiceCall<NotUsed, String> getTeamInfo(String teamName) {
        return request ->
                session.selectAll(String.format("select * from score.scoreboard where team='%s';", teamName))
                        .thenApply(list -> {
                            if (!list.isEmpty()) {
                                Row row = list.get(0);
                                return String.format("Team : %s\nCoach : %s\nODI : %d\nWins : %s", row.getString("team"),
                                        row.getString("coach"), row.getInt("odi"), row.getInt("wins"));
                            }
                            return String.format("There is no team in our database with name %s.", teamName);
                        });
    }

    @Override
    public ServiceCall<Coach, String> updateCoachName(String teamName) {

        return request -> session.selectAll(String.format("select * from score.scoreboard where team='%s';", teamName))
                .thenCompose(list -> {
                    if (list.isEmpty())
                        return CompletableFuture.completedFuture(String.format("There is no team with %s as name.", teamName));
                    return persistentEntityRegistry.refFor(ScoreEntity.class, teamName).ask(new ScoreCommand.UpdateCoach(request.coachName));
                });

    }


}
