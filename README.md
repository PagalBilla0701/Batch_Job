import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MyServiceTest {

    @Mock
    private MyRepository myRepository;

    @InjectMocks
    private MyService myService;

    @Before
    public void setUp() {
        // No initialization needed for Mockito
    }

    @Test
    public void testGetUserChannel() {
        // Mock repository response with valid data
        List<String> mockData = Arrays.asList("Channel1", "Channel2");
        when(myRepository.getData("P9992")).thenReturn(mockData);

        // Test for "S2S"
        assertEquals("Channel1", myService.getUserChannel("S2S"));

        // Test for "NS2S"
        assertEquals("Channel2", myService.getUserChannel("NS2S"));

        // Test for invalid userType
        assertEquals("", myService.getUserChannel("INVALID_TYPE"));

        // Mock repository response as empty list
        when(myRepository.getData("P9992")).thenReturn(Collections.emptyList());
        assertEquals("", myService.getUserChannel("S2S"));

        // Mock repository response as null
        when(myRepository.getData("P9992")).thenReturn(null);
        assertEquals("", myService.getUserChannel("S2S"));
    }
}
