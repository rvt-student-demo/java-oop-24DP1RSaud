package rvt.studentreg;

import rvt.studentreg.file.FileHandler;
import rvt.studentreg.menu.Menu;

/**
 * App — programmas ievadpunkts.
 *
 * Inicializē FileHandler ar CSV faila ceļu un palaiž CLI izvēlni.
 * CSV fails tiek saglabāts projekta "data/" mapē (jau eksistē tavā projektā).
 *
 * Palaišana ar Maven:
 *   mvn compile
 *   mvn exec:java -Dexec.mainClass="rvt.App"
 *
 * Vai ar javac manuāli:
 *   javac -encoding UTF-8 -d out src/main/java/rvt/**\/*.java
 *   java -cp out rvt.App
 */
public class App {

    // CSV fails tiek saglabāts "data/" mapē — tā jau eksistē tavā projektā!
    private static final String CSV_FILE_PATH = "data/students.csv";

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler(CSV_FILE_PATH);
        Menu menu = new Menu(fileHandler);
        menu.start();
    }
}
