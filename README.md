@Test
public void testCallStatusUpdateSTGenesys_ValidationFailure() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setFailedAuthOne(null);

    Mockito.doNothing().when(callActivityAction).removeFromListOfAuthentications(Mockito.any(), Mockito.eq("SOFT"));
    Mockito.doNothing().when(callActivityAction).updateCallActivity(Mockito.any());

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "F");

    // Assert
    assertEquals("success", result);
    assertEquals("F", call.getVerificationStatus());
    assertEquals("Soft Token", call.getVerificationTypeDesc());
    assertEquals("SOFT", call.getFailedAuthOne());
    Mockito.verify(callActivityAction, Mockito.times(1)).removeFromListOfAuthentications(call, "SOFT");
    Mockito.verify(callActivityAction, Mockito.times(1)).updateCallActivity(call);
}

@Test
public void testCallStatusUpdateSTGenesys_OneFaPresent() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa("EXISTING");

    Mockito.doNothing().when(callActivityAction).removeFromListOfAuthentications(Mockito.any(), Mockito.eq("SOFT"));
    Mockito.doNothing().when(callActivityAction).updateCallActivity(Mockito.any());

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "T");

    // Assert
    assertEquals("success", result);
    assertEquals("SOFT", call.getOneFa());
    assertEquals("P", call.getVerificationStatus());
    assertEquals("Soft Token", call.getVerificationTypeDesc());
    Mockito.verify(callActivityAction, Mockito.times(1)).removeFromListOfAuthentications(call, "SOFT");
    Mockito.verify(callActivityAction, Mockito.times(1)).updateCallActivity(call);
}

@Test
public void testCallStatusUpdateSTGenesys_TwoFaPresent() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa(null);
    call.setTwoFa("EXISTING");

    Mockito.doNothing().when(callActivityAction).removeFromListOfAuthentications(Mockito.any(), Mockito.eq("SOFT"));
    Mockito.doNothing().when(callActivityAction).updateCallActivity(Mockito.any());

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "T");

    // Assert
    assertEquals("success", result);
    assertEquals("ST", call.getTwoFa());
    Mockito.verify(callActivityAction, Mockito.times(1)).removeFromListOfAuthentications(call, "SOFT");
    Mockito.verify(callActivityAction, Mockito.times(1)).updateCallActivity(call);
}

@Test
public void testCallStatusUpdateSTGenesys_AlreadyCompleted() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();
    call.setOneFa("COMPLETED");
    call.setTwoFa("COMPLETED");

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "T");

    // Assert
    assertEquals("2FA already completed, cannot attempt again", result);
    Mockito.verify(callActivityAction, Mockito.never()).removeFromListOfAuthentications(Mockito.any(), Mockito.any());
    Mockito.verify(callActivityAction, Mockito.never()).updateCallActivity(Mockito.any());
}

@Test
public void testCallStatusUpdateSTGenesys_DefaultBehavior() throws Exception {
    // Arrange
    CallActivity call = new CallActivity();

    // Act
    String result = callActivityController.callStatusUpdateSTGenesys(call, "UNKNOWN");

    // Assert
    assertEquals("2FA already completed, cannot attempt again", result);
    Mockito.verify(callActivityAction, Mockito.never()).removeFromListOfAuthentications(Mockito.any(), Mockito.any());
    Mockito.verify(callActivityAction, Mockito.never()).updateCallActivity(Mockito.any());
}
