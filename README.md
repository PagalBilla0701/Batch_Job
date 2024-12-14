import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.data.assembler.model.IVRSRHoldingWrapper;
import com.scb.cems.data.assembler.model.SRComplaintResponseBody;
import com.scb.cems.data.assembler.service.internal.IVRServiceRequestDetails;
import com.scb.cems.data.assembler.service.internal.IVRSRResponseEntityService;

@RunWith(MockitoJUnitRunner.class)
public class IVRServiceRequestDetailsTest {

    @InjectMocks
    private IVRServiceRequestDetails ivrServiceRequestDetails;

    @Mock
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    private Map<String, Object> reqParamMap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "MY");
    }

    @Test
    public void testInvoke_SuccessfulResponse() throws Exception {
        // Arrange
        SRComplaintResponseBody srComplaintResponseBody = new SRComplaintResponseBody();
        srComplaintResponseBody.setPageable(new SRComplaintResponseBody.Pageable(10L));

        ResponseEntity<SRComplaintResponseBody> mockResponseEntity =
                new ResponseEntity<>(srComplaintResponseBody, HttpStatus.OK);

        when(ivrSRResponseEntityService.getReponseEntityforSR(
                any(Map.class),
                eq("URL12"),
                eq("ServiceRequest"),
                eq("summary"),
                eq("MY")
        )).thenReturn(mockResponseEntity);

        // Act
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);
        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals(10, wrapper.getOpenSRCount());
        assertEquals("MY", wrapper.getCountryCode());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                any(Map.class), eq("URL12"), eq("ServiceRequest"), eq("summary"), eq("MY")
        );
    }

    @Test
    public void testInvoke_NoResponse() throws Exception {
        // Arrange
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                any(Map.class),
                eq("URL12"),
                eq("ServiceRequest"),
                eq("summary"),
                eq("MY")
        )).thenReturn(null);

        // Act
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);
        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals(0, wrapper.getOpenSRCount());
        assertEquals("MY", wrapper.getCountryCode());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                any(Map.class), eq("URL12"), eq("ServiceRequest"), eq("summary"), eq("MY")
        );
    }

    @Test
    public void testInvoke_ExceptionThrown() throws Exception {
        // Arrange
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                any(Map.class),
                eq("URL12"),
                eq("ServiceRequest"),
                eq("summary"),
                eq("MY")
        )).thenThrow(new RuntimeException("Service error"));

        // Act
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);
        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals(0, wrapper.getOpenSRCount());
        assertEquals("MY", wrapper.getCountryCode());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                any(Map.class), eq("URL12"), eq("ServiceRequest"), eq("summary"), eq("MY")
        );
    }
}
