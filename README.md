@Test
public void loadNewCallActivityGenesys_test() throws Exception {
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

    // Set up mocks
    Mockito.when(callActivityService.getCustomerInfo("SG", "A0198678")).thenReturn(customerMap);
    Mockito.when(callActivityService.isSoftTokenEnableForCountry("SG")).thenReturn("true");
    Mockito.when(callActivityService.identifyAccountOrCustomerId("SG", "123456790")).thenReturn(accountMap);

    Mockito.when(callActivityAction.saveCallActivity(ArgumentMatchers.any())).thenReturn("SG23456789");
    Mockito.when(callActivityAction.getCallActivityByRefNo("SG23456789", "SG")).thenReturn(call);
    Mockito.doNothing().when(callActivityService).addToRecentItem(call, login);
    Mockito.when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

    // Execute the controller method
    controller.loadNewCallActivityGenesys(login, "TPIN", model, genesysData);

    // Verify interactions
    Mockito.verify(callActivityService, Mockito.times(1)).getCustomerInfo("SG", "A0198678");
    Mockito.verify(callActivityService, Mockito.times(1)).isSoftTokenEnableForCountry("SG");
    Mockito.verify(callActivityService, Mockito.atLeast(1)).identifyAccountOrCustomerId("SG", "123456790");
    Mockito.verify(callActivityAction, Mockito.times(1)).saveCallActivity(ArgumentMatchers.any());
}
