// ----------------------------------------------------------------------------------------
//  DecryptKS.java 
//     Knowledge source, decrypts the the cipher text using the cipher map on the blackboard. 
//     Can contribute when the cipher list is empty and there is no plain text.  
//     Processes the cipher text using the cipher map to produce plain text that is then 
//     placed on the blackboard 
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.Counter;
import blackBoard.blackboardObjects.TextObject;

public class DecryptKS extends SharedFunctionsKS {
	private TextObject cipherText; 
	private Counter progress; 

	public DecryptKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		cipherText = (TextObject) blackboard.layer("cipherText");
		progress = (Counter) blackboard.layer("progress");
	}

	public String decryptMessage(String key, String message) {
		String translated = "";
		String charsA = key;
		String charsB = alphabet;
		for (int i = 0; i < message.length(); i++) {
			String symbol = message.substring(i,i+1);
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

	public String decryptWithCipherletterMapping(String ciphertext, Map<Character, List<Character>> letterMapping) {
		char [] key = new char [alphabet.length()];
		for (int i = 0; i < alphabet.length(); i++) {
			key[i] = '=';
		}
		ciphertext = ciphertext.toLowerCase();
		for (int i = 0; i < alphabet.length(); i++) {
			Character cipherletter = alphabet.charAt(i);
			if (letterMapping.get(cipherletter).size() == 1) {
				int keyIndex = alphabet.indexOf(letterMapping.get(cipherletter).get(0));
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

	public boolean is_eager_to_contribute() {
		return cipherList.done() && plainText.toString().isEmpty();
	}
	
	public void contribute() {
		String message = cipherText.toString();
		Map<Character, List<Character>> letterMapping = cipherLetter.getData();
		plainText.add(decryptWithCipherletterMapping(message, letterMapping));
		progress.set(75);
	}

}
