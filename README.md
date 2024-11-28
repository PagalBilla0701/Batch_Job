@Override
public Object invoke(Map<String, Object> reqParamMap) {
    // Extract country code if present
    String countryCode = reqParamMap.get("countryCode") != null ? reqParamMap.get("countryCode").toString() : null;

    // Create request body and status enquiry objects
    SRRequestBody srRequestBody = new SRRequestBody();
    SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();

    // Set customer ID
    String customerId = (String) reqParamMap.get("customer-id");
    srStatusEnquiry.setCustomerId(customerId);

    // Set date range dynamically
    Map<String, Object> srCreationDateRangeMap = (Map<String, Object>) reqParamMap.get("sr-creation-date-range");
    if (srCreationDateRangeMap != null) {
        String fromDate = (String) srCreationDateRangeMap.get("fromdate");
        String toDate = (String) srCreationDateRangeMap.get("todate");

        SRRequestBody.SRCreationDateRange srDateRange = new SRRequestBody.SRCreationDateRange();
        srDateRange.setFromDate(fromDate);
        srDateRange.setToDate(toDate);

        srStatusEnquiry.setSrCreationDateRange(srDateRange);
    }

    // Set page navigation dynamically
    Map<String, Object> pageNavigationMap = (Map<String, Object>) reqParamMap.get("page-navigation");
    if (pageNavigationMap != null) {
        String pageNavigationFilter = (String) pageNavigationMap.get("page-navigation-filter");
        String pageNo = (String) pageNavigationMap.get("page-no");
        String pageSize = (String) pageNavigationMap.get("page-size");

        SRRequestBody.PageNavigation pageNavigation = new SRRequestBody.PageNavigation();
        pageNavigation.setPageNavigationFilter(pageNavigationFilter);
        pageNavigation.setPageNo(pageNo);
        pageNavigation.setPageSize(pageSize);

        srStatusEnquiry.setPageNavigation(pageNavigation);
    }

    // Add SRStatusEnquiry to the request body
    srRequestBody.setSrStatusEnquiry(srStatusEnquiry);

    // Return the populated request body object
    return srRequestBody;
}
