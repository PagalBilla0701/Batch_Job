@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsUiIntegrator cemsUiIntegrator;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private Logger logger;

    private Map<String, Object> responseValues;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDefaultIntVal() {
        // Prepare test data
        Map<String, Object> map = new HashMap<>();
        map.put("key1", 5);
        map.put("key2", null);

        // Test valid key
        int result = callActivityService.defaultIntVal(map, "key1");
        Assert.assertEquals(5, result);

        // Test null key
        try {
            callActivityService.defaultIntVal(map, "key2");
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testRenderCustomerSelection() {
        // Prepare test data
        List<Map<String, String>> customers = new ArrayList<>();
        Map<String, String> customer = new HashMap<>();
        customer.put("customerId", "12345");
        customers.add(customer);

        String productType = "Savings Staff";
        String accountNumber = "12345678";
        String currencyCode = "USD";
        String callActivityNo = "CALL123";
        LoginBean loginBean = new LoginBean();

        SectionDataResponse mockResponse = new SectionDataResponse();
        Mockito.when(cemsUiIntegrator.integrate(Mockito.anyMap(), Mockito.anyMap(), Mockito.any(LoginBean.class)))
                .thenReturn(mockResponse);

        // Execute
        SectionDataResponse response = callActivityService.renderCustomerSelection(customers, productType, accountNumber, currencyCode, callActivityNo, loginBean);

        // Verify
        Assert.assertNotNull(response);
        Mockito.verify(cemsUiIntegrator, Mockito.times(1)).integrate(Mockito.anyMap(), Mockito.anyMap(), Mockito.any(LoginBean.class));
    }

    @Test
    public void testLoadRecentCallSection() throws Exception {
        // Prepare test data
        String owner = "Owner1";
        String countryCode3 = "USA";

        List<CallActivity> callActivities = new ArrayList<>();
        CallActivity call = new CallActivity();
        call.setRefNoDesc("REF001");
        call.setCustId("12345");
        call.setCustName("John Doe");
        call.setGeneral(false);
        callActivities.add(call);

        Mockito.when(callActivityAction.getCallActivityByOwnerWithMaxResults(Mockito.eq(owner), Mockito.eq(countryCode3), Mockito.anyInt()))
                .thenReturn(callActivities);

        // Execute
        Map<String, Object> result = callActivityService.loadRecentCallSection(owner, countryCode3);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals("Recent Call Activity", result.get("title"));
        List<Map<String, Object>> recentCallActivityList = (List<Map<String, Object>>) result.get("recentCallActivityList");
        Assert.assertEquals(1, recentCallActivityList.size());
        Assert.assertEquals("REF001", recentCallActivityList.get(0).get("id"));

        Mockito.verify(callActivityAction, Mockito.times(1))
                .getCallActivityByOwnerWithMaxResults(Mockito.eq(owner), Mockito.eq(countryCode3), Mockito.anyInt());
    }
}
