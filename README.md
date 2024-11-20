@Test
public void testCallStatusUpdateSTGenesys_SuccessValidation() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa("2+1");
    call.setTwoFa(null);
    call.setFailedAuthOne(null);

    Mockito.doNothing().when(callActivityAction).removeFromListOfAuthentications(Mockito.any(), Mockito.eq("SOFT"));
    Mockito.doNothing().when(callActivityAction).updateCallActivity(Mockito.any());

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "T");

    // Assert
    assertEquals("success", result);
    Mockito.verify(callActivityAction, Mockito.times(1)).updateCallActivity(call);
}

@Test
public void testCallStatusUpdateSTGenesys_FailedValidation() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa(null);
    call.setTwoFa(null);
    call.setFailedAuthOne(null);

    Mockito.doNothing().when(callActivityAction).removeFromListOfAuthentications(Mockito.any(), Mockito.eq("SOFT"));
    Mockito.doNothing().when(callActivityAction).updateCallActivity(Mockito.any());

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "F");

    // Assert
    assertEquals("success", result);
    assertEquals("F", call.getVerificationStatus());
    assertEquals("Soft Token", call.getVerificationTypeDesc());
    Mockito.verify(callActivityAction, Mockito.times(1)).removeFromListOfAuthentications(call, "SOFT");
    Mockito.verify(callActivityAction, Mockito.times(1)).updateCallActivity(call);
}

@Test
public void testCallStatusUpdateSTGenesys_AlreadyCompleted() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa("SOFT");
    call.setTwoFa(null);

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "T");

    // Assert
    assertEquals("2FA already completed, cannot attempt again", result);
    Mockito.verify(callActivityAction, Mockito.never()).updateCallActivity(call);
}
