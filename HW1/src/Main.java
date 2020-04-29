import machine.DFA;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    DFA dfa = new DFA();
    //read data from file
    dfa.readData();
    ///get string
        Scanner sc = new Scanner(System.in);
        String st = sc.next();
        System.out.println(dfa.checkString(st));
    }
}
