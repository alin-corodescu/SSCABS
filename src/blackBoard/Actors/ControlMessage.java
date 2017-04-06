package blackBoard.Actors;

/**
 * Created by alin on 4/6/17.
 * Class used to represent ControlMessages between actors
 */
public class ControlMessage {
    public enum Types {START, DONE, WAITING};

    public Types getType() {
        return type;
    }

    public ControlMessage setType(Types type) {
        this.type = type;
        return this;
    }

    private Types type;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
