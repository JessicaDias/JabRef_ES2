package net.sf.jabref.gui.importer;


import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import net.sf.jabref.JabRefMain;
import net.sf.jabref.gui.AWTExceptionHandler;
import net.sf.jabref.gui.AbstractUITest;
import net.sf.jabref.gui.importer.ImportInspectionDialog;
import net.sf.jabref.gui.JabRefFrame;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.timing.Pause;
import org.junit.Test;

import static org.assertj.swing.finder.WindowFinder.findDialog;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class DuplicateImportTest extends AbstractUITest {

    @Override
    protected void onSetUp() {
        awtExceptionHandler = new AWTExceptionHandler();
        awtExceptionHandler.installExceptionDetectionInEDT();
        application(JabRefMain.class).start();

        robot().waitForIdle();
        robot().settings().timeoutToFindSubMenu(1_000);
        robot().settings().delayBetweenEvents(SPEED_NORMAL);

        mainFrame = findFrame(JabRefFrame.class).withTimeout(10_000).using(robot());
        robot().waitForIdle();
    }


    private void importBibIntoCurrentDatabase(String path) {
        mainFrame.menuItemWithPath("File", "Import into current database").click();
        JFileChooserFixture openFileDialog = mainFrame.fileChooser();
        robot().settings().delayBetweenEvents(1);
        openFileDialog.fileNameTextBox().enterText(path);
        openFileDialog.approve();
        Pause.pause(1_000);
    }


    @Test
    public void testDuplicateEntries() throws IOException {

        String path = new File(this.getClass().getClassLoader().getResource("net/sf/jabref/logic/importer/fileformat/csvFileWorking.csv").getFile()).getAbsolutePath();
        importBibIntoNewDatabase(path);
        importBibIntoCurrentDatabase(path);
        DialogFixture importInspectionDialog = findDialog(ImportInspectionDialog.class).withTimeout(10000).using(robot());

        importInspectionDialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(@Nonnull JButton jButton) {
                return "OK".equals(jButton.getText());
            }
        }).click();
        robot().settings().delayBetweenEvents(1000);
        importInspectionDialog.button(new GenericTypeMatcher<JButton>(JButton.class) {

            @Override
            protected boolean isMatching(@Nonnull JButton jButton) {
                    return "No".equals(jButton.getText());
            }
        }).click();

        exitJabRef();
    }
}
