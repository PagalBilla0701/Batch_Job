import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AttachmentServiceTest {

    @Mock
    private DefaultHttpClient httpClient;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @InjectMocks
    private AttachmentService attachmentService; // The class containing the method

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAttachmentFileContent_Success() throws Exception {
        // Arrange
        Map<String, Object> payLoad = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://mock-service.com/download");

        payLoad.put("key", "value");
        request.put("headerData", "mockHeader");

        when(attachmentService.getLMSEndPointeUrl("mockParamKey")).thenReturn(paramData);

        HttpPost mockHttpPost = mock(HttpPost.class);
        whenNew(HttpPost.class).withArguments("http://mock-service.com/download").thenReturn(mockHttpPost);

        when(httpClient.execute(mockHttpPost)).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);

        byte[] mockResponseData = "mockFileContent".getBytes();
        when(EntityUtils.toByteArray(httpResponse.getEntity())).thenReturn(mockResponseData);

        // Act
        byte[] result = attachmentService.getAttachmentFileContent(payLoad, request, "mockParamKey");

        // Assert
        assertNotNull(result);
        assertArrayEquals(mockResponseData, result);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testGetAttachmentFileContent_RuntimeException() throws Exception {
        // Arrange
        Map<String, Object> payLoad = new HashMap<>();
        Map<String, Object> request = new HashMap<>();

        when(attachmentService.getLMSEndPointeUrl("mockParamKey")).thenThrow(new Sales2ServiceRuntimeException("Mock Exception"));

        // Act
        attachmentService.getAttachmentFileContent(payLoad, request, "mockParamKey");
    }

    @Test(expected = IOException.class)
    public void testGetAttachmentFileContent_IOException() throws Exception {
        // Arrange
        Map<String, Object> payLoad = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://mock-service.com/download");

        when(attachmentService.getLMSEndPointeUrl("mockParamKey")).thenReturn(paramData);

        HttpPost mockHttpPost = mock(HttpPost.class);
        whenNew(HttpPost.class).withArguments("http://mock-service.com/download").thenReturn(mockHttpPost);

        when(httpClient.execute(mockHttpPost)).thenThrow(new IOException("Mock IOException"));

        // Act
        attachmentService.getAttachmentFileContent(payLoad, request, "mockParamKey");
    }

    @Test
    public void testGetAttachmentFileContent_NullResponse() throws Exception {
        // Arrange
        Map<String, Object> payLoad = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://mock-service.com/download");

        when(attachmentService.getLMSEndPointeUrl("mockParamKey")).thenReturn(paramData);

        HttpPost mockHttpPost = mock(HttpPost.class);
        whenNew(HttpPost.class).withArguments("http://mock-service.com/download").thenReturn(mockHttpPost);

        when(httpClient.execute(mockHttpPost)).thenReturn(null);

        // Act
        byte[] result = attachmentService.getAttachmentFileContent(payLoad, request, "mockParamKey");

        // Assert
        assertNull(result);
    }
}
