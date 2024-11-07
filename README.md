import java.lang.reflect.Method;
import java.util.Map;
import static org.junit.Assert.*;

// Inside your test class
@Test
public void testGetAttachedDataReflection() throws Exception {
    // Assuming you have already set the fields for callActivity
    callActivity.setCustId("12345");
    callActivity.setOneFa("1FA_Example");
    callActivity.setTwoFa("2FA_Example");

    // Access the private method getAttachedData using reflection
    Method getAttachedDataMethod = CallActivityServiceImpl.class.getDeclaredMethod("getAttachedData", CallActivity.class, String.class);
    getAttachedDataMethod.setAccessible(true); // Makes the private method accessible

    // Invoke the private method and cast the result
    Map<String, Object> attachedData = (Map<String, Object>) getAttachedDataMethod.invoke(callActivityService, callActivity, "Inbound");

    // Verify the returned data
    assertNotNull(attachedData);
    assertEquals("12345", attachedData.get("relld"));
    assertEquals("1FA_Example", attachedData.get("1FA"));
    assertEquals("2FA_Example", attachedData.get("2FA"));
    assertEquals("Inbound", attachedData.get("Call Type"));
}
