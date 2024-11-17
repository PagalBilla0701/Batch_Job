@Test
public void testGenesysTransfer_Success() throws Exception {
    String callRefNo = "SG20151125037392";
    String customerId = "cust123";
    String refNo = callRefNo.substring(2); // Mimics the method logic
    String countryCode = "SG";

    // Mock CallActivity
    CallActivity mockCall = mock(CallActivity.class);
    when(callActivityAction.getCallActivityByRefNo(refNo, countryCode)).thenReturn(mockCall);
    
    // Setup mock responses
    when(mockCall.getLang()).thenReturn("EN");
    when(mockCall.getCustomerSegment()).thenReturn("Premium");
    when(mockCall.getCallerId()).thenReturn("9999999999");
    when(mockCall.getDnis()).thenReturn("12345");
    when(mockCall.getIdntfTyp()).thenReturn("ID123");
    when(mockCall.getIdBlockcode()).thenReturn("BC001");
    when(mockCall.getAuthBlockCode()).thenReturn("AC002");
    when(mockCall.getSelfSrvcCode()).thenReturn("SSC003");
    when(mockCall.getAvailableAuth()).thenReturn("OTP|ST");
    when(mockCall.getCustIdIVR()).thenReturn("CID001");
    when(mockCall.getLastMobno()).thenReturn("8888888888");
    when(mockCall.getRmn()).thenReturn("RMN001");
    when(mockCall.isOneFaVerifed()).thenReturn(true);
    when(mockCall.getOneFa()).thenReturn("1FA");
    when(mockCall.isTwoFaVerified()).thenReturn(false);

    // Mock service response
    Map<String, Object> formFields = new HashMap<>();
    formFields.put("status", "success");
    when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(formFields);

    // Perform request and validate response
    mockMvc.perform(MockMvcRequestBuilders.post("/genesys-transfer.do")
            .param("callRefNo", callRefNo)
            .param("customerId", customerId)
            .sessionAttr("login", login)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"));

    // Verify mock interactions
    verify(callActivityAction).getCallActivityByRefNo(refNo, countryCode);
    verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
}
