Here is a corrected version of your loadNewCallActivityGenesys_test method. I've added missing semicolons, corrected typos in variable names, and adjusted the Mockito syntax. Let me know if this looks accurate:

@Test
public void loadNewCallActivityGenesys_test() throws Exception {

    // Initialize customer and account maps with test data
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
    Mockito.when(callActivityAction.getCallActivityByRefNo("5623456789", "SGP")).thenReturn(call);
    Mockito.doNothing().when(callActivityService).addToRecentItem(call, login);
    // Uncomment if needed for further verification
    // Mockito.when(callActivityAction.saveGenCallActivity(ArgumentMatchers.any())).thenReturn(genCall);
    Mockito.when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

    // Execute the test method
    controller.loadNewCallActivityGenesys(login, "TPIN", model, genesysData);

    // Verify interactions with mocks
    verify(callActivityService, times(2)).getCustomerInfo("SG", "A0198678");
    verify(callActivityService).isSoftTokenEnableForCountry("SG");
    verify(callActivityService, atLeast(1)).identifyAccountOrCustomerId("SG", "123456790");
    verify(callActivityAction).saveCallActivity(ArgumentMatchers.any());
    verify(callActivityAction).getCallActivityByRefNo("5623456789", "SGP");
    verify(callActivityService).addToRecentItem(call, login);
    // Uncomment if needed for further verification
    // verify(callActivityAction).saveGenCallActivity(ArgumentMatchers.any());
    verify(callActivityService).renderCallInfo(call, false, "SG", login);
}

Changes made:

1. Fixed customerMap.put("gender", "F"); (it was incorrectly written as customertap.put).


2. Corrected new HashMap<String, String>(); syntax and added semicolons.


3. Fixed Mockito statements (for instance, .thenReturn(...) was corrected for proper syntax).


4. Changed the verify() calls to match corrected variable and method names, including atLeast(1) syntax.



