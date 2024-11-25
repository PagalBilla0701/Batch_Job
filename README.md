import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SRRequestBody {

    @JsonProperty("sr-status-enquiry")
    private SRStatusEnquiry srStatusEnquiry;

    public SRStatusEnquiry getSrStatusEnquiry() {
        return srStatusEnquiry;
    }

    public void setSrStatusEnquiry(SRStatusEnquiry srStatusEnquiry) {
        this.srStatusEnquiry = srStatusEnquiry;
    }

    public static class SRStatusEnquiry {

        @JsonProperty("customer-id")
        private String customerId;

        @JsonProperty("sr-creation-date-range")
        private SRDateRange srCreationDateRange;

        @JsonProperty("page-navigation")
        private PageNavigation pageNavigation;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public SRDateRange getSrCreationDateRange() {
            return srCreationDateRange;
        }

        public void setSrCreationDateRange(SRDateRange srCreationDateRange) {
            this.srCreationDateRange = srCreationDateRange;
        }

        public PageNavigation getPageNavigation() {
            return pageNavigation;
        }

        public void setPageNavigation(PageNavigation pageNavigation) {
            this.pageNavigation = pageNavigation;
        }
    }

    public static class SRDateRange {

        @JsonProperty("fromdate")
        private String fromDate;

        @JsonProperty("todate")
        private String toDate;

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }
    }

    public static class PageNavigation {

        @JsonProperty("page-navigation-filter")
        private String pageNavigationFilter;

        @JsonProperty("page-no")
        private String pageNo;

        @JsonProperty("page-size")
        private String pageSize;

        public String getPageNavigationFilter() {
            return pageNavigationFilter;
        }

        public void setPageNavigationFilter(String pageNavigationFilter) {
            this.pageNavigationFilter = pageNavigationFilter;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }
    }

    public static void main(String[] args) throws Exception {
        // Create the request body
        SRRequestBody srRequestBody = new SRRequestBody();

        SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();
        srStatusEnquiry.setCustomerId("0150000350F");

        // Set date range
        SRDateRange srDateRange = new SRDateRange();
        srDateRange.setFromDate("23-07-2023");
        srDateRange.setToDate("23-10-2023");
        srStatusEnquiry.setSrCreationDateRange(srDateRange);

        // Set page navigation
        PageNavigation pageNavigation = new PageNavigation();
        pageNavigation.setPageNavigationFilter("Y");
        pageNavigation.setPageNo("1");
        pageNavigation.setPageSize("30");
        srStatusEnquiry.setPageNavigation(pageNavigation);

        // Add to request body
        srRequestBody.setSrStatusEnquiry(srStatusEnquiry);

        // Convert to map for API consumption
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

        // Print the request body map
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(srRequestBodyMap));
    }
}
