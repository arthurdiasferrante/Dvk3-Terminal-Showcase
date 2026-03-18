package core_logic.models.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    public void shouldReturnCleanTextWhenFrequencyMatches() {
        CryptoUtils crypto = new CryptoUtils();
        // texto original sem criptografia
        String originalMessage = "I can read this";

        // passa o texto pelo metodo de criptografia, porém com a mesma frequência
        String resultMessage = crypto.safeAlgorithm(originalMessage, 8000, 8000);

        // verifica se os dois conteúdos são iguais
        // ele deve retornar o mesmo texto por estar com a mesma frequência
        assertEquals(originalMessage, resultMessage, "text should be readable");
    }

    @Test
    public void shouldReturnCorruptedTextWhenFrequencyIsNotAligned() {
        CryptoUtils crypto = new CryptoUtils();
        // texto original sem criptografia
        String originalMessage = "I shouldn't be able to read this";

        // passa o texto pelo metodo de criptografia, porém com duas frequências distintas
        String resultMessage = crypto.safeAlgorithm(originalMessage, 3400, 8000);

        // textos devem diferir pela distância da frequência
        assertNotEquals(originalMessage, resultMessage, "text shouldn't be readable");
        assertFalse(resultMessage.contains("able"), "the word \"able\" leaked");
    }
}