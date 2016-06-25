package de.hhu.propra16.coastal.tddt;

public class Exercise {

    private final String mName;
    private String mDescription;
    private String mClassContent;
    private String mTestContent;
    private String mClassName;
    private String mTestName;

    public Exercise(String name) {
        this.mName = name;
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

    public String getClassContent() {
        return mClassContent;
    }

    public String getTestContent() {
        return mTestContent;
    }

    public String getClassName() {return mClassName; }

    public String getTestName() {return mTestName; }

    public String getDescription() {return mDescription; }

    public String toString() {return mName; }
}
