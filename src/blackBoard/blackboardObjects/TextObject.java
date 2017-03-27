// ----------------------------------------------------------------------------------------
//  TextObject.java 
//     Allows a string to be stored in the blackboard.
// ----------------------------------------------------------------------------------------
package blackBoard.blackboardObjects;

/**
 * Wrapper class for a string
 */
public class TextObject extends BlackboardObject {
	private String data = "";
	
	public void add(String data) {
		this.data = data;
	}

	public String toString() {
		return data;
	}

	public int length() {
		return data.length();
	}

	/**
	 * Counts the number of apparitions of the character c in the TextObject string
	 * @param c the character of which to count apparitions
	 * @return the number of apparitions found in the string
	 */
	public int count(char c) {
		int num = 0;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == c) {
				num++;
			}
		}
		return num;
	}
}
