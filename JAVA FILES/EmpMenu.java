import java.io.*;
import java.util.*;

public class EmpMenu {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch = Integer.parseInt(sc.nextLine());

            if (ch == 1) {
                System.out.print("Enter Employee Name: ");
                String name = sc.nextLine();

                System.out.print("Enter Employee ID: ");
                String id = sc.nextLine();

                System.out.print("Enter Designation: ");
                String des = sc.nextLine();

                System.out.print("Enter Salary: ");
                String salary = sc.nextLine();

                FileWriter fw = new FileWriter("employee.txt", true);
                fw.write(id + "," + name + "," + des + "," + salary + "\n");
                fw.close();

                System.out.println("Employee Added.");
            }

            else if (ch == 2) {
                File file = new File("employee.txt");

                if (!file.exists()) {
                    System.out.println("No employee records found.");
                    continue;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                System.out.println("\n--- All Employee Records ---");
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    System.out.println("ID: " + data[0] +
                                       ", Name: " + data[1] +
                                       ", Designation: " + data[2] +
                                       ", Salary: " + data[3]);
                }
                br.close();
            }

            else if (ch == 3) {
                System.out.println("Exiting...");
                break;
            }

            else {
                System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
