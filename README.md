import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MYIVRJDBCDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PicklistService picklistService;

    @InjectMocks
    private MYIVRJDBCDAO myivrJdbcDao;

    private ResourceBundle queryStore;

    @Before
    public void setUp() {
        queryStore = ResourceBundle.getBundle("com/scb/cems/crmxt/query/CVDAOQueries");
        myivrJdbcDao.setQueryStore("com/scb/cems/crmxt/query/CVDAOQueries");
    }

    @Test
    public void testGetCallActivitiesList_WithValidInput_ReturnsCustomerVO() {
        // Mock input parameters
        String crmCountry = "US";
        String customerId = "12345";
        int recordCount = 10;
        int pageNo = 1;

        // Mock the query result for call activities
        List<Map<String, String>> callActivitiesList = new ArrayList<>();
        Map<String, String> callActivityMap = new HashMap<>();
        callActivityMap.put("X_PRODUCT_TYPE", "101");
        callActivityMap.put("X_PRIMARY_CALL_TYPE", "201");
        callActivitiesList.add(callActivityMap);

        when(jdbcTemplate.query(anyString(), any(CVRowMapper.class), eq(crmCountry), eq(crmCountry), eq(customerId), anyInt(), anyInt()))
                .thenReturn(callActivitiesList);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(crmCountry), eq(customerId)))
                .thenReturn(50);

        // Mock picklist service
        when(picklistService.getPicklistValueByEntryId("101", false, crmCountry)).thenReturn("Product Type Desc");
        when(picklistService.getPicklistValueByEntryId("201", false, crmCountry)).thenReturn("Primary Call Type Desc");

        // Call the method
        CustomerVO result = myivrJdbcDao.getCallActivitiesList(crmCountry, crmCountry, customerId, recordCount, pageNo);

        // Verify results
        assertNotNull(result);
        assertEquals(pageNo, result.getCurrentPage());
        assertEquals(1, result.getCurrentRecordsCount());
        assertEquals(50, result.getTotalRecordsCount());
        assertEquals("Product Type Desc", result.getCallActivitesList().get(0).get("X_PRODUCT_TYPE_DESC"));
        assertEquals("Primary Call Type Desc", result.getCallActivitesList().get(0).get("X_PRIMARY_CALL_TYPE_DESC"));
    }

    @Test
    public void testGetCallActivitiesList_WithCustomerRequest_ReturnsCustomerVO() {
        // Mock input
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCrmCountry("US");
        customerRequest.setCustomerID("12345");

        // Mock the query result for call activities
        List<Map<String, String>> callActivitiesList = new ArrayList<>();
        Map<String, String> callActivityMap = new HashMap<>();
        callActivityMap.put("X_PRODUCT_TYPE", "101");
        callActivityMap.put("X_PRIMARY_CALL_TYPE", "201");
        callActivitiesList.add(callActivityMap);

        when(jdbcTemplate.query(anyString(), any(CVRowMapper.class), eq("US"), eq("US"), eq("12345")))
                .thenReturn(callActivitiesList);

        // Mock picklist service
        when(picklistService.getPicklistValueByEntryId("101", false, "US")).thenReturn("Product Type Desc");
        when(picklistService.getPicklistValueByEntryId("201", false, "US")).thenReturn("Primary Call Type Desc");

        // Call the method
        CustomerVO result = myivrJdbcDao.getCallActivitiesList(customerRequest);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.getCurrentRecordsCount());
        assertEquals("Product Type Desc", result.getCallActivitesList().get(0).get("X_PRODUCT_TYPE_DESC"));
        assertEquals("Primary Call Type Desc", result.getCallActivitesList().get(0).get("X_PRIMARY_CALL_TYPE_DESC"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCallActivitiesList_NullQueryStore_ThrowsException() {
        // Clear the query store to simulate the issue
        myivrJdbcDao.setQueryStore(null);

        // Call the method, expecting it to throw an exception
        myivrJdbcDao.getCallActivitiesList("US", "US", "12345", 10, 1);
    }
}
