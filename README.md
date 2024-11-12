import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class StrSubstitutorTest {

    private YourClassName instance; // Replace YourClassName with the actual class name containing strSubstitutor

    @Before
    public void setUp() {
        instance = new YourClassName();
    }

    @Test
    public void testStrSubstitutor_NormalSubstitution() throws Exception {
        String actualUrl = "https://example.com/api?param1=$(value1)&param2=$(value2)";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("value1", "test1");
        inputMap.put("value2", "test2");

        String result = instance.strSubstitutor(actualUrl, inputMap);
        assertEquals("https://example.com/api?param1=test1&param2=test2", result);
    }

    @Test
    public void testStrSubstitutor_NullUrl() throws Exception {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("value", "test");

        String result = instance.strSubstitutor(null, inputMap);
        assertNull(result);
    }

    @Test
    public void testStrSubstitutor_UrlWithoutQueryParams() throws Exception {
        String actualUrl = "https://example.com/api";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("value", "test");

        String result = instance.strSubstitutor(actualUrl, inputMap);
        assertEquals(actualUrl, result);
    }

    @Test
    public void testStrSubstitutor_MissingInputMapEntry() throws Exception {
        String actualUrl = "https://example.com/api?param1=$(value1)";
        Map<String, Object> inputMap = new HashMap<>();

        String result = instance.strSubstitutor(actualUrl, inputMap);
        assertEquals("https://example.com/api?param1=", result); // Expecting empty string for missing entry
    }

    @Test
    public void testStrSubstitutor_NullValuesInInputMap() throws Exception {
        String actualUrl = "https://example.com/api?param1=$(value1)";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("value1", null);

        String result = instance.strSubstitutor(actualUrl, inputMap);
        assertEquals("https://example.com/api?param1=", result);
    }

    @Test
    public void testStrSubstitutor_ExceptionHandling() {
        String actualUrl = "https://example.com/api?param1=$(value1)";
        Map<String, Object> inputMap = mock(Map.class);
        when(inputMap.entrySet()).thenThrow(new RuntimeException("Test exception"));

        try {
            instance.strSubstitutor(actualUrl, inputMap);
        } catch (Exception e) {
            fail("Exception should be caught within the method");
        }
    }
}
