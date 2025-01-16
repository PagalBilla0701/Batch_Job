@Test
public void testWipeCallCustomerAccount() throws Exception {
    // Arrange
    when(callActivityAction.updateCallActivity(any(CallActivity.class))).thenReturn(true); // Return boolean as expected

    SectionDataResponse mockResponse = new SectionDataResponse();
    CallActivityServiceImpl spyService = spy(callActivityService);
    doReturn(mockResponse).when(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
    when(callActivity.isGeneral()).thenReturn(true);

    // Act
    SectionDataResponse response = spyService.wipeCallCustomerAccount(callActivity, COUNTRY_CODE, loginBean);

    // Assert
    assertNotNull(response);
    assertEquals(mockResponse, response);

    // Verify that customer details were cleared
    assertNull(callActivity.getCustId());
    assertNull(callActivity.getCustName());
    assertNull(callActivity.getProductType());
    assertNull(callActivity.getAccountNo());
    assertNull(callActivity.getAccountCurrency());
    assertNull(callActivity.getName());
    assertNull(callActivity.getGender());
    assertNull(callActivity.getStaffCategory());

    // Verify method interactions
    verify(callActivityAction).updateCallActivity(callActivity);
    verify(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
    verify(callActivity).isGeneral();
}
