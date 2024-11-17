@Test
public void testGenesysTransfer_Success() throws Exception {
    // Test Input
    String callRefNo = "SG23456789";
    String refNo = "23456789";
    String customerId = "custId123";
    String countryShortDesc = "SGP";

    // Mock LoginBean and UserBean
    UserBean userBean = new UserBean();
    userBean.setCountryShortDesc(countryShortDesc);
    LoginBean login = new LoginBean();
    login.setUserBean(userBean);

    // Mock CallActivity
    CallActivity mockCallActivity = new CallActivity();
    mockCallActivity.setLang("EN");
    mockCallActivity.setCustomerSegment("Priority");
    mockCallActivity.setCallerId("12345");
    mockCallActivity.setDnis("67890");
    mockCallActivity.setIdntfTyp("Passport");
    mockCallActivity.setIdBlockcode("BLOCK1");
    mockCallActivity.setAuthBlockCode("AUTH1");
    mockCallActivity.setSelfSrvcCode("SELF1");
    mockCallActivity.setAvailableAuth("OTP|ST");
    mockCallActivity.setCustIdIVR("A0198678");
    mockCallActivity.setLastMobno("9876543210");
    mockCallActivity.setRmn("rmn123");
    mockCallActivity.setOneFa("2+1");
    mockCallActivity.setTwoFa("3+1");
    mockCallActivity.setOneFaVerified(true);
    mockCallActivity.setTwoFaVerified(true);

    // Mock Dependencies
    when(callActivityAction.getCallActivityByRefNo(refNo, countryShortDesc)).thenReturn(mockCallActivity);

    Map<String, String> transferPointsMap = new HashMap<>();
    transferPointsMap.put("Start", "Start Menu");
    transferPointsMap.put("MM", "Main menu");
    transferPointsMap.put("ARE", "Account related enquiries");
    transferPointsMap.put("PBM", "Phone banking menu");
    transferPointsMap.put("BE", "Balance Enquiry");
    transferPointsMap.put("CASA", "CASA menu");
    transferPointsMap.put("CCPL", "CCPL Menu");

    when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenAnswer(invocation -> invocation.getArgument(0));

    // Call the method under test
    ModelMap model = new ModelMap();
    Object response = controller.genesysTransfer(login, callRefNo, customerId, model);

    // Verify Results
    assertNotNull(response);
    assertTrue(response instanceof Map);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;

    assertEquals("EN", responseMap.get("CTI_LANGUAGE"));
    assertEquals("Priority", responseMap.get("CTI_SEGMENT"));
    assertEquals("12345", responseMap.get("CTI_CALLERID"));
    assertEquals("67890", responseMap.get("CTI_DNIS"));
    assertEquals("Passport", responseMap.get("CTI_IDENTIFICATIONTYPE"));
    assertEquals("BLOCK1", responseMap.get("CTI_IDENT_BLOCKCODES"));
    assertEquals("AUTH1", responseMap.get("CTI_AUTH_BLOCKCODES"));
    assertEquals("SELF1", responseMap.get("CTI_SELF_SERVICE_BLOCKCODES"));
    assertEquals("OTP|ST", responseMap.get("CTI_AVAILABLEAUTHENTICATION"));
    assertEquals("A0198678", responseMap.get("CTI_RELATIONSHIPID"));
    assertEquals("9876543210", responseMap.get("CTI_MOBILE_NUMBER"));
    assertEquals("rmn123", responseMap.get("CTI_RMN"));
    assertEquals("2+1/5", responseMap.get("CTI_VER1"));
    assertEquals("3+1|S", responseMap.get("CTI_VER2"));
    assertEquals(transferPointsMap, responseMap.get("transferPoints"));

    // Verify Interactions
    verify(callActivityAction).getCallActivityByRefNo(refNo, countryShortDesc);
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
}
