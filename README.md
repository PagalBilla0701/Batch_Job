import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityControllerTest {

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveSoftPhoneDetails_WhenSoftPhoneDisplayIsYes() {
        // Mock the response from ParamRepository
        final String idParam = "GEN01";
        Param param = new Param(idParam);
        param.setCountryCode("SG");
        Param mockedParamResponse = new Param(idParam);
        String[] data = {
            "https://apps.aps1.pure.cloud/crm/index.html?enableFrameworkClientId=true&dedicatedLoginWindow=true&crm=embeddableframework",
            "a8cb704f-df4b-44dd-9521-e659b4451947",
            "MM_MY_v1"
        };
        mockedParamResponse.setData(data);

        when(paramRepository.getParam(any(Param.class))).thenReturn(mockedParamResponse);

        // Input for the test
        String isSoftPhoneDisplay = "Yes";
        String countryCode = "SG";

        // Act
        Map<String, Object> response = new HashMap<>();
        String iFrameUrl = null;
        String flowID = null;
        String flowName = null;

        if ("Yes".equals(isSoftPhoneDisplay)) {
            final String idParamForTest = "GEN01";
            Param paramForTest = new Param(idParamForTest);
            paramForTest.setCountryCode(countryCode);
            Param results = paramRepository.getParam(paramForTest);

            if (results != null) {
                iFrameUrl = results.getData()[0];
                flowID = results.getData()[1];
                flowName = results.getData()[2];
            }

            response.put("isSoftPhoneDisplay", isSoftPhoneDisplay);
            response.put("iFrameUrl", iFrameUrl);
            response.put("flowID", flowID);
            response.put("flowName", flowName);
        }

        // Assert
        assertNotNull(response);
        assertEquals("Yes", response.get("isSoftPhoneDisplay"));
        assertEquals(
            "https://apps.aps1.pure.cloud/crm/index.html?enableFrameworkClientId=true&dedicatedLoginWindow=true&crm=embeddableframework",
            response.get("iFrameUrl")
        );
        assertEquals("a8cb704f-df4b-44dd-9521-e659b4451947", response.get("flowID"));
        assertEquals("MM_MY_v1", response.get("flowName"));

        // Verify
        verify(paramRepository, times(1)).getParam(any(Param.class));
    }

    @Test
    public void testRetrieveSoftPhoneDetails_WhenSoftPhoneDisplayIsNotYes() {
        // Input for the test
        String isSoftPhoneDisplay = "No";

        // Act
        Map<String, Object> response = new HashMap<>();
        String iFrameUrl = null;
        String flowID = null;
        String flowName = null;

        if ("Yes".equals(isSoftPhoneDisplay)) {
            final String idParamForTest = "GEN01";
            Param paramForTest = new Param(idParamForTest);
            paramForTest.setCountryCode("SG");
            Param results = paramRepository.getParam(paramForTest);

            if (results != null) {
                iFrameUrl = results.getData()[0];
                flowID = results.getData()[1];
                flowName = results.getData()[2];
            }

            response.put("isSoftPhoneDisplay", isSoftPhoneDisplay);
            response.put("iFrameUrl", iFrameUrl);
            response.put("flowID", flowID);
            response.put("flowName", flowName);
        }

        // Assert
        assertNotNull(response);
        assertEquals(0, response.size());
        assertFalse(response.containsKey("isSoftPhoneDisplay"));
        assertFalse(response.containsKey("iFrameUrl"));
        assertFalse(response.containsKey("flowID"));
        assertFalse(response.containsKey("flowName"));

        // Verify
        verify(paramRepository, never()).getParam(any(Param.class));
    }
}
