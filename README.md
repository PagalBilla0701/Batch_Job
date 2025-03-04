import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.scb.cems.central.beans.LoginBean;
import com.scb.cems.data.assembler.domain.AppException;
import com.scb.cems.entitlement.data.model.MenuItems;
import com.scb.cems.entitlement.repository.MenuAccessRepository;
import com.scb.cems.exceptions.MenuServiceRuntimeException;
import com.scb.cems.model.MainNavigationItems;
import com.scb.cems.model.StartMenuItems;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceImplTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMaintenanceErrorByMenuDispName_Success() {
        List<Param> mockParams = new ArrayList<>();
        Param param = new Param();
        param.setCountryCode("IN");
        param.setKeys(new String[]{"key1", "N", "TestMenu"});
        param.setData(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "true", "2024-01-01", "2025-01-01"});
        mockParams.add(param);

        when(paramRepository.getParamList(any())).thenReturn(mockParams);

        String result = menuService.getMaintenanceErrorByMenuDispName("TestMenu", "IN", "EN");
        assertNotNull(result);
    }

    @Test
    public void testGetMaintenanceErrorByMenuDispName_Fail_NoData() {
        when(paramRepository.getParamList(any())).thenReturn(Collections.emptyList());

        String result = menuService.getMaintenanceErrorByMenuDispName("TestMenu", "IN", "EN");
        assertNull(result);
    }

    @Test(expected = MenuServiceRuntimeException.class)
    public void testGetMainMenuAndStartMenu_Fail_NoAccess() {
        when(menuAccessRepository.getMainMenuAndStartMenu(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        menuService.getMainMenuAndStartMenu("user123", "inst1", "grp1", "IN", "EN", new LoginBean());
    }

    @Test
    public void testGetMainMenuAndStartMenu_Success() {
        List<MenuItems> menuItemsList = new ArrayList<>();
        MenuItems menuItem = new MenuItems();
        menuItem.setName("Dashboard");
        menuItem.setType("MainMenu");
        menuItem.setDisplayName("Dashboard");
        menuItem.setUrl("/dashboard");
        menuItem.setSalesObjectAccess("N");
        menuItemsList.add(menuItem);

        when(menuAccessRepository.getMainMenuAndStartMenu(any(), any(), any(), any(), any()))
                .thenReturn(menuItemsList);

        List<MainNavigationItems> result = menuService.getMainMenuAndStartMenu("user123", "inst1", "grp1", "IN", "EN", new LoginBean());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetSearchItems_Success() {
        List<MenuItems> searchItems = new ArrayList<>();
        MenuItems menuItem = new MenuItems();
        menuItem.setLabel("Search");
        menuItem.setSalesObjectAccess("N");
        searchItems.add(menuItem);

        when(menuAccessRepository.getSearchItems(any(), any(), any(), any(), any()))
                .thenReturn(searchItems);

        List<MenuItems> result = menuService.getSearchItems("user123", "inst1", "grp1", "IN", "EN");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test(expected = MenuServiceRuntimeException.class)
    public void testGetSearchItems_Fail_NoItems() {
        when(menuAccessRepository.getSearchItems(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        menuService.getSearchItems("user123", "inst1", "grp1", "IN", "EN");
    }

    @Test
    public void testGetKPIDashboardItems_Success() {
        List<MenuItems> kpiItems = new ArrayList<>();
        MenuItems menuItem = new MenuItems();
        menuItem.setName("KPI Report");
        kpiItems.add(menuItem);

        when(menuAccessRepository.getKPIDashboardItems(any(), any(), any(), any(), any()))
                .thenReturn(kpiItems);

        List<MenuItems> result = menuService.getKPIDashboardItems("user123", "inst1", "grp1", "IN", "EN");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetUserRole_Success() {
        when(menuAccessRepository.getUserRole(any(), any(), any())).thenReturn("Admin");

        String role = menuService.getUserRole("user123", "inst1", "IN");

        assertEquals("Admin", role);
    }

    @Test
    public void testGetUserRoleId_Success() {
        when(menuAccessRepository.getUserRoleId(any(), any(), any())).thenReturn(123);

        String roleId = menuService.getUserRoleId("user123", "inst1", "IN");

        assertEquals("123", roleId);
    }
}
