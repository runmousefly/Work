package com.cspaying.shanfu.ui.jsondata;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InitSignString {

	public final static String getSign(HashMap<String, String> map, String key) {
		String sign = null;
		if (map != null) {
			Collection<String> keyset = map.keySet();
			List<String> list = new ArrayList<String>(keyset);
			Collections.sort(list);
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					sign = list.get(i) + "=" + map.get(list.get(i));
				} else {
					sign = sign + "&" + list.get(i) + "="
							+ map.get(list.get(i));
				}
			}
			Log.e("++++++++++++++++++++++++++++++sign:", sign);
			sign = sign + "&" + "key" + "=" + key;
			Log.e("++++++++++++++++++++++++++++++sign+key:", sign);
			sign = MD5(sign).toUpperCase();
			Log.e("++++++++++++++++++++++++++++++sign+key+md5:", sign);
		}

		return sign;
	}

	public final static String getSign(JSONObject jsonObject, String key)
			throws JSONException {
		String sign = null;
		if (jsonObject != null) {
			Iterator<String> keys = jsonObject.keys();
			List<String> list = new ArrayList<String>();
			while (keys.hasNext()) {
				list.add(keys.next());
			}
			Collections.sort(list);
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					sign = list.get(i) + "=" + jsonObject.get(list.get(i));
				} else {
					sign = sign + "&" + list.get(i) + "="
							+ jsonObject.get(list.get(i));
				}
			}
			Log.e("++++++++++++++++++++++++++++++sign:", sign);
			sign = sign + "&" + "key" + "=" + key;
			Log.e("++++++++++++++++++++++++++++++sign+key:", sign);
			sign = MD5(sign).toUpperCase();
			Log.e("++++++++++++++++++++++++++++++sign+key+md5:", sign);
		}
		return sign;
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			byte[] btInput = s.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ����Ƶ��ַ���ʽ
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
