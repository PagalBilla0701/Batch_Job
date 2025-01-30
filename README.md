@RunWith(MockitoJUnitRunner.class)
public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service;

    @Mock
    private OpportunityPitchEntityDataRepository optyPitchRepo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ValidationFactory validationFactory;

    @Mock
    private OpportunityFactory opportunityFactory;

    @Mock
    private CemsUtil cemsUtil;

    @Mock
    private UserBean userBean;

    @Mock
    private FormData formData;

    @Mock
    private FormData oldData;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOpportunity() throws Exception {
        // Mocking dependencies
        String countryCode = "US";
        when(userBean.getCountryCode()).thenReturn(countryCode);
        when(userBean.getPeoplewiseld()).thenReturn("user123");
        when(formData.getCountry()).thenReturn(null); // Simulate a blank country code in formData
        when(formData.getAssignTo()).thenReturn("userX");
        when(formData.getOwnerId()).thenReturn("owner123");
        when(oldData.getAssignTo()).thenReturn("userY");
        when(oldData.getOwnerId()).thenReturn("owner123");
        when(validationFactory.getEditValidationEngine(countryCode)).thenReturn(mock(ValidationEngine.class));
        when(opportunityFactory.getOpportunityListingImpl(countryCode)).thenReturn(mock(OpportunityListingImpl.class));
        when(cemsUtil.convertToOpportunityPOJO(any())).thenReturn(new OpportunityReqRespJson());
        
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseData = new HashMap<>();
        List<OpportunityReqRespJson> oppList = new ArrayList<>();
        responseData.put("data", oppList);
        response.put("data", responseData);

        when(opportunityFactory.getOpportunityListingImpl(countryCode)
                .createOrUpdateOpportunity(eq(userBean), eq(request), any(), anyString()))
                .thenReturn(response);

        // Run the method under test
        Map<String, Object> result = service.saveOpportunity(userBean, request, formData, oldData, "UTC");

        // Verify interactions
        verify(validationFactory).getEditValidationEngine(countryCode);
        verify(opportunityFactory.getOpportunityListingImpl(countryCode))
                .createOrUpdateOpportunity(eq(userBean), eq(request), any(), anyString());
        verify(cemsUtil).convertToOpportunityPOJO(any());

        // Validate the results
        assertNotNull(result);
        assertTrue(result.containsKey("data"));
        assertNotNull(result.get("data"));
    }
}
