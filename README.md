@Test
public void testGenesysInitiateOutboundData() throws Exception {
    // Mock LoginBean and UserBean
    LoginBean loginBean = new LoginBean();
    UserBean userBean = new UserBean();
    userBean.setCountryCode("65");
    loginBean.setUserBean(userBean);

    // Test inputs
    String customerId = "CUST123";
    String mobilePhoneNumber = "9876543210";

    // Mock customer info from the service
    Map<String, String> mockCustomerInfo = new HashMap<>();
    mockCustomerInfo.put("mobilePhoneNumber", mobilePhoneNumber);

    when(callActivityService.getCustomerInfo("65", customerId)).thenReturn(mockCustomerInfo);

    // Mock response from makeResponseWrapper
    Map<String, Object> mockResponse = new HashMap<>();
    mockResponse.put("customerId", customerId);
    mockResponse.put("mobilePhoneNumber", mobilePhoneNumber);

    when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(mockResponse);

    // Call the method under test
    ModelMap model = new ModelMap();
    Object response = controller.genesysInitiateOutboundData(loginBean, customerId, model);

    // Verify the response
    assertNotNull(response);
    assertTrue(response instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;

    // Validate successful response
    assertEquals(customerId, responseMap.get("customerId"));
    assertEquals(mobilePhoneNumber, responseMap.get("mobilePhoneNumber"));

    // Verify service interactions
    verify(callActivityService).getCustomerInfo("65", customerId);
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
}
