import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
@JsonDeserialize
public final class Team {

    public final String coachName;
    public final int no_Of_ODI;
    public final int no_Of_Wins;

    @JsonCreator
    public Team(String coachName, int no_Of_ODI, int no_Of_Wins) {
        this.coachName = Preconditions.checkNotNull(coachName, "coachName shouldn't be null");
        this.no_Of_ODI = no_Of_ODI;
        this.no_Of_Wins = no_Of_Wins;
    }

}
