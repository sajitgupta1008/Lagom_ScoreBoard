import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public final class ScoreState implements CompressedJsonable {

    public final String coachName;
    public final int no_Of_ODI;
    public final int no_Of_Wins;
    public final String winPercentage;

    @JsonCreator
    public ScoreState(String coachName, int no_Of_ODI, int no_Of_Wins, String winPercentage) {
        this.coachName = Preconditions.checkNotNull(coachName, "coach name cannot be null");
        this.no_Of_ODI = no_Of_ODI;
        this.no_Of_Wins = no_Of_Wins;
        this.winPercentage = Preconditions.checkNotNull(winPercentage, "Win percentage cannot be null.");
    }
}
