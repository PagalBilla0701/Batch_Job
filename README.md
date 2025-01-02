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

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock sectionDataResponse behavior
        List<SectionData> sections = new ArrayList<>();
        SectionData section = new SectionData();
        section.setKeyValGridDataMap(new HashMap<>());
        section.setGridMetaData(new ArrayList<>());
        sections.add(section);

        Mockito.when(sectionDataResponse.getSections()).thenReturn(sections);
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
}
