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
    /*Testes - Validação do nome do autor*/
    @Test //[ERRO] Caracter especial
    public void testAuthorName1() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessic@");
        assertEquals(Optional.empty(), be.getField("author"));
    }
    @Test //[ERRO] Caracter numérico
    public void testAuthorName2() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessica 1");
        assertEquals(Optional.empty(), be.getField("author"));
    }
    @Test //[ERRO] Caracter numérico
    public void testAuthorName3() {
        BibEntry be = new BibEntry();
        be.setField("author", "2017Jessica");
        assertEquals(Optional.empty(), be.getField("author"));
    }
    @Test //[ERRO] Caracter numérico
    public void testAuthorName4() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessica;1 Caroline");
        assertEquals(Optional.empty(), be.getField("author"));
    }
    @Test //[OK] Caracter ;
    public void testAuthorName5() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessica; Caroline Dias");
        assertEquals(Optional.of("Jessica; Caroline Dias"), be.getField("author"));
    }
    @Test //[OK] Caracter ; e ,
    public void testAuthorName6() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessica; Caroline, Dias");
        assertEquals(Optional.of("Jessica; Caroline, Dias"), be.getField("author"));
    }
    @Test //[OK] Caracter .
    public void testAuthorName7() {
        BibEntry be = new BibEntry();
        be.setField("author", "Jessica C. Dias");
        assertEquals(Optional.of("Jessica C. Dias"), be.getField("author"));
    }
}
