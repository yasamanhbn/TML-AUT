import NDFA.NDFA;

public class Main {

    public static void main(String[] args) {
         NDFA ndfa = new NDFA();
         ndfa.readData();
         ndfa.removeLanda();
         if(ndfa.isLanda()){
             ndfa.removeLanda();
         }
         ndfa.findNewDFAState();
         ndfa.NFATransition();
    }
}
