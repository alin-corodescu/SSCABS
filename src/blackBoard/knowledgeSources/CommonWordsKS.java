// ----------------------------------------------------------------------------------------
//  CommonWordsKS.java 
//     Knowledge source, uses the word server, does the bulk of the deciphering. 
//     If the current word has more than 3 characters in length this ks can contribute 
//     The ks process then calculates the word code, sends it to the word server retrieves 
//     the list of dictionary words then adds the letters to a cipher map to be later
//     added to the blackboard. 
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.WordServerInterface;

public class CommonWordsKS extends SharedFunctionsKS {
	private WordServerInterface wp;

	public CommonWordsKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		wp = new WordServerInterface(); // TODO: found the place connection happens
	}

	public boolean is_eager_to_contribute() {   // only process words that are greater
		return cipherList.peek().length() >= 4; // than 3 characters in length
	}
	
	public void contribute() {
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping(); // make an empty cipher map
		String word = cipherList.pop();            // get the word
		String pattern = getWordPattern(word);     // calculate the word code
		String [] words = wp.allPatterns(pattern); // get the array of strings from the word server 
		if (words != null) {                       // check that we have data
			for (String candidate : words) {       // add each word to the cipher map
				cipher = cipherLetter.addLettersToMapping(cipher, word, candidate.toLowerCase());
			}
			update(cipher);                        // add the cipher map to the blackboard
		}
	}
}
