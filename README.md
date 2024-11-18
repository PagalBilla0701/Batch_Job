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
    verify(callActivityService).makeResponseWrapper(ArgumentMatchers.anyMap(), ArgumentMatchers.eq(false));
}
