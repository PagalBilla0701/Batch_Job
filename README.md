@Test
public void loadNewCallActivityGenesys_test() throws Exception {
    // Mock data for customer information
    Map<String, String> customerMap = new HashMap<>();
    customerMap.put("fullName", "XXXX");
    customerMap.put("gender", "F");
    customerMap.put("genderDesc", "Female");
    customerMap.put("staffCategoryCode", "01");
    customerMap.put("staffCategoryDesc", "STAFF");

    // Mock data for account information
    Map<String, String> accountMap = new HashMap<>();
    accountMap.put("custAcctProdType", "Savings Account");
    accountMap.put("custAcctIdentifier", "ACCT123456790");
    accountMap.put("currencyCode", "SGD");

    // Mock objects
    CallActivity call = new CallActivity(); // Replace with actual CallActivity instance
    CallActivity genCall = new CallActivity(); // Replace with actual GenCall instance
    SectionResponse sectionResponse = new SectionResponse(); // Replace with actual SectionResponse instance
    Login login = new Login(); // Replace with actual Login object
    Model model = new ExtendedModelMap();
    GenesysData genesysData = new GenesysData(); // Replace with actual GenesysData object

    // Mock behavior
    Mockito.when(callActivityService.getCustomerInfo("SG", "A0198678")).thenReturn(customerMap);
    Mockito.when(callActivityService.isSoftTokenEnableForCountry("SG")).thenReturn("true");
    Mockito.when(callActivityService.identifyAccountOrCustomerId("SG", "123456790")).thenReturn(accountMap);
    Mockito.when(callActivityAction.saveCallActivity(ArgumentMatchers.any())).thenReturn("SG23456789");
    Mockito.when(callActivityAction.getCallActivityByRefNo("SG23456789", "SGP")).thenReturn(call);
    Mockito.doNothing().when(callActivityService).addToRecentItem(call, login);
    Mockito.when(callActivityAction.saveGenCallActivity(ArgumentMatchers.any())).thenReturn(genCall);
    Mockito.when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

    // Ensure user is authorized
    Mockito.when(callActivityService.isAuthorized(ArgumentMatchers.any(), ArgumentMatchers.any()))
           .thenReturn(true); // Adjust the method signature if needed

    // Act
    controller.loadNewCallActivityGenesys(login, "SG", "1234-4555", model, genesysData);

    // Verify interactions
    verify(callActivityService, times(2)).getCustomerInfo("SG", "A0198678");
    verify(callActivityService).isSoftTokenEnableForCountry("SG");
    verify(callActivityService, atLeast(1)).identifyAccountOrCustomerId("SG", "123456790");
    verify(callActivityAction).saveCallActivity(ArgumentMatchers.any());
    verify(callActivityAction).getCallActivityByRefNo("SG23456789", "SGP");
    verify(callActivityService).addToRecentItem(call, login);
    verify(callActivityAction).saveGenCallActivity(ArgumentMatchers.any());
    verify(callActivityService).renderCallInfo(call, false, "SG", login);
}
