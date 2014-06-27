package hk.zdl.jddnspod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class Tool {

	public static InetAddress getPublicAddress() throws IOException {
		InputStream in = new URL("http://iframe.ip138.com/ic.asp").openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while (reader.ready()) {
			sb.append(reader.readLine());
		}
		return InetAddress.getByName(sb.substring(sb.indexOf("[") + 1, sb.indexOf("]")));
	}

	/**
	 * 读取本地ip
	 * @param path
	 * @return
	 */
	public static InetAddress getPublicAddress(String path) {
		
		if (null == path) {
			System.err.println("not found local_ip field!");
		} else {
			final File file = new File(path);
			if (null != file && file.exists()) {
				try {
					final BufferedReader reader = new BufferedReader(new InputStreamReader(
							new FileInputStream(file)));
					final String line = reader.readLine();
					if (line != null && !line.trim().equals("")) {
						return InetAddress.getByName(line);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				System.err.println(String.format("not found \"%1$s\" file", path));
			}
		}
		return null;
	}
}
