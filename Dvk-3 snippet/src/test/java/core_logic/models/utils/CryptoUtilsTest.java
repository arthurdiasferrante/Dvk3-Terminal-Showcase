package core_logic.models.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    public void shouldReturnCleanTextWhenFrequencyMatches() {
        CryptoUtils crypto = new CryptoUtils();
        String originalMessage = "I can read this";

        String resultMessage = crypto.safeAlgorithm(originalMessage, 8000, 8000);

        assertEquals(originalMessage, resultMessage, "text should be readable");
    }

    @Test
    public void shouldReturnCorruptedTextWhenFrequencyIsNotAligned() {
        CryptoUtils crypto = new CryptoUtils();
        String originalMessage = "I shouldn't be able to read this";

        String resultMessage = crypto.safeAlgorithm(originalMessage, 3400, 8000);

        assertNotEquals(originalMessage, resultMessage, "text shouldn't be readable");
        assertFalse(resultMessage.contains("able"), "the word \"able\" leaked");
    }
}