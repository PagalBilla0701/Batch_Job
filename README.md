import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

public class IVRCtomServiceDetailTest {

    @InjectMocks
    private IVRCtomServiceDetail ivrCtomServiceDetail;

    @Mock
    private IVRCtomResponseEntityService ivrCtomResponseEntityService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_Success() {
        // Mock input parameters
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "12345");
        reqParamMap.put("countryShortDesc", "US");
        reqParamMap.put("langCodeDescription", "EN");
        reqParamMap.put("countryCode", "US");

        // Mock response entity
        CtomComplaintResponseBody mockResponseBody = new CtomComplaintResponseBody();
        CtomComplaintData mockData = new CtomComplaintData();
        mockData.setTotalElements(5);
        mockData.setContent(Arrays.asList(
                Map.of("status", "Pending assigned"),
                Map.of("status", "In-progress"),
                Map.of("status", "Resolved")
        ));
        mockResponseBody.setData(mockData);

        ResponseEntity<CtomComplaintResponseBody> mockResponseEntity =
                new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(ivrCtomResponseEntityService.getReponseEntityforCTOM(anyMap(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResponseEntity);

        // Invoke the method
        Object result = ivrCtomServiceDetail.invoke(reqParamMap);

        // Validate response
        assertNotNull(result);
        assertThat(result, instanceOf(IVRCtomComplaintHoldingWrapper.class));

        IVRCtomComplaintHoldingWrapper wrapper = (IVRCtomComplaintHoldingWrapper) result;
        assertEquals(5, wrapper.getTotalResource());
        assertEquals(2, wrapper.getOpenComplaintsCount()); // Only "Pending assigned" and "In-progress" are open
        assertEquals("US", wrapper.getCountryCode());

        verify(ivrCtomResponseEntityService, times(1))
                .getReponseEntityforCTOM(anyMap(), eq("URL12"), eq("стом"), eq("summary"), eq("US"));
    }

    @Test
    public void testInvoke_Error() {
        // Mock input parameters
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "12345");

        // Mock response entity with null response
        when(ivrCtomResponseEntityService.getReponseEntityforCTOM(anyMap(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(null);

        // Invoke the method
        Object result = ivrCtomServiceDetail.invoke(reqParamMap);

        // Validate response
        assertNotNull(result);
        assertThat(result, instanceOf(IVRCtomComplaintHoldingWrapper.class));

        IVRCtomComplaintHoldingWrapper wrapper = (IVRCtomComplaintHoldingWrapper) result;
        assertEquals(0, wrapper.getTotalResource());
        assertEquals(0, wrapper.getOpenComplaintsCount());

        verify(ivrCtomResponseEntityService, times(1))
                .getReponseEntityforCTOM(anyMap(), eq("URL12"), eq("стом"), eq("summary"), anyString());
    }

    @Test
    public void testGetOpenComplaintsCount() {
        // Mock response body
        CtomComplaintResponseBody mockResponseBody = new CtomComplaintResponseBody();
        CtomComplaintData mockData = new CtomComplaintData();
        mockData.setContent(Arrays.asList(
                Map.of("status", "Pending assigned"),
                Map.of("status", "In-progress"),
                Map.of("status", "Resolved")
        ));
        mockResponseBody.setData(mockData);

        // Invoke the method
        int openComplaints = ivrCtomServiceDetail.getOpenComplaintsCount(mockResponseBody);

        // Validate response
        assertEquals(2, openComplaints); // Only "Pending assigned" and "In-progress" are open
    }

    @Test
    public void testGetOpenComplaintsCount_NoData() {
        // Mock response body with no data
        CtomComplaintResponseBody mockResponseBody = new CtomComplaintResponseBody();
        CtomComplaintData mockData = new CtomComplaintData();
        mockData.setContent(Collections.emptyList());
        mockResponseBody.setData(mockData);

        // Invoke the method
        int openComplaints = ivrCtomServiceDetail.getOpenComplaintsCount(mockResponseBody);

        // Validate response
        assertEquals(0, openComplaints);
    }
}
