Thank you for sharing the complete details. Let me help ensure everything aligns correctly and address any gaps or errors.

Key Observations and Adjustments:

1. Typographical Errors:

There are several syntax errors (e.g., getReponseEntityforSR instead of getResponseEntityforSR, | =null? instead of != null ?, etc.).

Missing semicolons (;) and braces ({ and }) need to be corrected.

Annotations like @JsonProperty are improperly formatted (e.g., #JsonProperty and JsonProperty without @).



2. Proper Variable and Method Naming:

Follow consistent Java naming conventions (ivrSRResponseEntityService should not have spaces or underscores).



3. Logical Enhancements:

The logic for creating HttpHeaders can be refactored into a separate method for reusability.

Ensure null checks for objects like responseEntity.getBody() to avoid NullPointerException.



4. Fix the Output Response Logic:

Ensure that the response structure aligns with your desired format:

{
  "data": [ ... ],
  "pageable": {
    ...
  }
}

Properly map the ServiceRequest objects from the API response to the final structure.




Corrected and Refactored Code Snippets

IVRServiceRequestDetails Class

@Service("IVRServiceRequestDetails")
public class IVRServiceRequestDetails implements CemsSectionService {

    @Autowired
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Map<String, Object> reqParamMap) {
        String countryCode = reqParamMap.get("countryCode") != null ? reqParamMap.get("countryCode").toString() : null;

        SRRequestBody srRequestBody = new SRRequestBody();
        SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();
        srStatusEnquiry.setCustomerId("0150000350F");

        // Date range setup
        SRRequestBody.SRCreationDateRange srDateRange = new SRRequestBody.SRCreationDateRange();
        srDateRange.setFromDate("23-08-2023");
        srDateRange.setToDate("23-10-2023");
        srStatusEnquiry.setSrCreationDateRange(srDateRange);

        // Page navigation setup
        SRRequestBody.PageNavigation pageNavigation = new SRRequestBody.PageNavigation();
        pageNavigation.setPageNavigationFilter("Y");
        pageNavigation.setPageNo("1");
        pageNavigation.setPageSize("30");
        srStatusEnquiry.setPageNavigation(pageNavigation);

        srRequestBody.setSrStatusEnquiry(srStatusEnquiry);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

        IVRSRComplaintHoldingWrapper ivrSRComplaintHoldingWrapper = new IVRSRComplaintHoldingWrapper();
        ResponseEntity<SRComplaintResponseBody> responseEntity = null;

        try {
            responseEntity = ivrSRResponseEntityService.getResponseEntityForSR(
                srRequestBodyMap, "URL12", "ServiceRequest", "summary", countryCode);

            if (responseEntity != null && HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                SRComplaintResponseBody srResponseBody = responseEntity.getBody();

                if (srResponseBody != null) {
                    List<ServiceRequest> serviceRequests = srResponseBody.getData();
                    int openComplaintCount = (serviceRequests != null) ? serviceRequests.size() : 0;

                    ivrSRComplaintHoldingWrapper.setOpenComplaintsCount(openComplaintCount);
                    ivrSRComplaintHoldingWrapper.setCountryCode(countryCode);
                    return ivrSRComplaintHoldingWrapper;
                }
            }
        } catch (Exception e) {
            Log.error("Error while fetching response from CTOM", e);
        }

        return ivrSRComplaintHoldingWrapper;
    }
}

IVRSRResponseEntityService Class

@Service
@Slf4j
public class IVRSRResponseEntityService {

    @Autowired
    private ParamRepository paramRepository;

    @Resource
    private List<HttpMessageConverter<?>> customMessageConverters;

    public ResponseEntity<SRComplaintResponseBody> getResponseEntityForSR(
            Map<String, Object> srRequestBody, String idParam, String xParamKey1, String xParamKey2, String countryCode) throws Exception {

        ResponseEntity<SRComplaintResponseBody> responseEntity = null;

        try {
            // Build URL and headers
            Map<String, String> paramData = getSREndPointURL(xParamKey1, xParamKey2, countryCode, idParam);
            String serviceUrl = paramData.get("service_url");

            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(srRequestBody, headers);

            RestTemplate restTemplate = new RestTemplate(customMessageConverters);
            responseEntity = restTemplate.postForEntity(serviceUrl, requestEntity, SRComplaintResponseBody.class);

        } catch (Exception e) {
            Log.error("Error while fetching response from SR: " + e.getMessage(), e);
            throw e;
        }

        return responseEntity;
    }

    private Map<String, String> getSREndPointURL(String xParamKey1, String xParamKey2, String countryCode, String idParam) {
        Map<String, String> dataMap = new HashMap<>();
        Param param = new Param(idParam);
        param.setCountryCode(countryCode);
        param.getKeys()[0] = xParamKey1;
        param.getKeys()[1] = xParamKey2;

        Param results = paramRepository.getParam(param);
        if (results != null) {
            dataMap.put("service_url", results.getData()[6]);
        }

        return dataMap;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Market", "MY");
        headers.set("tracking-id", UUID.randomUUID().toString());
        headers.set("application-id", "SR-PENDING");
        headers.set("channel-id", "CEMSSP-CEMSCENTRAL");
        return headers;
    }
}

Let me know if you need additional refinements or explanations.

