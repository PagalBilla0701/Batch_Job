@Test
public void testIdentifyAccountOrCustomerId() {
    // Mock inputs
    String countryCodeSG = "SG";
    String countryCodeAE = "AE";
    String accountNumber = "123456";

    // Mock outputs
    Map<String, Object> mockResponseSG = new HashMap<>();
    Map<String, String> respMapSG = new HashMap<>();
    respMapSG.put("customerId", "CUST123");
    mockResponseSG.put("resp", respMapSG);

    Map<String, Object> mockResponseAE = new HashMap<>();
    Map<String, String> respMapAE = new HashMap<>();
    respMapAE.put("customerId", "CUST456");
    mockResponseAE.put("resp", respMapAE);

    // Mock behavior
    lenient().when(callActivityEDMIService.getCustomerAcctRowID(countryCodeSG, accountNumber))
             .thenReturn(mockResponseSG);
    when(IEProxy.getAnswer(eq(countryCodeAE), eq("ACCTINFO"), any(String[].class)))
        .thenReturn(mockResponseAE);

    // Test for SG
    Map<String, String> resultSG = callActivityService.identifyAccountOrCustomerId(countryCodeSG, accountNumber);
    assertNotNull(resultSG);
    assertEquals("CUST123", resultSG.get("customerId"));

    // Test for AE
    Map<String, String> resultAE = callActivityService.identifyAccountOrCustomerId(countryCodeAE, accountNumber);
    assertNotNull(resultAE);
    assertEquals("CUST456", resultAE.get("customerId"));
}
