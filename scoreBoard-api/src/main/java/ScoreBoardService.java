import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

public interface ScoreBoardService extends Service {
    
    ServiceCall<Team, Done> newTeam(String teamName);
    
    ServiceCall<NotUsed, String> getWinPercentage(String teamName);
    
    ServiceCall<NotUsed, String> getTeamInfo(String teamName);
    
    ServiceCall<Coach, String> updateCoachName(String teamName);
    
    @Override
    default Descriptor descriptor() {
        return named("scoreboard")
                .withCalls(
                        restCall(Method.POST, "/api/team/:teamName", this::newTeam),
                        restCall(Method.GET, "/api/team/:teamName/wins", this::getWinPercentage),
                        restCall(Method.GET, "/api/team/:teamName", this::getTeamInfo),
                        restCall(Method.PUT, "/api/update/:teamName/coach", this::updateCoachName)
                ).withAutoAcl(true);
    }
    
}
