import com.scb.cems.central.beans.LoginBean;
import com.scb.cems.central.services.favorites.model.Favourites;
import com.scb.cems.dashboard.action.DashBoardAction;
import com.scb.cems.dashboard.data.model.DashBoardData;
import com.scb.cems.data.assembler.domain.SectionDataResponse;
import com.scb.cems.model.*;
import com.scb.cems.model.request.FavCustomerServiceRequest;
import com.scb.cems.servicehelper.FavouritesHelper;
import com.scb.cems.servicehelper.RecentItemsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceImplTest {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Mock
    private DashBoardAction dashboardAction;

    @Mock
    private FavouritesHelper favouritesHelper;

    @Mock
    private RecentItemsHelper recentItemsHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadDashboardSections() {
        List<DashBoardData> dashboardDataList = new ArrayList<>();
        DashBoardData announcementData = new DashBoardData();
        announcementData.setIdParam("ANNOUNCEMENT_PARAM_ID");
        announcementData.setAnnouncementDesc("New Announcement");

        DashBoardData quickLinkData = new DashBoardData();
        quickLinkData.setIdParam("QUICKLINK_PARAM_ID");
        quickLinkData.setQuickLinkTitle("Quick Link");
        quickLinkData.setQuickLinkURL("http://quicklink.com");

        dashboardDataList.add(announcementData);
        dashboardDataList.add(quickLinkData);

        when(dashboardAction.getDashBoardData(anyString(), anyString())).thenReturn(dashboardDataList);

        DashboardItems result = dashboardService.loadDashboardSections("IN", "EN");

        assertNotNull(result);
        assertEquals(1, result.getQuickLinks().size());
        assertEquals("Quick Link", result.getQuickLinks().get(0).getTitle());
        assertNotNull(result.getAnnouncement());
        assertEquals("New Announcement", result.getAnnouncement().getTitle());
    }

    @Test
    public void testSortQuickLinks() {
        List<QuickLinks> quickLinks = new ArrayList<>();
        quickLinks.add(new QuickLinks("B-Link", "EXTERNAL", "http://blink.com"));
        quickLinks.add(new QuickLinks("A-Link", "EXTERNAL", "http://alink.com"));

        dashboardService.sortQuickLinks(quickLinks);

        assertEquals("A-Link", quickLinks.get(0).getTitle());
    }

    @Test
    public void testLoadRecentAndFavoriteItems() {
        when(recentItemsHelper.loadRecentItems(anyString(), anyString(), any(), anyString(), any(LoginBean.class)))
                .thenReturn(new RecentFavouriteItems());
        when(favouritesHelper.loadFavoriteItems(anyString(), anyString(), any(), anyString(), any(LoginBean.class)))
                .thenReturn(new RecentFavouriteItems());

        List<RecentFavouriteItems> result = dashboardService.loadRecentAndFavoriteItems(
                "user", "IN", new LinkedHashMap<>(), "EN", new LoginBean());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testLoadRecentItemData() {
        List<SectionData> sectionDataList = new ArrayList<>();
        sectionDataList.add(new SectionData());

        when(recentItemsHelper.loadRecentItemData(anyString(), anyString(), any(), anyString(), any(LoginBean.class), anyString()))
                .thenReturn(sectionDataList);

        List<SectionData> result = dashboardService.loadRecentItemData(
                "user", "IN", new LinkedHashMap<>(), "EN", new LoginBean(), "category");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testLoadFavouriteItemData() {
        List<SectionData> sectionDataList = new ArrayList<>();
        sectionDataList.add(new SectionData());

        when(favouritesHelper.loadFavouriteItemData(anyString(), anyString(), any(), anyString(), any(LoginBean.class), anyString()))
                .thenReturn(sectionDataList);

        List<SectionData> result = dashboardService.loadFavouriteItemData(
                "user", "IN", new LinkedHashMap<>(), "EN", new LoginBean(), "category");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetFavouriteFullList() {
        SectionDataResponse response = new SectionDataResponse();
        when(favouritesHelper.getFavouriteFullList(any(FavCustomerServiceRequest.class), any(LoginBean.class)))
                .thenReturn(response);

        SectionDataResponse result = dashboardService.getFavouriteFullList(new FavCustomerServiceRequest(), new LoginBean());

        assertNotNull(result);
    }

    @Test
    public void testGetOldFavRecords() {
        List<Favourites> favouritesList = new ArrayList<>();
        favouritesList.add(new Favourites());

        when(favouritesHelper.getOldFavRecords(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(favouritesList);

        List<Favourites> result = dashboardService.getOldFavRecords("id", "accountCode", "itemType", "user", "IN");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateFavourite() {
        when(favouritesHelper.updateFavourite(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("Success");

        String result = dashboardService.updateFavourite("id", "name", "type", "account", "currency", "user", "IN");

        assertEquals("Success", result);
    }

    @Test
    public void testAddRecordToRecentItem() {
        doNothing().when(recentItemsHelper).addRecordToRecentItem(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        dashboardService.addRecordToRecentItem("id", "name", "type", "account", "currency", "recordId", "rowId", "user", "IN");

        verify(recentItemsHelper, times(1)).addRecordToRecentItem(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}
