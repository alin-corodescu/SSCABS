// ----------------------------------------------------------------------------------------
//  LetterFrequencyKS.java 
//     Knowledge source, uses the word server, does the bulk of the deciphering. 
//     The first ks used, if we cipher text but no cipher list this ks can contribute 
//     The ks counts the number of each alphabetical character, sorts into frequency order
//     and if there is a clear order the cipher letters are mapped against "etaoinshrdlu"   
//     the frequency order of the most common letters in English text.  The ks then splits
//     the cipher text into the cipher list before adding it to the blackboard. 
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.TextObject;

public class LetterFrequencyKS extends SharedFunctionsKS {
	private String data = "etaoinshrdlu";
	/** text to decipher */
	private TextObject cipherText;

	public LetterFrequencyKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		/**
		 * Get the cipher text from the blackboard object
         * @tag : shared object
		 */
		cipherText = (TextObject) blackboard.layer("cipherText");
	}

    /**
     * counts the letter frequency in the text passed as parameter
     * @param text text for which to count letter frequency;
     * @return a map containing the frequency of every character in the text
     */
	public Map<Character, Float> letterFrequency(String text) {
		Map<Character, Float> lcount = new HashMap<Character, Float>(); 
		text = removeMatches(text, " ");  // remove spaces for the calculation
		float total = text.length();
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			lcount.put(ch, lcount.getOrDefault(ch, (float) 0) + 1);
		}
		for (Character ch : lcount.keySet()) {
			lcount.put(ch, lcount.getOrDefault(ch, (float) 0) / total);
		}
		return lcount;
	}

    /**
     * checks if the length of the text to decipher is > 0 and the list of words
     * to decipher is not empty :-??
     * @return
     */
	public boolean is_eager_to_contribute() {
		return cipherText.length() > 0 && cipherList.length() == 0;
	}
	
	public void contribute() {
		String nonletter;
		// unwraps the text to decipher
		String ciphertext = cipherText.toString(); //   .get().toString();
		ciphertext = ciphertext.toLowerCase();
		//remove the letters from the text
		nonletter = removeMatches(ciphertext, alphabet);
		//clean up
		nonletter = removeDupes(nonletter);
		// let only letters in the ciphertext
		ciphertext = removeMatches(ciphertext, nonletter.trim());

		// compute the letter frequency in the ciphertext
		Map<Character, Float> lf = letterFrequency(ciphertext);
		List<Entry<Character, Float>> lfList = new ArrayList<Entry<Character, Float>>();
		Set<Entry<Character, Float>> lfSet = lf.entrySet();
		// create the entry set of the frequency (as a list)
		lfList.addAll(lfSet);

		// sort them descending - probably - too lazy to think
		lfList.sort(new Comparator<Entry<Character, Float>>() { 
			public int compare(Entry<Character, Float> lhs, Entry<Character, Float> rhs) {
				return lhs.getValue() > rhs.getValue() ? -1 : (lhs.getValue() < rhs.getValue() ? 1 : 0);
			}
		});

		// create the cipher with which to update
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping();
		// first 6 characters?
		for (int p = 0; p < 5; p++) {
			if (Math.abs(lfList.get(p).getValue() - lfList.get(p+1).getValue()) < 0.000001)
				break;
			List<Character> arr =  new ArrayList<Character>();
			// add the p most common letter in english ?
			arr.add(data.charAt(p));
			// put the mapping in the cipher
			cipher.put(lfList.get(p).getKey(), arr);
		}
		// add the words in the ciphertext into the cipher List
		cipherList.add( Arrays.asList(ciphertext.split(" ")) );
		// intersect the present cipher with the newly generated
		update(cipher);
	}
}
