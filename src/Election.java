public class Election extends Message {
    public Election(String payload) {
        super("ELECTION", payload);
    }
}
