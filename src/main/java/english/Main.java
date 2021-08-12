package english;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        System.out.println("---- Main Class1 -----");
        if(args.length > 0)
            Arrays.asList(args).forEach(System.out::println);
        else
            System.out.println("No arguments recieved!");
    }

}
