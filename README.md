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

    // Mock dependencies
    Map<String, String> mockCustomerInfo = new HashMap<>();
    mockCustomerInfo.put("mobilePhoneNumber", mobilePhoneNumber);

    when(callActivityService.getCustomerInfo("65", customerId)).thenReturn(mockCustomerInfo);

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

    // Verify mock interactions
    verify(callActivityService).getCustomerInfo("65", customerId);
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
}

@Test
public void testGenesysInitiateOutboundData_CustomerNotFound() throws Exception {
    // Mock LoginBean and UserBean
    LoginBean loginBean = new LoginBean();
    UserBean userBean = new UserBean();
    userBean.setCountryCode("65");
    loginBean.setUserBean(userBean);

    // Test inputs
    String customerId = "CUST123";

    // Mock dependencies
    when(callActivityService.getCustomerInfo("65", customerId)).thenReturn(null);

    // Call the method under test
    ModelMap model = new ModelMap();
    Object response = controller.genesysInitiateOutboundData(loginBean, customerId, model);

    // Verify the response
    assertNotNull(response);
    assertTrue(response instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;

    // Validate error response
    assertEquals("Customer not found, cannot initiate outbound call", responseMap.get("error"));

    // Verify mock interactions
    verify(callActivityService).getCustomerInfo("65", customerId);
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(false));
}

@Test
public void testGenesysInitiateOutboundData_NullCustomerId() throws Exception {
    // Mock LoginBean and UserBean
    LoginBean loginBean = new LoginBean();
    UserBean userBean = new UserBean();
    userBean.setCountryCode("65");
    loginBean.setUserBean(userBean);

    // Call the method under test with null customerId
    ModelMap model = new ModelMap();
    Object response = controller.genesysInitiateOutboundData(loginBean, null, model);

    // Verify the response
    assertNotNull(response);
    assertTrue(response instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;

    // Validate error response
    assertEquals("Customer Id is null, cannot initiate outbound call", responseMap.get("error"));

    // Verify mock interactions
    verify(callActivityService, never()).getCustomerInfo(anyString(), anyString());
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(false));
}
