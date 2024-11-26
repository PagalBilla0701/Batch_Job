Got it! Let's break this down so it's clear.

You aim to fetch service request data using a REST API, and you want the response format to align with the provided structure, including data and pageable details. Let's walk through the logic you're working with and address how it will return the response correctly.


---

1. Understanding the Response

Your desired response format includes:

A list of ServiceRequest objects under data.

Paging metadata (totalElements, offset, pageNumber, etc.) under pageable.


2. Your Existing Implementation

Your service (IVRServiceRequestDetails) is doing the following:

1. It constructs a request body (SRRequestBody) for the API call.


2. Sends the request to an external service using IVRSRResponseEntityService.getReponseEntityForSR().


3. Retrieves and parses the response.


4. Extracts the data list and calculates the open complaint count.



The problem here is that you're only returning ivrSRComplaintHoldingWrapper with a count and country code, but you need to structure the full response.


---

3. Changes Needed

Here’s what you need to do:

(a) Include pageable and data in your response

Instead of just returning ivrSRComplaintHoldingWrapper, include:

The list of ServiceRequest objects from SRComplaintResponseBody.

Paging metadata extracted from the response.


(b) Update the return type

Your invoke() method should return an object that matches the required structure:

{
  "data": [ ... ], // List of ServiceRequest objects
  "pageable": { ... } // Paging details
}

(c) Implement the logic

Here’s the updated invoke() method:


---

Updated invoke() Method

@Override
public Object invoke(Map<String, Object> reqParamMap) {
    String countryCode = reqParamMap.get("countryCode") != null ? reqParamMap.get("countryCode").toString() : null;
    SRRequestBody srRequestBody = new SRRequestBody();

    // Prepare the request body
    SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();
    srStatusEnquiry.setCustomerId("0150000350F");

    SRRequestBody.SRCreationDateRange srDateRange = new SRRequestBody.SRCreationDateRange();
    srDateRange.setFromDate("23-07-2023");
    srDateRange.setToDate("23-10-2023");
    srStatusEnquiry.setSrCreationDateRange(srDateRange);

    SRRequestBody.PageNavigation pageNavigation = new SRRequestBody.PageNavigation();
    pageNavigation.setPageNavigationFilter("Y");
    pageNavigation.setPageNo("1");
    pageNavigation.setPageSize("30");
    srStatusEnquiry.setPageNavigation(pageNavigation);

    srRequestBody.setSrStatusEnquiry(srStatusEnquiry);

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

    ResponseEntity<SRComplaintResponseBody> responseEntity;
    try {
        responseEntity = ivrSRResponseEntityService.getReponseEntityforSR(
                srRequestBodyMap, "URL12", "ServiceRequest", "summary", countryCode
        );

        if (responseEntity != null && HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            SRComplaintResponseBody srResponseBody = responseEntity.getBody();
            if (srResponseBody != null) {
                // Extract data and pageable details
                List<ServiceRequest> serviceRequests = srResponseBody.getData();
                Pageable pageable = srResponseBody.getPageable();

                // Construct and return the final response
                Map<String, Object> finalResponse = new HashMap<>();
                finalResponse.put("data", serviceRequests);
                finalResponse.put("pageable", pageable);
                return finalResponse;
            }
        }

        // Handle no response or error case
        throw new RuntimeException("No valid response from CTOM API");
    } catch (Exception e) {
        log.error("Error while fetching response from CTOM API", e);
        throw new RuntimeException(e.getMessage(), e);
    }
}


---

4. How It Works

1. Request Body Creation:

The logic creates an appropriate request object (srRequestBody) with necessary parameters, including date range and pagination details.



2. API Call:

The ivrSRResponseEntityService.getReponseEntityForSR() makes the REST API call and fetches the response.



3. Response Handling:

It extracts data (list of service requests) and pageable (paging metadata) from the response body.



4. Final Response:

Combines data and pageable into a Map<String, Object> and returns it.





---

5. Ensure Correct Mapping in SRComplaintResponseBody

Make sure SRComplaintResponseBody includes pageable:

public class SRComplaintResponseBody {
    private List<ServiceRequest> data;

    private Pageable pageable; // Include pageable details
}

public class Pageable {
    private String totalElements;
    private String offset;
    private String pageNumber;
    private String pageSize;
    private String numberOfElements;
    private boolean last;
    private boolean first;
}


---

6. Response Example

If the API response contains:

{
  "data": [ { "service-request-number": "202411000286", ... } ],
  "pageable": { "totalElements": "14", "pageNumber": "1", "pageSize": "30", ... }
}

Your method will return:

{
  "data": [
    { "service-request-number": "202411000286", ... }
  ],
  "pageable": {
    "totalElements": "14",
    "pageNumber": "1",
    "pageSize": "30",
    ...
  }
}


---

Let me know if you need more details on any part of this!

