import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Map;

import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

public class IVRSRResponseEntityServiceTest {

    @InjectMocks
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSREndPointURL_usingReflection() throws Exception {
        // Arrange
        String xParamKey1 = "key1";
        String xParamKey2 = "key2";
        String countryCodeForParam = "MY";
        String idParam = "ID001";

        Param mockParam = new Param(idParam);
        mockParam.setCountryCode(countryCodeForParam);
        mockParam.getKeys()[0] = xParamKey1;
        mockParam.getKeys()[1] = xParamKey2;
        String mockUrl = "https://example.com/api";
        mockParam.setData(new String[] {null, null, null, null, null, null, mockUrl});

        when(paramRepository.getParam(mockParam)).thenReturn(mockParam);

        // Use Reflection to access the private method
        Method method = IVRSRResponseEntityService.class.getDeclaredMethod(
            "getSREndPointURL",
            String.class,
            String.class,
            String.class,
            String.class
        );
        method.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) method.invoke(
            ivrSRResponseEntityService,
            xParamKey1,
            xParamKey2,
            countryCodeForParam,
            idParam
        );

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Service URL should match", mockUrl, result.get("service_url"));

        // Verify interactions
        verify(paramRepository, times(1)).getParam(any(Param.class));
    }
}
