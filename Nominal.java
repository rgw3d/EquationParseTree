import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class Nominal extends NumberStructure{

    private double Num;
    private double Var;

    public Nominal(){
        Num = 0;
        Var = 0;
    }
    public Nominal(double Num, double Var){
        this.Num = Num;
        this.Var = Var;
    }

    public double getNum(){
        return Num;
    }

    public double getVar(){
        return Var;
    }

    public boolean canEval(){ return true; }

    public Nominal getNominal() { return this; }

    public LinkedList<EquationNode> getList()
    {
        LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
        tmp.add(this);
        return tmp;
    }


    public String toString(){
        return ("Number: "+getNum() + "  Var: "+getVar());
    }


}
