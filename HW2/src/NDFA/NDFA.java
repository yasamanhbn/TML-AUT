package NDFA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NDFA {
    private String[] alphabet;
    private String[] state;
    private String[] finiteState;
    private String startState;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> lanaTransitions;
    private int stateCounter;
    private boolean landa;
    public NDFA(){
        landa = false;
        stateCounter = 0;
        transitions = new ArrayList<Transition>();
        lanaTransitions = new ArrayList<Transition>();
    }
    /**
     * readData method read data from file and determine start state, final state, alphabet and states;
     * @throws IOException if doesn't find file or can't find it
     */
    public void readData() {
        File file = new File(".\\NFA_Input_2.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            //read four first line for getting alphabet and states;
            for (int i = 0; i < 4; i++) {
                st = br.readLine();
                if (i == 0)
                    //split alphabet and push them in array
                    alphabet = st.split(" ");
                else if (i == 1)
                    //split state and put them in array
                    state = st.split(" ");
                else if (i == 2)
                    startState = st;
                else
                    finiteState = st.split(" ");
            }
            /*read remaining data and transform functions and create a stateChanger object
            for each function and store it in stateChangers array*/
            while ((st = br.readLine()) != null) {
                String[] s = st.split(" ");
                Transition stCh = new Transition(s[0], s[1].charAt(0), s[2]);
                if(s[1].charAt(0)=='λ') {
                    landa = true;
                    lanaTransitions.add(stCh);
                }
                transitions.add(stCh);
                stateCounter++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        ArrayList<String> temp = closure("q0");
        for(String s : temp){
            System.out.println(s);
        }
    }
    public ArrayList closure(String q){
        ArrayList<String> closure = new ArrayList<String>();
        closure.add(q);
        for(int j=0;j<stateCounter;j++) {
            for (Transition t : lanaTransitions) {
                if (t.getFirstState().equals(q)) {
                    if(!closure.contains(t.getEndState())) {
                        closure.add(t.getEndState());
                        q = t.getEndState();
                    }
                }
            }
        }
        return closure;
    }
}
