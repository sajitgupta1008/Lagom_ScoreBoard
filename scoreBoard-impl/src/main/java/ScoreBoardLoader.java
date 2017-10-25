import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class ScoreBoardLoader extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ScoreBoardService.class, ScoreBoardServiceImpl.class);
    }
}
