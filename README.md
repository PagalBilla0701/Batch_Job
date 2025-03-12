import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.data.repository.UserRepository;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;
import com.scb.cems.model.TaskAndAppointmentData;

@RunWith(MockitoJUnitRunner.class)
public class TaskAndAppointmentServiceImplTest {

    @InjectMocks
    private TaskAndAppointmentServiceImpl taskService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    private Map<String, Object> request;
    private Map<String, Object> payload;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new HashMap<>();
        payload = new HashMap<>();
    }

    // 1. Test imsDataProcess
    @Test
    public void testImsDataProcess() throws Exception {
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://test.com");
        paramData.put("httpMethod", "POST");

        when(paramRepository.getParam(any())).thenReturn(new Param());
        when(restTemplate.exchange(any(), any(), any(), eq(Map.class)))
            .thenReturn(ResponseEntity.ok(Collections.singletonMap("data", "success")));

        Map<String, Object> result = taskService.imsDataProcess(request, payload, "APPOINTMENTSAVE");

        assertNotNull(result);
        assertEquals("success", result.get("data"));
    }

    // 2. Test getLMSEndPointeUrl
    @Test
    public void testGetLMSEndPointeUrl() {
        Param param = new Param();
        param.setData(new String[]{"POST", "http://test.com", "/endpoint"});
        
        when(paramRepository.getParam(any())).thenReturn(param);
        
        Map<String, String> result = taskService.getLMSEndPointeUrl("APPOINTMENTSAVE");
        
        assertNotNull(result);
        assertEquals("POST", result.get("httpMethod"));
        assertTrue(result.get("serviceUrl").contains("http://test.com"));
    }

    // 3. Test strSubstitutor using Reflection
    @Test
    public void testStrSubstitutor() throws Exception {
        String actualUrl = "http://example.com/{param1}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");

        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("strSubstitutor", String.class, Map.class);
        method.setAccessible(true);
        String result = (String) method.invoke(taskService, actualUrl, inputMap);

        assertEquals("http://example.com/value1", result);
    }

    // 4. Test massUpdate
    @Test
    public void testMassUpdate() throws Exception {
        UserBean userBean = new UserBean();
        TaskAndAppointmentData taskData = new TaskAndAppointmentData();
        taskData.setOwner("123-ABC");
        List<TaskAndAppointmentData> payload = Arrays.asList(taskData);

        when(userRepository.getNameForId(any())).thenReturn(Collections.singletonMap("123", "John Doe"));

        Map<String, Object> result = taskService.massUpdate(userBean, payload);

        assertNotNull(result);
        assertTrue(result.containsKey("errstatus"));
        assertTrue(result.containsKey("succstatus"));
    }

    // 5. Test getUserChannel
    @Test
    public void testGetUserChannel() {
        Param param = new Param();
        param.setData(new String[]{"Channel1", "Channel2"});

        when(paramRepository.getParam(any())).thenReturn(param);

        String result = taskService.getUserChannel("S25");
        assertEquals("Channel1", result);

        result = taskService.getUserChannel("NS25");
        assertEquals("Channel2", result);
    }

    // 6. Test getHeaderString using Reflection
    @Test
    public void testGetHeaderString() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setUserId("testUser");

        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("getHeaderString", Object.class);
        method.setAccessible(true);
        String json = (String) method.invoke(taskService, userBean);

        assertNotNull(json);
        assertTrue(json.contains("testUser"));
    }
}
