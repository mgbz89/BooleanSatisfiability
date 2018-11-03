import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Proposition {

    ArrayList<ArrayList<Integer>> clauses;//Storage for each clause
    int numClauses;//Number of different clauses for the proposition
    int numVariables;//Number of different variables for the proposition
    ArrayList<Variable> variables;

    public Proposition(String filename){

        File file = new File(filename);
        Scanner fileReader = null;//Read the file line by line
        Scanner lineReader;//Read each line of ints individually

        try {//Try to open file
            fileReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(fileReader.hasNextLine()){//If there is a first line in the file
            lineReader = new Scanner(fileReader.nextLine());//Read line
            while(!lineReader.hasNextInt()){//Ignore p cnf
                lineReader.next();
            }
            numVariables = lineReader.nextInt();//Read first int for variables
            numClauses = lineReader.nextInt();//Read second int for clauses
        }

        variables = new ArrayList<>();
        for(int i = 0; i < numVariables; i++){
            variables.add(new Variable(i + 1, 0));
        }

        clauses = new ArrayList<>();

        while(fileReader.hasNextLine()) {//If there is another line
            lineReader = new Scanner(fileReader.nextLine());//Read line

            ArrayList<Integer> temp = new ArrayList<>();
            Integer num;

            while (lineReader.hasNextInt()) {//For each int in the line

                num = lineReader.nextInt();

                if (num == 0) {//If the int is 0 add the clause
                    clauses.add(temp);
                    break;
                }

                variables.get(Math.abs(num) - 1).degree++;//Add count to degree for variable
                temp.add(num);//If not 0 add variable to the clause
            }
        }

        sortValuesByDegree();
        for (Variable variable:variables) {
            System.out.print(variable.value + " ");
        }

        System.out.println("");
    }


    public void showProposition(){

        for(ArrayList<Integer> clause:clauses) {//For each clause
            System.out.print("(");
            for(Integer variable:clause) {//For each variable
                if(clause.indexOf(variable) == clause.size() - 1){//If not last variable
                    System.out.print("x" + variable);
                    break;
                }
                System.out.print("x" + variable + " V ");
            }
            System.out.print(") ^ ");
        }
        System.out.println();
    }


    public void sortValuesByDegree() {

        for (int i = 0; i < variables.size() - 1; i ++){
            for(int j = 0; j < variables.size() - i - 1; j++){
                if(variables.get(j).degree < variables.get(j+1).degree){
                    Variable temp = variables.get(j);
                    Variable temp2 = variables.get(j + 1);
                    variables.set(j, temp2);
                    variables.set(j + 1, temp);
                }
            }
        }
    }

    public int getDegree(Integer find){
        int count = 0;

        for (ArrayList<Integer> clause:clauses) {
            for (Integer value:clause) {
                if(find.equals(value) || find.equals(-value))
                    count++;
            }
        }

        return count;
    }
}
