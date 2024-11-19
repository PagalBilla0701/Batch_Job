import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Map;

public class IVRCtomResponseEntityServiceTest {

    private IVRCtomResponseEntityService instance;

    @Mock
    private ParamRepository paramRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        instance = new IVRCtomResponseEntityService(paramRepository);
    }

    @Test
    public void testGetCTOMEndPointURL() throws Exception {
        // Arrange
        String xParamKey1 = "testKey1";
        String xParamKey2 = "testKey2";
        String countryCode = "US";
        String idParam = "id123";

        String[] mockData = {"", "roleIdValue", "secretIdValue", "", "", "oAuthUrlValue", "serviceUrlValue"};

        Param paramMock = mock(Param.class);
        when(paramMock.getData()).thenReturn(mockData);

        when(paramRepository.getParam(any(Param.class))).thenReturn(paramMock);

        // Access private method using reflection
        Method method = IVRCtomResponseEntityService.class.getDeclaredMethod(
                "getCTOMEndPointURL",
                String.class, String.class, String.class, String.class
        );
        method.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>) method.invoke(instance, xParamKey1, xParamKey2, countryCode, idParam);

        // Assert
        assertEquals("roleIdValue", resultMap.get("roleld"));
        assertEquals("secretIdValue", resultMap.get("secretId"));
        assertEquals("oAuthUrlValue", resultMap.get("oAuth_url"));
        assertEquals("serviceUrlValue", resultMap.get("service_url"));
    }
}
