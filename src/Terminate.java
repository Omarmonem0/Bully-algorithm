public class Terminate extends Message {
    public Terminate(String payload) {
        super(Constants.terminate, payload);
    }
}
