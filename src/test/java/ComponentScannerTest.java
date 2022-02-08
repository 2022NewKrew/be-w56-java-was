import com.my.was.ControllerScanner;
import org.junit.jupiter.api.Test;

class ComponentScannerTest {

    @Test
    void test() {
        ControllerScanner componentScanner = new ControllerScanner();
        componentScanner.scan();
    }

}