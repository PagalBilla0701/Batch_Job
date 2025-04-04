package com.scb.cems.serviceImpl;

import com.scb.cems.crmxt.dao.CTOMACLDAO;
import com.scb.cems.crmxt.dao.SPACLDAO;
import com.scb.cems.crmxt.model.ACLMatrixMainMenu;  // Import actual class
import com.scb.cems.service.ACLMatrixService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ACLCallableTest {

    @Mock
    private ACLMatrixService ams;

    @Mock
    private SPACLDAO spDAO;

    @Mock
    private CTOMACLDAO ctomDAO;

    @InjectMocks
    private ACLCallable aclCallable;

    private final String countryCode = "US";
    private final String insCode = "123";

    @Before
    public void setUp() {
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
    }

    @Test
    public void testMainMenu() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        ACLMatrixMainMenu menu = new ACLMatrixMainMenu();
        menu.setRoleName("ROLE_ADMIN");
        menu.setMenuName("Dashboard");
        mockResponse.add(menu);
        when(ams.fetchACLMatrixMainMenuList(eq(countryCode), eq(insCode))).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("MainMenu"));
        assertEquals(mockResponse, result.get("MainMenu"));
        verify(ams).fetchACLMatrixMainMenuList(countryCode, insCode);
    }

    @Test
    public void testSearchItem() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "SearchItem", spDAO, ctomDAO);
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        ACLMatrixMainMenu menu = new ACLMatrixMainMenu();
        menu.setRoleName("ROLE_USER");
        menu.setMenuName("Search");
        mockResponse.add(menu);
        when(ams.fetchUnifiedSearchMatrix(eq(countryCode), eq(insCode))).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("SearchItem"));
        assertEquals(mockResponse, result.get("SearchItem"));
        verify(ams).fetchUnifiedSearchMatrix(countryCode, insCode);
    }

    @Test
    public void testAnalytics() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "Analytics", spDAO, ctomDAO);
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        ACLMatrixMainMenu menu = new ACLMatrixMainMenu();
        menu.setRoleName("ROLE_ANALYST");
        menu.setMenuName("Analytics");
        mockResponse.add(menu);
        when(ams.fetchAnalyticsList(eq(countryCode), eq(insCode))).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Analytics"));
        assertEquals(mockResponse, result.get("Analytics"));
        verify(ams).fetchAnalyticsList(countryCode, insCode);
    }

    @Test
    public void testSPWithException() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "SP", spDAO, ctomDAO);
        when(spDAO.getServiceModuleDetails(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Act
        Map<String, Object> result = aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("SP"));
        assertNull(result.get("SP"));
    }

    @Test
    public void testCalculateElapsedTimeUsingReflection() throws Exception {
        // Arrange
        Method method = ACLCallable.class.getDeclaredMethod("calculateElapsedTime", Long.class, Long.class);
        method.setAccessible(true);

        // Test case 1: Less than 1 second
        Long startTime1 = 1000L;
        Long endTime1 = 1500L;
        String result1 = (String) method.invoke(aclCallable, startTime1, endTime1);
        assertEquals("500 ms", result1);

        // Test case 2: More than 1 second
        Long startTime2 = 1000L;
        Long endTime2 = 2500L;
        String result2 = (String) method.invoke(aclCallable, startTime2, endTime2);
        assertEquals("1 s", result2);
    }
}
