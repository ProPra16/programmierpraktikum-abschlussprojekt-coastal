package de.hhu.propra16.coastal.tddt.atdd;

public class ATDD {

    public static boolean isAcceptanceTest(String methodName) {
        return methodName.startsWith("testFeature");
    }
}
