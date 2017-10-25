import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class ScoreEntity extends PersistentEntity<ScoreCommand, ScoreEvent, ScoreState> {
    @Override
    public Behavior initialBehavior(Optional<ScoreState> snapshotState) {
        BehaviorBuilder builder = newBehaviorBuilder(snapshotState.orElse(new ScoreState("", 0, 0, "")));

        builder.setCommandHandler(ScoreCommand.CreateTeam.class, (cmd, ctx) -> {
            return ctx.thenPersist(new ScoreEvent.NewTeamCreated(entityId(), cmd.coachName, cmd.no_Of_ODI, cmd.no_Of_Wins),
                    evt -> ctx.reply(Done.getInstance()));
        });

        builder.setEventHandler(ScoreEvent.NewTeamCreated.class,
                evt -> new ScoreState(evt.coachName, evt.no_Of_ODI, evt.no_Of_Wins,
                        String.valueOf(evt.no_Of_Wins * 100.0 / evt.no_Of_ODI)));

        builder.setReadOnlyCommandHandler(ScoreCommand.GetWinPercent.class, (evt, ctx) -> {
            ctx.reply("Team : " + entityId() + "\nWin percentage : " + state().winPercentage);
        });

        builder.setCommandHandler(ScoreCommand.UpdateCoach.class, (cmd, ctx) -> {
            return ctx.thenPersist(new ScoreEvent.CoachUpdated(entityId(), cmd.coachName),
                    evt -> ctx.reply(String.format("Team %s coach updated successfully.", entityId())));
        });

        builder.setEventHandler(ScoreEvent.CoachUpdated.class, evt -> new ScoreState(evt.coachName, state().no_Of_ODI, state().no_Of_Wins,
                state().winPercentage));

        return builder.build();
    }
}
