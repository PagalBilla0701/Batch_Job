@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private CallActivity callActivity;

    @Mock
    private LoginBean loginBean;

    @Mock
    private Logger logger;

    private Map<String, Object> responseValues;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Set up the mocked CallActivity
        callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setOneFa("1FA_Example");
        callActivity.setTwoFa("2FA_Example");
        callActivity.setAvailableAuth("AUTH_A | AUTH_B");
        callActivity.setFailedAuthOne("AUTH_C");
        callActivity.setFailedAuthTwo("AUTH_D");

        // Set up the mocked SectionDataResponse
        sectionDataResponse = new SectionDataResponse();
        List<SectionData> sections = new ArrayList<>();
        SectionData section = new SectionData();
        section.setKeyValGridDataMap(new HashMap<>());
        section.setGridMetaData(new ArrayList<>());
        sections.add(section);
        sectionDataResponse.setSections(sections);
    }

    @Test
    public void testRenderCallInfo_GeneralCall() {
        // Arrange
        Mockito.when(callActivity.isOneFaVerifed()).thenReturn(true);
        Mockito.when(callActivity.isGenCall()).thenReturn(true);
        Mockito.when(callActivity.getTwoFaVerificationType()).thenReturn("OTP");
        Mockito.when(callActivity.getTwoFaVerificationStatus()).thenReturn("Verified");

        // Act
        SectionDataResponse response = callActivityService.renderCallInfo(callActivity, true, "MY", loginBean);

        // Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.getSections().size());
        Assert.assertEquals("OTP", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationType"));
        Assert.assertEquals("Verified", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationStatus"));
    }

    @Test
    public void testRenderCallInfo_NonGeneralCall() {
        // Arrange
        Mockito.when(callActivity.isOneFaVerifed()).thenReturn(false);
        Mockito.when(callActivity.isGenCall()).thenReturn(false);
        Mockito.when(callActivity.getTwoFaVerificationType()).thenReturn("SoftToken");
        Mockito.when(callActivity.getTwoFaVerificationStatus()).thenReturn("Not Verified");

        // Act
        SectionDataResponse response = callActivityService.renderCallInfo(callActivity, false, "SG", loginBean);

        // Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.getSections().size());
        Assert.assertEquals("SoftToken", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationType"));
        Assert.assertEquals("Not Verified", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationStatus"));
    }

    @Test
    public void testModifyTwoFaForGenCall() {
        // Arrange
        Mockito.when(callActivity.getTwoFaVerificationType()).thenReturn("OTP");
        Mockito.when(callActivity.getTwoFaVerificationStatus()).thenReturn("Verified");

        // Act
        List<SectionData> sections = callActivityService.modifyTwoFaForGenCall(sectionDataResponse, callActivity);

        // Assert
        Assert.assertNotNull(sections);
        Assert.assertEquals(1, sections.size());
        Assert.assertEquals("OTP", sections.get(0).getKeyValGridDataMap().get("twofaVerificationType"));
        Assert.assertEquals("Verified", sections.get(0).getKeyValGridDataMap().get("twofaVerificationStatus"));
    }
}
