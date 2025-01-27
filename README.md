@Test
public void testGetButtons_OneFaVerified() throws Exception {
    // Set up
    callActivity.setOneFa("true");
    callActivity.setId(new CallActivityId("IN")); // Example setup for ID and country code

    Method getButtonsMethod = CallActivityServiceImpl.class.getDeclaredMethod("getButtons", CallActivity.class);
    getButtonsMethod.setAccessible(true);

    try {
        // Invoke the private method
        @SuppressWarnings("unchecked")
        Map<String, String> buttons = (Map<String, String>) getButtonsMethod.invoke(callActivityService, callActivity);

        // Assertions
        assertNotNull(buttons);
        assertTrue(buttons.containsKey("twoPlusOne"));
        assertEquals("Success", buttons.get("twoPlusOne"));
    } catch (InvocationTargetException e) {
        // Log and rethrow the actual exception
        Throwable cause = e.getCause();
        System.err.println("Error in getButtons method: " + cause.getMessage());
        cause.printStackTrace();
        throw e;
    }
}
