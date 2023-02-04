import io.quarkus.logging.Log;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Service {

    @Timeout(1000)
    @Fallback(ServiceFallbackHandler.class)
    public String serviceMethod() {
        Log.info("starting long running method");
        String result = serviceMethodLongRunning();
        Log.info("finished long running method");
        return result;
    }

    protected String serviceMethodLongRunning() {
        return "result real"; //should never be returned because replaced by mock
    }
}
