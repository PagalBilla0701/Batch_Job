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

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for renderVerified method.
     */
    @Test
    public void testRenderVerified() {
        // Arrange
        String countryCode = "US";
        Mockito.when(callActivity.isGeneral()).thenReturn(true);
        Mockito.when(callActivityService.renderCallInfo(callActivity, true, countryCode, loginBean))
                .thenReturn(sectionDataResponse);

        // Act
        SectionDataResponse result = callActivityService.renderVerified(callActivity, countryCode, loginBean);

        // Assert
        assertNotNull(result);
        Mockito.verify(callActivityService).renderCallInfo(callActivity, true, countryCode, loginBean);
    }

    /**
     * Test for renderVerificationQuestion method.
     */
    @Test
    public void testRenderVerificationQuestion() {
        // Arrange
        List<Object> renderedQuestions = List.of("Question 1", "Question 2");
        Mockito.when(callActivity.getCustId()).thenReturn("12345");
        Mockito.when(callActivity.getCustName()).thenReturn("John Doe");

        // Act
        Map<String, Object> result = callActivityService.renderVerificationQuestion(callActivity, renderedQuestions);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.get("custId"));
        assertEquals("John Doe", result.get("custName"));
        assertEquals(renderedQuestions, result.get("renderedQuestions"));
        Mockito.verify(callActivity).getCustId();
        Mockito.verify(callActivity).getCustName();
    }

    /**
     * Test for renderVerificationQuestionWithCallInfo method.
     */
    @Test
    public void testRenderVerificationQuestionWithCallInfo() {
        // Arrange
        List<Object> renderedQuestions = List.of("Question 1", "Question 2");
        SectionDataResponse mockCallData = new SectionDataResponse();
        List<SectionData> modifiedSections = List.of(new SectionData("Section1"));

        Mockito.when(callActivity.getCustId()).thenReturn("12345");
        Mockito.when(callActivity.getCustName()).thenReturn("John Doe");
        Mockito.when(callActivityService.renderNewCallInfo(callActivity, loginBean)).thenReturn(mockCallData);
        Mockito.when(callActivityService.modifyTwoFaTypeDescription(mockCallData)).thenReturn(modifiedSections);

        // Act
        Map<String, Object> result = callActivityService.renderVerificationQuestionWithCallInfo(
                callActivity, renderedQuestions, loginBean);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.get("custId"));
        assertEquals("John Doe", result.get("custName"));
        assertEquals(renderedQuestions, result.get("renderedQuestions"));
        assertNotNull(result.get("callData"));
        Mockito.verify(callActivityService).renderNewCallInfo(callActivity, loginBean);
        Mockito.verify(callActivityService).modifyTwoFaTypeDescription(mockCallData);
    }
}
