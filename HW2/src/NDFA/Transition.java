package NDFA;
/**
 * this class contains transition's data
 *  * @author Yasaman Haghbin
 *  * @since 2020
 *  * @version 1.0
 */
public class Transition {

    private String firstState;
    private char alphabet;
    private String endState;

    public String getFirstState() {
        return firstState;
    }

    public char getAlphabet() {
        return alphabet;
    }

    public String getEndState() {
        return endState;
    }

    Transition(String firstState, char alphabet, String endState) {
        this.firstState = firstState;
        this.alphabet = alphabet;
        this.endState = endState;
    }
}
