import java.util.ArrayList;

public class TestableValues {

    ArrayList<Variable> variables;
    TestableValues left;
    TestableValues right;
    TestableValues parent;
    Integer depth;
    boolean visited;

    public TestableValues(ArrayList<Variable> variables, Integer depth){
        this.variables = (ArrayList<Variable>) variables.clone();
        //this.variables = variables;
        right = null;
        left = null;
        parent = null;
        visited = false;
        this.depth = depth;
    }

    public void showVariables(){
        for (Variable variable:variables) {
            System.out.print(variable.value + ": " + variable.current + ", ");
        }
        System.out.println("");
    }
}
