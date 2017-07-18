package net.sf.jabref.model.entry;

import java.util.Optional;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BibEntryTest {
    private BibEntry entry;

    @Before
    public void setUp() {
        entry = new BibEntry();
    }

    @After
    public void tearDown() {
        entry = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void notOverrideReservedFields() {
        entry.setField(BibEntry.ID_FIELD, "somevalue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notClearReservedFields() {
        entry.clearField(BibEntry.ID_FIELD);
    }

    @Test
    public void getFieldIsCaseInsensitive() throws Exception {
        entry.setField("TeSt", "value");

        Assert.assertEquals(Optional.of("value"), entry.getField("tEsT"));
    }

    @Test
    public void clonedBibentryHasUniqueID() throws Exception {
        BibEntry entry = new BibEntry();
        BibEntry entryClone = (BibEntry) entry.clone();

        Assert.assertNotEquals(entry.getId(), entryClone.getId());
    }

    // Teste de entrada invalida para texto com dois ou mais caracteres
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearInvalidFieldText() { entry.setField("year", "teste"); }

    // Teste de entrada invalida para texto com numeros mas comecando com letras
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearInvalidFieldTextNumber() { entry.setField("year", "teste2017"); }

    // Teste de entrada invalida para textos comecados com numeros
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearInvalidFieldNumberText() { entry.setField("year", "2017teste"); }

    // Teste de entrada invalida para textos com caracteres especiais no inicio
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearInvalidFieldNumberWithSpecialCharacter() { entry.setField("year", "_2017"); }

    // Teste de entrada invalida para textos com menos de dois caracteres
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearInvalidFieldTextBelowTwoCharacters() { entry.setField("year", "t"); }

    // Teste de entrada invalida para textos com dois caracteres especiais
    @Test
    public void setInvalidYearInvalidFieldSpecialCharacters() { entry.setField("year", "7"); }

    // Teste de entrada invalida para textos com um digito apenas
    @Test (expected = IllegalArgumentException.class)
    public void setInvalidYearValidFieldText() { entry.setField("year", "teste"); }

    // Teste de entrada valida com mais de quatro caracteres
    @Test
    public void setInvalidYearMoreThanFourCharacters() { entry.setField("year", "02017"); }

    // Teste de entrada valida com quatro caracteres
    @Test
    public void setValidYearFourCharacters() { entry.setField("year", "2017"); }

    // Teste de entrada vazia
    @Test
    public void setEmpty() {
        Map<String, String> str2 = entry.getFieldMap();
        assertEquals(null, str2.get(""));
    }

    // Entrada valida de chave com mais de um caractere
    @Test
    public void setValidBibtexKeyTextNumber() {
        entry.setField("bibtexkey", "ES22017");
        Map<String, String> str2 = entry.getFieldMap();
        assertEquals("ES22017", str2.get("bibtexkey"));
    }

    // Entrada valida de chave com mais de um caractere e apenas letras
    @Test
    public void setValidBibtexKeyOnlyText() {
        entry.setField("bibtexkey", "Auri");
        Map<String, String> str2 = entry.getFieldMap();
        assertEquals("Auri", str2.get("bibtexkey"));
    }

    // Entrada valida com caracteres e caracteres especiais
    @Test
    public void setValidBibtexKeySpecialCharacter() {
        entry.setField("bibtexkey", "ES22017!");
        Map<String, String> str2 = entry.getFieldMap();
        assertEquals("ES22017!", str2.get("bibtexkey"));
    }

    // Entrada valida com exatamente dois caracteres
    @Test
    public void setValidBibtexKeyTwoCharacters() {
        entry.setField("bibtexkey", "ES");
        Map<String, String> str2 = entry.getFieldMap();
        assertEquals("ES", str2.get("bibtexkey"));
    }
}
