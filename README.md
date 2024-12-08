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
        MockitoAnnotations.openMocks(this);

        // Prepare request parameters
        reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "US");

        // Prepare mock response body
        mockResponseBody = new SRComplaintResponseBody();
        SRComplaintResponseBody.Pageable pageable = new SRComplaintResponseBody.Pageable();
        pageable.setTotalElements("30");
        mockResponseBody.setPageable(pageable);
    }

    @Test
    public void testInvoke_SuccessfulResponse() throws Exception {
        // Mock a successful response from IVRSRResponseEntityService
        ResponseEntity<SRComplaintResponseBody> responseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        )).thenReturn(responseEntity);

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(30, wrapper.getOpenSRCount());

        // Verify mock interactions
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        );
    }

    @Test
    public void testInvoke_NoResponse() throws Exception {
        // Mock a null response from IVRSRResponseEntityService
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        )).thenReturn(null);

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify mock interactions
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        );
    }

    @Test
    public void testInvoke_ExceptionHandling() throws Exception {
        // Mock an exception from IVRSRResponseEntityService
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        )).thenThrow(new RuntimeException("Test exception"));

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify the result
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify mock interactions
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq(reqParamMap),
                eq("summary"),
                eq("ServiceRequest"),
                eq("US"),
                anyString()
        );
    }
}
