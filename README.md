import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YourServiceTest {

    @Mock
    private ParamRepository paramRepository;

    @InjectMocks
    private YourService instance; // Replace 'YourService' with the actual service class name

    @Before
    public void setUp() {
        // Initialize any required setup
    }

    @Test
    public void testGetCTOMEndPointURL() {
        // Arrange
        String xParamKey1 = "testKey1";
        String xParamKey2 = "testKey2";
        String countryCode = "US";
        String idParam = "id123";
        
        Param param = new Param(idParam);
        param.setCountryCode(countryCode);
        
        Param results = Mockito.mock(Param.class);
        String[] data = {"", "roleIdValue", "secretIdValue", "", "", "oAuthUrlValue", "serviceUrlValue"};
        
        when(results.getData()).thenReturn(data);
        when(paramRepository.getParam(param)).thenReturn(results);

        // Act
        Map<String, String> resultMap = instance.getCTOMEndPointURL(xParamKey1, xParamKey2, countryCode, idParam);

        // Assert
        assertEquals("roleIdValue", resultMap.get("roleId"));
        assertEquals("secretIdValue", resultMap.get("secretId"));
        assertEquals("oAuthUrlValue", resultMap.get("oAuth_url"));
        assertEquals("serviceUrlValue", resultMap.get("service_url"));
    }
}
