import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class UserChannelServiceTest {

    private UserChannelService userChannelService;
    private ParamRepository paramRepository; // Mocked dependency

    @Before
    public void setUp() {
        paramRepository = mock(ParamRepository.class); // Mocking the repository
        userChannelService = new UserChannelService(paramRepository); // Injecting mock dependency
    }

    @Test
    public void testGetUserChannel_S2S() {
        // Arrange
        Param param = new Param("P9992");
        Param resultParam = new Param("P9992");
        resultParam.setData(new String[] { "Channel1", "Channel2" });

        when(paramRepository.getParam(any(Param.class))).thenReturn(resultParam);

        // Act
        String channel = userChannelService.getUserChannel("S2S");

        // Assert
        assertEquals("Channel1", channel);
    }

    @Test
    public void testGetUserChannel_NS2S() {
        // Arrange
        Param param = new Param("P9992");
        Param resultParam = new Param("P9992");
        resultParam.setData(new String[] { "Channel1", "Channel2" });

        when(paramRepository.getParam(any(Param.class))).thenReturn(resultParam);

        // Act
        String channel = userChannelService.getUserChannel("NS2S");

        // Assert
        assertEquals("Channel2", channel);
    }

    @Test
    public void testGetUserChannel_NullResults() {
        // Arrange
        when(paramRepository.getParam(any(Param.class))).thenReturn(null);

        // Act
        String channel = userChannelService.getUserChannel("S2S");

        // Assert
        assertEquals("", channel);
    }

    @Test
    public void testGetUserChannel_InvalidUserType() {
        // Arrange
        Param param = new Param("P9992");
        Param resultParam = new Param("P9992");
        resultParam.setData(new String[] { "Channel1", "Channel2" });

        when(paramRepository.getParam(any(Param.class))).thenReturn(resultParam);

        // Act
        String channel = userChannelService.getUserChannel("INVALID");

        // Assert
        assertEquals("", channel);
    }
}
