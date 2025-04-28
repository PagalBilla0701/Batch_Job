package com.scb.cems.util;

import com.scb.coms.data.assembler.enums.LangType;
import com.scb.i18n.common.util.DBMessageSourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppMessageSourceHelperTest {

    @Mock
    private DBMessageSourceHelper messageSourceHelper;

    @Mock
    private Logger logger;

    @InjectMocks
    private AppMessageSourceHelper appMessageSourceHelper;

    private static final String APP_GROUP = "cems-central-web";
    private static final String TEST_CODE = "test.code";
    private static final String TEST_COUNTRY = "US";
    private static final String TEST_LANGUAGE = "en";
    private static final String TEST_DEFAULT_VALUE = "Default message";
    private static final String EXPECTED_MESSAGE = "Translated message";
    private static final String CODE_NOT_CONFIGURED = "Message for i18n key '%s' is not configured. [%s, %s, %s]";

    @Before
    public void setUp() {
        // Ensure logger is mocked
        when(logger.isErrorEnabled()).thenReturn(true);
    }

    @Test
    public void testGetMessageWithCode() {
        // Arrange
        String expectedCodeNotConfigured = String.format(CODE_NOT_CONFIGURED, TEST_CODE, TEST_COUNTRY, TEST_LANGUAGE, APP_GROUP);
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(null), eq(expectedCodeNotConfigured), eq(TEST_COUNTRY.toUpperCase()), 
                eq(LangType.getLang(TEST_COUNTRY, TEST_LANGUAGE)), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(null), eq(expectedCodeNotConfigured), eq(TEST_COUNTRY.toUpperCase()), 
                eq(LangType.getLang(TEST_COUNTRY, TEST_LANGUAGE)), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeAndArgs() {
        // Arrange
        Object[] args = new Object[]{"arg1", "arg2"};
        String expectedCodeNotConfigured = String.format(CODE_NOT_CONFIGURED, TEST_CODE, TEST_COUNTRY, TEST_LANGUAGE, APP_GROUP);
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(args), eq(expectedCodeNotConfigured), eq(TEST_COUNTRY.toUpperCase()), 
                eq(LangType.getLang(TEST_COUNTRY, TEST_LANGUAGE)), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, args);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(args), eq(expectedCodeNotConfigured), eq(TEST_COUNTRY.toUpperCase()), 
                eq(LangType.getLang(TEST_COUNTRY, TEST_LANGUAGE)), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeArgsCountryAndLanguage() {
        // Arrange
        Object[] args = new Object[]{"arg1"};
        String countryCode = "TH";
        String languageCode = "th";
        String expectedCodeNotConfigured = String.format(CODE_NOT_CONFIGURED, TEST_CODE, countryCode, languageCode, APP_GROUP);
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(args), eq(expectedCodeNotConfigured), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, args, countryCode, languageCode);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(args), eq(expectedCodeNotConfigured), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeCountryAndLanguage() {
        // Arrange
        String countryCode = "TH";
        String languageCode = "th";
        String expectedCodeNotConfigured = String.format(CODE_NOT_CONFIGURED, TEST_CODE, countryCode, languageCode, APP_GROUP);
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(null), eq(expectedCodeNotConfigured), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, countryCode, languageCode);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(null), eq(expectedCodeNotConfigured), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeDefaultValueCountryAndLanguage() {
        // Arrange
        String countryCode = "TH";
        String languageCode = "th";
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(null), eq(TEST_DEFAULT_VALUE), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, TEST_DEFAULT_VALUE, countryCode, languageCode);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(null), eq(TEST_DEFAULT_VALUE), eq(countryCode.toUpperCase()), 
                eq(LangType.getLang(countryCode, languageCode)), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeDefaultValueAndLocale() {
        // Arrange
        Locale locale = new Locale("en", "US");
        when(messageSourceHelper.getMessage(eq(TEST_CODE), eq(null), eq(TEST_DEFAULT_VALUE), eq("US"), 
                eq(LangType.getLang("US", "en")), eq(APP_GROUP), eq(null)))
                .thenReturn(EXPECTED_MESSAGE);

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, TEST_DEFAULT_VALUE, locale);

        // Assert
        assertEquals(EXPECTED_MESSAGE, result);
        verify(messageSourceHelper).getMessage(eq(TEST_CODE), eq(null), eq(TEST_DEFAULT_VALUE), eq("US"), 
                eq(LangType.getLang("US", "en")), eq(APP_GROUP), eq(null));
    }

    @Test
    public void testGetMessageWithCodeDefaultValueAndLocaleException() {
        // Arrange
        Locale locale = mock(Locale.class);
        when(locale.getCountry()).thenThrow(new RuntimeException("Locale error"));

        // Act
        String result = appMessageSourceHelper.getMessage(TEST_CODE, TEST_DEFAULT_VALUE, locale);

        // Assert
        assertEquals(TEST_DEFAULT_VALUE, result);
        verify(logger).error(anyString(), any(Exception.class));
        verifyNoInteractions(messageSourceHelper);
    }
}
