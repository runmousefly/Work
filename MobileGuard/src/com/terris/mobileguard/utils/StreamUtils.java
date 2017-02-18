package com.terris.mobileguard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils
{
	public static String readStream(InputStream is)
	{
		if(is == null)
		{
			return null;
		}
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = is.read(buffer)) != -1)
			{
				os.write(buffer, 0, len);
			}
			is.close();
			return new String(os.toByteArray());
		} catch (IOException e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
