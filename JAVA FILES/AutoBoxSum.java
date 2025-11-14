import java.util.*;

public class AutoBoxSum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of integers: ");
        int n = Integer.parseInt(sc.nextLine());  // parse string → int

        List<Integer> list = new ArrayList<>();

        System.out.println("Enter " + n + " integers:");
        for (int i = 0; i < n; i++) {
            Integer num = Integer.parseInt(sc.nextLine());  // autoboxing
            list.add(num);
        }

        int sum = 0;
        for (Integer x : list) {
            sum += x;    // unboxing
        }

        System.out.println("Sum = " + sum);
    }
}
