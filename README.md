@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private Logger logger;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse httpResponse;

    private CallActivity callActivity;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        callActivity = new CallActivity();
        callActivity.setUserId("testUserId");
        callActivity.setConnectionId("12345");
        callActivity.setCallPrimaryType("PrimaryType");
        callActivity.setCallSecondaryType("SecondaryType");
        callActivity.setCallDriver("DriverType");
    }

    @Test
    public void testUpdateCallDispositionStatus_success() throws Exception {
        String countryCode = "IN";
        String url = "http://example.com/update";

        // Mocking ParamRepository behavior
        Param param = new Param("IVR03");
        Param results = Mockito.mock(Param.class);
        Mockito.when(results.getData()).thenReturn(new String[] { "", "", "", "", "", "", url, "" });
        Mockito.when(paramRepository.getParam(param)).thenReturn(results);

        // Mocking HttpResponse behavior
        Mockito.when(httpResponse.getStatusLine()).thenReturn(Mockito.mock(StatusLine.class));
        Mockito.when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        Mockito.when(httpClient.execute(Mockito.any(HttpPatch.class))).thenReturn(httpResponse);

        // Run the method
        callActivityService.updateCallDispositionStatus(countryCode, callActivity);

        // Verify interactions
        Mockito.verify(logger).info(Mockito.contains("Service URL"), Mockito.eq(url));
        Mockito.verify(logger).info(Mockito.contains("The resposnse received in updating Call Disposition status"));
    }

    @Test(expected = Exception.class)
    public void testUpdateCallDispositionStatus_failure() throws Exception {
        String countryCode = "IN";
        String url = "http://example.com/update";

        // Mocking ParamRepository behavior
        Param param = new Param("IVR03");
        Param results = Mockito.mock(Param.class);
        Mockito.when(results.getData()).thenReturn(new String[] { "", "", "", "", "", "", url, "" });
        Mockito.when(paramRepository.getParam(param)).thenReturn(results);

        // Mocking HttpResponse behavior
        Mockito.when(httpResponse.getStatusLine()).thenReturn(Mockito.mock(StatusLine.class));
        Mockito.when(httpResponse.getStatusLine().getStatusCode()).thenReturn(500);
        Mockito.when(httpClient.execute(Mockito.any(HttpPatch.class))).thenReturn(httpResponse);

        // Run the method
        callActivityService.updateCallDispositionStatus(countryCode, callActivity);
    }
}
