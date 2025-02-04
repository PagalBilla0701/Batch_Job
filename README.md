import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.scb.cems.model.request.FavCustomerServiceRequest;
import com.scb.cems.data.assembler.domain.SectionDataResponse;
import com.scb.cems.servicehelper.FavouritesHelper;
import com.scb.cems.central.beans.LoginBean;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DashboardServiceImplTest {

    @Mock
    private FavouritesHelper favouritesHelper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFavouriteFullList() {
        // Step 1: Mock input data
        FavCustomerServiceRequest serviceRequest = new FavCustomerServiceRequest();
        serviceRequest.setCustomerId("12345");
        serviceRequest.setFavouriteType("SAVINGS");

        LoginBean loginBean = new LoginBean();
        loginBean.setUserId("testUser");

        // Step 2: Mock the response from FavouritesHelper
        SectionDataResponse mockResponse = new SectionDataResponse();
        mockResponse.setSuccess(true);
        mockResponse.setMessage("Favourites fetched successfully.");

        when(favouritesHelper.getFavouriteFullList(serviceRequest, loginBean)).thenReturn(mockResponse);

        // Step 3: Call the method under test
        SectionDataResponse response = dashboardService.getFavouriteFullList(serviceRequest, loginBean);

        // Step 4: Verify the results
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Favourites fetched successfully.", response.getMessage());

        // Step 5: Verify the interaction with the mocked dependency
        verify(favouritesHelper, times(1)).getFavouriteFullList(serviceRequest, loginBean);
    }
}
