public class Main {
    public static void main(String[] args) {
        Scanner scann = new Scanner();
        scann.scanner_Implement();

        Parser parser = new Parser(scann.tokens);
        parser.parse();
    }
}