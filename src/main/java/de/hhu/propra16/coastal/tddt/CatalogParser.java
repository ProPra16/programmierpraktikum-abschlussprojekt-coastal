package de.hhu.propra16.coastal.tddt;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class CatalogParser extends DefaultHandler {

    private static final String DESCRIPTION = "description";
    private static final String EXERCISE = "exercise";
    private static final String EXERCISES = "exercises";
    private static final String CLASSES = "classes";
    private static final String CLASS = "class";
    private static final String TESTS = "tests";
    private static final String TEST = "test";
    private static final String CONFIG = "config";
    private static final String BABYSTEPS = "babysteps";
    private static final String TIMETRACKING = "timetracking";

    private Locator mLocator;

    private ArrayList<String> mTags;

    private Catalog mCatalog;
    private Exercise mExercise;
    private String mDescription;
    private String mClassContent;
    private String mTestContent;

    public Catalog parse(File file) throws ParserConfigurationException, SAXException, IOException {
        mCatalog = new Catalog();

        mTags = new ArrayList<>();

        clearCurrentExercise();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);
        xmlReader.parse(convertToFileURL(file));

        return mCatalog;
    }

    private static String convertToFileURL(File file) {
        String path = file.getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        mLocator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case EXERCISES:
                validateRootTag(EXERCISES);
                break;
            case EXERCISE:
                validateSubTag(EXERCISE, EXERCISES);
                mExercise = new Exercise(attributes.getValue("name"));
                break;
            case DESCRIPTION:
            case CLASSES:
            case TESTS:
            case CONFIG:
                validateSubTag(qName, EXERCISE);
                break;
            case CLASS:
                validateSubTag(CLASS, CLASSES);
                mExercise.addClassName(attributes.getValue("name"));
                break;
            case TEST:
                validateSubTag(TEST, TESTS);
                mExercise.addTestName(attributes.getValue("name"));
                break;
            case BABYSTEPS:
                validateSubTag(BABYSTEPS, CONFIG);

                String babystepsEnabled = attributes.getValue("value");
                if (Boolean.parseBoolean(babystepsEnabled)) {
                    String timeString = attributes.getValue("time");
                    LocalTime time;
                    try {
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:m:s");
                        time = LocalTime.parse("00:" + timeString, timeFormatter);
                        mExercise.addBabystepTime(time.getMinute() * 60 + time.getSecond());
                    } catch (DateTimeParseException e) {
                        e.printStackTrace(); // TODO throw
                    }
                }
                break;
            case TIMETRACKING:
                validateSubTag(TIMETRACKING, CONFIG);

                String trackingEnabled = attributes.getValue("value");
                boolean enabled = Boolean.parseBoolean(trackingEnabled);
                mExercise.setTracking(enabled);
                break;
            default:
                System.out.println("Skipping unknown tag: " + qName);
        }
        mTags.add(qName);
    }

    private void validateRootTag(String tag) throws CatalogRootTagException {
        if (mTags.size() > 0)
            throw new CatalogRootTagException(tag, mLocator);
    }

    private void validateSubTag(String subtag, String tag) throws CatalogTagException {
        if (!mTags.get(mTags.size() - 1).equals(tag))
            throw new CatalogTagException(subtag, tag, mLocator);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String substring = new String(ch).substring(start, start + length);
        if (substring.trim().length() == 0)
            return;

        switch (mTags.get(mTags.size() -1)) {
            case DESCRIPTION:
                mDescription += substring;
                break;
            case CLASS:
                mClassContent += substring;
                break;
            case TEST:
                mTestContent += substring;
                break;
            default:
                System.out.println("Can't handle characters: " + substring);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case DESCRIPTION:
                mExercise.addDescription(mDescription);
                break;
            case CLASS:
                mExercise.addClassContent(mClassContent);
                break;
            case TEST:
                mExercise.addTestContent(mTestContent);
                break;
            case EXERCISE:
                if (mExercise == null)
                    throw new CatalogTagNotFoundException(EXERCISE, EXERCISES, mLocator);
                else if (mExercise.getClassName() == null)
                    throw new CatalogTagNotFoundException(CLASS, CLASSES, mLocator);
                else if (mExercise.getTestName() == null)
                    throw new CatalogTagNotFoundException(TEST, TESTS, mLocator);
                mCatalog.addExercise(mExercise);
                clearCurrentExercise();
                mExercise = null;
                break;
        }
        mTags.remove(mTags. size() - 1);
    }

    private void clearCurrentExercise() {
        mExercise = null;
        mDescription = "";
        mClassContent = "";
        mTestContent = "";
    }

    private class CatalogTagException extends SAXParseException {
        public CatalogTagException(String subtag, String tag, Locator mLocator) {
            super(String.format("%s ist kein Untertag von %s", subtag, tag), mLocator);
        }
    }

    private class CatalogTagNotFoundException extends SAXParseException {
        public CatalogTagNotFoundException(String subtag, String tag, Locator mLocator) {
            super(String.format("%s enthält keinen oder ungültigen %s-Untertag", tag, subtag), mLocator);
        }
    }

    private class CatalogRootTagException extends SAXParseException {
        public CatalogRootTagException(String tag, Locator mLocator) {
            super(String.format("%s ist kein Roottag", tag), mLocator);
        }
    }
}
