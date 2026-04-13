package rvt.studentreg.display;

import rvt.studentreg.model.Student;
import java.util.List;

/**
 * StudentDisplay — atbild tikai par to, kā dati tiek izvadīti konsolē.
 * Formatē studentus tabulas veidā ar printf.
 */
public class StudentDisplay {

    private static final int W_NR      = 4;
    private static final int W_NAME    = 16;
    private static final int W_SURNAME = 16;
    private static final int W_EMAIL   = 26;
    private static final int W_CODE    = 14;
    private static final int W_DATE    = 17;
    private static final int TOTAL_WIDTH = W_NR + W_NAME + W_SURNAME + W_EMAIL + W_CODE + W_DATE + 13;

    // -----------------------------------------------------------------------
    // Galvenā tabulas izvades metode
    // -----------------------------------------------------------------------

    public static void printTable(List<Student> students) {
        if (students.isEmpty()) {
            printBox("Nav reģistrētu studentu.");
            return;
        }

        printSeparator('=');
        printHeader();
        printSeparator('-');

        int nr = 1;
        for (Student s : students) {
            printRow(nr++, s);
        }

        printSeparator('=');
        System.out.printf("  Kopā reģistrēti: %d students%s%n",
                students.size(), students.size() == 1 ? "" : "i");
        System.out.println();
    }

    public static void printSingleStudent(Student s) {
        System.out.println();
        printSeparator('=');
        System.out.println("  STUDENTA KARTE");
        printSeparator('-');
        System.out.printf("  %-14s : %s %s%n",  "Vārds, Uzvārds", s.getFirstName(), s.getLastName());
        System.out.printf("  %-14s : %s%n",      "E-pasts",        s.getEmail());
        System.out.printf("  %-14s : %s%n",      "Personas kods",  s.getPersonalCode());
        System.out.printf("  %-14s : %s%n",      "Reģistrēts",     s.getRegisteredAt());
        printSeparator('=');
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Statusa ziņojumi
    // -----------------------------------------------------------------------

    public static void printSuccess(String message) {
        System.out.println("\n  ✓ " + message + "\n");
    }

    public static void printError(String message) {
        System.out.println("\n  ✗ KĻŪDA: " + message + "\n");
    }

    public static void printInfo(String message) {
        System.out.println("  → " + message);
    }

    public static void printBox(String message) {
        String border = "+" + "-".repeat(message.length() + 4) + "+";
        System.out.println("\n  " + border);
        System.out.println("  |  " + message + "  |");
        System.out.println("  " + border + "\n");
    }

    public static void printWelcome() {
        System.out.println();
        printSeparator('*');
        System.out.println("  STUDENTU REĢISTRĀCIJAS SISTĒMA  v1.0");
        printSeparator('*');
        System.out.println("  Komandas: register | show | remove | edit | search | stats | exit");
        printSeparator('-');
        System.out.println();
    }

    public static void printPrompt() {
        System.out.print("\n  > Ievadiet komandu: ");
    }

    public static void printHelp() {
        System.out.println();
        printSeparator('-');
        System.out.println("  PIEEJAMĀS KOMANDAS:");
        System.out.println("  register  — reģistrēt jaunu studentu");
        System.out.println("  show      — parādīt visus studentus");
        System.out.println("  remove    — dzēst studentu pēc personas koda");
        System.out.println("  edit      — rediģēt studenta datus");
        System.out.println("  search    — meklēt studentu pēc vārda/e-pasta");
        System.out.println("  stats     — rādīt statistiku");
        System.out.println("  exit      — iziet no programmas");
        printSeparator('-');
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Privātās palīgmetodes
    // -----------------------------------------------------------------------

    private static void printHeader() {
        System.out.printf("  %-" + W_NR + "s | %-" + W_NAME + "s | %-" + W_SURNAME + "s | %-"
                + W_EMAIL + "s | %-" + W_CODE + "s | %-" + W_DATE + "s%n",
                "#", "Vārds", "Uzvārds", "E-pasts", "Personas kods", "Reģistrācijas laiks");
    }

    private static void printRow(int nr, Student s) {
        System.out.printf("  %-" + W_NR + "d | %-" + W_NAME + "s | %-" + W_SURNAME + "s | %-"
                + W_EMAIL + "s | %-" + W_CODE + "s | %-" + W_DATE + "s%n",
                nr,
                truncate(s.getFirstName(),  W_NAME),
                truncate(s.getLastName(),   W_SURNAME),
                truncate(s.getEmail(),      W_EMAIL),
                s.getPersonalCode(),
                s.getRegisteredAt());
    }

    private static void printSeparator(char ch) {
        System.out.println("  " + String.valueOf(ch).repeat(TOTAL_WIDTH));
    }

    private static String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}
