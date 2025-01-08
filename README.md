@Before
public void setup() {
    MockitoAnnotations.openMocks(this);

    // Mocking necessary objects
    loginBean = new LoginBean();

    // Mocking dependencies
    GridMetaData mockGridMetaData = new GridMetaData();
    mockGridMetaData.setPageSize(10);

    when(gridMetaDataAction.getGridMetaDataBySectionId(anyList(), eq(loginBean)))
            .thenReturn(Collections.singletonList(mockGridMetaData));

    when(callActivityAction.getCallActivityByOwnerAndCallStatusCount(anyString(), anyString(), anyString()))
            .thenReturn(100L);

    CallActivity mockCallActivity = new CallActivity();
    mockCallActivity.setCustId("12345");
    mockCallActivity.setRefNoDesc("REF123");
    mockCallActivity.setNotes("Sample Notes");
    mockCallActivity.setCallProductType("Product1");
    mockCallActivity.setCallPrimaryType("PrimaryType");
    mockCallActivity.setCallSecondaryType("SecondaryType");
    mockCallActivity.setVerificationTypeDesc("VerificationType");
    mockCallActivity.setTransferred(true);

    when(callActivityAction.getCallActivityByOwnerAndCallStatus(anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Collections.singletonList(mockCallActivity));

    when(cvGridDataAction.getSectionData(any(), anyList(), anyInt(), anyInt(), eq(loginBean)))
            .thenReturn(new SectionDataResponse());
}

@Test
public void testLoadPendingCallSection() throws Exception {
    String countryCode3 = "USA";
    String owner = "JohnDoe";
    int pageNo = 1;
    int totalRecords = -1;

    // Execute the method under test
    SectionDataResponse result = callActivityService.loadPendingCallSection(countryCode3, owner, loginBean, pageNo, totalRecords);

    // Assertions
    assertNotNull(result);
    verify(logger).info("Loading pending calls for country code: {} and owner: {}", countryCode3, owner);
    verify(gridMetaDataAction).getGridMetaDataBySectionId(anyList(), eq(loginBean));
    verify(callActivityAction).getCallActivityByOwnerAndCallStatusCount(eq(owner), anyString(), eq(countryCode3));
    verify(callActivityAction).getCallActivityByOwnerAndCallStatus(eq(owner), anyString(), eq(countryCode3), eq(pageNo), anyInt());
    verify(cvGridDataAction).getSectionData(any(), anyList(), eq(pageNo), anyInt(), eq(loginBean));
}
