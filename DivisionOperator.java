import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class DivisionOperator extends Operator{
    public DivisionOperator(){
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public DivisionOperator(LinkedList<EquationNode> Terms){
        super(Terms);
        this.Terms = Terms;
    }

    public double getNum(){
        double value =0;
        for(EquationNode tmp: Terms){
            if(tmp == Terms.getFirst()){
                value = tmp.getNum();
            }
            else
            {
                value /= tmp.getNum();
            }
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

    public double getVar(){
        double value=  0;
        for(EquationNode tmp: Terms){
            if(tmp == Terms.getFirst()){
                value = tmp.getVar();
            }
            else
            {
                value -= tmp.getVar();
            }
        }
        return value;
    }

    public boolean canEval(){
        boolean canEval = true;

        for(EquationNode tmp : Terms){
            canEval &= tmp instanceof  Nominal;//tests to see if instance of nominal.
        }

        return canEval;
    }

    public LinkedList<EquationNode> getList(){

        LinkedList<EquationNode> result = new LinkedList<EquationNode>();

        LinkedList<EquationNode> nominals = new LinkedList<EquationNode>();
        nominals.add(new Nominal(1,0));//set group to have a default value
        LinkedList<LinkedList<EquationNode>> groups = new LinkedList<LinkedList<EquationNode>>();
        groups.add(new Nominal(1, 0).getList());//set groups to have a default value

        for (EquationNode searchThrough : Terms) {//tests to see if instance of nominal
            if (searchThrough instanceof Nominal)
                nominals.add(searchThrough);
            else {
                //.add(searchThrough.getList());
                //groups.
            }
        }

        Nominal simplifiedNominal = multiplyNominals(nominals);//multiply the nominals together
        LinkedList<EquationNode> simplifiedLinkedList = multiplyLists(groups);//multiply the lists together
        result = multiplyFinal(simplifiedNominal, simplifiedLinkedList);//multiply everything together

        return result;

    }

    public Nominal multiplyNominals(LinkedList<EquationNode> nomials){

        Nominal combine = new Nominal(1,0);//set default value
        for(EquationNode cycle: nomials){
            combine  =  new Nominal(combine.getNum() * cycle.getNum(), combine.getVar() + cycle.getVar());
        }//combines everything together

        return combine;

    }

    public LinkedList<EquationNode> multiplyLists(LinkedList<LinkedList<EquationNode>> groups){
        LinkedList<EquationNode> result = new LinkedList<EquationNode>();
        result.add(new Nominal(1, 0));//set default value

        for(LinkedList<EquationNode> groupList: groups){
            for(EquationNode insideNominal: groupList) {
                LinkedList<EquationNode> tmpResult = new LinkedList<EquationNode>();
                for (EquationNode tmp : result) {
                    tmpResult.add(new Nominal(insideNominal.getNum()*tmp.getNum(), tmp.getVar()+ insideNominal.getNum()));
                }
                result = tmpResult;//switch the two lists
            }
        }
        return result;
    }

    public LinkedList<EquationNode> multiplyFinal(Nominal nom, LinkedList<EquationNode> list){

        LinkedList<EquationNode> finalList = new LinkedList<EquationNode>();

        for(EquationNode x: list){
            finalList.add(new Nominal((nom.getNum() * x.getNum()),(nom.getVar() + x.getVar())));
        }

        return finalList;
    }

}
