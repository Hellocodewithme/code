import java.io.*;

class Student implements Serializable {
    String name;
    int id;
    double marks;

    Student(String name, int id, double marks) {
        this.name = name;
        this.id = id;
        this.marks = marks;
    }
}

public class StudentSer {
    public static void main(String[] args) {
        try {
            // SERIALIZATION
            Student s = new Student("Rahul", 101, 89.5);

            ObjectOutputStream o =
                    new ObjectOutputStream(new FileOutputStream("student.dat"));
            o.writeObject(s);
            o.close();
            System.out.println("Serialization completed.");

            // DESERIALIZATION
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream("student.dat"));
            Student obj = (Student) in.readObject();
            in.close();

            System.out.println("\nDeserialized Student:");
            System.out.println("Name: " + obj.name);
            System.out.println("ID: " + obj.id);
            System.out.println("Marks: " + obj.marks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
