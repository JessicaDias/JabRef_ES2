package net.sf.jabref.logic.importer.fileformat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import net.sf.jabref.Globals;
import net.sf.jabref.logic.util.FileExtensions;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.preferences.JabRefPreferences;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CSVImporterTest {

    private CSVImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new CSVImporter();
    }

    @Test
    public void testGetFormatName() {
        assertEquals("Comma Separated Values", importer.getName());
    }

    @Test
    public void testGetCLIId() {
        assertEquals("CSV", importer.getId());
    }


    @Test
    public void testsGetExtensions() {
        assertEquals(FileExtensions.CSV, importer.getExtensions());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Importer for the CSV format.", importer.getDescription());
    }

    @Test
    public void testIsRecognizedFormat() throws IOException, URISyntaxException {
        Path file = Paths.get(CSVImporterTest.class.getResource("csvFileWorking.csv").toURI());
        assertTrue(importer.isRecognizedFormat(file, StandardCharsets.UTF_8));
    }

    @Test
    public void testIsRecognizedFormatReject() throws IOException, URISyntaxException {
        Path file = Paths.get(CSVImporterTest.class.getResource("csvFileNotWorking.csv").toURI());
        assertFalse(importer.isRecognizedFormat(file, StandardCharsets.UTF_8));
    }

    @Test
    public void testCsvFields() throws IOException, URISyntaxException {
        Path file = Paths.get(CSVImporterTest.class.getResource("csvFileWorking.csv").toURI());
        List<BibEntry> bibEntries = importer.importDatabase(file, StandardCharsets.UTF_8).getDatabase().getEntries();


        for (BibEntry entry : bibEntries) {

            if (entry.getCiteKey().equals("small")) {
                assertEquals(Optional.of("Guerra, B. D."), entry.getField("author"));
                assertEquals(Optional.of("ES2 é Amor"), entry.getField("title"));
                assertEquals(Optional.of("O journal do Auri"), entry.getField("journal"));
                assertEquals(Optional.of("2017"), entry.getField("year"));
                assertEquals(Optional.of("-1"), entry.getField("volume"));
                assertEquals(Optional.of("to appear"), entry.getField("note"));
            } else if (entry.getCiteKey().equals("big")) {
                assertEquals(Optional.of("Marx, Karl"), entry.getField("author"));
                assertEquals(Optional.of("A big communist paper"), entry.getField("title"));
                assertEquals(Optional.of("JOURNAL OF THE COMMUNISM WILL WIN"), entry.getField("journal"));
                assertEquals(Optional.of("1994"), entry.getField("year"));
                assertEquals(Optional.of("MCMXCVII"), entry.getField("volume"));
            }
        }

    }

}
