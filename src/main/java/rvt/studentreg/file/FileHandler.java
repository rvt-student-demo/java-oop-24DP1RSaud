package rvt.studentreg.file;

import rvt.studentreg.exceptions.DuplicateEntryException;
import rvt.studentreg.exceptions.StudentNotFoundException;
import rvt.studentreg.model.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler — atbild par visām CSV faila operācijām.
 * Lasīšana, rakstīšana, dzēšana, rediģēšana.
 *
 * CSV formāts:
 *   firstName,lastName,email,personalCode,registeredAt
 */
public class FileHandler {

    private static final String CSV_HEADER = "firstName,lastName,email,personalCode,registeredAt";
    private final String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    // -----------------------------------------------------------------------
    // Publiskās metodes
    // -----------------------------------------------------------------------

    /**
     * Nolasa visus studentus no CSV faila.
     * @return studentu saraksts (var būt tukšs)
     */
    public List<Student> loadAll() {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) return students;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (firstLine && line.startsWith("firstName")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                try {
                    students.add(Student.fromCsvLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("[BRĪDINĀJUMS] Izlaista nepareiza CSV rinda: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[KĻŪDA] Nevar nolasīt failu: " + e.getMessage());
        }
        return students;
    }

    /**
     * Saglabā jaunu studentu CSV failā.
     * Pirms saglabāšanas pārbauda, vai e-pasts un personas kods nav aizņemts.
     *
     * @throws DuplicateEntryException ja e-pasts vai personas kods jau eksistē
     */
    public void saveStudent(Student student) {
        List<Student> existing = loadAll();
        checkDuplicates(student, existing, null);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            writer.write(student.toCsvLine());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[KĻŪDA] Nevar saglabāt studentu: " + e.getMessage());
        }
    }

    /**
     * Dzēš studentu pēc personas koda.
     *
     * @throws StudentNotFoundException ja students nav atrasts
     */
    public void deleteByPersonalCode(String personalCode) {
        List<Student> students = loadAll();
        boolean found = students.removeIf(
                s -> s.getPersonalCode().equalsIgnoreCase(personalCode.trim()));
        if (!found) {
            throw new StudentNotFoundException(personalCode);
        }
        writeAll(students);
        System.out.println("  ✓ Students ar personas kodu '" + personalCode + "' dzēsts.");
    }

    /**
     * Rediģē esošā studenta datus pēc personas koda.
     *
     * @throws StudentNotFoundException  ja students nav atrasts
     * @throws DuplicateEntryException   ja jaunie dati konfliktē ar citiem studentiem
     */
    public void editStudent(String personalCode, Student updated) {
        List<Student> students = loadAll();

        int index = -1;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getPersonalCode().equalsIgnoreCase(personalCode.trim())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new StudentNotFoundException(personalCode);
        }

        checkDuplicates(updated, students, personalCode);
        students.set(index, updated);
        writeAll(students);
    }

    /**
     * Atrod vienu studentu pēc personas koda.
     * @return Student objekts vai null, ja nav atrasts
     */
    public Student findByPersonalCode(String personalCode) {
        return loadAll().stream()
                .filter(s -> s.getPersonalCode().equalsIgnoreCase(personalCode.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Meklē studentus pēc vārda, uzvārda vai e-pasta (daļēja sakritība).
     */
    public List<Student> search(String keyword) {
        String kw = keyword.trim().toLowerCase();
        List<Student> results = new ArrayList<>();
        for (Student s : loadAll()) {
            if (s.getFirstName().toLowerCase().contains(kw) ||
                s.getLastName().toLowerCase().contains(kw)  ||
                s.getEmail().toLowerCase().contains(kw)) {
                results.add(s);
            }
        }
        return results;
    }

    // -----------------------------------------------------------------------
    // Privātās palīgmetodes
    // -----------------------------------------------------------------------

    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                    writer.write(CSV_HEADER);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("[KĻŪDA] Nevar izveidot failu: " + e.getMessage());
            }
        }
    }

    private void writeAll(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, false), StandardCharsets.UTF_8))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Student s : students) {
                writer.write(s.toCsvLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[KĻŪDA] Nevar uzrakstīt failu: " + e.getMessage());
        }
    }

    private void checkDuplicates(Student newStudent, List<Student> existing, String excludeCode) {
        for (Student s : existing) {
            if (excludeCode != null && s.getPersonalCode().equalsIgnoreCase(excludeCode)) {
                continue;
            }
            if (s.getEmail().equalsIgnoreCase(newStudent.getEmail())) {
                throw new DuplicateEntryException(
                        "E-pasts '" + newStudent.getEmail() + "' jau ir reģistrēts!");
            }
            if (s.getPersonalCode().equalsIgnoreCase(newStudent.getPersonalCode())) {
                throw new DuplicateEntryException(
                        "Personas kods '" + newStudent.getPersonalCode() + "' jau eksistē sistēmā!");
            }
        }
    }
}
