Let me help you modify the test class. The error suggests that fetchACLMatrixMainMenuList should return a List instead of a generic Object. I'll update the test class accordingly and add a test for the private calculateElapsedTime method using Java Reflection.Here's the updated test class:package com.scb.cems.serviceImpl;

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

    @Before
    public void setUp() {
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
    }

    @Test
    public void testMainMenu() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "MainMenu", spDAO, ctomDAO);
        List<String> mockResponse = new ArrayList<>();  // Changed to List
        mockResponse.add("menu1");
        when(ams.fetchACLMatrixMainMenuList(countryCode, insCode)).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = (Map<String, Object>) aclCallable.call();

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
        List<String> mockResponse = new ArrayList<>();  // Changed to List
        mockResponse.add("search1");
        when(ams.fetchUnifiedSearchMatrix(countryCode, insCode)).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = (Map<String, Object>) aclCallable.call();

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
        List<String> mockResponse = new ArrayList<>();  // Changed to List
        mockResponse.add("analytics1");
        when(ams.fetchAnalyticsList(countryCode, insCode)).thenReturn(mockResponse);

        // Act
        Map<String, Object> result = (Map<String, Object>) aclCallable.call();

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
        Map<String, Object> result = (Map<String, Object>) aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("SP"));
        assertNull(result.get("SP"));
    }

    @Test
    public void testCalculateElapsedTimeUsingReflection() throws Exception {
        // Arrange
        Method method = ACLCallable.class.getDeclaredMethod("calculateElapsedTime", Long.class, Long.class);
        method.setAccessible(true);  // Make private method accessible

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
}Key changes made:Return Type Fix: Changed the mock responses from Object to List<String> for methods like fetchACLMatrixMainMenuList, fetchUnifiedSearchMatrix, and fetchAnalyticsList. This assumes these methods should return Lists - adjust the generic type (String) based on your actual implementation.Reflection Test: Added testCalculateElapsedTimeUsingReflection() which:Uses getDeclaredMethod to access the private calculateElapsedTime methodSets it accessible with setAccessible(true)Invokes it with method.invoke()Tests both cases (less than 1 second and more than 1 second)To make this work:Ensure your ACLMatrixService interface methods (fetchACLMatrixMainMenuList, etc.) are defined to return List<T> where T is the appropriate type.The reflection test doesn't need Mockito since it directly tests the private method's logic.If you're still getting failures, please share:The exact error messageThe ACLMatrixService interface definitionAny specific test case that's failingThis should resolve the List return type issue and properly test the private method using reflection.
