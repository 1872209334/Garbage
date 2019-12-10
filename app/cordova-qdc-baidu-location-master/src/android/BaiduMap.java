 
package com.qdc.plugins.baidu;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * This calls out to the ZXing barcode reader and returns the result.
 */
public class BaiduMap extends CordovaPlugin {
    private static final String pgbaidumapljdh = "pgbaidumapljdh";//公交、驾车、步行导航路径导航
    private static final String pgbaidumapsearch = "pgbaidumapsearch"; //周边查询
   
    private static final String baiduad = "baiduad";//地址查询
    private static final String baidudw = "baidudw";//地址查询
    private static final String CITY = "city";
    private static final String ADDRESS = "address";
    private static final String ZOOM = "zoom";//设置地图层级
    
 
    private static final String LOCA = "location";
    
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String REG = "region"; 
    
    private static final String QUERY = "query";
    private static final String CITYLOC = "cityloc";
    
	private CallbackContext callbackContext; 
    public String callback;
        
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		 this.callbackContext = callbackContext;        
        if (action.equals(baiduad)) {
        	
           JSONObject cityad = args.optJSONObject(0);
            if (cityad != null) {
            	 String city = cityad.optString(CITY);
  	  		     String address = cityad.optString(ADDRESS);
               String zoom = cityad.optString(ZOOM);
  	  			 Log.d("城市", "城市：" + city);
  	  		     Log.d("地址", "地址：" + address);
  	  		     
  	  		Intent Intent = new Intent(); 	  		 

  	  		Intent.putExtra("city", city); //传值给BaiduMapActivity
  	  		Intent.putExtra("address", address);//传值给BaiduMapActivity
          Intent.putExtra("zoom", zoom);//传值给BaiduMapActivity

 			Intent.setClass(cordova.getActivity(), BaiduMapActivity.class);//启动另一个activity
 			//Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 			//this.cordova.getActivity().startActivity(Intent);
			if (this.cordova != null) {
			    this.cordova.startActivityForResult((CordovaPlugin) this,Intent,1);
			} 
               
            } else {
           	  callbackContext.error("输入的城市或地址有误！");            	
              return false;
           }
            
        }
        else if (action.equals(baidudw)) { 
        	//JSONObject adljdh = args.optJSONObject(0);  //暂时保留
        	 JSONObject cityad = args.optJSONObject(0);
             if (cityad != null) {
             	 String city = cityad.optString(CITY);
   	  		     String address = cityad.optString(ADDRESS); 
   	  			 Log.d("城市", "城市：" + city);
   	  		     Log.d("地址", "地址：" + address);
   	  		Intent Intent = new Intent(); 	  		 

   	  		Intent.putExtra("city", city); //传值给BaiduMapActivity
   	  		Intent.putExtra("address", address);//传值给BaiduMapActivity

  			Intent.setClass(cordova.getActivity(), BaiduMapActivitylocal.class);//启动另一个activity
  			//Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  			//this.cordova.getActivity().startActivity(Intent);
 			if (this.cordova != null) {
 			    this.cordova.startActivityForResult((CordovaPlugin) this,Intent,1);
 			} 
                
             } else {
            	  callbackContext.error("输入的城市或地址有误！");            	
               return false;
            }
        	
        	
        }else {
        	callbackContext.error("Invalid Action");
            return false;
        }
        
        return true;
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 String  info = data.getStringExtra("info");  //获取js传过来的city
		if (resultCode >0 ) {
//			ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
//			JSONArray res = new JSONArray(fileNames);
//			this.callbackContext.success("success");
			String detail="";
			if(resultCode==1){
				detail="list:";
			}else if(resultCode==2){
				detail="detail:"+info;
			}else if(resultCode==3){
				detail="nav:"+info;
			}else if(resultCode==4){
				detail=info;
			}else if(resultCode==5){
				detail=info;
			}
				
				this.callbackContext.success(detail);
			 Log.d("城市8888", "城市S888：" + detail);
		}
		else {
//			 Intent intent = getIntent();
//		     city = intent.getStringExtra("city");  //获取js传过来的city
			this.callbackContext.error("No images selected");

		}
//		  Intent intent = getIntent();
	     
//	        address = intent.getStringExtra("address");//获取js传过来的address
	}

    
    
	 
	 
}

 



