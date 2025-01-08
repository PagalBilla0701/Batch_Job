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

    private List<Long> sectionIDs;
    private List<CallActivity> callActivities;
    private GridMetaData gridMetaData;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        sectionIDs = new ArrayList<>();
        sectionIDs.add(16012L);

        gridMetaData = new GridMetaData();
        gridMetaData.setPageSize(10);

        callActivities = new ArrayList<>();
        CallActivity callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setRefNoDesc("REF123");
        callActivity.setNotes("Sample Notes");
        callActivity.setCallProductType("Product1");
        callActivity.setCallPrimaryType("PrimaryType");
        callActivity.setCallSecondaryType("SecondaryType");
        callActivity.setVerificationTypeDesc("VerificationType");
        callActivity.setTransferred(true);
        callActivities.add(callActivity);
    }

    @Test
    public void testLoadPendingCallSection() throws Exception {
        // Mock behaviors
        when(gridMetaDataAction.getGridMetaDataBySectionId(eq(sectionIDs), eq(loginBean)))
                .thenReturn(Collections.singletonList(gridMetaData));
        when(callActivityAction.getCallActivityByOwnerAndCallStatusCount(anyString(), eq("PENDING_CALL_STATUS"), anyString()))
                .thenReturn(100L);
        when(callActivityAction.getCallActivityByOwnerAndCallStatus(anyString(), eq("PENDING_CALL_STATUS"), anyString(), anyInt(), anyInt()))
                .thenReturn(callActivities);
        when(cvGridDataAction.getSectionData(any(SectionDataRequest.class), anyList(), anyInt(), anyInt(), eq(loginBean)))
                .thenReturn(new SectionDataResponse());

        // Input data
        String countryCode3 = "USA";
        String owner = "JohnDoe";
        int pageNo = 1;
        int totalRecords = -1;

        // Execute the method under test
        SectionDataResponse result = callActivityService.loadPendingCallSection(countryCode3, owner, loginBean, pageNo, totalRecords);

        // Assertions
        assertNotNull(result);

        // Verify interactions
        verify(logger).info("Loading pending calls for country code: {} and owner: {}", countryCode3, owner);
        verify(gridMetaDataAction).getGridMetaDataBySectionId(eq(sectionIDs), eq(loginBean));
        verify(callActivityAction).getCallActivityByOwnerAndCallStatusCount(eq(owner), eq("PENDING_CALL_STATUS"), eq(countryCode3));
        verify(callActivityAction).getCallActivityByOwnerAndCallStatus(eq(owner), eq("PENDING_CALL_STATUS"), eq(countryCode3), eq(pageNo), eq(gridMetaData.getPageSize()));
        verify(cvGridDataAction).getSectionData(any(SectionDataRequest.class), anyList(), eq(pageNo), anyInt(), eq(loginBean));
    }
}
