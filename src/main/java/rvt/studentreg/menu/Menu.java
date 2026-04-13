package rvt.studentreg.menu;

import rvt.studentreg.display.StudentDisplay;
import rvt.studentreg.exceptions.DuplicateEntryException;
import rvt.studentreg.exceptions.StudentNotFoundException;
import rvt.studentreg.exceptions.ValidationException;
import rvt.studentreg.file.FileHandler;
import rvt.studentreg.model.Student;
import rvt.studentreg.validation.Validator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Menu — galvenā CLI klase.
 * Apstrādā lietotāja ievadi, izsauc atbilstošās operācijas,
 * parāda rezultātus caur StudentDisplay.
 */
public class Menu {

    private final FileHandler fileHandler;
    private final Scanner scanner;
    private static final DateTimeFormatter DT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Menu(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    // -----------------------------------------------------------------------
    // Galvenā cilpa
    // -----------------------------------------------------------------------

    public void start() {
        StudentDisplay.printWelcome();

        boolean running = true;
        while (running) {
            StudentDisplay.printPrompt();
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "register" -> register();
                case "show"     -> show();
                case "remove"   -> remove();
                case "edit"     -> edit();
                case "search"   -> search();
                case "stats"    -> stats();
                case "help"     -> StudentDisplay.printHelp();
                case "exit"     -> {
                    running = false;
                    System.out.println("\n  Uz redzēšanos! Programma apturēta.\n");
                }
                default -> {
                    StudentDisplay.printError("Nezināma komanda: '" + command + "'");
                    StudentDisplay.printInfo("Ierakstiet 'help', lai redzētu pieejamās komandas.");
                }
            }
        }
        scanner.close();
    }

    // -----------------------------------------------------------------------
    // Komandu implementācijas
    // -----------------------------------------------------------------------

    private void register() {
        System.out.println("\n  ── JAUNA STUDENTA REĢISTRĀCIJA ──");

        try {
            String firstName    = askValidated("  Vārds        : ", "Vārds",   "name");
            String lastName     = askValidated("  Uzvārds      : ", "Uzvārds", "name");
            String email        = askValidated("  E-pasts      : ", null,      "email");
            String personalCode = askValidated("  Personas kods: ", null,      "code");

            System.out.print("  Datums/laiks [Enter = tagad | vai ievadiet manuāli]: ");
            String rawDate = scanner.nextLine().trim();
            String registeredAt;
            if (rawDate.isEmpty()) {
                registeredAt = LocalDateTime.now().format(DT_FORMAT);
            } else {
                Validator.validateDateTime(rawDate);
                registeredAt = rawDate;
            }

            Student student = new Student(firstName, lastName, email, personalCode, registeredAt);
            fileHandler.saveStudent(student);

            StudentDisplay.printSuccess("Students veiksmīgi reģistrēts!");
            StudentDisplay.printSingleStudent(student);

        } catch (ValidationException | DuplicateEntryException e) {
            StudentDisplay.printError(e.getMessage());
        }
    }

    private void show() {
        System.out.println("\n  ── VISI STUDENTI ──");
        List<Student> students = fileHandler.loadAll();
        StudentDisplay.printTable(students);
    }

    private void remove() {
        System.out.println("\n  ── STUDENTA DZĒŠANA ──");
        System.out.print("  Ievadiet personas kodu: ");
        String code = scanner.nextLine().trim();

        if (code.isEmpty()) {
            StudentDisplay.printError("Personas kods nedrīkst būt tukšs!");
            return;
        }

        Student found = fileHandler.findByPersonalCode(code);
        if (found == null) {
            StudentDisplay.printError("Students ar personas kodu '" + code + "' nav atrasts.");
            return;
        }

        System.out.println("  Atrastais students: " +
                found.getFirstName() + " " + found.getLastName() +
                " (" + found.getEmail() + ")");
        System.out.print("  Vai tiešām dzēst? (j/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("j") || confirm.equals("jā") || confirm.equals("yes")) {
            try {
                fileHandler.deleteByPersonalCode(code);
                StudentDisplay.printSuccess("Students dzēsts!");
            } catch (StudentNotFoundException e) {
                StudentDisplay.printError(e.getMessage());
            }
        } else {
            StudentDisplay.printInfo("Dzēšana atcelta.");
        }
    }

    private void edit() {
        System.out.println("\n  ── STUDENTA REDIĢĒŠANA ──");
        System.out.print("  Ievadiet personas kodu: ");
        String code = scanner.nextLine().trim();

        Student existing = fileHandler.findByPersonalCode(code);
        if (existing == null) {
            StudentDisplay.printError("Students ar personas kodu '" + code + "' nav atrasts.");
            return;
        }

        System.out.println("  Atrasts: " + existing.getFirstName() + " " + existing.getLastName());
        System.out.println("  [Nospiediet Enter, lai atstātu esošo vērtību]\n");

        try {
            String firstName = askOrKeep("  Vārds        [" + existing.getFirstName() + "]: ",
                    existing.getFirstName(), "name", "Vārds");
            String lastName  = askOrKeep("  Uzvārds      [" + existing.getLastName() + "]: ",
                    existing.getLastName(),  "name", "Uzvārds");
            String email     = askOrKeep("  E-pasts      [" + existing.getEmail() + "]: ",
                    existing.getEmail(),     "email", null);
            String newCode   = askOrKeep("  Personas kods[" + existing.getPersonalCode() + "]: ",
                    existing.getPersonalCode(), "code", null);

            System.out.print("  Datums/laiks  [" + existing.getRegisteredAt() + "]: ");
            String rawDate = scanner.nextLine().trim();
            String registeredAt = rawDate.isEmpty() ? existing.getRegisteredAt() : rawDate;
            if (!rawDate.isEmpty()) {
                Validator.validateDateTime(rawDate);
            }

            Student updated = new Student(firstName, lastName, email, newCode, registeredAt);
            fileHandler.editStudent(code, updated);
            StudentDisplay.printSuccess("Dati atjaunināti veiksmīgi!");
            StudentDisplay.printSingleStudent(updated);

        } catch (ValidationException | DuplicateEntryException e) {
            StudentDisplay.printError(e.getMessage());
        }
    }

    private void search() {
        System.out.println("\n  ── MEKLĒŠANA ──");
        System.out.print("  Ievadiet meklēšanas frāzi (vārds, uzvārds vai e-pasts): ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            StudentDisplay.printError("Meklēšanas frāze nedrīkst būt tukša!");
            return;
        }

        List<Student> results = fileHandler.search(keyword);
        if (results.isEmpty()) {
            StudentDisplay.printBox("Nav rezultātu priekš: '" + keyword + "'");
        } else {
            System.out.println("  Atrasti " + results.size() + " rezultāti priekš '" + keyword + "':");
            StudentDisplay.printTable(results);
        }
    }

    private void stats() {
        System.out.println("\n  ── STATISTIKA ──");
        List<Student> students = fileHandler.loadAll();

        System.out.println("  Reģistrēto studentu skaits : " + students.size());

        if (!students.isEmpty()) {
            students.stream()
                    .max((a, b) -> a.getRegisteredAt().compareTo(b.getRegisteredAt()))
                    .ifPresent(s -> System.out.println(
                            "  Pēdējais reģistrētais      : " +
                            s.getFirstName() + " " + s.getLastName() +
                            " (" + s.getRegisteredAt() + ")"));

            students.stream()
                    .map(s -> s.getEmail().contains("@") ?
                            s.getEmail().split("@")[1] : "nezināms")
                    .distinct()
                    .limit(3)
                    .forEach(domain ->
                            System.out.println("  E-pasta domēns             : @" + domain));
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Privātās palīgmetodes
    // -----------------------------------------------------------------------

    private String askValidated(String prompt, String label, String type) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        switch (type) {
            case "name"  -> Validator.validateName(value, label);
            case "email" -> Validator.validateEmail(value);
            case "code"  -> Validator.validatePersonalCode(value);
        }
        return value;
    }

    private String askOrKeep(String prompt, String existing, String type, String label) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return existing;
        switch (type) {
            case "name"  -> Validator.validateName(input, label);
            case "email" -> Validator.validateEmail(input);
            case "code"  -> Validator.validatePersonalCode(input);
        }
        return input;
    }
}
