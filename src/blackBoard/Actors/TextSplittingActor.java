package blackBoard.Actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.Decryption;
import blackBoard.blackboardObjects.TextObject;
import scala.Char;

import java.util.List;

import static blackBoard.Actors.TextUtils.splitIntoWords;

/**
 * Created by alin on 4/6/17.
 */
public class TextSplittingActor extends UntypedActor {


    public static Props props() {
        return Props.create(TextSplittingActor.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            List<String> words = splitIntoWords((TextObject) message);
            for (String word : words) {
                sendWord(word,getSender());
            }
            // Tell the sender the word splitting is done
            getSender().tell(new ControlMessage().setType(ControlMessage.Types.DONE), getSelf());
        }
        else if (message instanceof Decryption) {
            // now we need to split into single word (smaller) Decryption
            // we received just
            generateSingleWordDecryptions((Decryption) message);
            // we are done splitting the decryption
            getSender().tell(new ControlMessage().setType(ControlMessage.Types.DONE), getSelf());
        }
        else unhandled(message);
    }

    private void generateSingleWordDecryptions(Decryption message) {
        String plainText = message.decrypted.toString();
        String cipherText = message.encrypted.toString();

        assert(plainText.length() == cipherText.length());

        String encryptedWord = "", decryptedWord = "";
        for (int i = 0; i < plainText.length(); i++) {
            // new word begins
            // TODO check cause there might be errors with references here
            Character currentChar = cipherText.charAt(i);
            if (currentChar == ' ') {
                if (!encryptedWord.isEmpty())
                {
                    getSender().tell(new Decryption(encryptedWord,decryptedWord), getSelf());
                }
                encryptedWord = "";
                decryptedWord = "";
            }
            // if it's a letter add it to the words
            else if (currentChar >= 'a'
                    && currentChar <= 'z') {
                encryptedWord.concat(Character.toString(currentChar));
                decryptedWord.concat(Character.toString(plainText.charAt(i)));
            }

        }
        if (!encryptedWord.isEmpty())
        {
            getSender().tell(new Decryption(encryptedWord,decryptedWord), getSelf());
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
