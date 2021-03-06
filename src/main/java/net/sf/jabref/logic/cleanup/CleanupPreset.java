package net.sf.jabref.logic.cleanup;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import net.sf.jabref.logic.util.OS;
import net.sf.jabref.model.cleanup.FieldFormatterCleanups;
import net.sf.jabref.preferences.JabRefPreferences;

public class CleanupPreset {

    private final Set<CleanupStep> activeJobs;
    private final FieldFormatterCleanups formatterCleanups;


    public CleanupPreset(Set<CleanupStep> activeJobs) {
        this(activeJobs, new FieldFormatterCleanups(false, new ArrayList<>()));
    }

    public CleanupPreset(CleanupStep activeJob) {
        this(EnumSet.of(activeJob));
    }

    public CleanupPreset(FieldFormatterCleanups formatterCleanups) {
        this(EnumSet.noneOf(CleanupStep.class), formatterCleanups);
    }

    public CleanupPreset(Set<CleanupStep> activeJobs, FieldFormatterCleanups formatterCleanups) {
        this.activeJobs = activeJobs;
        this.formatterCleanups = Objects.requireNonNull(formatterCleanups);
    }

    public static CleanupPreset loadFromPreferences(JabRefPreferences preferences) {

        Set<CleanupStep> activeJobs = EnumSet.noneOf(CleanupStep.class);

        if (preferences.getBoolean(JabRefPreferences.CLEANUP_DOI)) {
            activeJobs.add(CleanupStep.CLEAN_UP_DOI);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_ISSN)) {
            activeJobs.add(CleanupStep.CLEAN_UP_ISSN);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_MOVE_PDF)) {
            activeJobs.add(CleanupStep.MOVE_PDF);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_MAKE_PATHS_RELATIVE)) {
            activeJobs.add(CleanupStep.MAKE_PATHS_RELATIVE);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_RENAME_PDF)) {
            activeJobs.add(CleanupStep.RENAME_PDF);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_RENAME_PDF_ONLY_RELATIVE_PATHS)) {
            activeJobs.add(CleanupStep.RENAME_PDF_ONLY_RELATIVE_PATHS);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_UPGRADE_EXTERNAL_LINKS)) {
            activeJobs.add(CleanupStep.CLEAN_UP_UPGRADE_EXTERNAL_LINKS);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_CONVERT_TO_BIBLATEX)) {
            activeJobs.add(CleanupStep.CONVERT_TO_BIBLATEX);
        }
        if (preferences.getBoolean(JabRefPreferences.CLEANUP_FIX_FILE_LINKS)) {
            activeJobs.add(CleanupStep.FIX_FILE_LINKS);
        }

        FieldFormatterCleanups formatterCleanups = Cleanups.parse(
                preferences.getStringList(JabRefPreferences.CLEANUP_FORMATTERS));

        return new CleanupPreset(activeJobs, formatterCleanups);
    }

    public boolean isCleanUpUpgradeExternalLinks() {
        return isActive(CleanupStep.CLEAN_UP_UPGRADE_EXTERNAL_LINKS);
    }

    public boolean isCleanUpDOI() {
        return isActive(CleanupStep.CLEAN_UP_DOI);
    }

    public boolean isCleanUpISSN() {
        return isActive(CleanupStep.CLEAN_UP_ISSN);
    }

    public boolean isFixFileLinks() {
        return isActive(CleanupStep.FIX_FILE_LINKS);
    }

    public boolean isMovePDF() {
        return isActive(CleanupStep.MOVE_PDF);
    }

    public boolean isMakePathsRelative() {
        return isActive(CleanupStep.MAKE_PATHS_RELATIVE);
    }

    public boolean isRenamePDF() {
        return isActive(CleanupStep.RENAME_PDF) || isActive(CleanupStep.RENAME_PDF_ONLY_RELATIVE_PATHS);
    }

    public boolean isConvertToBiblatex() {
        return isActive(CleanupStep.CONVERT_TO_BIBLATEX);
    }

    public boolean isRenamePdfOnlyRelativePaths() {
        return isActive(CleanupStep.RENAME_PDF_ONLY_RELATIVE_PATHS);
    }

    public void storeInPreferences(JabRefPreferences preferences) {
        preferences.putBoolean(JabRefPreferences.CLEANUP_DOI, isActive(CleanupStep.CLEAN_UP_DOI));
        preferences.putBoolean(JabRefPreferences.CLEANUP_ISSN, isActive(CleanupStep.CLEAN_UP_ISSN));
        preferences.putBoolean(JabRefPreferences.CLEANUP_MOVE_PDF, isActive(CleanupStep.MOVE_PDF));
        preferences.putBoolean(JabRefPreferences.CLEANUP_MAKE_PATHS_RELATIVE, isActive(CleanupStep.MAKE_PATHS_RELATIVE));
        preferences.putBoolean(JabRefPreferences.CLEANUP_RENAME_PDF, isActive(CleanupStep.RENAME_PDF));
        preferences.putBoolean(JabRefPreferences.CLEANUP_RENAME_PDF_ONLY_RELATIVE_PATHS,
                isActive(CleanupStep.RENAME_PDF_ONLY_RELATIVE_PATHS));
        preferences.putBoolean(JabRefPreferences.CLEANUP_UPGRADE_EXTERNAL_LINKS,
                isActive(CleanupStep.CLEAN_UP_UPGRADE_EXTERNAL_LINKS));
        preferences.putBoolean(JabRefPreferences.CLEANUP_CONVERT_TO_BIBLATEX, isActive(CleanupStep.CONVERT_TO_BIBLATEX));
        preferences.putBoolean(JabRefPreferences.CLEANUP_FIX_FILE_LINKS, isActive(CleanupStep.FIX_FILE_LINKS));

        preferences.putStringList(JabRefPreferences.CLEANUP_FORMATTERS, formatterCleanups.getAsStringList(OS.NEWLINE));
    }

    private Boolean isActive(CleanupStep step) {
        return activeJobs.contains(step);
    }

    public FieldFormatterCleanups getFormatterCleanups() {
        return formatterCleanups;
    }

    public enum CleanupStep {
        /**
         * Removes the http://... for each DOI. Moves DOIs from URL and NOTE filed to DOI field.
         */
        CLEAN_UP_DOI,
        MAKE_PATHS_RELATIVE,
        RENAME_PDF,
        RENAME_PDF_ONLY_RELATIVE_PATHS,
        /**
         * Collects file links from the pdf or ps field, and adds them to the list contained in the file field.
         */
        CLEAN_UP_UPGRADE_EXTERNAL_LINKS,
        /**
         * Converts to BibLatex format
         */
        CONVERT_TO_BIBLATEX,
        MOVE_PDF,
        FIX_FILE_LINKS,
        CLEAN_UP_ISSN
    }

}
