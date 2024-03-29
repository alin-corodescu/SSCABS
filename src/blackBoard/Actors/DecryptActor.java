package blackBoard.Actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;
import com.typesafe.config.ConfigException;
import scala.Char;

import java.util.List;
import java.util.Map;

import static blackBoard.Actors.ControlMessage.Types.DONE;

/**
 * Created by alin on 4/5/17.
 * Actor used to decrypt a text, given a cipher key
 */
public class DecryptActor extends UntypedActor {
    Map<Character, List<Character>> cipher;

    /**
     * Constructor which passes a cipher to be used for decryption
     * @param cipher cipher to be used for decription
     */
    public DecryptActor(Map<Character, List<Character>> cipher) {
        this.cipher = cipher;
    }

    /**
     * Method used to create Props of this actor
     * @param cipher - cipher to be used when constructing the instance
     * @return Props of DecryptActor
     */
    public static Props props(Map<Character, List<Character>> cipher) {
        return Props.create(DecryptActor.class, cipher);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            String decryption = decryptMessage((TextObject) message);
            ControlMessage doneMessage = new ControlMessage().setType(DONE);
            doneMessage.setData(decryption);
            getSender().tell(doneMessage,getSelf());
        }
        else {
            unhandled(message);
        }
    }

    /**
     * Method called to decrypt text contained in a TextObject
     * @return the plain text (result) of the decryption
     */
    private String decryptMessage(TextObject textObject) {
        String message = textObject.toString();
        String plainText = decryptWithCipherletterMapping(message, cipher);
        return plainText;
    }

    /**
     * Translates a message using the key
     * @param key - permutation of the alphabet
     * @param message - message to decrypt
     * @return decrypted message
     */
    public String decryptMessage(String key, String message) {
        String translated = "";
        // key is how each letter is mapped -> identic key = key[0] =a; etc
        String charsA = key;
        String charsB = TextUtils.alphabet;
        for (int i = 0; i < message.length(); i++) {
            String symbol = message.substring(i,i+1);
            // it should be contained if it is a letter
            if (charsA.contains(symbol)) {
                int symIndex = charsA.indexOf(symbol);
                translated += charsB.charAt(symIndex);
            } else {
                translated += symbol;
            }
        }
        return translated;
    }

    /**
     *
     * @param ciphertext text to decipher
     * @param letterMapping cipher to be used when deciphering
     * @return the "plain text" - result of the decryption
     */
    public String decryptWithCipherletterMapping(String ciphertext, Map<Character, List<Character>> letterMapping) {
        /** create a new alphabet permutation */
        char [] key = new char [TextUtils.alphabet.length()];
        // set it's default to all equals
        for (int i = 0; i < TextUtils.alphabet.length(); i++) {
            key[i] = '=';
        }
        ciphertext = ciphertext.toLowerCase();
        // map each letter of the alphabet with it's mapping
        // needs to be a one to one mapping
        for (int i = 0; i < TextUtils.alphabet.length(); i++) {
            Character cipherletter = TextUtils.alphabet.charAt(i);
            if (letterMapping.get(cipherletter).size() == 1) {
                int keyIndex = -1;
                keyIndex = TextUtils.alphabet.indexOf(letterMapping.get(cipherletter).get(0));
                if (keyIndex > -1) {
                    key[keyIndex] = cipherletter;
                }
            }
            else {
                ciphertext = ciphertext.replace(cipherletter, '_');
            }
        }
        CharSequence seq = new String(key);
        String keyString = String.join("", seq);
        return decryptMessage(keyString, ciphertext);
    }
}
