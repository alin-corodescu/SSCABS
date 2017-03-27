// ----------------------------------------------------------------------------------------
//  Counter.java 
//     Allows an integer to be stored in the blackboard. It is usually used to count things
// ----------------------------------------------------------------------------------------
package blackBoard.blackboardObjects;

/**
 * Wrapper class for a counter (int)
 */
public class Counter extends BlackboardObject {
	private int count = 0;

	public int toInt() {
		return count;
	}
	
	public void set(int val) {
		count = val;
	}
}
