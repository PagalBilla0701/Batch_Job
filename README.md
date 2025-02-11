import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class StrSubstitutorTest {

    @Test
    public void testStrSubstitutor_Success() throws Exception {
        // Arrange
        String actualUrl = "http://example.com/api?param1=${param1}&param2=${param2}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");
        inputMap.put("param2", "value with spaces");

        // Act
        String result = strSubstitutor(actualUrl, inputMap);

        // Assert
        String expectedUrl = "http://example.com/api?param1=" + URLEncoder.encode("value1", "UTF-8")
                            + "&param2=" + URLEncoder.encode("value with spaces", "UTF-8");
        assertEquals(expectedUrl, result);
    }

    @Test
    public void testStrSubstitutor_EmptyInputMap() throws Exception {
        // Arrange
        String actualUrl = "http://example.com/api?param1=${param1}&param2=${param2}";
        Map<String, Object> inputMap = new HashMap<>();

        // Act
        String result = strSubstitutor(actualUrl, inputMap);

        // Assert
        assertEquals(actualUrl, result);
    }

    @Test(expected = NullPointerException.class)
    public void testStrSubstitutor_NullInputMap() throws Exception {
        // Arrange
        String actualUrl = "http://example.com/api?param1=${param1}&param2=${param2}";
        Map<String, Object> inputMap = null;

        // Act
        strSubstitutor(actualUrl, inputMap);
    }

    @Test(expected = NullPointerException.class)
    public void testStrSubstitutor_NullUrl() throws Exception {
        // Arrange
        String actualUrl = null;
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");

        // Act
        strSubstitutor(actualUrl, inputMap);
    }

    // Helper method for testing
    public String strSubstitutor(String actualUrl, Map<String, Object> inputMap) throws Exception {
        Map<String, String> nMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            nMap.put(entry.getKey(), URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
        org.apache.commons.text.StrSubstitutor sub = new org.apache.commons.text.StrSubstitutor(nMap);
        actualUrl = sub.replace(actualUrl);
        return actualUrl;
    }
}
