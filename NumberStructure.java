import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public abstract class NumberStructure implements EquationNode {


    public LinkedList<EquationNode> getBottom(){
        LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
        toReturn.add(Nominal.One);
        return toReturn;
    }
    public LinkedList<EquationNode> getTop(){
        LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
        toReturn.add(Nominal.One);
        return toReturn;
    };
}
