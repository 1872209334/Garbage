package com.qdc.plugins.baidu; 

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener; 
  
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.toysoho.light.R;


import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
  
/**
 * 根据城市名，地址名获取地址信息 （GPS纬度、经度值）
 * @author android_ls
 *
 */

public class BaiduMapActivitylocal extends Activity implements BaiduMap.OnMapClickListener {
	private static final String LTAG = "百度地图";
	public LocationClient mLocationClient = null; 
	public MyLocationListenner myListener = new MyLocationListenner(); 
    private static MapView mMapView = null;
    protected RelativeLayout layoutBase;
    LocationClient mLocClient;
    MyLocationData locData = null;
    String localinfo;
    String jingewei;
	private TextView  popupText = null;//泡泡view
	//UI相关
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;//是否手动触发请求定位
	boolean isFirstLoc = true;//是否首次定位
	boolean isLocationClientStop = false;
	String getAddrStr = ""; // 本地地址
	String getcityStr = ""; // 本地城市
	String city = "";
	String address = "";
	//浏览路线节点相关
	Button mBtnDrive = null;	// 驾车搜索
	Button mBtnTransit = null;	// 公交搜索
	Button mBtnWalk = null;	// 步行搜索
	  
 
	int nodeIndex = -1;//节点索引,供浏览节点时使用
	RouteLine route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
	TransitRouteOverlay transit = null;//保存公交路线图层数据的变量，供浏览节点时使用
	int searchType = -1;//记录搜索的类型，区分驾车/步行和公交

	//搜索相关
	RoutePlanSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	GeoCoder geomSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
    BaiduMap mBaidumap = null;
    
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    String  code= "";
    int info;
	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s); 
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(BaiduMapActivitylocal.this.getApplicationContext(),"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",Toast.LENGTH_LONG).show();
				 
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(BaiduMapActivitylocal.this.getApplicationContext(),"网络出错",Toast.LENGTH_LONG).show();
				 
			}
		}
	}

	private SDKReceiver mReceiver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.baidumap_activitylocal);    //加载样式文件
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.mapsView);  
        mBaidumap = mMapView.getMap();
    
        Intent intent = getIntent();
        city = intent.getStringExtra("city");  //获取js传过来的city
        address = intent.getStringExtra("address");//获取js传过来的address
        
        SharedPreferences settings = getSharedPreferences("setting",0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("city1", city);  //城市
		editor.putString("address1", address);//地址
		editor.commit(); 
  
        
         // 注册 SDK 广播监听者
     	IntentFilter iFilter = new IntentFilter();
     	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
     	iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
     	mReceiver = new SDKReceiver();
     	registerReceiver(mReceiver, iFilter);
  
    	 TextView tvLocation;
    	 Button btnOk;
		tvLocation = (TextView) findViewById(R.id.tv_location_lli);
		btnOk = (Button) findViewById(R.id.btn_ok_lli);
        //返回按钮的监听事件，点击后关闭掉当前页面
        Button btn1=(Button)findViewById(R.id.return_btn);
        
        btn1.setOnClickListener(new View.OnClickListener() {
        	@Override
        	  public void onClick(View arg0) { 
        		Intent Intent = new Intent(); 
		  		Bundle bundle=new Bundle();
		  		bundle.putString("info", "退出");
		  		Intent.putExtras(bundle);
		  		setResult(5, Intent);
		  		finish();
        	   }
        	 });
        
        //定位按钮的监听事件，点击后获取定位信息并加载到map
        Button btn2=(Button)findViewById(R.id.dingwei);
        btn2.setOnClickListener(new View.OnClickListener() {
        	@Override
        	  public void onClick(View arg0) {
        		
        		onClickdingwei();//定位
        		
        	   }
        	 });
        
//        Button btn3 =(Button)findViewById(R.id.ljdh_btn); 
//
//		  
//		   
//	 	 btn3.setOnClickListener(new View.OnClickListener() {
//	        	@Override
//	        	  public void onClick(View arg0) {
//	        		setResult(RESULT_OK );
//			  		finish();
////	        		onClickquzhi();//自动获取定位信息
////	    			
//						
//	        	   }
//	        	 });
         
       
	
	     mBaiduMap = mMapView.getMap();
 		 mUiSettings = mBaiduMap.getUiSettings();
 		 MapStatus ms = new MapStatus.Builder().overlook(-10).build();
 		 MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
 		 mBaiduMap.animateMapStatus(u, 1000);
         mMapView.showZoomControls(false);
         mUiSettings.setZoomGesturesEnabled(true);//是否启用缩放手势
         mUiSettings.setScrollGesturesEnabled(true);//是否启用平移手势
         mUiSettings.setRotateGesturesEnabled(false);//是否启用旋转手势
         mUiSettings.setCompassEnabled(false); //是否启用指南针图层
         mUiSettings.setOverlookingGesturesEnabled(true);//是否启用俯视手势
      
         mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

                 @Override
                 public void onMapStatusChangeStart(MapStatus status) {

                 }

                 @Override
                 public void onMapStatusChangeFinish(MapStatus status) {
                	 updateMapState(status);

                 }

                 @Override
                 public void onMapStatusChange(MapStatus status) {

                 }
         });
     // 开启定位图层  
        mBaiduMap.setMyLocationEnabled(true);  
       
     // 初始地图gps 上海市的GPS纬度经度值:31.232310,121.469160
//        LatLng p = new LatLng((int) (31.232310 * 1E6),(int)(121.469160 * 1E6));
//		mMapView = new MapView(this,
//				new BaiduMapOptions().mapStatus(new MapStatus.Builder()
//						.target(p).build()));
//        
        initMapView();
        LatLng latLng = getMapCenter(); 
        JSONArray array;
        try {
        	JSONObject obj;
			array = new JSONArray(address);
			obj = array.getJSONObject(info);
//			char[] x=obj.getString("lat").toCharArray();
//			int x = Integer.valueOf(obj.getString("lat")).intValue();
//			int y = Integer.valueOf(obj.getString("lon")).intValue();
            float y = Float.parseFloat(obj.getString("lon"));
            float x = Float.parseFloat(obj.getString("lat")); 
			if(x==0&&y==0){
	
	        	onClickdingwei();
	        	
	        }else{
	        	jingewei=y+","+x;
	            LatLng lp = new LatLng(y,x);
	            setMapCenter(lp, 15);
	            if (geomSearch == null) {
	    			geomSearch = GeoCoder.newInstance();
	    		}
	    		if(lp!=null)
	    		{
	    			reverseGeoCode(lp, Listener, geomSearch);	
	    		}
	        }} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        
        
       

        
         mUiSettings.setCompassPosition(new Point(200, 200));  //设置指南针显示在左上角
         System.out.println(address);
         
         //设置导航栏颜色
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {  
             
             getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
             getWindow().setStatusBarColor(this.getResources().getColor(R.color.blue));
         }            
    }
	
    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        Log.e(LTAG,"纬度："+  mCenterLatLng.latitude+"/"+mCenterLatLng.longitude);
        jingewei=mCenterLatLng.latitude+","+mCenterLatLng.longitude;
        //解析获得地址
		if (geomSearch == null) {
			geomSearch = GeoCoder.newInstance();
		}
		if(mCenterLatLng!=null)
		{
			reverseGeoCode(mCenterLatLng, Listener, geomSearch);		
		}        
/*        *//**获取经纬度*//*
        double lat = mCenterLatLng.latitude;
        double lng = mCenterLatLng.longitude;*/
    }
	protected LatLng getMapCenter()
	{
		return mBaidumap.getMapStatus().target;
	}

	protected void setMapCenter(LatLng latLng, float zoom) {
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(zoom).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaidumap.setMapStatus(mMapStatusUpdate);		
	}
	/** 反地理编码（坐标转地址） */
	public static void reverseGeoCode(LatLng latLng,
			OnGetGeoCoderResultListener listener, GeoCoder mSearch) {
		mSearch.setOnGetGeoCodeResultListener(listener);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
	}
   @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	Intent Intent = new Intent(); 
	  		Bundle bundle=new Bundle();
	  		bundle.putString("info", "退出");
	  		Intent.putExtras(bundle);
	  		setResult(5, Intent);
	  		finish();
  
        }  
          
        return false;  
          
    }
	private OnGetGeoCoderResultListener Listener = new OnGetGeoCoderResultListener() {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
				final AddressComponent addressl = result.getAddressDetail();
		       	 TextView tvLocation;
		   		tvLocation = (TextView) findViewById(R.id.tv_location_lli);
		   		Log.e("百度地图",addressl.street+addressl.streetNumber);
		   		if(addressl.street !=null || addressl.streetNumber != null)
		   		{
			   		tvLocation.setText(addressl.street+addressl.streetNumber);
		   		}

		   		localinfo=jingewei+","+addressl.city+","+addressl.district+","+addressl.street+","+addressl.streetNumber;
               	Button btok;
              	btok = (Button) findViewById(R.id.btn_ok_lli);
              	btok.setOnClickListener(new View.OnClickListener() {
        			 public void onClick(View v) {
        				Intent Intent = new Intent(); 
        			  	Bundle bundle=new Bundle();
//        			  	System.out.println(localinfo+","+"588");
        			  	bundle.putString("info", localinfo);
        			  	Intent.putExtras(bundle);
        			  	setResult(4, Intent);
        			  	finish();
        				 
        			 }
        		});
//		   		try {
//					localinfo.put("hhh", addressl.city);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				Log.e("onGetReverseGeoCodeResult", addressl.city+addressl.district+addressl.street+addressl.streetNumber);
/*				locationInfoView.setLocation(address.district+address.street+address.streetNumber);
				disDialog();*/
			}else {
/*				dialog.showToastShort(getActivity(), getString(R.string.connect_server_fail));*/
			}
		}
	};	
	//自动获取定位信息
	public void onClickquzhi() { 
		// 开启定位图层
				isRequest = false; 
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mLocClient = new LocationClient(this);
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);        			 		
    		    option.setAddrType("all");//返回的定位结果包含地址信息	  
    			option.setProdName("BaiduLoc");
    			option.setScanSpan(500);//当所设的整数值大于等于1000（）时，定位SDK内部使用定时定位模式。当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
    			option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
    			option.setIsNeedAddress(true);//返回的定位结果包含地址信息
    			option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向 
				mLocClient.setLocOption(option);
				System.out.println(option);
				mLocClient.start();
 	}
 
	
    //手动定位
	public void onClickdingwei() { 
		// 开启定位图层
				isRequest = true;
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
				mLocClient = new LocationClient(this);
				mLocClient.registerLocationListener(myListener);
				
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);        			 		
    		    option.setAddrType("all");//返回的定位结果包含地址信息	  
    			option.setProdName("BaiduLoc");
    			option.setScanSpan(500);//当所设的整数值大于等于1000（）时，定位SDK内部使用定时定位模式。当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
    			option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
    			option.setIsNeedAddress(true);//返回的定位结果包含地址信息
    			option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向  
				mLocClient.setLocOption(option);
				mLocClient.start();
				
		
	}
	 
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return; 
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			
		    Log.e(LTAG,"定位的纬度："+locData.latitude);
            Log.e(LTAG,"定位的经度"+locData.longitude); 
            Log.e(LTAG,"定位的城市"+location.getCity()); 
            Log.e(LTAG,"定位的城市777"+location.getAddrStr()); 
//            Log.e("district", location.getDistrict());
//            Log.e("district", location.getStreet());
//            Log.e("district", location.getStreetNumber());
	       	TextView tvLocation;
	       	Button btnOk;
	   		tvLocation = (TextView) findViewById(R.id.tv_location_lli);
	   		btnOk = (Button) findViewById(R.id.btn_ok_lli);
	   		if(location.getStreet() != null || location.getStreetNumber() != null)
	   		{
		   		tvLocation.setText(location.getStreet()+location.getStreetNumber());	   			
	   		}

	   		jingewei=locData.latitude+","+locData.longitude;
	   		localinfo=jingewei+","+location.getCity()+","+location.getDistrict()+","+location.getStreet()+","+location.getStreetNumber();
//	   		tvLocation.setText();
	   		btnOk.setOnClickListener(new View.OnClickListener() {
    			 public void onClick(View v) {
    				Intent Intent = new Intent(); 
    			  	Bundle bundle=new Bundle();
//    			  	System.out.println(localinfo+","+"588");
    			  	bundle.putString("info", localinfo);
    			  	Intent.putExtras(bundle);
    			  	setResult(4, Intent);
    			  	finish();
    				 
    			 }
    		});
          //经纬度存储          
            Context ctx = BaiduMapActivitylocal.this;   
            SharedPreferences settings = ctx.getSharedPreferences("setting",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("lat1",locData.latitude+"" );  //经纬度存储
			editor.putString("lon1",locData.longitude+""); 
			editor.putString("loccity",location.getCity()+""); 
			editor.commit();
			
			if (isRequest) {
				isRequest = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			
		}
	}
	
	
	
    private void initMapView() {
         mMapView.setLongClickable(true);
        //mMapController.setMapClickEnable(true);
        //mMapView.setSatellite(false);
    }
    
    protected void onSaveInstanceState(Bundle outState) {
    	  super.onSaveInstanceState(outState);
    	 
    	
    }
    
    
 
  
	/**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
 
        //获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        //移动节点至中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(BaiduMapActivitylocal.this);
//        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }
 

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

   
  
    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
    	return false;
    }

  
	
	
	 
	   @Override
     protected void onPause() {
		    	/**
		    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		    	 */
			   
			    mMapView.onPause();
		        super.onPause();
		    }
		    
		    @Override
	 protected void onResume() {
		    	/**
		    	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		    	 */
		    	 
		    	mMapView.onResume();
		        super.onResume();
		    }
		    
		    @Override
	 protected void onDestroy() {
		    	/**
		    	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		    	 */
		    	if (mLocClient != null)
		            mLocClient.stop(); 
		        mMapView.onDestroy();
		      // 取消监听 SDK 广播
				unregisterReceiver(mReceiver);
		        super.onDestroy();
		    }
	    
    
 
}


    