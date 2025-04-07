Here's a JUnit 4 test class for the ACLMatrixServiceImpl class, testing all its methods using Mockito for mocking dependencies:package com.scb.cems.serviceImpl;

import com.scb.cems.central.services.login.repository.ACLMatrixRepository;
import com.scb.cems.crmxt.model.ACLMatrixMainMenu;
import com.scb.cems.crmxt.model.HighPrivilegeUserRoleDetails;
import com.scb.cems.crmxt.model.RoleDetails;
import com.scb.cems.crmxt.model.SAVSectionAccessDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ACLMatrixServiceImplTest {

    @Mock
    private ACLMatrixRepository aclMatrixRepository;

    @InjectMocks
    private ACLMatrixServiceImpl aclMatrixService;

    private static final String COUNTRY_CODE = "TH";
    private static final String INS_CODE = "SCB";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchACLMatrixMainMenuList() {
        // Arrange
        List<ACLMatrixMainMenu> expectedList = Arrays.asList(new ACLMatrixMainMenu());
        when(aclMatrixRepository.fetchACLMatrixMainMenuList(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<ACLMatrixMainMenu> result = aclMatrixService.fetchACLMatrixMainMenuList(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchACLMatrixMainMenuList(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchSAVAccessSectionList() {
        // Arrange
        List<SAVSectionAccessDetails> expectedList = Arrays.asList(new SAVSectionAccessDetails());
        when(aclMatrixRepository.fetchSAVAccessSectionList(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<SAVSectionAccessDetails> result = aclMatrixService.fetchSAVAccessSectionList(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchSAVAccessSectionList(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchRoleList() {
        // Arrange
        List<RoleDetails> expectedList = Arrays.asList(new RoleDetails());
        when(aclMatrixRepository.fetchRoleList(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<RoleDetails> result = aclMatrixService.fetchRoleList(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchRoleList(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchHighPrivilegeRoleList() {
        // Arrange
        List<HighPrivilegeUserRoleDetails> expectedList = Arrays.asList(new HighPrivilegeUserRoleDetails());
        when(aclMatrixRepository.fetchHighPrivileageRoleList())
                .thenReturn(expectedList);

        // Act
        List<HighPrivilegeUserRoleDetails> result = aclMatrixService.fetchHighPrivileageRoleList();

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchHighPrivileageRoleList();
    }

    @Test
    public void testFetchUnifiedSearchMatrix() {
        // Arrange
        List<ACLMatrixMainMenu> expectedList = Arrays.asList(new ACLMatrixMainMenu());
        when(aclMatrixRepository.fetchUnifiedSearchMatrix(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<ACLMatrixMainMenu> result = aclMatrixService.fetchUnifiedSearchMatrix(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchUnifiedSearchMatrix(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchAnalyticsList() {
        // Arrange
        List<ACLMatrixMainMenu> expectedList = Arrays.asList(new ACLMatrixMainMenu());
        when(aclMatrixRepository.fetchAnalyticsList(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<ACLMatrixMainMenu> result = aclMatrixService.fetchAnalyticsList(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchAnalyticsList(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchStartNewMenu() {
        // Arrange
        List<ACLMatrixMainMenu> expectedList = Arrays.asList(new ACLMatrixMainMenu());
        when(aclMatrixRepository.fetchStartNewMenu(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<ACLMatrixMainMenu> result = aclMatrixService.fetchStartNewMenu(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchStartNewMenu(COUNTRY_CODE, INS_CODE);
    }

    @Test
    public void testFetchCCMainTabsMenu() {
        // Arrange
        List<ACLMatrixMainMenu> expectedList = Arrays.asList(new ACLMatrixMainMenu());
        when(aclMatrixRepository.fetchCCMainTabsMenu(COUNTRY_CODE, INS_CODE))
                .thenReturn(expectedList);

        // Act
        List<ACLMatrixMainMenu> result = aclMatrixService.fetchCCMainTabsMenu(COUNTRY_CODE, INS_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(aclMatrixRepository).fetchCCMainTabsMenu(COUNTRY_CODE, INS_CODE);
    }
}This test class includes:Necessary imports for JUnit 4 and Mockito@RunWith(MockitoJUnitRunner.class) to enable Mockito annotationsMocked ACLMatrixRepository using @MockThe service implementation under test using @InjectMocksTest constants for country code and institution codeA @Before setup method to initialize mocksIndividual test methods for each service methodEach test method follows the Arrange-Act-Assert pattern:Arrange: Sets up mock behavior and expected resultsAct: Calls the service method being testedAssert: Verifies the results and mock interactionsThe tests:Verify that the methods return non-null resultsCheck that the returned lists match the expected mock resultsVerify that the repository methods are called with correct parametersUse Mockito's when().thenReturn() to mock repository responsesUse verify() to ensure proper repository method invocationNote: There were some syntax errors in the original code (e.g., misplaced curly braces, typos in method names) which I've corrected in the test class assumptions. The test class assumes the corrected method signatures from the service implementation.To run these tests, you'll need the following dependencies in your project:JUnit 4MockitoSpring Test (for Spring-specific testing utilities)You might need to adjust the package names or import statements based on your actual project structure.
