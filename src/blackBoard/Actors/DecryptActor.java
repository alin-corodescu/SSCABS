package blackBoard.Actors;

import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;

import java.util.List;
import java.util.Map;

/**
 * Created by alin on 4/5/17.
 */
public class DecryptActor extends UntypedActor {
    Map<Character, List<Character>> cipher;

    /**
     * constructor which passes a cipher to be used for decryption
     * @param cipher cipher to be used for decription
     */
    public DecryptActor(Map<Character, List<Character>> cipher) {
        this.cipher = cipher;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            String decryption = decryptMessage((TextObject) message);
            TextObject wrapped = new TextObject();
            wrapped.add(decryption);
            getSender().tell(wrapped,getSelf());
        }
        else {
            unhandled(message);
        }
    }

    private String decryptMessage(TextObject textObject) {
        String message = textObject.toString();
        String plainText = decryptWithCipherletterMapping(message, cipher);
        return plainText;
    }

    /**
     * translates a message using the key
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
            }
            else {
                translated += symbol;
            }
        }
        return translated;
    }

    /**
     *
     * @param ciphertext text to decipher
     * @param letterMapping one to one letter mapping
     * @return
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
                int keyIndex = TextUtils.alphabet.indexOf(letterMapping.get(cipherletter).get(0));
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
