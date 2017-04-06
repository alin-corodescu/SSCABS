package blackBoard.Actors;

import blackBoard.blackboardObjects.TextObject;

/**
 * Created by alin on 4/5/17.
 */
public interface Contributor {
    /**
     * checks if the Actor can decipher the TextObject
     * @param text TextObject to be checked
     * @return true / false
     */
    boolean canDecipher(TextObject text);
}
