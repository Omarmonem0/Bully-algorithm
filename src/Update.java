public class Update extends Message {
    public Update(String payload) {
        super(Constants.update, payload);
    }
}
