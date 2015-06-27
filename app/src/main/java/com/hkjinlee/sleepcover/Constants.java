package com.hkjinlee.sleepcover;

/**
 * An interface that holds common constant values
 *
 * Created by hkjinlee on 15. 6. 20..
 */
public interface Constants {
    String NULL = "NULL";

    /**
     * Constants for eBook MIME type
     */
    String MIMETYPE_EPUB = "application/epub+zip";
    String MIMETYPE_PDF = "application/pdf";

    /**
     * Key names for SharedPreferences
     */
    String PREFS_DEFAULT_READER_EPUB = "default_reader_epub";
    String PREFS_DEFAULT_READER_PDF = "default_reader_pdf";
    String PREFS_SHOW_LOG = "show_log";
    String PREFS_CHANGE_LOCKSCREEN = "change_lockscreen";
    String PREFS_CLEAR_PREFS = "clear_prefs";
    String PREFS_CURRENT_EBOOK_URI = "current_ebook_uri";
    String PREFS_ABOUT = "about";

    /**
     * Used for ContentProvider
     * (Provides a sample epub file inside the application)
     */
    String CONTENT_URI_PREFIX = "com.hkjinlee.sleepcover";
    String SAMPLE_EPUB_PATH = "raw";

    /**
     * Action name for invoking ReaderChooseActivity
     */
    String ACTION_CHOOSE_READER = "com.hkjinlee.sleepcover.ACTION_CHOOSE_READER";
    int NOTIFY_READER_CHOICE = 1001;
}
