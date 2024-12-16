import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CallActivityServiceImplTest {

    private CallActivityServiceImpl callActivityService = new CallActivityServiceImpl();

    @Test
    public void testDefaultIntVal() {
        Map<String, Object> map = new HashMap<>();

        // Case 1: Key exists with an integer value
        map.put("key1", 10);
        assertEquals(10, callActivityService.defaultIntVal(map, "key1"));

        // Case 2: Key does not exist
        assertEquals(0, callActivityService.defaultIntVal(map, "key2"));

        // Case 3: Key exists with a null value
        map.put("key3", null);
        assertEquals(0, callActivityService.defaultIntVal(map, "key3"));
    }
}
