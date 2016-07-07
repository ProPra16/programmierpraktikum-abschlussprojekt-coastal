package de.hhu.propra16.coastal.tddt;

public class ATDD {

    public static boolean isAcceptanceTest(String methodName) {
        return methodName.startsWith("testFeature");
    }
}
