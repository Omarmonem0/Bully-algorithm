public class Election extends Message {
    public Election(String payload) {
        super(Constants.election, payload);
    }
}
