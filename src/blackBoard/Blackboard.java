// ----------------------------------------------------------------------------------------
//  Blackboard.java 
//     Maintains a hash table of the blackboard objects i.e. data that is shared
//     between the different knowledge sources.
// ----------------------------------------------------------------------------------------
package blackBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blackBoard.blackboardObjects.BlackboardObject;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.CipherList;
import blackBoard.blackboardObjects.Counter;
import blackBoard.blackboardObjects.PlainList;
import blackBoard.blackboardObjects.TextObject;
import blackBoard.knowledgeSources.KnowledgeSource;

public class Blackboard {
	private Map<String, BlackboardObject> layers = new HashMap<String, BlackboardObject>();
	private List<KnowledgeSource> experts = new ArrayList<KnowledgeSource>();
	
	public Blackboard() {
		layers.put("cipherText", new TextObject());
		layers.put("cipherList", new CipherList());
		layers.put("plainList", new PlainList());
		layers.put("cipherLetter", new CipherLetter());
		layers.put("plainText", new TextObject());
		layers.put("unresolved", new Counter());
		layers.put("progress", new Counter());
	}
	
	public void add_expert(KnowledgeSource ks){
		experts.add(ks);		
	}

	public List<KnowledgeSource> experts() {
		return experts;		
	}
	
	public BlackboardObject layer(String name){
		return layers.getOrDefault(name, null);
	}

}
