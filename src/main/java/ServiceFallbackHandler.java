import io.quarkus.logging.Log;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceFallbackHandler implements FallbackHandler<String> {
    @Override
    public String handle(ExecutionContext executionContext) {
        Log.info("started fallback handling. Cause: ", executionContext.getFailure());
        return "result fallback";
    }
}
