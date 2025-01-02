@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CallActivity callActivity;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private LoginBean loginBean;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testRenderCallInfo_GeneralCall() {
        // Arrange
        Mockito.when(callActivity.isOneFaVerifed()).thenReturn(true);
        Mockito.when(callActivity.isGenCall()).thenReturn(true);
        Mockito.when(callActivity.getTwoFaVerificationType()).thenReturn("OTP");
        Mockito.when(callActivity.getTwoFaVerificationStatus()).thenReturn("Verified");

        List<SectionData> sections = new ArrayList<>();
        SectionData section = new SectionData();
        section.setKeyValGridDataMap(new HashMap<>());
        sections.add(section);
        Mockito.when(sectionDataResponse.getSections()).thenReturn(sections);

        // Act
        SectionDataResponse response = callActivityService.renderCallInfo(callActivity, true, "MY", loginBean);

        // Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.getSections().size());
        Assert.assertEquals("OTP", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationType"));
        Assert.assertEquals("Verified", response.getSections().get(0).getKeyValGridDataMap().get("twofaVerificationStatus"));
    }
}
