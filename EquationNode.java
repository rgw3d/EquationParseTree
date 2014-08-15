import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 *
 */
public interface EquationNode {
    public double getNum();//returns the number from the operation
    public double getVar();//returns the variable from the operation
    public Nominal getNominal() throws Exception; //returns a nominal
    public boolean canEval(); // if the getNum/getVar/getNominal methods can be called
    public LinkedList<EquationNode> getList();//returns a list of nominals
        
}
