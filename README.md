@Test
public void testGetMobileNumberForOtp() {
    // Arrange
    String customerId = "12345";
    String countryCode = "IN";
    String expectedMobileNumber = "9876543210";

    CallActivityController mockController = Mockito.mock(CallActivityController.class);
    CallActivityService mockService = Mockito.mock(CallActivityService.class);
    Mockito.when(mockService.getCustomerInfo(Mockito.eq(countryCode), Mockito.eq(customerId)))
           .thenReturn(Collections.singletonMap("mobilePhoneNumber", expectedMobileNumber));
    
    // Act
    String mobileNumber = mockController.getMobileNumberForotp(customerId, countryCode);
    
    // Assert
    assertEquals(expectedMobileNumber, mobileNumber);
}
