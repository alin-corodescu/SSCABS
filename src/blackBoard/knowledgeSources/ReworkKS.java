// ----------------------------------------------------------------------------------------
//  ReworkKS.java 
//     Knowledge source, used as a second pass tries to resolve any unresolved letters. 
//     this ks can contribute if we have finished the cipher list but have unresolved letters.  
//     The process looks for words containing underscores "_" as these are unresolved letters 
//     it then uses the word server again to find the best matching word and suggests the 
//     plain text letter as a solution. When finished it clears the plain text so decryption
//     can occur again.
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.WordServerInterface;

public class ReworkKS extends SharedFunctionsKS {
	private WordServerInterface wp; // TODO connection initializes here

	public ReworkKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		wp = new WordServerInterface();
	}

	public String bestMatch(String pattern, String [] words) {
		String bestWord = "";
		int count;
		int similarity = 0;
		for (String word : words) {
			word = word.toLowerCase();
			count = 0;
			for (int c = 0; c < pattern.length(); c++) {
				if (word.charAt(c) == pattern.charAt(c)) {
					count++;
				}
				else if (pattern.charAt(c) == '_') {
					continue;
				}
				else {
				    // this here makes no sense
					count = 0;
					break;
				}				
			}
			if (count > similarity) {
				bestWord = word;
				similarity = count;
			}
		}
		return bestWord;
	}
	
	public boolean is_eager_to_contribute() {
		return cipherList.done() && stuffToFix();
	}

	/**
	 * this one uses the plain text, not the encrypted one
	 */
	public void contribute() {
		String plaintext = plainText.toString();
		String nonletters = removeMatches(plaintext, alphabet + "_");
		//get a list of words in the plain text
		nonletters = removeDupes(nonletters);
		plaintext = removeMatches(plaintext, nonletters.replace(" ",""));
		String [] plainList = plaintext.split(" ");
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping();

		// for every word in the word list of the plain text
		for (int index = 0; index < plainList.length; index++){
			String word = plainList[index];
			// if the word length is less than 3? -> server can't help
			if (word.length() <= 3) {
				continue;
			}
			// if the word contains an unidentified letter
			if (word.contains("_")) {
			    // get the cipher word != plain text word
				String cipherword = cipherList.toList().get(index);
				// ask the server about some suggestions
                String pattern = getWordPattern(cipherword);
                // get the response
				String [] words = wp.allPatterns(pattern);
				// find the best match
				String candidate = bestMatch(word, words);
				if (candidate != null && !candidate.isEmpty()) {
					int pos = word.indexOf("_");
					List<Character> temp = new ArrayList<Character>();
					temp.add(candidate.charAt(pos));
					// add the new match to cipher mapping
					// resets the cipher for this letter everytime
					cipher.put(cipherword.charAt(pos), temp);
				}
			}
		}
		// update the cipher
		update(cipher);
        // clears the plain text
		plainText.add("");
	}
}
