import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;


public interface ScoreCommand extends Jsonable {

    @Immutable
    @JsonDeserialize
    final class CreateTeam implements ScoreCommand, PersistentEntity.ReplyType<Done>, CompressedJsonable {
        public final String coachName;
        public final int no_Of_ODI;
        public final int no_Of_Wins;

        @JsonCreator
        public CreateTeam(String coachName, int no_Of_ODI, int no_Of_Wins) {
            Preconditions.checkArgument(coachName != null && !coachName.equals(""), "Give a proper name for team coach.");
            this.coachName = coachName;
            this.no_Of_ODI = no_Of_ODI;
            this.no_Of_Wins = no_Of_Wins;
        }
    }

    @Immutable
    final class GetWinPercent implements ScoreCommand, PersistentEntity.ReplyType<String>, CompressedJsonable {

    }

    @Immutable
    @JsonDeserialize
    final class UpdateCoach implements ScoreCommand, PersistentEntity.ReplyType<String>, CompressedJsonable {
        public final String coachName;

        @JsonCreator
        public UpdateCoach(String coachName) {
            Preconditions.checkArgument(coachName != null && !coachName.equals(""), "Give a proper name for team coach.");
            this.coachName = coachName;
        }
    }
}
