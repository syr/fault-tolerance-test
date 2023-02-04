import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;

@QuarkusTest
public class ServiceTest {

    @InjectSpy
    Service service;

    @InjectSpy
    MyFallbackHandler myFallbackHandler;

    @Test
    public void testServiceFallback_TimeoutNotExceeded() {

        Mockito.doAnswer( new AnswersWithDelay( 500,  new Returns("result mock")) )
                .when(service).serviceMethodLongRunning();

        Assertions.assertEquals("result mock", service.serviceMethod());
    }

    @Test
    public void testServiceFallback_TimeoutExceeded() {

        Mockito.doAnswer( new AnswersWithDelay( 2000,  new Returns("result mock")) )
                .when(service).serviceMethodLongRunning();

        Assertions.assertEquals("result fallback", service.serviceMethod());
    }
}
