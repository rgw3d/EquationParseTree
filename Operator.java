import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public abstract class Operator implements EquationNode{

    public LinkedList<EquationNode> Terms;

    public Operator(){
        Terms  = new LinkedList<EquationNode>();
    }
    public Operator(LinkedList<EquationNode> Terms){
        this.Terms = Terms;
    }

    public void addTerm(EquationNode eqt){
        this.Terms.add(eqt);
    }


}
