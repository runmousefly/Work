package com.cspaying.shanfu.ui.utils;

import com.cspaying.shanfu.ui.MyApplication;

import android.content.Context;


public class DialogUtil {
	private static DialogUtil instance;
	private Context context;
	 private CustomProgressDialog progressDialog = null;
	public DialogUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static DialogUtil getInstance(Context context){
		if(instance == null){
			instance = new DialogUtil(context);
		}
		
		return instance;
		
	}
	
	 /**
     * ��ʼProgressDialog()
     */
    public void startProgressDialog(Context context,String mess){
    	try {
    		if (progressDialog == null){
    	    	   progressDialog = CustomProgressDialog.createDialog(context);
    	    	   progressDialog.setMessage(mess);
    	    	}
    	    	progressDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
    }
    
    /**
     * ����ProgressDialog()
     */
    public void stopProgressDialog(){    
    	try {
    		if (progressDialog != null){
    	    	  progressDialog.dismiss();      
    			  //progressDialog.cancel();
    	         progressDialog = null;
    	      }
		} catch (Exception e) {
			// TODO: handle exception
		}
	  
    }
    
	
}
