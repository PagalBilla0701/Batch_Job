import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IVRCtomResponseEntityServiceTest {

    @Mock
    private ParamRepository paramRepository;

    @InjectMocks
    private IVRCtomResponseEntityService instance; // Replace with the actual service class name

    @Test
    public void testGetCTOMEndPointURL() {
        try {
            // Arrange
            String xParamKey1 = "testKey1";
            String xParamKey2 = "testKey2";
            String countryCode = "US";
            String idParam = "id123";
            
            Param param = new Param(idParam);
            param.setCountryCode(countryCode);
            
            Param results = Mockito.mock(Param.class);
            String[] data = {"", "roleIdValue", "secretIdValue", "", "", "oAuthUrlValue", "serviceUrlValue"};
            
            // Mock behavior for results and paramRepository
            when(results.getData()).thenReturn(data);
            when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(results);

            // Access private method using reflection
            Method method = IVRCtomResponseEntityService.class.getDeclaredMethod("getCTOMEndPointURL", String.class, String.class, String.class, String.class);
            method.setAccessible(true);

            // Act - Invoke the private method
            Map<String, String> resultMap = (Map<String, String>) method.invoke(instance, xParamKey1, xParamKey2, countryCode, idParam);

            // Assert
            assertEquals("roleIdValue", resultMap.get("roleId"));
            assertEquals("secretIdValue", resultMap.get("secretId"));
            assertEquals("oAuthUrlValue", resultMap.get("oAuth_url"));
            assertEquals("serviceUrlValue", resultMap.get("service_url"));
            
        } catch (InvocationTargetException e) {
            // Print the underlying exception for debugging
            Throwable cause = e.getCause();
            System.err.println("Exception in getCTOMEndPointURL: " + cause);
            cause.printStackTrace();
            throw new RuntimeException("Failed to invoke getCTOMEndPointURL", cause);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error setting up test", e);
        }
    }
}
