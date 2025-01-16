@Test
public void testRenderVerified() {
    // Arrange
    callActivity = mock(CallActivity.class); // Ensure CallActivity is mocked
    when(callActivity.isGeneral()).thenReturn(true);

    SectionDataResponse mockResponse = new SectionDataResponse();

    // Spy on the service to mock renderCallInfo method
    CallActivityServiceImpl spyService = spy(callActivityService);
    doReturn(mockResponse).when(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);

    // Act
    SectionDataResponse response = spyService.renderVerified(callActivity, COUNTRY_CODE, loginBean);

    // Assert
    assertNotNull(response);
    assertEquals(mockResponse, response);

    // Verify interactions
    verify(callActivity).isGeneral();
    verify(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
}
