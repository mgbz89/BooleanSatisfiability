import java.lang.reflect.Array;
import java.util.ArrayList;

public class PropEvaluator {

    private Proposition prop;
    private TestableValues start;

    enum Eval {
        TRUE, MAYBE, FALSE
    }

    public PropEvaluator(Proposition prop){
        this.prop = prop;
        start = new TestableValues(new ArrayList<Variable>(), 0);
        ArrayList<Variable> answer = BackTracking(start);
        if(answer == null) {
            System.out.println("No solution");
            return;
        }
        sortVariablesByValue(answer);

        for (Variable variable:answer) {
            System.out.println("Variable: " + variable.value);
            System.out.println("Value: " + variable.current);
        }
    }

    public void sortVariablesByValue(ArrayList<Variable> variables) {

        for (int i = 0; i < variables.size() - 1; i ++){
            for(int j = 0; j < variables.size() - i - 1; j++){
                if(variables.get(j).value > variables.get(j+1).value){
                    Variable temp = variables.get(j);
                    Variable temp2 = variables.get(j + 1);
                    variables.set(j, temp2);
                    variables.set(j + 1, temp);
                }
            }
        }
    }

    private ArrayList<Variable> BackTracking(TestableValues start) {
        TestableValues current = start;
        TestableValues test;
        int count = 0;

        while (true) {


            if (current.visited == false) {
                count++;
                current.visited = true;
                //System.out.println("Affected by FC: " + ForwardChecking(current.variables));
                Eval eval = EvaluateProp(prop.clauses, current.variables);
                current.showVariables();

                if(eval == Eval.FALSE){
                    if(current.parent == null) {
                        System.out.println("Number of tests: " + count);
                        return null;
                    }
                    else {
                        for (int i = current.variables.size() - 1; i >= 0; i--){
                            if(!containsVariable(current.parent.variables, current.variables.get(i).value)){
                                current.variables.get(i).current = true;
                                prop.variables.add(0, current.variables.get(i));
                                continue;
                            }
                        }

                        current = current.parent;
                        continue;
                    }
                }
                if(eval == Eval.TRUE){
                    System.out.println("Number of tests: " + count);
                    return current.variables;
                }

            } else {
                if (current.left == null) {
                    Variable newVariable = prop.variables.remove(0);
                    TestableValues left = new TestableValues(current.variables, current.depth + 1);
                    left.variables.add(newVariable);
                    current.left = left;
                    left.parent = current;
                    current = left;
                    continue;
                }
                if (current.right == null) {
                    Variable newVariable = prop.variables.remove(0);
                    newVariable.current = false;
                    TestableValues right = new TestableValues(current.variables, current.depth + 1);
                    right.variables.add(newVariable);
                    current.right = right;
                    right.parent = current;
                    current = right;
                    continue;
                }
                if (current.parent == null) {
                    System.out.println("Number of tests: " + count);
                    return null;
                } else {
                    for (int i = current.variables.size() - 1; i >= 0; i--){
                        if(!containsVariable(current.parent.variables, current.variables.get(i).value)){
                            current.variables.get(i).current = true;
                            prop.variables.add(0, current.variables.get(i));
                            continue;
                        }
                    }
                    current.left = null;
                    current.right = null;
                    current = current.parent;
                }
            }
        }
    }

    private int ForwardChecking(ArrayList<Variable> variables){

        int count = 0;
        int countNew = 0;
        ArrayList<Integer> inClause = new ArrayList<>();
        ArrayList<Variable> addedVariables = new ArrayList<>();

        for (ArrayList<Integer> clause:this.prop.clauses) {
            if(EvaluateClause(clause, variables) != Eval.TRUE) {
                for (Variable variable : variables) {
                    if (clause.contains(variable.value) || clause.contains(-variable.value)) {
                        count++;
                        inClause.add(variable.value);
                    }
                }
                if (count == clause.size() - 1) {
                    for (Integer value : clause) {
                        if (!inClause.contains(value)) {
                            if (!containsVariable(variables, Math.abs(value))) {
                                Variable newVariable = new Variable(Math.abs(value), prop.getDegree(value));
                                for (int i = 0; i < prop.variables.size(); i++) {
                                    if(newVariable.isEqual(prop.variables.get(i))){
                                        prop.variables.remove(i);
                                        break;
                                    }
                                }
                                if (value < 0)
                                    newVariable.current = false;
                                variables.add(newVariable);
//                                if(!containsVariable(addedVariables, newVariable.value))
//                                    addedVariables.add(newVariable);
                                countNew++;
                                break;
                            }
                        }
                    }
                }
                inClause.clear();
                count = 0;
            }
        }

        //variables.addAll(addedVariables);
        return countNew;
    }


    private boolean containsVariable(ArrayList<Variable> variables, Integer value){

        for(Variable variable:variables){
            if(value.equals(variable.value))
                return true;
        }

        return false;
    }

    private Eval EvaluateClause(ArrayList<Integer> clause, ArrayList<Variable> variables){//Evaluates individual clause

        int count = 0;

        for(Variable variable:variables){
            int index = clause.indexOf(variable.value);
            if(index == -1) {
                index = clause.indexOf(-variable.value);
                if(index == -1)
                    continue;
            }
            count++;
            if(variable.current && clause.get(index) > 0) {
                //System.out.println("TRUE!");
                return Eval.TRUE;
            }
            if(!variable.current && clause.get(index) < 0) {
                //System.out.println("TRUE!");
                return Eval.TRUE;
            }
        }

        //System.out.println("COUNT: " + count);
        if(count == clause.size()) {
            //System.out.println("FALSE!");
            return Eval.FALSE;
        }

        //System.out.println("MAYBE!");
        return Eval.MAYBE;
    }

    private Eval EvaluateProp(ArrayList<ArrayList<Integer>> prop, ArrayList<Variable> variables){//Evaluates entire prop

        if(variables.size() == 0)
            return Eval.MAYBE;

        boolean notTrue = false;

        for(ArrayList<Integer> clause:prop) {
            switch (EvaluateClause(clause, variables)){
                case FALSE:
                    return Eval.FALSE;

                case MAYBE:
                    notTrue = true;
                    break;
            }
        }

        if(notTrue == false)
            return Eval.TRUE;

        return Eval.MAYBE;
    }
}
