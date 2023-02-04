import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ServiceTest {

    @InjectSpy
    Service service;

    @InjectSpy
    ServiceFallbackHandler serviceFallbackHandler;

    @Test
    public void testServiceFallback_TimeoutNotExceeded() {

        Mockito.doAnswer(delayed("result mock", 500))
                .when(service).serviceMethodLongRunning();

        assertEquals("result mock", service.serviceMethod());
    }

    @Test
    public void testServiceFallback_TimeoutExceeded() {

        Mockito.doAnswer(delayed("result mock", 2000))
                .when(service).serviceMethodLongRunning();

        assertEquals("result fallback", service.serviceMethod());

        assertEquals(getHandledThrowable().getClass(), TimeoutException.class);
    }

    @Test
    public void testServiceFallback_ExceptionOccured() {

        Mockito.doThrow(NullPointerException.class)
                .when(service).serviceMethodLongRunning();

        assertEquals("result fallback", service.serviceMethod());

        assertEquals(getHandledThrowable().getClass(), NullPointerException.class);
    }

    private Throwable getHandledThrowable() {
        ArgumentCaptor<ExecutionContext> fallbackHandlerCaptor = ArgumentCaptor.forClass(ExecutionContext.class);
        Mockito.verify(serviceFallbackHandler).handle(fallbackHandlerCaptor.capture());
        Throwable failure = fallbackHandlerCaptor.getValue().getFailure();
        return failure;
    }

    private static Answer<String> delayed(String str, int delayMillis) {
        return AdditionalAnswers.answersWithDelay(delayMillis, invocation -> str);
    }
}
