package de.hhu.propra16.coastal.tddt.catalog;

public class Exercise {

    private final String mName;
    private boolean mBabystepEnabled;
    private int mBabystepTime;
    private String mDescription;
    private String mClassContent;
    private String mTestContent;
    private String mClassName;
    private String mTestName;
    private boolean mTracking;

    public Exercise(String name) {
        this.mName = name;
    }

    public void addBabystepTime(int time) {
        mBabystepEnabled = true;
        mBabystepTime = time;
    }

    public void addDescription(String description) {
        this.mDescription = description;
    }

    public void addClassContent(String classContent) {
        this.mClassContent = classContent;
    }

    public void addTestContent(String testContent) {
        this.mTestContent = testContent;
    }

    public void addClassName(String className) {
        this.mClassName = className;
    }

    public void addTestName(String testName) {
        this.mTestName = testName;
    }

    public void setTracking(boolean enabled) {
        this.mTracking = enabled;
    }

    public String getClassContent() {
        return mClassContent;
    }

    public String getTestContent() {
        return mTestContent;
    }

    public String getClassName() {return mClassName; }

    public String getTestName() {return mTestName; }

    public String getDescription() {return mDescription; }

    public int getBabystepTime() {return mBabystepTime; }

    public boolean isBabysteps() {return mBabystepEnabled; }

    public boolean isTracking() {return mTracking; }

    public String toString() {return mName; }
}
