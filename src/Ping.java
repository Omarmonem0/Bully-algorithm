public class Ping extends Message {
    public Ping(String payload) {
        super("PING", payload);
    }
}
