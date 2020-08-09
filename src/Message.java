import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private String payload;

    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }
}
