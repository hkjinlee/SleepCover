package com.hkjinlee.sleepcover;

import android.graphics.Path;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

/**
 * Created by hkjinlee on 15. 6. 27..
 */
public class EpubCoverTestCase extends TestCase {
    private static final String FILEPATH = "../samples/";

    public void testLS() throws IOException {
        File dir = new File(FILEPATH);
        List<String> files = Arrays.asList(dir.list());

        assertTrue(files.contains("gago sipeul ddae gago sipeun goseuro - sinseungyeol.zip"));
    }

    public void testGagosipulddae() throws IOException {
        final String FILENAME = FILEPATH + "gago sipeul ddae gago sipeun goseuro - sinseungyeol.zip";

        EpubReader reader = new EpubReader();
        Book book = reader.readEpubLazy(FILENAME, "UTF-8");

        Resource cover = book.getCoverPage();
        assertEquals("id0", cover.getId());
        cover.setHref("/" + cover.getHref());
        String content = new String(cover.getData());
        assertTrue(Pattern.compile("<img.* src=\"images/img1.jpg\".*/>").matcher(content).find());
    }

    public void testRelativePath() throws Exception {
        URI a = new URI("sub1/a.html");
        URI b = a.resolve("../sub2/b.img");
        System.out.println(b.getPath());
    }
}
