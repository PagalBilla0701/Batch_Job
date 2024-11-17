public class CallActivityControllerTest {

    @Mock
    protected GridDataAction gridDataAction;

    @Mock
    public CallActivityService callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    @Qualifier("CemsSectionDataUiIntegrator")
    private CemsUiIntegrator cemsUiIntegrator;

    @Mock
    CVQuestionAction cvQuestionAction;

    @Mock
    private AppMessageSourceHelper messageHelper;

    @Mock
    private UserAction userAction;

    @Mock
    @Qualifier("menuAccessRepository")
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private com.scb.cems.service.MenuService menuService;

    @Mock
    private S2SOpportunityService s250pportunityService;

    @Mock
    private CallActivityEDMIService callActivityEDMIService;

    @Mock
    private CemsUtil cemsUtil;

    @Mock
    private S2OpportunitylistingFactory opptyListingFactory;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private BLPCIDSSDAO BIPCIDSSDAO;

    @Mock
    private SoftTokenPropertiesUtil softTokenPropertiesUtil;

    // Added as part of TSYS implementation
    @Mock
    private ODSCpanTpanService odsCpanTpanService;

    @Mock
    private TSYSKongCommonService tsysKongCommonService;

    @Mock
    SectionDataResponse sectionResponse;

    @InjectMocks
    CallActivityController controller;

    GenesysCallActivity genCall;

    LoginBean login;

    ModelMap model;

    UserBean userBean;

    CallActivity call;

    Map<String, String> responseMap;

    Map finalMap;
    Map header;

    GenesysRequestData genesysData;

    private MockMvc mockMvc;

    @InjectMocks
    private CallActivityController callActivityController;

    @Before
    public void setup() {
        genCall = new GenesysCallActivity();
        genCall.setAvailableAuth("OTP|ST");
        genCall.setOneFa("2+1");

        call = new CallActivity();
        model = new ModelMap();

        login = new LoginBean();
        userBean = new UserBean();
        userBean.setCountryShortDesc("SGP");
        userBean.setCountryCode("SG");
        userBean.setInstanceCode("CB_SG");
        userBean.setFullname("XXXX");

        login.setUserBean(userBean);
        login.setUsername("1200046");

        call = new CallActivity("23456789", "SGP");
        call.setCustId("A0198678");
        call.setAccountNoIVR("123456790");

        header = ArrayUtils.toMap(new Object[][]{
            {"timestamp", GregorianCalendar.getInstance().getTimeInMillis()},
            {"responseStatus", "true"}
        });

        responseMap = new HashMap<>();
        responseMap.put("RelationshipNo", call.getCustId());
        responseMap.put("AccountNo", call.getAccountNoIVR());
        responseMap.put("callRefNo", "SG23456789");
        responseMap.put("type", "ST");

        finalMap = ArrayUtils.toMap(new Object[][]{
            {"header", header},
            {"body", responseMap},
            {"statusCode", "OK"}
        });

        genesysData = new GenesysRequestData();
        genesysData.setAccountNo("123456790");
        genesysData.setRelId("A0198678");

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();
    }

    @Test
    public void testGenesysTriggerAttachedData() throws Exception {
        // Mocking behavior
        when(callActivityAction.getCallActivityByRefNo(anyString(), anyString())).thenReturn(call);
        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(finalMap);

        // Perform the request
        mockMvc.perform(post("/genesys-trigger-verify.do")
                .param("callActivityNo", "SG23456789")
                .param("relationshipNo", "A0198678")
                .param("type", "ST")
                .param("customerName", "John Doe")
                .param("productType", "Loan")
                .param("customerAccountNo", "1234567890")
                .sessionAttr("login", login))
                .andExpect(status().isOk()) // Expecting OK response
                .andExpect(jsonPath("$.statusCode").value("OK"))
                .andExpect(jsonPath("$.header.responseStatus").value("true"))
                .andExpect(jsonPath("$.body.callRefNo").value("SG23456789"))
                .andExpect(jsonPath("$.body.RelationshipNo").value("A0198678"));
        
        // Verify that service methods are called
        verify(callActivityAction, times(1)).getCallActivityByRefNo(anyString(), anyString());
        verify(callActivityService, times(1)).makeResponseWrapper(anyMap(), eq(true));
    }

}
