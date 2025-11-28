import java.util.Scanner;
import java.util.List;

public class AdminCLI {
    public static void main(String[] args) {
        CareerEngine engine = new CareerEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Career System Backend Admin ===");

        while (true) {
            System.out.println("\n1. List All Skills");
            System.out.println("2. Add New Career");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<String> skills = engine.getAllSkills();
                    System.out.println("\n--- Available Skills ---");
                    for (String s : skills) {
                        System.out.println("- " + s);
                    }
                    break;

                case "2":
                    System.out.print("\nEnter Career Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Required Skills (comma separated): ");
                    String skillInput = scanner.nextLine();

                    System.out.print("Enter Description: ");
                    String desc = scanner.nextLine();

                    try {
                        engine.addCareer(title, skillInput, desc);
                        System.out.println("Success: Career added to database.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.println("Exiting Admin Tool.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
