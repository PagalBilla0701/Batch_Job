To accommodate the additional field pageable in your response and the new fields under it, updates are required in your classes to match the structure of the response. Below are the necessary updates to your code:


---

1. Add a Pageable Class

This class represents the pageable section of the response.

public class Pageable {

    @JsonProperty("totalElements")
    private String totalElements;

    @JsonProperty("offset")
    private String offset;

    @JsonProperty("pageNumber")
    private String pageNumber;

    @JsonProperty("pageSize")
    private String pageSize;

    @JsonProperty("numberOfElements")
    private String numberOfElements;

    @JsonProperty("last")
    private boolean last;

    @JsonProperty("first")
    private boolean first;

    // Getters and Setters
}


---

2. Update SRComplaintResponseBody Class

Include the Pageable object.

public class SRComplaintResponseBody {

    private List<ServiceRequest> data;

    @JsonProperty("pageable")
    private Pageable pageable;

    // Getters and Setters
}


---

3. Update the ServiceRequest Class

Ensure all fields from the data array in the response are mapped correctly. It seems some annotations or fields were malformed in your pasted code. Here's the corrected class:

public class ServiceRequest {

    @JsonProperty("service-request-number")
    private String serviceRequestNumber;

    @JsonProperty("account-number")
    private String accountNumber;

    @JsonProperty("service-request-type")
    private String serviceRequestType;

    @JsonProperty("sr-last-process-group-name")
    private String srLastProcessGroupName;

    @JsonProperty("sr-last-process-group")
    private String srLastProcessGroup;

    @JsonProperty("service-request-status")
    private String serviceRequestStatus;

    @JsonProperty("service-owner-location")
    private String serviceOwnerLocation;

    @JsonProperty("service-request-created-date")
    private String serviceRequestCreatedDate;

    @JsonProperty("service-request-due-date")
    private String serviceRequestDueDate;

    @JsonProperty("service-request-closed-date")
    private String serviceRequestClosedDate;

    @JsonProperty("service-request-logger-url")
    private String serviceRequestLoggerUrl;

    @JsonProperty("service-request-handler-url")
    private String serviceRequestHandlerUrl;

    @JsonProperty("customer-name")
    private String customerName;

    @JsonProperty("service-request-category")
    private String serviceRequestCategory;

    @JsonProperty("service-request-esclation")
    private String serviceRequestEscalation;

    @JsonProperty("sr-current-process-group-name")
    private String srCurrentProcessGroupName;

    @JsonProperty("sr-current-process-group")
    private String srCurrentProcessGroup;

    @JsonProperty("service-request-current-handler")
    private String serviceRequestCurrentHandler;

    @JsonProperty("service-request-tat-standard")
    private String serviceRequestTatStandard;

    @JsonProperty("service-request-slamet")
    private String serviceRequestSlamet;

    @JsonProperty("user-id")
    private String userId;

    @JsonProperty("priority-name")
    private String priorityName;

    @JsonProperty("sr-call-activity-number")
    private String srCallActivityNumber;

    // Getters and Setters
}


---

4. Update Logic in IVRServiceRequestDetails

Use the pageable field from the response to incorporate pagination details.

if (responseEntity != null && HttpStatus.OK.equals(responseEntity.getStatusCode())) {
    SRComplaintResponseBody srResponseBody = responseEntity.getBody();

    if (srResponseBody != null) {
        List<ServiceRequest> serviceRequests = srResponseBody.getData();
        int openComplaintCount = (serviceRequests != null) ? serviceRequests.size() : 0;

        Pageable pageable = srResponseBody.getPageable();
        Log.info("Page Details - Page Number: {}, Total Elements: {}",
                 pageable.getPageNumber(), pageable.getTotalElements());

        ivrSRComplaintHoldingWrapper.setOpenComplaintsCount(openComplaintCount);
        ivrSRComplaintHoldingWrapper.setCountryCode(countryCode);

        return ivrSRComplaintHoldingWrapper;
    } else {
        Log.error("Error: No valid response from API");
    }
}


---

Summary

New Class: Pageable for the pageable section.

Modified Classes: SRComplaintResponseBody and ServiceRequest.

Logic Update: Adjust IVRServiceRequestDetails to log and utilize the new fields from Pageable.


These updates ensure your application handles the new response structure correctly. Let me know if you need further assistance!

