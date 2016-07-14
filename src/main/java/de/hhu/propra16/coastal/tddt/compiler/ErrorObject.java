package de.hhu.propra16.coastal.tddt.compiler;


public class ErrorObject {

    private ErrorType error;
    private String message;
    private String section;

    public ErrorObject(ErrorType error, String message, String section) {
        this.error = error;
        this.message = message;
        this.section = section;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        if(error == ErrorType.compilerErrorProgram) {
           return "Kompilierfehler im Programm";
        } else if (error == ErrorType.compilerErrorTest) {
          return "Kompilierfehler im Test";
        }  else if(error == ErrorType.TestsNotFailed) {
          return "Kein Test fehlgeschlagen in RED";
        }
          return "Nicht alle Tests erfüllt in " + section;
    }

}
