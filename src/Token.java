import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class Token {
    private String name;
    private String datatype;

    public Token(String name, String datatype) {
        this.name = name;
        this.datatype = datatype;
    }

    public String getName() {
        return name;
    }

    public String getDatatype() {
        return datatype;
    }
}
