import java.util.Comparator;

public class Variable {

    Integer value;//ID of variable
    boolean canBeTrue;//Constraint
    boolean canBeFalse;//Constraint
    Integer degree;//Number of occurrances in prop
    boolean current;//Current bool for testing

    public Variable(Integer value, Integer degree){
        this.value = value;
        canBeTrue = true;
        canBeFalse = true;
        this.degree = degree;
        current = true;
    }

    public boolean isEqual(Variable test){

        if(test.value.equals(this.value))
            return true;
        else
            return false;
    }
}
