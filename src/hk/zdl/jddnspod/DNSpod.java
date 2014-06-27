package hk.zdl.jddnspod;

import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class DNSpod {

	public static JSONObject getInfo(String url,Option option) throws Exception{
		return new JSONObject(UseSSL.visit(new URL(url),option.toString()));
	}
	
	public static List<Domain> listDomains(Option option) throws Exception{
		System.out.println("data: " + option.toString());
		String str = UseSSL.visit(new URL("https://dnsapi.cn/Domain.List"), option.toString());
		System.out.println(str);
		JSONObject obj = new JSONObject(str);
		JSONArray array = obj.getJSONArray("domains");
		JSONArrayList<JSONObject> list = new JSONArrayList<JSONObject>(array);
		List<Domain> l = new LinkedList<Domain>();
		for(JSONObject obj1:list){
			Domain domain = new Domain();
			for(String key:obj1){
				domain.put(key, obj1.get(key).toString());
			}
			l.add(domain);
		}
		return l;
	}
	public static List<Record> listRecords(Option option,Domain domain) throws Exception{
		Option o = new Option(option.get("login_email"),option.get("login_password"));
		o.put("domain", domain.getName());
		String str = UseSSL.visit(new URL("https://dnsapi.cn/Record.List"), o.toString());
		JSONObject obj = new JSONObject(str);
		JSONArray array = obj.getJSONArray("records");
		JSONArrayList<JSONObject> list = new JSONArrayList<JSONObject>(array);
		List<Record> l = new LinkedList<Record>();
		for(JSONObject obj1:list){
			Record record = new Record();
			for(String key:obj1){
				record.put(key, obj1.get(key).toString());
			}
			l.add(record);
		}
		return l;
	}
	public static JSONObject recordModify(Option option,Domain domain,Record record) throws Exception{
		String str = UseSSL.visit(new URL("https://dnsapi.cn/Record.Modify"), option.toString());
		JSONObject obj = new JSONObject(str);
		return obj;
	}
	public static JSONObject updateARecord(Option option,Domain domain,String sub_domain,InetAddress addr,int ttl) throws Exception{
		List<Record> records = listRecords(option, domain);
		Record record = new Record();
		for(Record r:records){
			if(r.get("name").equals(sub_domain)){
				record = r;
				break;
			}
		}
		Option o = new Option(option.get("login_email"),option.get("login_password"));
		o.put("domain_id", domain.get("id"));
		o.put("record_id", record.get("id"));
		o.put("sub_domain",sub_domain);
		o.put("record_type", "A");
		o.put("record_line", record.get("line"));
		o.put("value", addr.getHostAddress());
		o.put("ttl", String.valueOf(ttl));
		JSONObject obj =  recordModify(o, domain, record);
		return obj.getJSONObject("record");
	}
}
