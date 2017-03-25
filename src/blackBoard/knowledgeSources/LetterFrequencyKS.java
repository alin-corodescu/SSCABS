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
	private TextObject cipherText;

	public LetterFrequencyKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		cipherText = (TextObject) blackboard.layer("cipherText");
	}
	
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

	public boolean is_eager_to_contribute() {
		return cipherText.length() > 0 && cipherList.length() == 0;
	}
	
	public void contribute() {
		String nonletter;
		String ciphertext = cipherText.toString(); //   .get().toString();
		ciphertext = ciphertext.toLowerCase();
		nonletter = removeMatches(ciphertext, alphabet);
		nonletter = removeDupes(nonletter);
		ciphertext = removeMatches(ciphertext, nonletter.trim());
		
		Map<Character, Float> lf = letterFrequency(ciphertext);
		List<Entry<Character, Float>> lfList = new ArrayList<Entry<Character, Float>>();
		Set<Entry<Character, Float>> lfSet = lf.entrySet();
		lfList.addAll(lfSet);

		lfList.sort(new Comparator<Entry<Character, Float>>() { 
			public int compare(Entry<Character, Float> lhs, Entry<Character, Float> rhs) {
				return lhs.getValue() > rhs.getValue() ? -1 : (lhs.getValue() < rhs.getValue() ? 1 : 0);
			}
		});
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping();
		for (int p = 0; p < 5; p++) {
			if (Math.abs(lfList.get(p).getValue() - lfList.get(p+1).getValue()) < 0.000001)
				break;
			List<Character> arr =  new ArrayList<Character>();
			arr.add(data.charAt(p));
			cipher.put(lfList.get(p).getKey(), arr);
		}
		cipherList.add( Arrays.asList(ciphertext.split(" ")) );
		update(cipher);
	}
}
