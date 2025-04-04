Here's a JUnit 4 test class for the ACLCallable class. I'll use Mockito for mocking dependencies and test the main call() method with different aclFor scenarios:package com.scb.cems.serviceImpl;

import com.scb.cems.crmxt.dao.CTOMACLDAO;
import com.scb.cems.crmxt.dao.SPACLDAO;
import com.scb.cems.service.ACLMatrixService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.HashMap;
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

    @Mock
    private Logger logger;

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
        Object mockResponse = new Object();
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
        Object mockResponse = new Object();
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
    publicusing System.currentTimeMillis() void testAnalytics() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "Analytics", spDAO, ctomDAO);
        Object mockResponse = new Object();
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
    public void testCTOMWithException() throws Exception {
        // Arrange
        aclCallable = new ACLCallable(ams, countryCode, insCode, "CTOM", spDAO, ctomDAO);
        when(ctomDAO.getComplaintModuleDetails(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // Act
        Map<String, Object> result = (Map<String, Object>) aclCallable.call();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("CTOM"));
        assertNull(result.get("CTOM"));
    }

    @Test
    public void testCalculateElapsedTimeLessThanSecond() {
        // Arrange
        Long startTime = 1000L;
        Long endTime = 1500L;

        // Act
        String result = aclCallable.calculateElapsedTime(startTime, endTime);

        // Assert
        assertEquals("500 ms", result);
    }

    @Test
    public void testCalculateElapsedTimeMoreThanSecond() {
        // Arrange
        Long startTime = 1000L;
        Long endTime = 2500L;

        // Act
        String result = aclCallable.calculateElapsedTime(startTime, endTime);

        // Assert
        assertEquals("1 s", result);
    }
}This test class:Uses MockitoJUnitRunner for dependency injection and mockingMocks all external dependencies (ACLMatrixService, SPACLDAO, CTOMACLDAO, Logger)Tests several key scenarios:MainMenu caseSearchItem caseAnalytics caseSP case with exception handlingCTOM case with exception handlingcalculateElapsedTime method for both <1s and >1s casesTo use this test, you'll need these dependencies in your pom.xml:<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>4.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>Key features of the tests:Verifies correct method calls on dependenciesChecks proper key-value pairs in the result mapTests exception handling for SP and CTOM casesValidates timing calculation logicUses proper mocking to isolate the unit under testNote: I didn't test every single aclFor case to keep the example concise, but you can easily extend it to cover all cases following the same pattern. Also, some method signatures in the original code had typos which I corrected in the test assumptions (e.g., fetchSAVccessSectionList → fetchSAVAccessSectionList). You might need to adjust the method names to match your actual interface definitions.
