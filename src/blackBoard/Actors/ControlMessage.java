package blackBoard.Actors;

/**
 * Created by alin on 4/6/17.
 * Class used to represent ControlMessages between actors
 */
public class ControlMessage {
    /**
     * Possible types of the ControlMessages
     */
    public enum Types {START, DONE, WAITING};

    public Types getType() {
        return type;
    }

    public ControlMessage setType(Types type) {
        this.type = type;
        return this;
    }

    /**
     * Type of the control message
     */
    private Types type;
    /**
     * Data to be included in the control message (optional)
     */
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
