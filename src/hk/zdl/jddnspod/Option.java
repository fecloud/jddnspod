package hk.zdl.jddnspod;

import java.util.HashMap;
import java.util.Map;

public class Option extends HashMap<String,String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Option(String login_email, String login_password) {
		put("login_email", login_email);
		put("login_password", login_password);
		put("format", "json");
	}
	public Option(String login_email, String login_password, String format, String lang, String error_on_empty, String user_id) {
		this(login_email,login_password);
		put("format", format);
		put("lang", lang);
		put("error_on_empty", error_on_empty);
		put("user_id", user_id);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String,String> entry:entrySet()){
			if(entry.getValue()==null){
				continue;
			}
			builder.append(entry.getKey());
			builder.append('=');
			builder.append(entry.getValue());
			builder.append('&');
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}
