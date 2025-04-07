Let's create JUnit 4 tests for the AppConfigServiceImpl class. We'll test the public method directly and use reflection for the private method. Here's a comprehensive test class:

```java
import com.scb.cems.entitlement.data.model.AppConfigItem;
import com.scb.cems.entitlement.repository.AppConfigRepository;
import com.scb.cems.model.AppConfigData;
import com.scb.cems.service.CallActivityService;
import com.scb.cems.service.AppConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppConfigServiceImplTest {

    @Mock
    private AppConfigRepository appConfigRepository;

    @Mock
    private CallActivityService callActivityService;

    @InjectMocks
    private AppConfigServiceImpl appConfigService;

    private List<AppConfigItem> mockConfigItems;

    @Before
    public void setUp() {
        mockConfigItems = new ArrayList<>();
        AppConfigItem item1 = new AppConfigItem();
        item1.setContext("session");
        item1.setKey("timeout");
        item1.setValue("30");
        
        AppConfigItem item2 = new AppConfigItem();
        item2.setContext("callactivity");
        item2.setKey("enabled");
        item2.setValue("true");
        
        mockConfigItems.add(item1);
        mockConfigItems.add(item2);
    }

    @Test
    public void testGetApplicationConfigByLoginId() {
        // Arrange
        String loginId = "testUser";
        String countryCode = "SG";
        String languageCode = "EN";
        String expectedLang = "SG_EN";
        
        when(appConfigRepository.getApplicationConfig()).thenReturn(mockConfigItems);
        when(callActivityService.isSoftTokenEnableForCountry(countryCode)).thenReturn("true");

        // Act
        AppConfigData result = appConfigService.getApplicationConfigByLoginId(loginId, countryCode, languageCode);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getSettings());
        
        LinkedHashMap<String, LinkedHashMap<String, String>> settings = result.getSettings();
        assertTrue(settings.containsKey("session"));
        assertTrue(settings.containsKey("callactivity"));
        
        assertEquals("30", settings.get("session").get("timeout"));
        assertEquals(expectedLang, settings.get("session").get("language"));
        assertEquals("true", settings.get("callactivity").get("stVerifiedButton"));
        assertEquals("true", settings.get("callactivity").get("enabled"));
        
        verify(appConfigRepository, times(1)).getApplicationConfig();
        verify(callActivityService, times(1)).isSoftTokenEnableForCountry(countryCode);
    }

    @Test
    public void testExtractSettingsFromListUsingReflection() throws Exception {
        // Arrange
        List<AppConfigItem> configList = mockConfigItems;
        LinkedHashMap<String, LinkedHashMap<String, String>> settingsMap = new LinkedHashMap<>();

        // Get private method using reflection
        Method method = AppConfigServiceImpl.class.getDeclaredMethod("extractSettingsFromList", 
            List.class, LinkedHashMap.class);
        method.setAccessible(true);

        // Act
        method.invoke(appConfigService, configList, settingsMap);

        // Assert
        assertEquals(2, settingsMap.size());
        assertTrue(settingsMap.containsKey("session"));
        assertTrue(settingsMap.containsKey("callactivity"));
        
        LinkedHashMap<String, String> sessionSettings = settingsMap.get("session");
        assertEquals(1, sessionSettings.size());
        assertEquals("30", sessionSettings.get("timeout"));
        
        LinkedHashMap<String, String> callActivitySettings = settingsMap.get("callactivity");
        assertEquals(1, callActivitySettings.size());
        assertEquals("true", callActivitySettings.get("enabled"));
    }

    @Test
    public void testGetApplicationConfigByLoginIdWithEmptyConfig() {
        // Arrange
        String loginId = "testUser";
        String countryCode = "SG";
        String languageCode = "EN";
        
        when(appConfigRepository.getApplicationConfig()).thenReturn(new ArrayList<>());
        when(callActivityService.isSoftTokenEnableForCountry(countryCode)).thenReturn("false");

        // Act
        AppConfigData result = appConfigService.getApplicationConfigByLoginId(loginId, countryCode, languageCode);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getSettings());
        
        LinkedHashMap<String, LinkedHashMap<String, String>> settings = result.getSettings();
        assertTrue(settings.containsKey("session"));
        assertTrue(settings.containsKey("callactivity"));
        
        assertEquals("SG_EN", settings.get("session").get("language"));
        assertEquals("false", settings.get("callactivity").get("stVerifiedButton"));
    }

    @Test
    public void testExtractSettingsFromListWithEmptyListUsingReflection() throws Exception {
        // Arrange
        List<AppConfigItem> configList = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> settingsMap = new LinkedHashMap<>();

        Method method = AppConfigServiceImpl.class.getDeclaredMethod("extractSettingsFromList", 
            List.class, LinkedHashMap.class);
        method.setAccessible(true);

        // Act
        method.invoke(appConfigService, configList, settingsMap);

        // Assert
        assertEquals(0, settingsMap.size());
    }
}
```

This test class includes:

1. Required dependencies and annotations:
   - `@RunWith(MockitoJUnitRunner.class)` for Mockito support
   - `@Mock` for mocking dependencies
   - `@InjectMocks` for the class under test

2. Setup method:
   - Creates mock data for testing
   - Initializes mockConfigItems with sample data

3. Test cases:
   - `testGetApplicationConfigByLoginId`: Tests the main public method with normal case
   - `testExtractSettingsFromListUsingReflection`: Tests the private method using reflection
   - `testGetApplicationConfigByLoginIdWithEmptyConfig`: Tests the public method with empty config list
   - `testExtractSettingsFromListWithEmptyListUsingReflection`: Tests the private method with empty list

Key features of the tests:
- Uses Mockito to mock dependencies (AppConfigRepository and CallActivityService)
- Verifies method interactions using `verify()`
- Tests both happy path and edge cases
- Uses reflection to test private method
- Checks expected outputs and map structures
- Validates caching behavior indirectly through method call verification

Note: You might need to adjust some assertions based on:
- Exact behavior of `LangType.getLangIdentifier()`
- The actual structure of `AppConfigData`
- Any specific business rules not visible in the provided code

Dependencies required in your pom.xml:
```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>3.12.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
