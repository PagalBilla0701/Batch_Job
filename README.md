@Test
public void testGenesysTriggerAttachedData() throws Exception {
    // Test inputs
    String callRefNo = "SG20151125037392";
    String relId = "REL123";
    String type = "TYPE_A";
    String customerName = "John Doe";
    String productType = "Savings";
    String accountNumber = "ACC123";

    // Mock loginBean and userBean
    LoginBean loginBean = new LoginBean();
    UserBean userBean = new UserBean();
    userBean.setCountryCode("65");
    userBean.setCountryShortDesc("SG");
    userBean.setPeoplewiseId("PWISE123"); // Ensure this is set
    loginBean.setUserBean(userBean);

    // Mock CallActivity
    CallActivity mockCallActivity = new CallActivity();
    mockCallActivity.setLang("EN");
    mockCallActivity.setCustomerSegment("Premium");
    mockCallActivity.setCallerId("CallerID123");
    mockCallActivity.setDnis("DNIS123");
    mockCallActivity.setIdntfTyp("IDType123");
    mockCallActivity.setIdBlockcode("Block123");
    mockCallActivity.setAuthBlockCode("AuthBlock123");
    mockCallActivity.setSelfSrvcCode("SelfService123");
    mockCallActivity.setAvailableAuth("AuthAvailable");
    mockCallActivity.setRmn("9876543210");
    mockCallActivity.setMobileNo("9876543210");
    mockCallActivity.setAni("CEMS_PWISE123"); // Correct ANI to match expected
    mockCallActivity.setOutboundCall("Y");
    mockCallActivity.setOneFa("OneFAIS"); // Ensure these match expected values
    mockCallActivity.setTwoFa("TwoFAIS");

    // Mock dependencies
    when(callActivityAction.getCallActivityByRefNo(callRefNo, "SG")).thenReturn(mockCallActivity);

    Map<String, Object> expectedFormFields = new HashMap<>();
    expectedFormFields.put("CTI_LANGUAGE", "EN");
    expectedFormFields.put("CTI_SEGMENT", "Premium");
    expectedFormFields.put("CTI_CALLERID", "CallerID123");
    expectedFormFields.put("CTI_DNIS", "DNIS123");
    expectedFormFields.put("CTI_IDENTIFICATIONTYPE", "IDType123");
    expectedFormFields.put("CTI_IDENT_BLOCKCODES", "Block123");
    expectedFormFields.put("CTI_AUTH_BLOCKCODES", "AuthBlock123");
    expectedFormFields.put("CTI_SELF_SERVICE_BLOCKCODES", "SelfService123");
    expectedFormFields.put("CTI_AVAILABLEAUTHENTICATION", "AuthAvailable");
    expectedFormFields.put("CTI_RELATIONSHIPID", relId);
    expectedFormFields.put("CTI_MOBILE_NUMBER", "9876543210");
    expectedFormFields.put("CTI_RMN", "9876543210");
    expectedFormFields.put("refNo", callRefNo);
    expectedFormFields.put("CTI_VER1", "OneFAIS");
    expectedFormFields.put("CTI_VER2", "TwoFAIS");
    expectedFormFields.put("CTI_ANI", "CEMS_PWISE123");
    expectedFormFields.put("ENTRY_POINT", type);

    // Ensure the mock service returns expected fields
    when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(expectedFormFields);

    // Call the method under test
    ModelMap model = new ModelMap();
    Object response = controller.genesysTriggerAttachedData(
        callRefNo, relId, type, customerName, productType, accountNumber, loginBean, model
    );

    // Verify results
    assertNotNull(response);
    assertTrue(response instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;

    // Validate responseMap contents
    assertEquals("EN", responseMap.get("CTI_LANGUAGE"));
    assertEquals("Premium", responseMap.get("CTI_SEGMENT"));
    assertEquals("CallerID123", responseMap.get("CTI_CALLERID"));
    assertEquals("DNIS123", responseMap.get("CTI_DNIS"));
    assertEquals("IDType123", responseMap.get("CTI_IDENTIFICATIONTYPE"));
    assertEquals("Block123", responseMap.get("CTI_IDENT_BLOCKCODES"));
    assertEquals("AuthBlock123", responseMap.get("CTI_AUTH_BLOCKCODES"));
    assertEquals("SelfService123", responseMap.get("CTI_SELF_SERVICE_BLOCKCODES"));
    assertEquals("AuthAvailable", responseMap.get("CTI_AVAILABLEAUTHENTICATION"));
    assertEquals(relId, responseMap.get("CTI_RELATIONSHIPID"));
    assertEquals("9876543210", responseMap.get("CTI_MOBILE_NUMBER"));
    assertEquals("9876543210", responseMap.get("CTI_RMN"));
    assertEquals(callRefNo, responseMap.get("refNo"));
    assertEquals("OneFAIS", responseMap.get("CTI_VER1"));
    assertEquals("TwoFAIS", responseMap.get("CTI_VER2"));
    assertEquals("CEMS_PWISE123", responseMap.get("CTI_ANI"));
    assertEquals(type, responseMap.get("ENTRY_POINT"));

    // Verify mock interactions
    verify(callActivityAction).getCallActivityByRefNo(callRefNo, "SG");
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
}
