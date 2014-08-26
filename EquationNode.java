import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 *
 */
public interface EquationNode {
    public double getNum() throws CanNotEval;//returns the number from the operation
    public double getVar() throws CanNotEval;//returns the variable from the operation
    public Nominal getNominal() throws CanNotEval; //returns a nominal
    public boolean canEval() throws CanNotEval; // if the getNum/getVar/getNominal methods can be called
    public LinkedList<EquationNode> getList() throws CanNotEval;//returns a list of nominals

}
