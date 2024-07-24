import java.io.*;
import java.util.*;

public class MyProgram {
    private static final String FILE_NAME = "student_grades.txt";
    private static HashMap<Integer, ArrayList<Integer>> studentDatabase;

    public static void main(String[] args) {
        studentDatabase = loadDatabaseFromFile();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nStudent Database Menu:");
            System.out.println("a. Add student");
            System.out.println("b. Delete student");
            System.out.println("c. Display student");
            System.out.println("d. Exit");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "a":
                    addStudent(scanner);
                    break;
                case "b":
                    deleteStudent(scanner);
                    break;
                case "c":
                    displayStudent();
                    break;
                case "d":
                    saveDatabaseToFile();
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option (a, b, c, or d).");
            }
        }
    }

    private static void addStudent(Scanner scanner) {
        System.out.print("Enter student number (6 digits): ");
        int studentNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ArrayList<Integer> existingGrades = studentDatabase.get(studentNumber);
            if (existingGrades != null) {
            System.out.println("Student already exists in the database. Do you want to add more grades? (yes/no)");
            String addMoreGrades = scanner.nextLine().toLowerCase();
            if (!addMoreGrades.equals("yes")) {
                return;
            }
        } else {
            existingGrades = new ArrayList<>();
        }
        System.out.print("Enter additional grades (comma-separated): ");
        String[] gradesInput = scanner.nextLine().split(",");
        for (String grade : gradesInput) {
            try {
                int gradeValue = Integer.parseInt(grade.trim());
                existingGrades.add(gradeValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid grade format. Please enter numeric grades separated by commas.");
                return;
            }
        }
        studentDatabase.put(studentNumber, existingGrades);
        System.out.println("Grades added successfully.");
        saveDatabaseToFile(); // Save database to file after adding grades
    }


    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter student number to delete (6 digits): ");
        int studentNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (!studentDatabase.containsKey(studentNumber)) {
            System.out.println("Student not found in the database.");
            return;
        }
        studentDatabase.remove(studentNumber);
        System.out.println("Student deleted successfully.");
    }

    private static void displayStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student number to display (6 digits): ");
        int studentNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ArrayList<Integer> grades = studentDatabase.get(studentNumber);
        if (grades == null) {
            System.out.println("Student not found in the database.");
        } else {
            System.out.println("Student Number: " + studentNumber);
            System.out.println("Grades: " + grades);
            
            // Calculate average grade
                double sum = 0;
            for (Integer grade : grades) {
                sum += grade;
            }
            double average = sum / grades.size();
            System.out.println("Average Grade: " + average);
        }
    }


    private static HashMap<Integer, ArrayList<Integer>> loadDatabaseFromFile() {
        HashMap<Integer, ArrayList<Integer>> database = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int studentNumber = Integer.parseInt(parts[0]);
                ArrayList<Integer> grades = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    grades.add(Integer.parseInt(parts[i]));
                }
                database.put(studentNumber, grades);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Database file not found. Creating new database.");
        } catch (IOException e) {
            System.out.println("Error reading from database file.");
        }
        return database;
    }

    private static void saveDatabaseToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<Integer, ArrayList<Integer>> entry : studentDatabase.entrySet()) {
                if (!entry.getValue().isEmpty()) { // Check if grades list is not empty
                    pw.print(entry.getKey() + ",");
                    ArrayList<Integer> grades = entry.getValue();
                    for (int i = 0; i < grades.size(); i++) {
                        pw.print(grades.get(i));
                        if (i < grades.size() - 1) {
                            pw.print(",");
                        }
                    }
                    pw.println();
                    }
                }
        } catch (IOException e) {
            System.out.println("Error writing to database file.");
        }
    }

}
