@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private VerificationScriptProxy proxy;

    @Mock
    private VerificationScriptProxyIE IEProxy;

    @Mock
    private CallActivityEDMIService callActivityEDMIService;

    private static final String COUNTRY_CODE = "IN";
    private static final String CREDIT_CARD_NO = "1234567890123456";
    private static final String ACCOUNT_NUMBER = "987654321";
    private static final String CURRENCY_CODE = "USD";
    private static final String PRODUCT_TYPE = "Savings";

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnquireCardDetails() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, String> respMap = new HashMap<>();
        respMap.put("cardHolder", "John Doe");
        mockResponse.put("resp", respMap);

        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "CCARDINFO", new String[]{"Account", CREDIT_CARD_NO}))
               .thenReturn(mockResponse);

        Map<String, String> result = callActivityService.enquireCardDetails(COUNTRY_CODE, CREDIT_CARD_NO);

        Assert.assertNotNull(result);
        Assert.assertEquals("John Doe", result.get("cardHolder"));
    }

    @Test
    public void testGetLinkedCustomers() {
        Map<String, Object> mockResponse = new HashMap<>();
        List<Map<String, String>> respList = new ArrayList<>();
        Map<String, String> customerData = new HashMap<>();
        customerData.put("customerId", "CUST123");
        respList.add(customerData);
        mockResponse.put("resp", respList);

        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "LINKEDCUST", new String[]{"Account", ACCOUNT_NUMBER, CURRENCY_CODE}))
               .thenReturn(mockResponse);

        List<Map<String, String>> result = callActivityService.getLinkedCustomers(COUNTRY_CODE, ACCOUNT_NUMBER, CURRENCY_CODE);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("CUST123", result.get(0).get("customerId"));
    }

    @Test
    public void testGetLinkedCustomersForAE() {
        Map<String, Object> mockResponse = new HashMap<>();
        List<Map<String, String>> respList = new ArrayList<>();
        Map<String, String> customerData = new HashMap<>();
        customerData.put("customerId", "CUST456");
        respList.add(customerData);
        mockResponse.put("resp", respList);

        Mockito.when(IEProxy.getAnswer(COUNTRY_CODE, "LINKEDCUST", new String[]{"Account", ACCOUNT_NUMBER, CURRENCY_CODE, PRODUCT_TYPE}))
               .thenReturn(mockResponse);

        List<Map<String, String>> result = callActivityService.getLinkedCustomersForAE(COUNTRY_CODE, ACCOUNT_NUMBER, CURRENCY_CODE, PRODUCT_TYPE);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("CUST456", result.get(0).get("customerId"));
    }

    @Test
    public void testEnquireFDAccount() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, String> respMap = new HashMap<>();
        respMap.put("fdAccount", "FD123456");
        mockResponse.put("resp", respMap);

        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "TDINFO", new String[]{"Account", ACCOUNT_NUMBER, CURRENCY_CODE}))
               .thenReturn(mockResponse);

        Map<String, String> result = callActivityService.enquireFDAccount(COUNTRY_CODE, ACCOUNT_NUMBER, CURRENCY_CODE);

        Assert.assertNotNull(result);
        Assert.assertEquals("FD123456", result.get("fdAccount"));
    }

    @Test
    public void testEnquireAccount() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, String> respMap = new HashMap<>();
        respMap.put("accountHolder", "Jane Smith");
        mockResponse.put("resp", respMap);

        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "EBBSINFO", new String[]{"Account", ACCOUNT_NUMBER, CURRENCY_CODE}))
               .thenReturn(mockResponse);

        Map<String, String> result = callActivityService.enquireAccount(COUNTRY_CODE, ACCOUNT_NUMBER, CURRENCY_CODE);

        Assert.assertNotNull(result);
        Assert.assertEquals("Jane Smith", result.get("accountHolder"));
    }

    @Test
    public void testEnquireLoanAccount() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, String> respMap = new HashMap<>();
        respMap.put("loanAccount", "LN987654");
        mockResponse.put("resp", respMap);

        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "RLSINFO", new String[]{"Account", ACCOUNT_NUMBER}))
               .thenReturn(mockResponse);

        Map<String, String> result = callActivityService.enquireLoanAccount(COUNTRY_CODE, ACCOUNT_NUMBER);

        Assert.assertNotNull(result);
        Assert.assertEquals("LN987654", result.get("loanAccount"));
    }

    @Test
    public void testGetLinkedCustomers_NullResponse() {
        Mockito.when(proxy.getAnswer(COUNTRY_CODE, "LINKEDCUST", new String[]{"Account", ACCOUNT_NUMBER, CURRENCY_CODE}))
               .thenReturn(null);

        List<Map<String, String>> result = callActivityService.getLinkedCustomers(COUNTRY_CODE, ACCOUNT_NUMBER, CURRENCY_CODE);

        Assert.assertNull(result);
    }
}
