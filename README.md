import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private ParamRepository paramRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserChannel() {
        // Mocking response
        Param results = new Param("P9992");
        results.setData(new String[]{"Channel_S2S", "Channel_NS2S"});

        when(paramRepository.getParam(any(Param.class))).thenReturn(results);

        // Execute and verify multiple cases in one test
        assertEquals("Channel_S2S", userService.getUserChannel("S2S"), "S2S case failed");
        assertEquals("Channel_NS2S", userService.getUserChannel("NS2S"), "NS2S case failed");

        // Mock null response
        when(paramRepository.getParam(any(Param.class))).thenReturn(null);
        assertEquals("", userService.getUserChannel("S2S"), "Null response case failed");
    }
}
