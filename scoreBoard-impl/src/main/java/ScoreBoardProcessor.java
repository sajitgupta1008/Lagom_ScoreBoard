import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ScoreBoardProcessor extends ReadSideProcessor<ScoreEvent> {

    private final CassandraReadSide cassandraReadSide;
    private final CassandraSession cassandraSession;
    private PreparedStatement create_table;


    @Inject
    public ScoreBoardProcessor(CassandraReadSide cassandraReadSide, CassandraSession cassandraSession) {
        this.cassandraReadSide = cassandraReadSide;
        this.cassandraSession = cassandraSession;
    }

    @Override
    public ReadSideHandler<ScoreEvent> buildHandler() {
        CassandraReadSide.ReadSideHandlerBuilder<ScoreEvent> builder = cassandraReadSide.<ScoreEvent>builder("scoreboard");
        return builder.setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareCreateStatement())
                .setEventHandler(ScoreEvent.NewTeamCreated.class, this::handleCreateEvent)
                .setEventHandler(ScoreEvent.CoachUpdated.class, this::handleUpdateCoach)
                .build();
    }

    private CompletionStage<List<BoundStatement>> handleUpdateCoach(ScoreEvent.CoachUpdated evt) {
        return cassandraSession.prepare("UPDATE score.scoreboard SET coach=? WHERE team=?;")
                .thenCompose(ps -> {
                    BoundStatement boundStatement = ps.bind().setString(0, evt.coachName).setString(1, evt.teamName);
                    return CassandraReadSide.completedStatements(Arrays.asList(boundStatement));
                });
    }

    private CompletionStage<List<BoundStatement>> handleCreateEvent(ScoreEvent.NewTeamCreated evt) {
        BoundStatement boundStatement = create_table.bind()
                .setString("team", evt.teamName)
                .setString("coach", evt.coachName)
                .setInt("odi", evt.no_Of_ODI)
                .setInt("wins", evt.no_Of_Wins);
        return CassandraReadSide.completedStatements(Arrays.asList(boundStatement));
    }


    private CompletionStage<Done> prepareCreateStatement() {
        return cassandraSession.prepare("INSERT INTO scoreboard (team,coach,odi,wins) VALUES(?,?,?,?);")
                .thenApply(preparedStatement -> {
                    create_table = preparedStatement;
                    return Done.getInstance();
                });
    }


    private CompletionStage<Done> createTable() {
        return cassandraSession.executeCreateTable("CREATE TABLE IF NOT EXISTS scoreboard (team TEXT, coach TEXT, " +
                "odi int, wins int, PRIMARY KEY(team));");
    }

    @Override
    public PSequence<AggregateEventTag<ScoreEvent>> aggregateTags() {
        return TreePVector.singleton(ScoreEvent.TAG);
    }
}
