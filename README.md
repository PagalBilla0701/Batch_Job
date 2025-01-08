@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CallActivity callActivity;

    @Mock
    private LoginBean loginBean;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private Logger logger;

    private List<Object> renderedQuestions;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        renderedQuestions = new ArrayList<>();
        renderedQuestions.add("Question1");
        renderedQuestions.add("Question2");

        callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setCustName("John Doe");
        callActivity.setGeneral(true);
    }

    @Test
    public void testRenderVerified() {
        // Mock the behavior of renderCallInfo
        SectionDataResponse mockedResponse = new SectionDataResponse();
        when(callActivityService.renderCallInfo(eq(callActivity), eq(true), eq("USA"), eq(loginBean)))
                .thenReturn(mockedResponse);

        // Execute the method under test
        SectionDataResponse response = callActivityService.renderVerified(callActivity, "USA", loginBean);

        // Assertions
        assertNotNull(response);
        assertEquals(mockedResponse, response);

        // Verify the interaction
        verify(callActivityService).renderCallInfo(eq(callActivity), eq(true), eq("USA"), eq(loginBean));
    }

    @Test
    public void testRenderVerificationQuestion() {
        // Execute the method under test
        Map<String, Object> response = callActivityService.renderVerificationQuestion(callActivity, renderedQuestions);

        // Assertions
        assertNotNull(response);
        assertEquals("12345", response.get("relationshipNo"));
        assertEquals("John Doe", response.get("fullName"));
        assertEquals("../call-activity/next-question.do", response.get("refreshQuestionUrl"));
        assertEquals(renderedQuestions, response.get("questions"));
        assertNotNull(response.get("data"));

        // Verify no unexpected interaction
        verifyNoInteractions(logger);
    }

    @Test
    public void testRenderVerificationQuestionWithCallInfo() {
        // Mock the behavior of renderNewCallInfo
        SectionDataResponse mockedCallData = new SectionDataResponse();
        when(callActivityService.renderNewCallInfo(eq(callActivity), eq(loginBean))).thenReturn(mockedCallData);

        // Mock the modifyTwoFaTypeDescription behavior
        List<SectionData> modifiedSections = new ArrayList<>();
        SectionData sectionData = new SectionData();
        modifiedSections.add(sectionData);
        when(callActivityService.modifyTwoFaTypeDescription(eq(mockedCallData))).thenReturn(modifiedSections);

        // Execute the method under test
        Map<String, Object> response = callActivityService.renderVerificationQuestionWithCallInfo(callActivity, renderedQuestions, loginBean);

        // Assertions
        assertNotNull(response);
        assertEquals("12345", response.get("relationshipNo"));
        assertEquals("John Doe", response.get("fullName"));
        assertEquals("../call-activity/next-question.do", response.get("refreshQuestionUrl"));
        assertEquals(renderedQuestions, response.get("questions"));
        assertEquals(mockedCallData, response.get("data"));

        // Verify interactions
        verify(callActivityService).renderNewCallInfo(eq(callActivity), eq(loginBean));
        verify(callActivityService).modifyTwoFaTypeDescription(eq(mockedCallData));
    }
}
