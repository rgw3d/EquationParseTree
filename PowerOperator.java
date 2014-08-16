import java.util.LinkedList;

/**
 * Created by rgw3d on 8/15/2014.
 */
public class PowerOperator extends Operator {

    public PowerOperator(){
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public PowerOperator(LinkedList<EquationNode> Terms){
        super(Terms);
        this.Terms = Terms;
    }
    public PowerOperator(LinkedList<EquationNode> Terms, boolean raiseVar){
        super(Terms);
        this.Terms = Terms;
        RaiseVar = raiseVar;
    }

    private boolean RaiseVar = false;

    public double getNum(){
        if (canEval() && !RaiseVar) {
            return Math.pow(Terms.getFirst().getNum(), Terms.getLast().getNum());
        }
       

    }

    public double getVar(){
        float value = 1;
        for(EquationNode tmp: Terms){
            value *=tmp.getVar();
        }
        return value;
    }

    public Nominal getNominal() throws Exception {
        if(canEval()) {
            return new Nominal(getNum(), getVar());
        }
        else
            throw new Exception("Error: Cannot evaluate expression");
    }

    public boolean canEval(){
        //justs tests to see if the power has a variable or not.  hopefully not
        boolean canEval = true;
        for(EquationNode tmp : Terms){
            canEval &= tmp instanceof  Nominal;//tests to see if instance of nominal.
        }
        return Terms.getLast().getVar() == 0 && canEval;
    }

    public LinkedList<EquationNode> getList(){

        LinkedList<EquationNode> result = new LinkedList<EquationNode>();

        LinkedList<EquationNode> nominals = new LinkedList<EquationNode>();
        nominals.add(new Nominal(1,0));//set group to have a default value
        LinkedList<LinkedList<EquationNode>> groups = new LinkedList<LinkedList<EquationNode>>();
        groups.add(new Nominal(1,0).getList());//set groups to have a default value

        for (EquationNode searchThrough : Terms) {//tests to see if instance of nominal
            if (searchThrough instanceof Nominal)
                nominals.add(searchThrough);
            else
                groups.add(searchThrough.getList());
        }


        return result;

    }


}
