package blackBoard.blackboardObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/13/17.
 * Rework algorithm which selects the candidate with most votes for each character that needs replacing
 */
public class MajorityReworkAlgorithm extends ReworkAlgorithm {
    /**
     * Mapping of a character with it's possible candidates, each with it's own number of votes
     */
    private Map<Character, Map<Character, Integer>> votes = new HashMap<>();
    @Override
    public void addNewCipher(Map<Character, List<Character>> cipher) {
        // Updates the map containing the votes
        for (Map.Entry<Character,List<Character>> entry : cipher.entrySet()) {
            Character key = entry.getKey();
            List<Character> candidates = entry.getValue();
            // if this character has been included before
            if (votes.containsKey(key)) {
                for (Character candidate : candidates) {
                    Integer numberOfVotes = 1;
                    if (votes.get(key).containsKey(candidate)) {
                        numberOfVotes = votes.get(key).get(candidate) + 1;
                    }
                    votes.get(key).put(candidate, numberOfVotes);
                }
            }
            else {
                Map<Character, Integer> initialVotes = new HashMap<>();
                for (Character candidate : candidates) {
                    initialVotes.put(candidate,1);
                }
                votes.put(key,initialVotes);
            }
        }
    }

    @Override
    public Map<Character, List<Character>> getCipher() {
        constructedCipher = getBlankMapping();
        for (Character key : constructedCipher.keySet()) {
            if (votes.containsKey(key)) {
                Character bestCandidate = selectCandidate(votes.get(key));
                List<Character> mapping = new ArrayList<>();
                if (bestCandidate != null) {
                    mapping.add(bestCandidate);
                }
                constructedCipher.put(key, mapping);
            }
        }
        return constructedCipher;
    }

    /**
     * Selects the best candidate from the mapping of "votes"
     */
    private Character selectCandidate(Map<Character, Integer> candidates) {
        int maximum = 0;
        Character bestCandidate = null;
        for (Map.Entry<Character,Integer> entry : candidates.entrySet()) {
            if (entry.getValue() > maximum){
                maximum = entry.getValue();
                bestCandidate = entry.getKey();
            }
        }

        return bestCandidate;
    }
}
