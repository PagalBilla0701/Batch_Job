import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.data.repository.UserRepository;
import com.scb.core.codeparam.repository.ParamRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateConsentRefID() {
        String targetRefID = "TR123";
        String originalRefID = "OR456";

        doNothing().when(userRepository).updateConsentForm(targetRefID, originalRefID);

        service.updateConsentRefID(targetRefID, originalRefID);

        verify(userRepository, times(1)).updateConsentForm(targetRefID, originalRefID);
    }

    @Test
    public void testGetNameForUsers() {
        Map<String, String> mockData = new HashMap<>();
        mockData.put("1", "John Doe");

        when(userRepository.getNameForUsers()).thenReturn(mockData);

        Map<String, String> result = service.getNameForUsers();

        assertEquals(mockData, result);
        verify(userRepository, times(1)).getNameForUsers();
    }

    @Test
    public void testGetNameForId() {
        String countryCode = "IN";
        Map<String, String> mockData = new HashMap<>();
        mockData.put("2", "Jane Doe");

        when(userRepository.getNameForId(countryCode)).thenReturn(mockData);

        Map<String, String> result = service.getNameforId(countryCode);

        assertEquals(mockData, result);
        verify(userRepository, times(1)).getNameForId(countryCode);
    }

    @Test
    public void testGetOpportunityListingPage() {
        Map<String, Object> request = new HashMap<>();
        request.put("queuetype", "Sales");
        request.put("countrycode", "US");

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("opportunity", "Lead");

        when(userRepository.getOpportunityListingPage("Sales", "US")).thenReturn(mockResponse);

        Map<String, Object> result = service.getOpportunityListingPage(request);

        assertEquals(mockResponse, result);
        verify(userRepository, times(1)).getOpportunityListingPage("Sales", "US");
    }

    @Test
    public void testGetEventLeadSearchRoles() {
        String countryCode = "IN";
        List<Param> mockParams = new ArrayList<>();
        Param param1 = new Param();
        param1.setData(new String[]{"Role1", "Role2"});
        mockParams.add(param1);

        when(paramRepository.getParamList("EL" + countryCode)).thenReturn(mockParams);

        Map<String, Object> result = service.getEventLeadSearchRoles(countryCode);

        assertEquals(1, result.size());
        verify(paramRepository, times(1)).getParamList("EL" + countryCode);
    }

    @Test
    public void testLogJson() {
        Object request = new Object();
        Object payload = new Object();
        ObjectMapper mapper = mock(ObjectMapper.class);

        try {
            when(mapper.writeValueAsString(request)).thenReturn("{\"request\":\"value\"}");
            when(mapper.writeValueAsString(payload)).thenReturn("{\"payload\":\"value\"}");

            service.logJson(request, payload);

            verify(mapper, times(1)).writeValueAsString(request);
            verify(mapper, times(1)).writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
