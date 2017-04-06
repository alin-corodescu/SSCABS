package blackBoard.Actors;

/**
 * Created by alin on 4/6/17.
 * Class used to represent ControlMessages between actors
 */
public class ControlMessage {
    public enum Types {START, FINISHED};

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
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
