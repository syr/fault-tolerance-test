import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ServiceTest {

    @InjectSpy
    Service service;

    @InjectSpy
    MyFallbackHandler myFallbackHandler;

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
    }

    private static Answer<String> delayed(String str, int delayMillis) {
        return AdditionalAnswers.answersWithDelay(delayMillis, invocation -> str);
    }
}
