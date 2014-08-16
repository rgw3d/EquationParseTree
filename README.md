EquationParseTree
=================

This time I can actually parse equations using compiler logic.  yay

This Program is designed to Parse Equations and solve them from a user input string.  This program uses the logic of a
parse tree in order to correctly store and manipulate equations.

Input:
    Currently, the input is designed to handle Equations, not expressions.  In other words, the input must have an
    equals sign.  Future versions will simplify an expression as well, but for now it requires a Parenthesis.
    Sample Input are as follows:  1+2+3=x   2*3x*54=64x*2x  34/34+34-34+x=1 x^2+3^3=10  2*x*(34+(34+34))=0

Output:
    Current output does not actually solve the equation.  It will soon, but for now it simplifies each side and that
    is a step in the right direction.  The output just displays the count and the variable exponent
