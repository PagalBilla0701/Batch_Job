import org.springframework.test.util.ReflectionTestUtils;

@Test
public void testGetResponseEntityForSR_Success() throws Exception {
    // Arrange
    ObjectMapper objectMapper = new ObjectMapper();
    SRRequestBody srRequestBody = new SRRequestBody();
    SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();

    srStatusEnquiry.setCustomerId("0150000350F");

    LocalDate currentDate = LocalDate.now();
    LocalDate previousDate = currentDate.minusDays(180);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    SRRequestBody.SRCreationDateRange srDateRange = new SRRequestBody.SRCreationDateRange();
    srDateRange.setFromDate(previousDate.format(formatter));
    srDateRange.setToDate(currentDate.format(formatter));

    srStatusEnquiry.setSrCreationDateRange(srDateRange);

    SRRequestBody.PageNavigation pageNavigation = new SRRequestBody.PageNavigation();
    pageNavigation.setPageNavigationFilter("Y");
    pageNavigation.setPageNo("1");
    pageNavigation.setPageSize("1");

    srStatusEnquiry.setPageNavigation(pageNavigation);
    srRequestBody.setSrStatusEnquiry(srStatusEnquiry);

    Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

    String idParam = "URL12";
    String xParamKey1 = "ServiceRequest";
    String xParamKey2 = "summary";
    String countryCodeForParam = "MY";

    // Mocking getParam behavior
    Param mockParam = new Param(idParam);
    mockParam.setCountryCode(countryCodeForParam);
    mockParam.setKeys(new String[] { xParamKey1, xParamKey2 });

    Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(mockParam);

    // Mocking RestTemplate response
    SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
    ResponseEntity<SRComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

    Mockito.when(restTemplate.postForEntity(
            Mockito.anyString(),
            Mockito.any(HttpEntity.class),
            Mockito.eq(SRComplaintResponseBody.class)))
            .thenReturn(mockResponseEntity);

    // Act
    // Use Reflection to invoke the private method
    Map<String, String> paramData = new HashMap<>();
    paramData.put("service_url", "http://mock-service-url.com");

    ReflectionTestUtils.setField(ivrService, "restTemplate", restTemplate);
    ReflectionTestUtils.invokeMethod(
            ivrService,
            "getSREndPointURL",
            idParam,
            xParamKey1,
            xParamKey2,
            countryCodeForParam
    );

    ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
            srRequestBodyMap, idParam, xParamKey1, xParamKey2, countryCodeForParam);

    // Assert
    assertNotNull("Response should not be null", response);
    assertEquals("Status code should be 200 OK", HttpStatus.OK, response.getStatusCode());
    assertNotNull("Response body should not be null", response.getBody());

    // Verify interactions
    Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any(Param.class));
    Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(
            Mockito.anyString(),
            Mockito.any(HttpEntity.class),
            Mockito.eq(SRComplaintResponseBody.class));
}
