package com.scb.cems.serviceImpl;

import com.scb.cems.central.beans.LoginBean;
import com.scb.cems.data.assembler.domain.AppException;
import com.scb.cems.entitlement.data.model.MenuItems;
import com.scb.cems.entitlement.repository.MenuAccessRepository;
import com.scb.cems.exceptions.MenuServiceRuntimeException;
import com.scb.cems.model.MainNavigationItems;
import com.scb.cems.model.StartMenuItems;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceImplTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private ParamRepository paramRepository;

    private List<MenuItems> mockMenuItems;
    private List<Param> mockParams;
    private LoginBean mockLoginBean;

    @Before
    public void setUp() {
        mockMenuItems = new ArrayList<>();
        mockParams = new ArrayList<>();
        mockLoginBean = new LoginBean();

        // Mock MenuItems
        MenuItems menuItem = new MenuItems();
        menuItem.setType("MainMenu");
        menuItem.setName("Dashboard");
        menuItem.setDisplayName("Dashboard");
        menuItem.setUrl("/dashboard");
        menuItem.setSalesObjectAccess("N");
        mockMenuItems.add(menuItem);

        // Mock Params
        Param param = new Param();
        param.setKeys(new String[]{"SYS", "N", "Dashboard"});
        param.setData(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Y", "2023-01-01", "2025-12-31"});
        mockParams.add(param);

        // Mock repository behavior
        when(menuAccessRepository.getMainMenuAndStartMenu(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockMenuItems);
        when(paramRepository.getParamList(anyString())).thenReturn(mockParams);
    }

    @Test
    public void testGetMaintenanceErrorByMenuDispName_ReturnsNull() {
        String result = menuService.getMaintenanceErrorByMenuDispName("NonExisting", "IN", "EN");
        assertNull(result);
    }

    @Test
    public void testGetMainMenuAndStartMenu_Success() {
        when(menuAccessRepository.getMainMenuAndStartMenu(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockMenuItems);

        List<MainNavigationItems> result = menuService.getMainMenuAndStartMenu("user1", "inst1", "grp1", "IN", "EN", mockLoginBean);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dashboard", result.get(0).getLabel());
    }

    @Test(expected = MenuServiceRuntimeException.class)
    public void testGetMainMenuAndStartMenu_NoAccess_ThrowsException() {
        when(menuAccessRepository.getMainMenuAndStartMenu(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        menuService.getMainMenuAndStartMenu("user1", "inst1", "grp1", "IN", "EN", mockLoginBean);
    }

    @Test
    public void testGetSearchItems_ReturnsItems() {
        when(menuAccessRepository.getSearchItems(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockMenuItems);

        List<MenuItems> result = menuService.getSearchItems("user1", "inst1", "grp1", "IN", "EN");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test(expected = MenuServiceRuntimeException.class)
    public void testGetSearchItems_NoItems_ThrowsException() {
        when(menuAccessRepository.getSearchItems(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(null);

        menuService.getSearchItems("user1", "inst1", "grp1", "IN", "EN");
    }

    @Test
    public void testGetUserRole_Success() {
        when(menuAccessRepository.getUserRole(anyString(), anyString(), anyString())).thenReturn("Admin");

        String result = menuService.getUserRole("user1", "inst1", "IN");
        assertEquals("Admin", result);
    }

    @Test
    public void testGetUserRoleId_Success() {
        when(menuAccessRepository.getUserRoleId(anyString(), anyString(), anyString())).thenReturn(1);

        String result = menuService.getUserRoleId("user1", "inst1", "IN");
        assertEquals("1", result);
    }
}
