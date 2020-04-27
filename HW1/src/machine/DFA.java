package machine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DFA {
    private String[] alphabet;
    private String[] state;
    private String[] finiteState;
    private String startState;
    private StateChanger[] stateChangers;
    private int stateCounter;
    public DFA() {
        stateChangers = new StateChanger[1000];
    }

    public void readData() {
        File file = new File("E:\\university\\theory of machines and languages\\HW1\\dfa.txt");
        int counter = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            for (int i = 0; i < 4; i++) {
                st = br.readLine();
                if (i == 0)
                    alphabet = st.split(" ");
                else if (i == 1)
                    state = st.split(" ");
                else if (i == 2)
                    startState = st;
                else
                    finiteState = st.split(" ");
            }
            while ((st = br.readLine()) != null) {
                String[] s = st.split(" ");
                StateChanger stCh = new StateChanger(s[0], s[1].charAt(0), s[2]);
                stateChangers[counter] = stCh;
                counter++;
            }
            stateCounter = counter-1;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showData() {
        for (String i : alphabet)
            System.out.println("alphabet  " + i);
        for (String i : state)
            System.out.println("state  " + i);
        System.out.println("startState  " + startState);
        for (String i : finiteState)
            System.out.println("finiteState  " + i);
    }

    public Boolean checkString(String myString) {
        boolean check;
        for (int i = 0; i < myString.length(); i++) {
             check = false;
            for (int j=0;j<=stateCounter;j++){
                if (stateChangers[j].getFirstState().equals(startState)) {
                    if (stateChangers[j].getAlphabet() == myString.charAt(i)) {
                        startState = stateChangers[j].getEndState();
                        check = true;
                        break;
                    }
                }
            }
            if(!check) {
                return false;
            }
        }
        boolean checkResult= false;
        for (String s : finiteState) {
            if (startState.equals(s)) {
                checkResult = true;
                break;
            }
        }
        return checkResult;
    }
}

