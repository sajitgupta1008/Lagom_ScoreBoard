import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;

public interface ScoreEvent extends Jsonable, AggregateEvent<ScoreEvent> {

    public AggregateEventTag<ScoreEvent> TAG = AggregateEventTag.of(ScoreEvent.class);

    @Override
    default AggregateEventTagger<ScoreEvent> aggregateTag() {
        return TAG;
    }

    @Immutable
    @JsonDeserialize
    final class NewTeamCreated implements ScoreEvent {
        public final String teamName;
        public final String coachName;
        public final int no_Of_ODI;
        public final int no_Of_Wins;

        @JsonCreator
        public NewTeamCreated(String teamName, String coachName, int no_Of_ODI, int no_Of_Wins) {
            this.teamName = Preconditions.checkNotNull(teamName, "team name cannot be null");
            this.coachName = Preconditions.checkNotNull(coachName, "coach name cannot be null");
            this.no_Of_ODI = no_Of_ODI;
            this.no_Of_Wins = no_Of_Wins;
        }
    }

    @Immutable
    @JsonDeserialize
    final class CoachUpdated implements ScoreEvent {
        public final String coachName;
        public final String teamName;


        @JsonCreator
        public CoachUpdated(String teamName,String coachName) {
            this.teamName = Preconditions.checkNotNull(teamName, "team name cannot be null");
            this.coachName = Preconditions.checkNotNull(coachName, "coach name cannot be null");
        }
    }

}
