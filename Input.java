import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {

    public static void main(String[] args) throws UnparsedString, CanNotEval {

        while(true){
            System.out.println("Enter an expression to see it simplified");

            String input = new Scanner(System.in).nextLine();
            Date start = new Date();
            if (!isEquation(input)) {
                System.out.println("\nBad Expression. Please Revise\n");
                continue;
            }

            input = handSanitizer(input);

            EqualsOperator equalsOperator = new EqualsOperator();
            Parser parser = new Parser();
            equalsOperator.addTerm(parser.ParseEquation(input));//parse the expression
            //now just dealing with expression, so we no longer need the equals operators
            //equalsOperator.addTerm(parser.ParseEquation(input.substring(0, input.indexOf("="))));//parse left side of the equation into the Parser
            //equalsOperator.addTerm(parser.ParseEquation(input.substring(input.indexOf("=") + 1)));//parse right side of the equation into the Parser

            printSimplifiedResult(equalsOperator.getList());//get the result here
            System.out.println("It took " + (new Date().getTime() - start.getTime()) + " milliseconds");//print time
            System.out.println("");
        }
    }

    /**
     * This is used to print the output after recieving the list of results.
     * uses addition between every term
     * does not return anything, as it simply prints out something.
     * @param list must send a linkedList<EquationNode>
     */
    private static void printSimplifiedResult(LinkedList<EquationNode> list) {
        String toPrint = "";
        for (EquationNode x : list) {
            toPrint += "+" + x.toString();
        }
        toPrint = toPrint.substring(1);//remove the first + sign
        System.out.println(toPrint);
    }

    /**
     *
     * @param equation the string to be tested
     * @return boolean if the expression qualifies as an acceptable expression
     */
    private static boolean isEquation(String equation) {
        if (!(equation.length() >= 3)) //to short
        {
            System.out.print("Too Short to be considered an expression");
            return false;
        }

        String endOfEq = equation.substring(equation.length() - 1);//ends bad
        if (endOfEq.equals("+") || endOfEq.equals("-") || endOfEq.equals("*") || endOfEq.equals("/")) {
            System.out.print("Ends with a +, -, * or /");
            return false;
        }

        String beginOfEq = "" + equation.charAt(0); //starts bad
        if (beginOfEq.equals("+") || beginOfEq.equals("-") || beginOfEq.equals("*") || beginOfEq.equals("/")) {
            System.out.print("Starts with a +, -, * or /");
            return false;
        }

        Pattern p = Pattern.compile("[^(0-9,*,/,\\-,\\.,+,x,X,\\^,\\s)]");
        Matcher m = p.matcher(equation);
        if (m.find()) {//detects illegal character
            System.out.println("Illegal Character(s): " + m.group());
            return false;
        }


        Pattern n = Pattern.compile("[\\+,/,\\^,\\*]{2,}");
        Matcher o = n.matcher(equation);
        if (o.find()) {
            System.out.print("Two or more of a kind");
            return false;

        } else {//good syntax

            System.out.print("Syntax Passed");
            return true;
        }
        /*if(!(equation.contains("=")))//Not enough = signs!
        {
            System.out.print("No Equals Sign");
            return false;
        }


        if(equation.indexOf('=')!= equation.lastIndexOf('='))//Too many = signs!
        {
            System.out.print("Too many Equals Signs");
            return false;
        }
        */
    }

    /**
     *
     * @param fix the string to reformat so that the parser can parse it
     * @return returns the "fixed"  string
     */
    private static String handSanitizer(String fix) {
        System.out.print("\n\tInput Equation: " + fix);
        fix = fix.replace(" ", "");//Geting rid of spaces

        fix = fix.replace("--", "+"); //minus a minus is addition.  make it simple

        fix = fix.replace("-", "+-");  //replace a negative with just plus a minus.

        fix = fix.replace("X", "x");//just cuz

        fix = fix.replace("^+-", "^-"); //common error that happens after one of the above methods run. negative exponents

        fix = fix.replace("*+-", "*-"); //common error that happens if multiplying by a negative

        fix = fix.replace("(+-", "(-"); //common error that happens if multiplying by a negative

        fix = fix.replace(")(",")*(");//multiply by parenthesis

        //this will be updated later as I fix all the syntax errors that come with exponents and parentheses
        System.out.println("\tReformatted Equation: " + fix);
        return fix;
    }


}
