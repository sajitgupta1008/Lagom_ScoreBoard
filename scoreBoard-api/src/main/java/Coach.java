import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public final class Coach {
    public final String coachName;

    @JsonCreator
    public Coach(String coachName) {
        this.coachName = Preconditions.checkNotNull(coachName, "coach name cannot be null");
    }
}
