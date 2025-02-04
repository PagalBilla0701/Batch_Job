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
        // Step 1: Use the Builder pattern to create FavCustomerServiceRequest
        FavCustomerServiceRequest serviceRequest = new FavCustomerServiceRequest.Builder()
                .userName("testUser")
                .type("SAVINGS")
                .countryCode("IN")
                .sort("date")
                .sortOrder("asc")
                .start(0)
                .page(1)
                .build();

        // Step 2: Mock LoginBean
        LoginBean loginBean = new LoginBean();
        loginBean.setUserId("testUser");

        // Step 3: Mock the response from FavouritesHelper
        SectionDataResponse mockResponse = new SectionDataResponse();
        mockResponse.setSuccess(true);
        mockResponse.setMessage("Favourites fetched successfully.");

        when(favouritesHelper.getFavouriteFullList(serviceRequest, loginBean)).thenReturn(mockResponse);

        // Step 4: Call the method under test
        SectionDataResponse response = dashboardService.getFavouriteFullList(serviceRequest, loginBean);

        // Step 5: Verify the results
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Favourites fetched successfully.", response.getMessage());

        // Step 6: Verify interaction with mocked dependency
        verify(favouritesHelper, times(1)).getFavouriteFullList(serviceRequest, loginBean);
    }
}
