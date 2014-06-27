package hk.zdl.jddnspod;

import java.util.AbstractList;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONArrayList<E> extends AbstractList<E> {

	private final JSONArray array;

	public JSONArrayList(JSONArray array) {
		super();
		this.array = array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		try {
			return (E) array.get(index);
		} catch (JSONException e) {
			throw new IndexOutOfBoundsException(e.getMessage());
		}
	}

	@Override
	public int size() {
		return array.length();
	}
}
