import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceImplTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMaintenanceErrorByMenuDispName_Success() {
        // Mock data setup
        List<Param> mockParams = new ArrayList<>();
        Param param = new Param();
        param.setCountryCode("IN");
        param.setKeys(new String[]{"paramId", "N", "TestMenu", "extraValue"}); // Avoid ArrayIndexOutOfBounds
        param.setData(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "2024-01-01", "2025-01-01", ""});

        mockParams.add(param);

        // Mock repository call
        when(paramRepository.getParamList(any())).thenReturn(mockParams);

        // Call method under test
        String result = menuService.getMaintenanceErrorByMenuDispName("TestMenu", "IN", "EN");

        // Assertions
        assertNotNull("Result should not be null", result);
    }
}
