@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private GridMetaDataAction gridMetaDataAction;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private CVGridDataAction cvGridDataAction;

    @Mock
    private Logger logger;

    @Mock
    private LoginBean loginBean;

    @Mock
    private CallActivity callActivity;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadPendingCallSection() throws Exception {
        // Mocking input parameters
        String countryCode3 = "USA";
        String owner = "JohnDoe";
        int pageNo = 1;
        int totalRecords = -1;

        // Mocking behavior of GridMetaDataAction
        GridMetaData mockGridMetaData = new GridMetaData();
        mockGridMetaData.setPageSize(10);
        when(gridMetaDataAction.getGridMetaDataBySectionId(anyList(), eq(loginBean)))
                .thenReturn(Collections.singletonList(mockGridMetaData));

        // Mocking behavior of CallActivityAction for total records count
        when(callActivityAction.getCallActivityByOwnerAndCallStatusCount(eq(owner), anyString(), eq(countryCode3)))
                .thenReturn(100L);

        // Mocking behavior of CallActivityAction for call activities
        List<CallActivity> callActivities = new ArrayList<>();
        CallActivity mockCallActivity = new CallActivity();
        mockCallActivity.setCustId("12345");
        mockCallActivity.setRefNoDesc("REF123");
        mockCallActivity.setNotes("Sample Notes");
        mockCallActivity.setCallProductType("Product1");
        mockCallActivity.setCallPrimaryType("PrimaryType");
        mockCallActivity.setCallSecondaryType("SecondaryType");
        mockCallActivity.setVerificationTypeDesc("VerificationType");
        mockCallActivity.setTransferred(true);
        callActivities.add(mockCallActivity);

        when(callActivityAction.getCallActivityByOwnerAndCallStatus(eq(owner), anyString(), eq(countryCode3), eq(pageNo), eq(10)))
                .thenReturn(callActivities);

        // Mocking behavior of CVGridDataAction
        SectionDataResponse mockResponse = new SectionDataResponse();
        when(cvGridDataAction.getSectionData(any(), anyList(), eq(pageNo), eq(100), eq(loginBean)))
                .thenReturn(mockResponse);

        // Call the method under test
        SectionDataResponse result = callActivityService.loadPendingCallSection(countryCode3, owner, loginBean, pageNo, totalRecords);

        // Assertions
        assertNotNull(result);
        verify(logger).info("Loading pending calls for country code: {} and owner: {}", countryCode3, owner);
        verify(gridMetaDataAction).getGridMetaDataBySectionId(anyList(), eq(loginBean));
        verify(callActivityAction).getCallActivityByOwnerAndCallStatusCount(eq(owner), anyString(), eq(countryCode3));
        verify(callActivityAction).getCallActivityByOwnerAndCallStatus(eq(owner), anyString(), eq(countryCode3), eq(pageNo), eq(10));
        verify(cvGridDataAction).getSectionData(any(), anyList(), eq(pageNo), eq(100), eq(loginBean));
    }
}
