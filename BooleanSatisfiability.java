import java.io.InputStream;
import java.util.Scanner;

public class BooleanSatisfiability{

    public static void main(String args[]){


        Scanner reader = new Scanner(System.in);
        System.out.print("Enter a file to test (No quotes needed): ");
        String filename = reader.nextLine();

        Proposition test = new Proposition("src/" + filename);
        test.showProposition();
        float start = System.nanoTime();
        PropEvaluator evaluator = new PropEvaluator(test);
        System.out.println(System.nanoTime() - start);
    }
}