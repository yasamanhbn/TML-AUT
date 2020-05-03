package NDFA;

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

    public Transition(String firstState, char alphabet, String endState) {
        this.firstState = firstState;
        this.alphabet = alphabet;
        this.endState = endState;
    }
}
