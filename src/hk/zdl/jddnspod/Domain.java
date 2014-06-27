package hk.zdl.jddnspod;

import java.util.TreeMap;


public class Domain extends TreeMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getName() {
		return get("name");
	}

	public int getId() {
		return Integer.parseInt(get("id"));
	}
	
	public int getRecords() {
		return Integer.parseInt(get("records"));
	}
}
