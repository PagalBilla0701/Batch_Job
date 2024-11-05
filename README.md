@Test
public void loadNewCallActivityGenesys_test() throws Exception {
    // Arrange
    Map<String, String> customerMap = new HashMap<>();
    customerMap.put("fullName", "xxxx");
    customerMap.put("gender", "F");
    customerMap.put("genderDesc", "Female");
    customerMap.put("staffCategoryCode", "01");
    customerMap.put("staffCategoryDesc", "STAFF");

    Map<String, String> accountMap = new HashMap<>();
    accountMap.put("custAcctProdType", "Savings Account");
    accountMap.put("custAcctIdentifier", "ACCT");
    accountMap.put("currencyCode", "SGD");

    Mockito.when(callActivityService.getCustomerInfo("SG", "A@198678")).thenReturn(customerMap);
    Mockito.when(callActivityService.isSoftTokenEnableForCountry("SG")).thenReturn("true");
    Mockito.when(callActivityService.identifyAccountOrCustomerId("SG", "123456790")).thenReturn(accountMap);
    Mockito.when(callActivityAction.saveCallActivity(ArgumentMatchers.any())).thenReturn("SG23456789");
    Mockito.when(callActivityAction.getCallActivityByRefNo("5623456789", "SGP")).thenReturn(call);
    Mockito.doNothing().when(callActivityService).addToRecentItem(call, login);
    Mockito.when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

    // Act
    controller.loadNewCallActivityGenesys(login, "TPIN", model, genesysData, "A0198678");

    // Assert
    verify(callActivityService, times(2)).getCustomerInfo("SG", "A@198678");
    verify(callActivityService).isSoftTokenEnableForCountry("SG");
    verify(callActivityService, atLeastOnce()).identifyAccountOrCustomerId("SG", "123456790");
    verify(callActivityAction).saveCallActivity(ArgumentMatchers.any());
    verify(callActivityService).renderCallInfo(call, false, "SG", login);

    // Add assertions for expected outcomes if needed, e.g., checking model attributes
}
