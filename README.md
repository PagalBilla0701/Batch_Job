import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class IVRServiceRequestDetailsTest {

    @InjectMocks
    private IVRServiceRequestDetails ivrServiceRequestDetails;

    @Mock
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    private Map<String, Object> reqParamMap;
    private SRComplaintResponseBody mockResponseBody;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "US");

        mockResponseBody = new SRComplaintResponseBody();
        SRComplaintResponseBody.Pageable pageable = new SRComplaintResponseBody.Pageable();
        pageable.setTotalElements("30");
        mockResponseBody.setPageable(pageable);
    }

    @Test
    public void testInvoke_SuccessfulResponse() throws Exception {
        // Mock the response entity
        ResponseEntity<SRComplaintResponseBody> responseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenReturn(responseEntity);

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(30, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }

    @Test
    public void testInvoke_NoResponse() throws Exception {
        // Mock no response from the service
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenReturn(null);

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }

    @Test
    public void testInvoke_ExceptionHandling() throws Exception {
        // Mock an exception from the service
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenThrow(new RuntimeException("Test exception"));

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }
}
