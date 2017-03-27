package com.mct.common;

import java.util.HashMap;
import java.util.Map;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

/*
 * 全局接口调用过滤器
 * 
 */
public class MctSetPropertyFilter
{
	public static final String TAG = "MctSetPropertyFilter";
	public static int FILTER_LEVEL_IGNORE 	= 0;//关闭过滤机制
	public static int FILTER_LEVEL_NORMAL = 1;//缓存积累在一定范围内时过滤
	public static int FILTER_LEVEL_EMERGY 	= 2;//缓存积累比较大时开始过滤
	
	private static int DEFAULT_MIN_ACCESS_TIME = 1000;
	private static volatile MctSetPropertyFilter gInstance = null;  
	private Map<Integer, AccessProperty> mAccessPropertyCache = new HashMap<Integer, AccessProperty>();
	private int mFilterLevel = FILTER_LEVEL_IGNORE;
    private MctSetPropertyFilter(){  
    	mFilterLevel = FILTER_LEVEL_IGNORE;
    }  
   
    /*
     * 获取过滤器
     */
    public static MctSetPropertyFilter getInstance() {  
        if (gInstance == null) {  
            synchronized (MctSetPropertyFilter.class) {  
                if (gInstance == null) {  
                	gInstance = new MctSetPropertyFilter();  
                }  
            }  
        }  
        return gInstance;  
    }
    
    /*
     * 是否开启过滤机制
     */
    public void updateFilterLevel(int filterLevel)
    {
    	Log.i(TAG, "updateFilterLevel:"+filterLevel);
    	mFilterLevel = filterLevel;
    }
    
    /*
     * 是否能进行接口调用
     */
    public  boolean canAccess(int propId,String propValue)
    {
    	if(mFilterLevel == FILTER_LEVEL_IGNORE)
    	{
    		return true;
    	}
    	boolean bRet = false;
    	if(mAccessPropertyCache.containsKey(propId))
    	{
    		AccessProperty property = mAccessPropertyCache.get(propId);
    		//紧急情况下，不允许小于1s的同接口调用(不判断参数内容)
    		if(mFilterLevel == FILTER_LEVEL_EMERGY && 
    				SystemClock.uptimeMillis() - property.getAccessTime() >= DEFAULT_MIN_ACCESS_TIME)
    		{
    			bRet = true;
    		}
    		//紧急情况下，不允许小于1s的同接口同参数调用
    		else if(mFilterLevel == FILTER_LEVEL_NORMAL &&
    				(!TextUtils.equals(property.getPropValue() , propValue) ||
    						SystemClock.uptimeMillis() - property.getAccessTime() >= DEFAULT_MIN_ACCESS_TIME))
    		{
    			bRet = true;
    		}
    		else
    		{
    			Log.i(TAG, "filter access property,propId:"+propId+",value:"+propValue);
    			bRet =  false;
    		}
    	}
    	else
    	{
    		mAccessPropertyCache.put(propId, new AccessProperty(propId, propValue));
    		bRet = true;
    		return bRet;
    	}
    	if(bRet)
    	{
    		mAccessPropertyCache.get(propId).update(propId, propValue);
    	}
    	return bRet;
    }
    
    private class AccessProperty
    {
    	private int 		propId;
    	private String 	propValue;
    	private long 		accessTime;
    	
    	public AccessProperty(int propId,String propValue)
    	{
    		update(propId, propValue);
    	}

		public int getPropId()
		{
			return propId;
		}

		public void setPropId(int propId)
		{
			this.propId = propId;
		}

		public String getPropValue()
		{
			return propValue;
		}

		public void setPropValue(String propValue)
		{
			this.propValue = propValue;
		}

		public long getAccessTime()
		{
			return accessTime;
		}

		public void setAccessTime(long accessTime)
		{
			this.accessTime = accessTime;
		}
    	
		public void update(int propId,String propValue)
    	{
    		this.propId = propId;
    		this.propValue = propValue;
    		this.accessTime = SystemClock.uptimeMillis();
    	}
		
    }
}
