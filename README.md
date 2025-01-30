@RunWith(MockitoJUnitRunner.class)
public class OpportunityServiceTest {

    @InjectMocks
    private OpportunityService opportunityService; // Assuming the class containing the method is named OpportunityService.

    @Mock
    private ValidationFactory validationFactory;

    @Mock
    private CemsUtil cemsUtil;

    @Mock
    private OpportunityFactory opportunityFactory;

    @Mock
    private ValidationEngine validationEngine;

    @Mock
    private OpportunityListingImpl opportunityListing;

    @Test
    public void testCreateOpportunity() throws Exception {
        // Arrange
        UserBean userBean = new UserBean();
        userBean.setCountryCode("US");
        userBean.setPeoplewiseId("12345");

        FormData formData = new FormData();
        formData.setApplicationId("1001");

        Map<String, Object> request = new HashMap<>();
        String sourceTimeZoneOffset = "UTC+0";

        Attributes attributes = new Attributes();
        OpportunityReqRespJson responseOpportunity = new OpportunityReqRespJson();
        responseOpportunity.setId("1001");
        responseOpportunity.setType("applications");

        Map<String, Object> responseMap = new HashMap<>();
        List<OpportunityReqRespJson> oppList = new ArrayList<>();
        oppList.add(responseOpportunity);
        responseMap.put("data", oppList);

        // Mocking dependencies
        when(validationFactory.getValidationEngine("US")).thenReturn(validationEngine);
        doNothing().when(validationEngine).performPreSaveActions(userBean, formData);
        doNothing().when(validationEngine).performPostSaveActions(eq(userBean), any(OpportunityReqRespJson.class));
        doNothing().when(cemsUtil).copyFormDataToAttributes(formData, attributes);

        when(opportunityFactory.getOpportunityListingImpl("US")).thenReturn(opportunityListing);
        when(opportunityListing.createOrUpdateOpportunity(eq(userBean), eq(request), anyMap(), eq(sourceTimeZoneOffset)))
            .thenReturn(responseMap);

        when(cemsUtil.convertToOpportunityPOJO(responseOpportunity)).thenReturn(responseOpportunity);

        // Act
        Map<String, Object> result = opportunityService.createOpportunity(userBean, request, formData, sourceTimeZoneOffset);

        // Assert
        assertNotNull(result);
        assertEquals(responseMap, result);
        verify(validationFactory).getValidationEngine("US");
        verify(validationEngine).performPreSaveActions(userBean, formData);
        verify(validationEngine).performPostSaveActions(eq(userBean), eq(responseOpportunity));
        verify(opportunityFactory).getOpportunityListingImpl("US");
        verify(opportunityListing).createOrUpdateOpportunity(eq(userBean), eq(request), anyMap(), eq(sourceTimeZoneOffset));
    }
}
