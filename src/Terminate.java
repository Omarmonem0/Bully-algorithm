public class Terminate extends Message {
    public Terminate(String payload) {
        super("EXIT", payload);
    }
}
