import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DashboardServiceImplTest {

    @Test
    public void testSortQuickLinksUsingReflection() throws Exception {
        // Step 1: Create an instance of DashboardServiceImpl
        DashboardServiceImpl dashboardService = new DashboardServiceImpl();

        // Step 2: Prepare test data
        List<QuickLinks> quickLinks = new ArrayList<>();
        quickLinks.add(new QuickLinks("Z-Link", "EXTERNAL", "https://zlink.com"));
        quickLinks.add(new QuickLinks("A-Link", "EXTERNAL", "https://alink.com"));
        quickLinks.add(new QuickLinks("M-Link", "EXTERNAL", "https://mlink.com"));

        // Step 3: Access and invoke the private sortQuickLinks method using reflection
        Method sortQuickLinksMethod = DashboardServiceImpl.class.getDeclaredMethod("sortQuickLinks", List.class);
        sortQuickLinksMethod.setAccessible(true); // Allow access to the private method
        sortQuickLinksMethod.invoke(dashboardService, quickLinks); // Invoke the method

        // Step 4: Verify the results
        assertEquals("A-Link", quickLinks.get(0).getTextValue());
        assertEquals("M-Link", quickLinks.get(1).getTextValue());
        assertEquals("Z-Link", quickLinks.get(2).getTextValue());
    }

    // Mock QuickLinks class for testing
    static class QuickLinks {
        private String textValue;
        private String linkType;
        private String linkURL;

        public QuickLinks(String textValue, String linkType, String linkURL) {
            this.textValue = textValue;
            this.linkType = linkType;
            this.linkURL = linkURL;
        }

        public String getTextValue() {
            return textValue;
        }
    }
}
