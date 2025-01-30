import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class YourServiceTest {

    private final OptyPitchRepo optyPitchRepo = Mockito.mock(OptyPitchRepo.class);
    private final YourService yourService = new YourService(optyPitchRepo);

    @Test
    void testUpdateConsentRefID() {
        // Arrange
        String targetRefID = "newRef123";
        String originalRefID = "oldRef456";

        // Act
        yourService.updateConsentRefID(targetRefID, originalRefID);

        // Assert
        verify(optyPitchRepo).updateConsentForm(targetRefID, originalRefID);
    }
}
