import java.util.List;
import java.util.Scanner;

public class Parser {

    private List<Token> tokens;
    private int currentTokenIndex;
    private int count;
    Parser parse = new Parser();

    public Parser()
    {

    }


    public Parser(List<Token> tokens,count) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.count=count;
    }

    public void parse() {
        while (currentTokenIndex < tokens.size()) {
            parseStatement();
        }
    }
    public void parseStatement()
    {
        // i=a+b;

        parseAssignStmt();

        if(match("if"))
        {

            parseIfStmt();

        }

        else if(match("for"))
        {
            parseForStmt();
        }

        else if (match("while"))
        {
            parseWhileLoop();
        }


    }
//i=a+b;
    private boolean match(String expected) {
        if (check(expected)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(String expected) {
       // System.out.println("token>>  "+peek().getName()+" expected>>  "+expected);
        // end of token
        if (isAtEnd()) {
            return false;
        }
        // a => identifier , if => keyword
        if(!peek().getName().equals(expected))
        {
            if(peek().getDatatype().equals(expected)){
                return true;
            }
            else
                return false;
        }
        else if(peek().getName().equals(expected)) {;
            return true;
        }
        else
            return false;
    }

    private boolean isAtEnd() {
        return currentTokenIndex == tokens.size();
    }

    private Token peek() {
        return tokens.get(currentTokenIndex);
    }

    private Token advance() {
        if (!isAtEnd()) {
            currentTokenIndex++; //currentTokenIndex=0  1
        }
        return tokens.get(currentTokenIndex -1);
    }

    private void checkBraces(String expected) {
        if (check(expected)) {
            advance();
        }

        else {
            // Handle parsing error
            System.err.println("Error: Expected '" + expected + "in line : ");
            System.exit(1);
        }
    }



    public void condition()
    {
        parseExpression();
        if(match("==")||match("!=") || match("<=")||match(">=")||match(">")||match("<")||match("&&")||match("||"))
        {
            parseExpression();
        }
        else
        {
            System.err.println("Error: Expected logical operator");
            System.exit(1);
        }
    }


    //i=(a*3)+4
    public void parseExpression()
    {
        parseTerm();

        while(match("+") || match("-") )
        {
            parseTerm();
        }

    }

    public void parseTerm()
    {
        // (a*2)*4
        parseFactor();


        while(match("*") || match("/") || match("%"))
        {

            parseFactor();
        }

    }

    public void parseFactor()
    {
        // (a*b)
        if (match("("))
        {
            parseExpression();
            if(!match(")")){
                System.err.println("Error: Expected ')'");
                System.exit(1);
            }
        }
        //i= +b
        else if(match("Identifier") || match("Numeric Constant"))
        {
            return;
        }

        else
        {
            System.err.println("Error: Expected factor");
            System.exit(1);
        }
    }

    public void parseBlock()
    {
        int maxIterations = 1000; // Set a reasonable maximum number of iterations
        // more than one statement
        try{
            int iterationCount = 0;

            while (!check("}")) {
                if(isAtEnd()) break;
                parseStatement();
                checkSemicolon();
                iterationCount++;

                if (iterationCount > maxIterations) {
                    throw new InfiniteLoopException("ERROR!");
                }
            }}
        catch (InfiniteLoopException e)
        {
            System.err.println(e.getMessage());

        }
    }
    // i=a+b;
    public void parseAssignStmt()
    {

        if(match("Identifier"))
        {
            if(match("="))
            {
                parseExpression();

            }
            else
            {
                System.err.println("Error: Expected '='");
                System.exit(1);
            }
        }

    }

    public void parseIfStmt()
    {
        checkBraces("(");
        condition();
        checkBraces(")");

        parseOneOrBlockStmt();

    }

    public void parseOneOrBlockStmt()
    {
        //Block statement
        if(match("{")) {
            parseBlock();
            checkBraces("}");


            if(match("else"))
            {
                parseOneOrBlockStmt();
            }
        }

        //one statement
        else {

            parseStatement();
            checkSemicolon();

            if(match("else"))
            {
                parseOneOrBlockStmt();
            }

            else if(!isAtEnd())
            {
                System.err.println("Syntax Error");
                System.exit(1);
            }

        }

    }

    public void checkSemicolon()
    {

        if(!match(";"))
        {
            System.err.println("Error: Expected ';' in line");

        }

        else return;
    }

    public void parseForStmt()
    {

        checkBraces("(");
        parseAssignStmt();
        checkSemicolon();
        condition();
        checkSemicolon();
        parseAssignStmt();
        checkBraces(")");

        parseOneOrBlockStmt();


    }

    public void parseWhileLoop()
    {
        checkBraces("(");
        condition();
        checkBraces(")");
        parseOneOrBlockStmt();
    }
}
class InfiniteLoopException extends RuntimeException {
    public InfiniteLoopException(String message) {
        super(message);
    }
}
