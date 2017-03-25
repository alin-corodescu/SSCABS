// ----------------------------------------------------------------------------------------
//  TextObject.java 
//     Allows a string to be stored in the blackboard.
// ----------------------------------------------------------------------------------------
package blackBoard.blackboardObjects;

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
