import sun.awt.image.ImageWatched;

import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class Nominal extends NumberStructure{

    private double Num;
    private double Var;

    public static Nominal One = new Nominal(1,0);

    public Nominal(){
        Num = 0;
        Var = 0;
    }

    @Override
    public LinkedList<EquationNode> getTop() {
        LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
        toReturn.add(getNominal());
        return toReturn;
    }
    //does not override getBottom();

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

    //"Number: "+getNum() + "  Var: "+getVar());
    public String toString(){
        String toReturn = ""+getNum();
        if(getVar()!=0){
            toReturn+="x^"+getVar();
        }

        return toReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nominal nominal = (Nominal) o;

        if (Double.compare(nominal.Num, Num) != 0) return false;
        if (Double.compare(nominal.Var, Var) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(Num);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Var);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
