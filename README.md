import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicHttpResponse;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class CallDispositionServiceTest {

    private CallDispositionService callDispositionService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private HttpClient httpClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        callDispositionService = new CallDispositionService(paramRepository, httpClient);
    }

    @Test
    public void testUpdateCallDispositionStatus_SuccessfulResponse() throws Exception {
        String countryCode = "US";
        CallActivity call = new CallActivity();
        call.setConnectionId("12345");
        call.setCallPrimaryType("Primary");
        call.setCallSecondaryType("Secondary");
        call.setCallDriver("Driver");
        call.setUserId("user123");

        // Mock ParamRepository behavior
        Param param = new Param("IVR03");
        Param resultParam = new Param();
        resultParam.setData(new String[]{"", "", "", "", "", "", "http://api.test.com", "/update"});
        when(paramRepository.getParam(param)).thenReturn(resultParam);

        // Mock HttpClient behavior
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpPatch.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine().getStatusCode()).thenReturn(200);

        // Mock response handler
        ResponseHandler<String> handler = new BasicResponseHandler();
        when(handler.handleResponse(mockResponse)).thenReturn("{ \"status\": \"success\" }");

        callDispositionService.updateCallDispositionStatus(countryCode, call);

        verify(httpClient, times(1)).execute(any(HttpPatch.class));
    }

    @Test(expected = Exception.class)
    public void testUpdateCallDispositionStatus_ErrorResponse() throws Exception {
        String countryCode = "US";
        CallActivity call = new CallActivity();
        call.setConnectionId("12345");
        call.setCallPrimaryType("Primary");
        call.setCallSecondaryType("Secondary");
        call.setCallDriver("Driver");
        call.setUserId("user123");

        // Mock ParamRepository behavior
        Param param = new Param("IVR03");
        Param resultParam = new Param();
        resultParam.setData(new String[]{"", "", "", "", "", "", "http://api.test.com", "/update"});
        when(paramRepository.getParam(param)).thenReturn(resultParam);

        // Mock HttpClient behavior
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpPatch.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine().getStatusCode()).thenReturn(500);

        callDispositionService.updateCallDispositionStatus(countryCode, call);
    }
}
