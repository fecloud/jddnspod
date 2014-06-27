package hk.zdl.jddnspod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.JSONObject;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getAnonymousLogger();
		log.info("JDDNSpod by Super-User<zdl@zdl.hk>");
		log.info("2012/4/19");
		Properties pros = new Properties();
		if (args != null && args.length > 2) {
			if (args[0].equals("-c")) {
				pros.load(new InputStreamReader(new FileInputStream(args[1])) );
			}
		}else {
			pros.load(new InputStreamReader(new FileInputStream("conf.txt")));
		}

		pros.list(System.out);
		Option o = new Option(pros.getProperty("login_email"), pros.getProperty("login_password"));
		log.info("login_email:" + pros.getProperty("login_email"));
		log.info("login_password:" + pros.getProperty("login_password"));
		String domain_name = pros.getProperty("domain");
		log.info("domain:" + domain_name);
		String sub_domain = pros.getProperty("sub_domain", "@");
		log.info("sub_domain:" + sub_domain);
		int ttl = Integer.parseInt(pros.getProperty("ttl", "600"));
		log.info("time to live:" + ttl);
		long sleep = Long.parseLong(pros.getProperty("sleep", "120"));
		log.info("sleep between checks:" + sleep + " seconds");

		Domain domain = null;
		for (Domain d : DNSpod.listDomains(o)) {
			if (d.getName().equals(domain_name)) {
				domain = d;
				log.info("domain " + domain_name + " found, id:" + domain.getId());
			}
		}
		if (domain == null) {
			log.severe("Domain not found! Will now exit...");
			System.exit(1);
		}

		Record record = null;
		for (Record r : DNSpod.listRecords(o, domain)) {
			if (r.get("type").equals("A") && r.get("name").equals(sub_domain)) {
				record = r;
				log.info("record found for " + sub_domain + " , id: " + r.get("id"));
			}
		}

		if (record == null) {
			log.severe("Record not found! Will now exit...");
			System.exit(1);
		}

		InetAddress last_addr = InetAddress.getByName(record.get("value"));
		while (true) {
			InetAddress addr = null;
			try {
				log.info("getPublicAddress");
				if (pros.getProperty("get_ip_sw", "0").equals("1")) {
					addr = Tool.getPublicAddress(pros.getProperty("local_ip", "ip.txt"));
				} else {
					addr = Tool.getPublicAddress();
				}
				if (null == addr)
					continue;
			} catch (Exception x) {
				log.severe("failed to obtain your IP address, your server maybe offline!");
				log.throwing("", "", x);
			}
			log.info("address:" + addr.getHostAddress());
			if (!addr.equals(last_addr)) {
				log.info("address changed to:" + addr.getHostAddress());
				log.info("trying to update A record for:"
						+ (sub_domain.equals("@") ? "" : sub_domain + ".") + domain_name);
				try {
					JSONObject obj = DNSpod.updateARecord(o, domain, sub_domain, addr, ttl);
					String val = obj.getString("value");
					if (!val.equals(addr.getHostAddress())) {
						throw new Exception();
					}
				} catch (Exception x) {
					log.severe("failed to update A record for "
							+ (sub_domain.equals("@") ? "" : sub_domain + ".") + domain_name
							+ " to " + addr.getHostAddress());
					log.throwing("", "", x);
				}
				log.info("update succeed!");
				last_addr = addr;
			}
			Thread.sleep(sleep * 1000);
		}
	}
}
