   package com.scb.cems.serviceImpl;

import com.scb.cems.crmxt.dao.CTOMACLDAO;
import com.scb.cems.crmxt.dao.SPACLDAO;
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

    // Assuming ACLMatrixMainMenu is a class in your project
    private static class ACLMatrixMainMenu {
        // Add fields and methods as per your actual class definition
        private String menuName;

        public ACLMatrixMainMenu(String menuName) {
            this.menuName = menuName;
        }

        public String getMenuName() {
            return menuName;
        }
    }

    @Before
    public void setUp() {
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
    }

    @Test
    public void testMainMenu() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        mockResponse.add(new ACLMatrixMainMenu("menu1"));
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
        // Assuming fetchUnifiedSearchMatrix also returns List<ACLMatrixMainMenu>
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        mockResponse.add(new ACLMatrixMainMenu("search1"));
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
        // Assuming fetchAnalyticsList also returns List<ACLMatrixMainMenu>
        List<ACLMatrixMainMenu> mockResponse = new ArrayList<>();
        mockResponse.add(new ACLMatrixMainMenu("analytics1"));
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
