import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Scanner {

    static String lexeme = "", op = "", const_char = "";
    static java.util.Scanner inputCode;
    static String filePath = "C:\\Users\\Nourhan\\Desktop\\code.txt";
    static String identifier_pattern = "[a-zA-Z][a-zA-Z0-9]*";
    static String digit_pattern = "[0-9]+";
    static String character_constant_pattern = "'.'";
    static List keywords = new ArrayList();
    static List SpecialCharacter = new ArrayList();
    static List<String> Operator = new ArrayList();
    List<Token> tokens = new ArrayList<>();
    int count=0;
    Parser parse = new Parser();



    public  int  scanner_Implement(){
        keywords.add("if");
        keywords.add("else");
        keywords.add("else if");
        keywords.add("for");
        keywords.add("while");
        keywords.add("do");
        keywords.add("continue");
        keywords.add("break");
        keywords.add("int");
        keywords.add("double");
        keywords.add("float");
        keywords.add("static");
        keywords.add("const");
        keywords.add("char");
        keywords.add("boolean");
        keywords.add("long");
        keywords.add("void");
        keywords.add("auto");
        keywords.add("sizeof");
        keywords.add("main");
        keywords.add("enum");
        keywords.add("struct");
        keywords.add("union");
        keywords.add("scanf");
        keywords.add("printf");
        keywords.add("print");
        keywords.add("return");

        SpecialCharacter.add("!");
        SpecialCharacter.add("$");
        SpecialCharacter.add("#");
        SpecialCharacter.add("@");
        SpecialCharacter.add("?");
        SpecialCharacter.add("~");
        SpecialCharacter.add(";");
        SpecialCharacter.add("(");
        SpecialCharacter.add(")");
        SpecialCharacter.add("{");
        SpecialCharacter.add("}");
        SpecialCharacter.add("[");
        SpecialCharacter.add("]");

        Operator.add("+");
        Operator.add("-");
        Operator.add("*");
        Operator.add("/");
        Operator.add("%");
        Operator.add("=");
        Operator.add("++");
        Operator.add("--");
        Operator.add("||");
        Operator.add("&&");
        Operator.add("==");
        Operator.add("!=");
        Operator.add("<=");
        Operator.add(">=");
        Operator.add(">");
        Operator.add("<");

        try {
            inputCode = new java.util.Scanner(new File(filePath));


            while (inputCode.hasNext()) {
                count++;

                String token = inputCode.nextLine();

                for (int i = 0; i < token.length(); i++) {

                    // comment
                    if (token.charAt(i) == '/') {
                        scanComment(token);
                        break;
                    }


                    if ((Character.isLetter(token.charAt(i)) || Character.isDigit(token.charAt(i)))) {
                        lexeme += token.charAt(i);
                    }

                    else {
                        // i+3 ==> check i before +

                        check(lexeme);
                        lexeme = "";

                        if (!(Character.isLetter(token.charAt(i)) || Character.isDigit(token.charAt(i))) && token.charAt(i) != ' ') {


                            //two operators <=,>=!=,==

                            if (i != (token.length() - 1) && !(Character.isLetter(token.charAt(i + 1)) || Character.isDigit(token.charAt(i + 1))) && token.charAt(i) != ' ') {

                                //<=
                                if ((token.charAt(i + 1) == '=' && (token.charAt(i) == '<' || token.charAt(i) == '>' || token.charAt(i) == '=' || token.charAt(i) == '!')) && token.charAt(i) != ' ') {
                                    op += token.charAt(i);
                                    op += token.charAt(i + 1);
                                    i++;

                                    //&& ||

                                }
                                else if (i != (token.length() - 1) && (token.charAt(i) == token.charAt(i + 1))) {
                                    op += token.charAt(i);
                                    op += token.charAt(i + 1);
                                    i++;
                                }
                                // < with space
                                else if (i != (token.length() - 1) && token.charAt(i + 1) == ' ') {

                                    op = String.valueOf(token.charAt(i));
                                    i++;
                                }
                                // < without space
                                else op = String.valueOf(token.charAt(i));
                            }
                            // 'a'
                            else if (i != (token.length() - 2) && token.charAt(i) == '\'') {
                                const_char += (token.charAt(i));
                                const_char += (token.charAt(i + 1));
                                const_char += (token.charAt(i + 2));
                                i += 2;
                                check(const_char);
                            }
                            else
                                op = String.valueOf(token.charAt(i));
                        }
                    }

                    check(op);

                }



                // if there is no operator after lexeme ==> int main
                check(lexeme);
                lexeme = "";
            }



        }
        catch (FileNotFoundException ex) {
            System.out.println("File code not found !!");
        }
        return 0;
    }



    //check types of lexems
    public  void check(String input) {

        if (keywords.contains(input)) {
            System.out.println("<" + input + "," + "keyword" + ">");
            lexeme = "";
        } else if (Pattern.matches(identifier_pattern, input)) {
            System.out.println("<" + input + "," + "Identifier" + ">");
            lexeme = "";
        } else if (Pattern.matches(digit_pattern, input)) {
            System.out.println("<" + input + "," + "Numeric Constant" + ">");
            lexeme = "";
        } else if (SpecialCharacter.contains(input)) {

            System.out.println("<" + input + "," + "special Character" + ">");
            op = "";
        } else if (Operator.contains(input)) {
            System.out.println("<" + input + "," + "Operator" + ">");
            op = "";
        } else if (Pattern.matches(character_constant_pattern, input)) {
            System.out.println("<" + input + "," + "Character Constant" + ">");
            const_char = "";
        }

        if (!input.isEmpty()) {
            // to store token value , token type
            tokens.add(new Token(input, determineDatatype(input)));

        }

        //System.out.println(count);
    }

    public static void scanComment(String token) {
        System.out.print("<");
        for (int i = 0; i < token.length(); i++) {
            System.out.print(token.charAt(i));
        }
        System.out.println(", Comment>");
    }

    public static String determineDatatype(String input) {
        if (keywords.contains(input)) {
            return "keyword";
        } else if (Pattern.matches(identifier_pattern, input)) {
            return "Identifier";
        } else if (Pattern.matches(digit_pattern, input)) {
            return "Numeric Constant";
        } else if (SpecialCharacter.contains(input)) {
            return "Special Character";
        } else if (Operator.contains(input)) {
            return "Operator";
        } else if (Pattern.matches(character_constant_pattern, input)) {
            return "Character Constant";
        } else {
            return "Unknown";
        }
    }


}