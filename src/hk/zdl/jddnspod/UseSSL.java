
package hk.zdl.jddnspod;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class UseSSL {
    private static class TrustAnyTrustManager implements X509TrustManager {
    
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public static String visit(URL console,String postData) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new HostnameVerifier(){

			@Override
			public boolean verify(String paramString, SSLSession paramSSLSession) {
				return true;
			}});
        
        conn.setDoInput(true);
        conn.setDoOutput(true);
        BufferedOutputStream hurlBufOus=new BufferedOutputStream(conn.getOutputStream());
        hurlBufOus.write(postData.getBytes());//这里面已经将RequestMethod设置为POST.前面设置无效
        hurlBufOus.flush();
        
        conn.connect();
        if(conn.getResponseCode()!=200){
        	throw new IllegalStateException("ResponseCode not 200!");
        }
        InputStream in=conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while(reader.ready()){
			sb.append(reader.readLine());
			sb.append('\n');
		}
		return sb.toString();
    }
}