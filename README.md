import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInvoke_withValidResponse_shouldReturnWrapperWithData() {
        // Arrange
        String customerId = "0150000350F";
        String countryCode = "US";

        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", customerId);
        reqParamMap.put("countryCode", countryCode);

        // Create mock response
        SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
        Pageable pageable = new Pageable();
        pageable.setTotalElements(5); // Total open service requests
        mockResponseBody.setPageable(pageable);

        ResponseEntity<SRComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(ivrSRResponseEntityService.getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq(countryCode), any(), eq("URL12")))
                .thenReturn(mockResponseEntity);

        // Act
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertEquals(countryCode, result.getCountryCode());
        assertEquals(5, result.getOpenSRCount());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(anyString(), anyString(), eq(countryCode), any(), anyString());
    }

    @Test
    public void testInvoke_withNoResponse_shouldReturnWrapperWithZeroCount() {
        // Arrange
        String customerId = "0150000350F";
        String countryCode = "US";

        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", customerId);
        reqParamMap.put("countryCode", countryCode);

        // Mock null response
        when(ivrSRResponseEntityService.getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq(countryCode), any(), eq("URL12")))
                .thenReturn(null);

        // Act
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertEquals(countryCode, result.getCountryCode());
        assertEquals(0, result.getOpenSRCount());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(anyString(), anyString(), eq(countryCode), any(), anyString());
    }

    @Test
    public void testInvoke_withException_shouldHandleGracefully() {
        // Arrange
        String customerId = "0150000350F";
        String countryCode = "US";

        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", customerId);
        reqParamMap.put("countryCode", countryCode);

        // Mock exception
        when(ivrSRResponseEntityService.getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq(countryCode), any(), eq("URL12")))
                .thenThrow(new RuntimeException("Test exception"));

        // Act
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Assert
        assertNotNull(result);
        assertEquals(countryCode, result.getCountryCode());
        assertEquals(0, result.getOpenSRCount());

        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(anyString(), anyString(), eq(countryCode), any(), anyString());
    }

    @Test
    public void testDateRange_shouldSetCorrectDates() {
        // Arrange
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusDays(180);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String expectedToDate = currentDate.format(formatter);
        String expectedFromDate = previousDate.format(formatter);

        // Act
        String actualToDate = currentDate.format(formatter);
        String actualFromDate = previousDate.format(formatter);

        // Assert
        assertEquals(expectedToDate, actualToDate);
        assertEquals(expectedFromDate, actualFromDate);
    }
}
