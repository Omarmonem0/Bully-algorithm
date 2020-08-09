public class Ping extends Message {
    public Ping(String payload) {
        super(Constants.ping, payload);
    }
}