package com.cspaying.shanfu.ui.service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;



public class HttpUtil {

	private final static String Tag = "HttpUtil";
	//public final static String baseUrl = "http://test.cspaying.com/aps/cloudplatform/api/trade.html";
	//public final static String baseUrl = "https://mp.cspaying.com/aps/cloudplatform/api/trade.html";
	
	//public final static String baseUrl = "http://yyqg.40064006.com/?/mobile/api2/userlogin2";
	public final static String baseUrl = "https://mch.cspaying.com/cloud/cloudplatform/api/trade.html";
	                                        //https://mch.cspaying.com/cloud/cloudplatform/api/trade.html
	private final static int TIME_OUT = 1000 * 3;
	private final static String STATUSCODE = "returnCode";
	private final static String RESULT_ERROR = "";  
	private final static String NETWORD_ERROR = "";
	private final static String MESSAGE = "content";
	public final static int RESULT_ERROR_MESSAGE = 100;//  
	public final static int RESULT_SERVER_ERROR = -1;// 
	public final static int RESULT_SUCCEED = 0;     // ok
	public final static int RESULT_NETWORD_ERROR = 3;// 
	public final static int RESULT_PARSER_ERROR = 2;//     

	private static HttpUtil instance;


	private ExecutorService executor;

	private HttpUtil(Context context) {
		executor = Executors.newFixedThreadPool(3);
	}

	public static HttpUtil getInstance(Context context) {
		if (instance == null)
			instance = new HttpUtil(context);
		return instance;
	}

	

	/**
	 * 
	 * @param url 请求地址
	 * @param data 请求数据（json字符串）
	 * @param listener 请求回调接口
	 * @return
	 */
	public boolean reques(final String data,final String url,final OnRequestListener listener) {
		Log.e(Tag, "reques");
		/*if (!NetWorkUtil.isNetAvaiable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			Log.e(Tag, "NETWORD_ERROR");
			return false;
		}*/
			

		if (listener == null) {  
			Log.e(Tag, "requestGrape_variety: listener=null");
			return false;
		}
		
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				String[] result = new String[1];
				int resultCode = httpsPost(data, result,url);
				Log.e(Tag, "resultCode: " + resultCode
					+ "---result[0]:" + result[0]);
				if (resultCode == RESULT_SUCCEED) {
					try {
						JSONTokener jsonParser = new JSONTokener(
								result[0].toString());
						JSONObject jb = (JSONObject) jsonParser.nextValue();
						String stateCode = jb.getString(STATUSCODE);
						
						if(stateCode.equals("0")){
							//Log.e("+++++++++++++++++++++++getString", str);
							listener.onResult(RESULT_SUCCEED,result[0].toString());
						}else{
								listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
						}


					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
					}
				}else{
					listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
				}
			}
		};
		executor.execute(ra);
		return true;
	}

	
	private int httpPost(String data, String[] result,String url) {
		Log.e(Tag, "HttpPost url: " + url);
		Log.e(Tag, "HttpPost data: " + data);

		try {
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("", URLEncoder.encode(data,
					HTTP.UTF_8)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpRequest.addHeader("Content-Type", "application/json");
			httpRequest.setEntity(new StringEntity(data, "UTF_8"));
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					TIME_OUT);
			HttpResponse httpResponse = client.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(Tag, "statusCode: " + statusCode);
			String ret = null;
			if (statusCode == HttpStatus.SC_OK) {
				ret = EntityUtils.toString(httpResponse.getEntity());
				Log.e(Tag, "++ " + ret);
			} else {
				//Log.e(Tag, "statusCode: " + statusCode);
				String error = EntityUtils.toString(httpResponse.getEntity());
				//Log.e(Tag, "=="+error);
			}

			if (result != null) {
				result[0] = ret;
				return RESULT_SUCCEED;
			} else {
				return RESULT_NETWORD_ERROR;
			}

		} catch (ClientProtocolException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (UnsupportedEncodingException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (IOException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		}
	}
	
	
	
	private int httpsPost(String data, String[] result,String url) {
		Log.e(Tag, "HttpPost url: " + url);
		Log.e(Tag, "HttpPost data: " + data);

		try {
			HttpPost httpRequest = new HttpPost(url);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("", URLEncoder.encode(data,
					HTTP.UTF_8)));
			
			
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpRequest.addHeader("Content-Type", "application/json");
			httpRequest.setEntity(new StringEntity(data, "UTF_8"));
			
			
			
			HttpClient client =getHttpClient(); //new DefaultHttpClient();
			
			
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					TIME_OUT);
			
			
			
			
			
			HttpResponse httpResponse = client.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(Tag, "statusCode: " + statusCode);
			String ret = null;
			if (statusCode == HttpStatus.SC_OK) {    
				ret = EntityUtils.toString(httpResponse.getEntity());
				Log.e(Tag, "++ " + ret);
			} else {
				//Log.e(Tag, "statusCode: " + statusCode);
				String error = EntityUtils.toString(httpResponse.getEntity());
				//Log.e(Tag, "=="+error);
			}

			if (result != null) {
				result[0] = ret;
				return RESULT_SUCCEED;
			} else {
				return RESULT_NETWORD_ERROR;
			}

		} catch (ClientProtocolException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (UnsupportedEncodingException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (IOException e) {
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(Tag, e.toString());
			return RESULT_NETWORD_ERROR;
		}
	}
	

	public  synchronized HttpClient getHttpClient() {
		HttpClient HttpClient = null;
		if (null == HttpClient) {
			// 初始化工作
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  //允许所有主机的验证

				HttpParams params = new BasicHttpParams();

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params,
						HTTP.DEFAULT_CONTENT_CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);

				// 设置连接管理器的超时
				ConnManagerParams.setTimeout(params, 10000);
				// 设置连接超时
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				// 设置socket超时
				HttpConnectionParams.setSoTimeout(params, 10000);

				// 设置http https支持
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sf, 443));

				ClientConnectionManager conManager = new ThreadSafeClientConnManager(
						params, schReg);

				HttpClient = new DefaultHttpClient(conManager, params);
			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient();
			}
		}
		return HttpClient;
	}
	
	
	class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {

				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port,
					autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
	
	/**
	 * 鐠囬攱鐪伴崶鐐剁殶
	 * @author lenovo
	 *
	 */
	public interface OnRequestListener {
		/**
		 * 
		 * @param statusCode:0娑撶儤鍨氶崝鐔烘纯閹恒儴袙閺嬫劕姘ㄩ崣顖欎簰閿涘苯鍙炬禒鏍纯閹恒儱鐨㈤柨娆掝嚖娣団剝浼卼aost缂佹瑧鏁ら幋锟�
		 * @param str閿涙碍婀囬崝鈥虫珤鏉╂柨娲栭惃鍕殶閹诡喗鍨ㄩ懓鍗猻g闁挎瑨顕ゆ穱鈩冧紖
		 */
		void onResult(int statusCode,String str);
	}
	
	
	
	
	
		
		
		
		
				
}
