@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private VerificationScriptProxy proxy;

    @Mock
    private VerificationScriptProxyIE IEProxy;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private Logger logger;

    private CallActivity callActivity;
    private LoginBean loginBean;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        callActivity = new CallActivity();
        loginBean = new LoginBean();

        // Initialize CallActivity fields
        callActivity.setCustId("12345");
        callActivity.setCustName("John Doe");
        callActivity.setAccountNo("987654321");
    }

    @Test
    public void testRenderVerified() {
        // Mock behavior
        when(callActivity.isGeneral()).thenReturn(true);

        // Call the method
        SectionDataResponse response = callActivityService.renderVerified(callActivity, "IN", loginBean);

        // Verify results
        assertNotNull(response);
        verify(callActivityAction, never()).updateCallActivity(callActivity);
    }

    @Test
    public void testRenderVerificationQuestion() {
        List<Object> renderedQuestions = Arrays.asList("Question1", "Question2");

        // Call the method
        Map response = callActivityService.renderVerificationQuestion(callActivity, renderedQuestions);

        // Verify results
        assertNotNull(response);
    }

    @Test
    public void testWipeCallCustomerAccount() throws Exception {
        // Call the method
        SectionDataResponse response = callActivityService.wipeCallCustomerAccount(callActivity, "IN", loginBean);

        // Verify results
        assertNull(callActivity.getCustId());
        assertNull(callActivity.getCustName());
        assertNotNull(response);
    }

    @Test
    public void testUpdateForceClosedStatus() throws Exception {
        AppConfigItem appConfigItem = new AppConfigItem();
        appConfigItem.setValue("5");
        when(paramRepository.getApplicationConfig("callactivity", "forceCloseDurationInDays"))
            .thenReturn(appConfigItem);

        // Call the method
        int updatedRecords = callActivityService.updateForceClosedStatus("IN");

        // Verify results
        assertEquals(0, updatedRecords);
        verify(callActivityAction, times(1)).updateForceClosedStatus("IN", 5);
    }

    @Test
    public void testAutoSavingNotes() throws Exception {
        // Call the method
        Map<String, Object> response = callActivityService.autoSavingNotes("Sample notes", "123", "IN");

        // Verify results
        assertNotNull(response);
        assertEquals("SAVED", response.get("status"));
    }

    @Test(expected = CallActivityAutoSavingNotesException.class)
    public void testAutoSavingNotesException() throws Exception {
        doThrow(new Exception("Test Exception")).when(callActivityAction)
            .updateAutoSavingNotes(anyString(), anyString(), anyString());

        // Call the method
        callActivityService.autoSavingNotes("Sample notes", "123", "IN");
    }

    @Test
    public void testGetCustomerVerify() {
        // Mock behavior
        when(IEProxy.getAnswer("IN", "CUSTVERIFY", new String[]{"Customer", "12345", "IN"}))
            .thenReturn(Boolean.TRUE);

        // Call the method
        Boolean isVerified = callActivityService.getCustomerVerify("IN", "12345");

        // Verify results
        assertTrue(isVerified);
    }

    @Test
    public void testIsSoftTokenEnableForCountry() {
        Param param = new Param();
        param.setData(new String[]{"1"});
        when(paramRepository.getParam(any())).thenReturn(param);

        // Call the method
        String result = callActivityService.isSoftTokenEnableForCountry("IN");

        // Verify results
        assertEquals("true", result);
    }

    @Test
    public void testIsSoftTokenEnableForCountry_NotEnabled() {
        Param param = new Param();
        param.setData(new String[]{"0"});
        when(paramRepository.getParam(any())).thenReturn(param);

        // Call the method
        String result = callActivityService.isSoftTokenEnableForCountry("IN");

        // Verify results
        assertEquals("false", result);
    }
}
