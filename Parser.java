import java.util.Scanner;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rgw3d on 8/12/2014.
 */
public class Parser
{

    public Parser(){}

    /**
     * Essentially this will be called recursivly, until the only thing left to parse are
     * Nominals/variables which will end parsing.
     * The rules for parsing:
     *  Find + outside of Parenthesis
     *  Find Mult or Div outside of Parenthesis
     *  Find Exponents outsside of Parenthesis
     *  Find Parenthesis
     *  Find everything else that we just did but inside this new parenthesis
     *
     * Essentially it is reverse Pemdas, as the things that are found last are
     * the statements that will be done first.  This parser will find the things that
     * are done last first.
     */
    public EquationNode ParseEquation(String input) throws UnparsedString {

        /*
        start from end.  skip parenthesis.
        if nothing is found, use parenthesis
        runs through, in this order, +, *, /, ^(not attatched to paren)
        if nothing has been found, find parentheiss.
        if parenthesis has not been found, its just a number and then make a nominal

        set the Terms of the operator to the left side of the split string
        and to the right side of the split string

        */

        String[] operands = new String[] {"+","*","/","^"};

        for(String op: operands){//loop through each of the operands once
            //go backwards through the string to search for the operand
            //skip parenthesis
            //if anyting is found, foundOutParen is set to true.

            boolean foundOutParen = false;
            boolean hasParen = false;

            for(int indx = input.length()-1; indx>0; indx--){
                //loop through backwards
                String eqtIndx = ""+input.charAt(indx);
                //first check to see if it is a closed paren.
                if(eqtIndx.equals(")")){
                    hasParen = true;
                    //skips paren and sets indx to its proper "skipped" value
                    indx = skipParen(input,indx);
                    continue;
                    //skip the rest of the statements so that indx can be again incremented
                    // this is to prevent indx from being equal to 0 and being decremented
                }
                if(eqtIndx.equals(op)){
                    //yay we have a hit
                    //make sure that it is not a ^ next to a parenthesis
                    if(op.equals("^") && (input.charAt(indx-1)+"").equals(")")){
                        continue;
                        //not something that we deal with here. skip it
                    }

                    Operator operator;

                    if(op.equals("+")){
                        operator = new AdditionOperator();
                    }
                    else if(op.equals("*")){
                        operator = new MultiplicationOperator();
                    }
                    else if(op.equals("/")){
                        operator = new DivisionOperator();
                    }
                    else {
                        operator = new AdditionOperator();
                    }

                    foundOutParen = true;

                    operator.addTerm(ParseEquation(input.substring(0,indx)));//left side
                    operator.addTerm(ParseEquation(input.substring(indx + 1)));//right side
                    return operator;
                    //everything is added recursively
                }
            }
            /*
            gone all the way through the first for loop.
            if hasParenthesis is true, then we continue
            if  hasParenthesis is false, and foundOutParen is false,
            then we must return the nominal that is left over
            */
            //for both these if s,  op.equals(^) because it has to be the last iteration of the top for loop
            if(hasParen && !foundOutParen&& op.equals("^")){//loop inside the parenthesis because otherwise it would have returned an operator
                //trim the parenthesis and call it again
                return ParseEquation(input.substring(1,input.length()-1));
                //should return the proper thing.  maybe
            }
            else if(!hasParen && !foundOutParen && op.equals("^")){
                ParseNominal parseNominal = new ParseNominal(input);
                return new Nominal(parseNominal.constantCount,parseNominal.varExponent);//currently only parses numbers
            }

        }
        throw new UnparsedString("Wow.  it didnt work at all");

    }

    public class ParseNominal{
        public ParseNominal(String input){
            this.input=input;
            parseInput();
        }
        public double constantCount = 0;
        public double varExponent = 0;
        private String input;

        private void parseInput()
        {
            if(input.contains("x"))//if there is a x in it
            {
                varExponent=1;
            }

            if(input.equals("x")||input.equals("-x"))// if it just a "x"
                input=input.replace("x","1x");
            if(input.equals("-"))//another special case where it sends just a negative
                input=input.replace("-","-1");
            input=input.replace("x","");
            constantCount=Double.parseDouble(input);

        }
    }

    public int skipParen(String input,int indx){
        int openCount = 0;
        int closedCount = 1;
        while(indx>0){
            indx--;
            if((input.charAt(indx)+"").equals(")")){
                closedCount++;//increment closed count
            }
            if((input.charAt(indx)+"").equals("(")){
                openCount++;//increment open count
            }

            if(openCount == closedCount){
                return indx;
            }
        }

        //should throw error that parenthesis are not found
        return 0;
    }





   /* public static ArrayList<EquationNode> parseEquation(String input)
    {
        ArrayList<Equation> parsedEquation = new ArrayList<EquationNode>();

        ArrayList<String> useWithIterator = new ArrayList<String>();
        for(int indx=0; indx<input.length(); indx++)
        {
            useWithIterator.add(input.charAt(indx)+"");
        }
        ListIterator<String> charByChar= useWithIterator.listIterator();
        //Scanner charByChar = new Scanner(input);
        //charByChar.useDelimiter("");
        String temp="";
        String stringToChange="";
        while(charByChar.hasNext())
        {
            temp=charByChar.next();
            if(isOperator(temp))//if it is one of the operators
            {
                if(!stringToChange.equals(""))//if the stringToChange string is not empty
                {
                    parsedEquation.add(new Nomial_new(stringToChange));//add nomial
                    stringToChange="";//set it back to nothing
                }

                parsedEquation.add(new Operator(temp));//add the operator
                continue;
            }
            else if(temp.equals("("))//if it is the start of a parenthesis
            {
                String oneParenthesis="(";
                int otherOpen=0;
                int otherClosed=0;
                    String parenTemp="";
                    while(charByChar.hasNext())
                    {
                    parenTemp= charByChar.next();
                    if(parenTemp.equals("("))
                        otherOpen++;
                    else if(parenTemp.equals(")"))
                        otherClosed++;
                    oneParenthesis+=parenTemp;
                    if(otherClosed==otherOpen+1)//this means that they are equal number
                    {
                        if(charByChar.hasNext())
                        {
                            if(charByChar.next().equals("^"))
                            {
                                if(charByChar.next().equals("-"))
                                {
                                    parsedEquation.add(new Parenthesis_new(oneParenthesis,-1*Double.parseDouble(charByChar.next())));
                                    break;
                                }
                                else
                                {
                                    parsedEquation.add(new Parenthesis_new(oneParenthesis,Double.parseDouble(charByChar.previous())));
                                    break;
                                }
                            }
                            else
                                charByChar.previous();
                        }
                        parsedEquation.add(new Parenthesis_new(oneParenthesis));
                        break;
                    }
                }
            }
            else
                stringToChange+=temp;
        }
        if(!stringToChange.equals(""))//if on the last loop it does not add the last object
        {
            parsedEquation.add(new Nomial_new(stringToChange));
            stringToChange="";
        }
        return parsedEquation;
    }
    public static boolean isOperator(String temp)
    {
        if(temp.equals("+")||temp.equals("/")||temp.equals("*"))//||temp.equals("(")||temp.equals(")")
            return true;
        else
            return false;
    }*/
}
