import com.fasterxml.jackson.annotation.JsonProperty;

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
        private SRCreationDateRange srCreationDateRange;

        @JsonProperty("page-navigation")
        private PageNavigation pageNavigation;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public SRCreationDateRange getSrCreationDateRange() {
            return srCreationDateRange;
        }

        public void setSrCreationDateRange(SRCreationDateRange srCreationDateRange) {
            this.srCreationDateRange = srCreationDateRange;
        }

        public PageNavigation getPageNavigation() {
            return pageNavigation;
        }

        public void setPageNavigation(PageNavigation pageNavigation) {
            this.pageNavigation = pageNavigation;
        }
    }

    public static class SRCreationDateRange {

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
}
