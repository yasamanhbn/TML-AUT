package NDFA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NDFA {
    private String[] alphabet;
    private ArrayList<String> state;
    private ArrayList<String> finiteState;
    private String startState;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> lanaTransitions;
    private int stateCounter;
    private boolean landa;

    public NDFA() {
        landa = false;
        stateCounter = 0;
        transitions = new ArrayList<Transition>();
        lanaTransitions = new ArrayList<Transition>();
    }

    /**
     * readData method read data from file and determine start state, final state, alphabet and states;
     *
     * @throws IOException if doesn't find file or can't find it
     */
    public void readData() {
        String[] finiteSt = null;
        File file = new File(".\\NFA_Input_2.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] temp;
            String st;
            //read four first line for getting alphabet and states;
            for (int i = 0; i < 4; i++) {
                st = br.readLine();
                if (i == 0)
                    //split alphabet and push them in array
                    alphabet = st.split(" ");
                else if (i == 1) {
                    //split state and put them in array
                    temp = st.split(" ");
                    state = new ArrayList<String>(Arrays.asList(temp));
                } else if (i == 2)
                    startState = st;
                else
                    finiteSt = st.split(" ");
            }
            assert finiteSt != null;
            finiteState = new ArrayList<String>(Arrays.asList(finiteSt));
            /*read remaining data and transform functions and create a stateChanger object
            for each function and store it in stateChangers array*/
            while ((st = br.readLine()) != null) {
                String[] s = st.split(" ");
                Transition stCh = new Transition(s[0], s[1].charAt(0), s[2]);
                if (s[1].charAt(0) == 'λ') {
                    landa = true;
                    lanaTransitions.add(stCh);
                }
                transitions.add(stCh);
                stateCounter++;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private ArrayList closure(String q) {
        ArrayList<String> closure = new ArrayList<String>();
        closure.add(q);
        for (int j = 0; j < stateCounter; j++) {
            for (Transition t : lanaTransitions) {
                if (t.getFirstState().equals(q)) {
                    if (!closure.contains(t.getEndState())) {
                        closure.add(t.getEndState());
                        q = t.getEndState();
                    }
                }
            }
        }
        return closure;
    }

    public void removeLanda() {
        ArrayList<Transition> newTransition = new ArrayList<Transition>();
        ArrayList<String> finiteStateTemp = new ArrayList<String>();
        for (int i = 0; i < state.size(); i++) {
            ArrayList<String> temp = this.closure(state.get(i));
            for (String st : finiteState) {
                if (temp.contains(st)) {
                    if (!finiteState.contains(state.get(i)) && !finiteStateTemp.contains(state.get(i))) {
                        finiteStateTemp.add(state.get(i));
                    }
                }
            }
            for (int j = 0; j < alphabet.length; j++) {
                ArrayList<String> endState = new ArrayList<>();
                for (String s : temp) {
                    for (Transition tt : transitions) {
                        if (tt.getFirstState().equals(s) && tt.getAlphabet() == alphabet[j].charAt(0)) {
                            endState.add(tt.getEndState());
                        }
                    }
                }
                for (String s : endState) {
                    ArrayList<String> endClosure = this.closure(s);
                    for (String ss : endClosure) {
                        Transition transition = new Transition(state.get(i), alphabet[j].charAt(0), ss);
                        if (!isDuplicated(transition, newTransition))
                            newTransition.add(transition);
                    }
                }
            }
        }
        transitions = newTransition;
//        for (Transition t : newTransition) {
//            System.out.println(t.getFirstState() + "  " + t.getAlphabet() + "  " + t.getEndState());
//        }
        finiteState.addAll(finiteStateTemp);
//        for(String s: finiteState){
//            System.out.println(s);
//        }
    }

    private boolean isDuplicated(Transition transition, ArrayList<Transition> newTransition) {
        boolean check = false;
        for (Transition t : newTransition) {
            if (t.getAlphabet() == transition.getAlphabet() &&
                    t.getFirstState().equals(transition.getFirstState()) &&
                    t.getEndState().equals(transition.getEndState())) {
                check = true;
                break;
            }
        }
        return check;
    }

    /**
     * this method find all subSet of our states for convert NFA to DFA
     */
    public void findNewDFAState() {
        ArrayList<String> tempState = new ArrayList<>();
        int n = state.size();
        for (int i = 0; i < (1 << n); i++) {
            String newState = "";
            for (int j = 0; j < n; j++) {
                /* (1<<j) is a number with jth bit 1
                 so when we 'and' them with the
                 subset number we get which numbers
                 are present in the subset and which
                 are not*/
                if ((i & (1 << j)) > 0)
                    newState += (state.get(j) + "-");
                if(!tempState.contains(newState) && !newState.equals(""))
                    tempState.add(newState);
            }
        }
        state = tempState;
    }

    public boolean isLanda() {
        return landa;
    }

    public void NFATransition(){
        String[] temp;
        ArrayList<Transition> tempTransitions = new ArrayList<>();
        String endState="";
        for(String stateName : state){
            temp = stateName.split("-");
            for(int i =0 ;i<alphabet.length;i++){
            for(String st : temp){
                if(finiteState.contains(st) && !finiteState.contains(stateName))
                    finiteState.add(stateName);
                    for(Transition transition : transitions){
                        if(transition.getFirstState().equals(st) && transition.getAlphabet()==alphabet[i].charAt(0)){
                            endState += (transition.getEndState() + "-");
                        }
                    }
                }
            if(!endState.equals("")) {
                Transition t = new Transition(stateName, alphabet[i].charAt(0), endState);
                tempTransitions.add(t);
            }
//                System.out.println("fist: "+stateName+" alpha:"+alphabet[i]+" end:"+endState);
                endState="";
            }
        }
        transitions = tempTransitions;
        for(Transition t:transitions)
            showTransition(t);
//        for(String s: finiteState)
//            System.out.println("finiteState:" + s);
    }
    public void showTransition(Transition t){
        System.out.println("firstState:"+t.getFirstState().replace("-","") + " alphabet:" + t.getAlphabet()
                +"  endState:" + t.getEndState().replace("-",""));
    }
}
