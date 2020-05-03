package machine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class read data from file and check whether string accepted or not
 * @author Yasaman Haghbin
 * @since 2020
 * @version 1.0
 */
public class DFA {
    private String[] alphabet;
    private String[] state;
    private String[] finiteState;
    private String startState;
    private ArrayList<StateChanger> stateChangers;
    private int stateCounter;
    public DFA()
    {
        stateChangers = new ArrayList<StateChanger>();
        stateCounter=0;
    }

    /**
     * readData method read data from file and determine start state, final state, alphabet and states;
     * @throws IOException if doesn't find file or can't find it
     */
    public void readData() {
        File file = new File(".\\DFA_Input_1.txt");
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
                StateChanger stCh = new StateChanger(s[0], s[1].charAt(0), s[2]);
                stateChangers.add(stCh);
                stateCounter++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
      /**
       * checkString method check whether myString accepted with dfa or not
        **/
    public Boolean checkString(String myString) {
        boolean check;
        //read all the string
        for (int i = 0; i < myString.length(); i++) {
            //this boolean is for checking whether string can continue or it isn't accepted
             check = false;
             //read all the transform function
            for (int j=0;j<stateCounter;j++){
                //if string can accepted with one of the state,I change the startState
                if ((stateChangers.get(j)).getFirstState().equals(startState)) {
                    if ((stateChangers.get(j)).getAlphabet() == myString.charAt(i)) {
                        startState = stateChangers.get(j).getEndState();
                        check = true;
                        break;
                    }
                }
            }
            if(!check) {
                return false;
            }
        }
        //if machine is on finite state,it accepted, else failed
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

