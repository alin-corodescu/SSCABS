package blackBoard.Actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;

import java.util.List;

import static blackBoard.Actors.TextUtils.splitIntoWords;

/**
 * Created by alin on 4/6/17.
 */
public class TextSplittingActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            List<String> words = splitIntoWords((TextObject) message);
            for (String word : words) {
                sendWord(word,getSender());
            }
        }
        else {
            unhandled(message);
        }
    }

    /**
     * method used to send words extracted from the text to the listener
     * @param word word to be sent to the listener
     * @param destination ActorRef of the listener
     */
    private void sendWord(String word, ActorRef destination) {
        // do the necessary wrapping here
        destination.tell(word,getSelf());
    }

}
