package com.hkjinlee.sleepcover;

import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Extracts cover image from eBook file.
 * Supports ePub format only.
 *
 * Created by hkjinlee on 15. 6. 21..
 */
abstract public class CoverImageExtractor implements Constants {
    protected static final String TAG = "CoverImageExtractor";

    public static CoverImageExtractor getInstance(String mimeType) {
        switch (mimeType) {
            case MIMETYPE_EPUB:
                return new EpubCoverImageExtractor();
            default:
                return null;
        }
    }

    abstract public CoverImage extract(Uri fileUri) throws IOException;
}

class EpubCoverImageExtractor extends CoverImageExtractor {
    public CoverImage extract(Uri fileUri) throws IOException {
        Log.d(TAG, "extract() start");
        ZipFile zip_file = new ZipFile(fileUri.getPath());

        Book book = new EpubReader().readEpubLazy(fileUri.getPath(), "UTF-8");

        Resource cover = book.getCoverImage();
        if (cover == null) {
            Log.d(TAG, "CoverImage doesn't exist. Finding CoverPage instead");
            try {
                Resource coverPage = book.getCoverPage();
                if (!coverPage.getHref().contains("/")) {
                    coverPage.setHref("/" + coverPage.getHref());
                }
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                        coverPage.getInputStream()
                );

                XPath xpath = XPathFactory.newInstance().newXPath();
                XPathExpression expr = xpath.compile("//img/@src");

                String image_href = expr.evaluate(document, XPathConstants.STRING).toString();
                image_href = new URI(coverPage.getHref()).resolve(image_href).getPath();
                cover = book.getResources().getByHref(image_href);

                Log.d(TAG, "Image URI = " + image_href);
                Log.d(TAG, "Image = " + cover);
            } catch (SAXException e) {
                Log.d(TAG, null, e);
            } catch (ParserConfigurationException e) {
                Log.d(TAG, null, e);
            } catch (XPathExpressionException e) {
                Log.d(TAG, null, e);
            } catch (URISyntaxException e) {
                Log.d(TAG, null, e);
            }
        }

        return new CoverImage(cover.getData(), cover.getMediaType().getDefaultExtension());
    }
}

class PDFCoverImageExtractor extends CoverImageExtractor {
    @Override
    public CoverImage extract(Uri fileUri) throws IOException {
        return null;
    }
}