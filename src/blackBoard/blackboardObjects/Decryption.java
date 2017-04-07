package blackBoard.blackboardObjects;

import blackBoard.Actors.DecryptActor;

import javax.xml.soap.Text;

/**
 * Created by alin on 4/5/17.
 * Wrapper class for a Decryption, holds both the encrypted text and the decrypted one
 * used to be passed around between actors during deciphering
 */
public class Decryption {
    public TextObject encrypted, decrypted;
    public Decryption() {
        // those 2 lines might not be necessary
        encrypted = new TextObject();
        decrypted = new TextObject();
    }
    public Decryption(String encrypted, String decrypted) {
        this.encrypted = new TextObject();
        this.decrypted = new TextObject();

        this.encrypted.add(encrypted);
        this.decrypted.add(decrypted);
    }

}
