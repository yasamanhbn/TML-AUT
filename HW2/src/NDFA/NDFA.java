package NDFA;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * this class first convert λ-NFA to NFA and after that convert it to DFA
 *  * @author Yasaman Haghbin
 *  * @since 2020
 *  * @version 1.0
 */

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
                //check whether we have λ in transitions or not
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

    /**
     * This method find closure of input state
     * @param q is state which we want to find is closure
     * @return array contains closure of q
     */
    private ArrayList closure(String q) {
        ArrayList<String> closure = new ArrayList<String>();
        //each state's closure contains itSelf
        closure.add(q);
        //iterate through lanaTransitions to find states which we reach with λ
        for (int j = 0; j < stateCounter; j++) {
            for (Transition t : lanaTransitions) {
                if (t.getFirstState().equals(q)) {
                    if (!closure.contains(t.getEndState())) {
                        closure.add(t.getEndState());
                        //q becomes new state with reading λ
                        q = t.getEndState();
                    }
                }
            }
        }
        return closure;
    }

    /**
     * This method convert λ-NFA to NFA and set new transition and finalState
     */
    public void removeLanda() {
        ArrayList<Transition> newTransition = new ArrayList<Transition>();
        ArrayList<String> finiteStateTemp = new ArrayList<String>();
        //for each state first find it's closure. after that find all transition with specific alphabet
        //after find all endState, we find closure of this endState
        for (int i = 0; i < state.size(); i++) {
            ArrayList<String> temp = this.closure(state.get(i));
            //find finiteState for these transitions
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
                        //check if transition are duplicate, don't add it again to transitions arrayList
                        Transition transition = new Transition(state.get(i), alphabet[j].charAt(0), ss);
                        if (!isDuplicated(transition, newTransition))
                            newTransition.add(transition);
                    }
                }
            }
        }
        transitions = newTransition;
        finiteState.addAll(finiteStateTemp);
    }

    /**
     * This method check duplicate transition
     * @param transition is transition which we want to know is duplicate or not
     * @param newTransition is transition's arrayList
     * @return a boolean which determined transition is duplicate or not
     */
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

    /**
     * NFATransition method convert NFA to DFA
     */
    public void NFATransition(){
        String[] temp;
        ArrayList<Transition> tempTransitions = new ArrayList<>();
        String endState="";
        //find all transition for newState for each alphabet
        for(String stateName : state){
            temp = stateName.split("-");
            for(int i =0 ;i<alphabet.length;i++){
            for(String st : temp){
                if(finiteState.contains(st) && !finiteState.contains(stateName.replace("-","")))
                    finiteState.add(stateName.replace("-",""));
                    for(Transition transition : transitions){
                        if(transition.getFirstState().equals(st) && transition.getAlphabet()==alphabet[i].charAt(0) && !endState.contains(transition.getEndState())){
                            endState += (transition.getEndState() + "-");
                        }
                    }
                }
            //add new Transition to our array
            if(!endState.equals("")) {
                Transition t = new Transition(stateName, alphabet[i].charAt(0), endState.replace("-",""));
                tempTransitions.add(t);
            }
                endState="";
            }
        }
        transitions = tempTransitions;
    }

    /**
     * showTransition method write result in output file
     */
    public void showTransition(){
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(".\\DFA_Output _2.txt"));
            for(int i=0;i<alphabet.length;i++)
                writer.print(alphabet[i]+" ");
            writer.println();
            writer.flush();
            for(String st : state)
                writer.print(st.replace("-","")+" ");
            writer.println();
            writer.flush();
            writer.println(startState);
            for(String s : finiteState)
                writer.print(s+" ");
            writer.println();
            writer.flush();
            for(Transition t:transitions){
                writer.println(t.getFirstState().replace("-","")+ " "+t.getAlphabet()+" "+t.getEndState());
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
    public boolean isLanda() {
        return landa;
    }
}
