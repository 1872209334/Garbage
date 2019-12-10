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
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
  
/**
 * 根据城市名，地址名获取地址信息 （GPS纬度、经度值）
 * @author android_ls
 *
 */

public class BaiduMapActivity extends Activity implements BaiduMap.OnMapClickListener,
OnGetRoutePlanResultListener,OnGetGeoCoderResultListener {
	private static final String LTAG = "百度地图";
	public LocationClient mLocationClient = null; 
	public MyLocationListenner myListener = new MyLocationListenner(); 
    private static MapView mMapView = null;
    protected RelativeLayout layoutBase;
    LocationClient mLocClient;
    MyLocationData locData = null;
    private Marker markertemp;  
    private Marker[] makers; 
    private Marker markerp; 
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
	int zoom; // 定义地图初始显示层级
	// String preMarker = "";//用于保存上一个marker的信息
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
	int markerStatus;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s); 
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(BaiduMapActivity.this.getApplicationContext(),"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",Toast.LENGTH_LONG).show();
				 
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(BaiduMapActivity.this.getApplicationContext(),"网络出错",Toast.LENGTH_LONG).show();
				 
			}
		}
	}

	private SDKReceiver mReceiver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.baidumap_activity);    //加载样式文件
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.mapsView);  
        mBaidumap = mMapView.getMap();
    
        Intent intent = getIntent();
        city = intent.getStringExtra("city");  //获取js传过来的city
        address = intent.getStringExtra("address");//获取js传过来的address
        zoom = Integer.parseInt(intent.getStringExtra("zoom"));//获取js传过来的zoom
		
		Log.e("zoom",String.valueOf(zoom)); 
		// int marker_status = Integer.parseInt(obj.getString("marker_status"));参考格式，无用
		// zoom = 18;
        SharedPreferences settings = getSharedPreferences("setting",0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("city1", city);  //城市
		editor.putString("address1", address);//地址
		// editor.putString("zoom1", zoom);//层级
		editor.commit(); 
  
        
         // 注册 SDK 广播监听者
     	IntentFilter iFilter = new IntentFilter();
     	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
     	iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
     	mReceiver = new SDKReceiver();
     	registerReceiver(mReceiver, iFilter);
  
        
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
        
        Button btn3 =(Button)findViewById(R.id.ljdh_btn); 
	 	 //初始化按键
//        mBtnDrive = (Button)findViewById(R.id.zijia);
//        mBtnTransit = (Button)findViewById(R.id.gongjiao);
//        mBtnWalk = (Button)findViewById(R.id.buxing);
//	 	 OnClickListener clickListener = new OnClickListener(){
//				public void onClick(View v) {
					//发起搜索
//					SearchButtonProcess(v);
//				}
//	        };
	 
	        			
//			mBtnDrive.setOnClickListener(clickListener); 
//		    mBtnTransit.setOnClickListener(clickListener); 
//		    mBtnWalk.setOnClickListener(clickListener);
		  
		   
	 	 btn3.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	  public void onClick(View arg0) {
	        		Intent Intent = new Intent(); 
			  		Bundle bundle=new Bundle();
			  		bundle.putString("info", "list:");
			  		Intent.putExtras(bundle);
			  		setResult(1, Intent);
			  		finish();
//	        		onClickquzhi();//自动获取定位信息
//	    			
//	        		Button btn4 = (Button)findViewById(R.id.zijia);
//	        	 	Button btn5 = (Button)findViewById(R.id.gongjiao);
//	        	 	Button btn6 = (Button)findViewById(R.id.buxing);
//	        	    if(btn4.getVisibility() == View.INVISIBLE){
//	         			btn4.setVisibility(View.VISIBLE);
//	         			btn5.setVisibility(View.VISIBLE);
//	         	 		btn6.setVisibility(View.VISIBLE);
//	         		}else{
//	         		   btn4.setVisibility(View.INVISIBLE);
//	         	 	   btn5.setVisibility(View.INVISIBLE);
//	         	 	   btn6.setVisibility(View.INVISIBLE);
//	 		        }							
	        	   }
	        	 });
         
       
	
	     mBaiduMap = mMapView.getMap();
 		 mUiSettings = mBaiduMap.getUiSettings();
 		 MapStatus ms = new MapStatus.Builder().overlook(-10).build();
 		 MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
 		 mBaiduMap.animateMapStatus(u, 1000);
         mMapView.showZoomControls(false);
         mUiSettings.setZoomGesturesEnabled(true);//是否启用缩放手势
         mUiSettings.setScrollGesturesEnabled(true);//是否启用平移手势
/*         mUiSettings.setRotateGesturesEnabled(false);//是否启用旋转手势
*/         mUiSettings.setCompassEnabled(true); //是否启用指南针图层
         mUiSettings.setOverlookingGesturesEnabled(true);//是否启用俯视手势
      
      
     // 开启定位图层  
        mBaiduMap.setMyLocationEnabled(true);  
         
     // 初始地图gps 上海市的GPS纬度经度值:31.232310,121.469160
        LatLng p = new LatLng((int) (31.232310 * 1E6),(int)(121.469160 * 1E6));
		mMapView = new MapView(this,
				new BaiduMapOptions().mapStatus(new MapStatus.Builder()
						.target(p).build()));
        
//               initMapView();

     	
         mUiSettings.setCompassPosition(new Point(200, 200));  //设置指南针显示在左上角
            
         
        
         // 初始化地理坐标转换模块，注册事件监听
//     	geomSearch = GeoCoder.newInstance();
//     	geomSearch.setOnGetGeoCodeResultListener(this); 
        // Geo搜索
//       geomSearch.geocode(new GeoCodeOption().city(city).address(address));
//     	mMapView = new MapView(this,
//				new BaiduMapOptions().mapStatus(new MapStatus.Builder()
//						.target(p).build()));
     	//(116.331398,39.897445) 
		
		// private void setMarkerStatus(){
			// Map<String, String> marker = new HashMap<String,String>();
			// marker.put("markerId", "123");
			// marker.put("markerIcon", "ssdasdawdqw");
			// marker.get("markerId");
		// }
		
        //地图点击事件处理
//         address="[{'busId':1,'busLine':'惺惺惜惺惺'},{'busId':2,'busLine':'502'}]";
         JSONArray array;
		try {
			array = new JSONArray(address);
			JSONObject obj;
//			Marker marker = null;
			makers =new Marker[array.length()];
			OverlayOptions overlayOptions = null;  
	         for (int i = 0; i < array.length(); i++) {
	             obj = array.getJSONObject(i);
	             float lon = Float.parseFloat(obj.getString("lon"));
	             float lat = Float.parseFloat(obj.getString("lat"));
				 int marker_status = Integer.parseInt(obj.getString("marker_status"));
//	             String lon=(String) obj.getString("lon");
//	             String lat=(String) obj.getString("lat");
	            LatLng lp = new LatLng(lat,lon);
	            if(i == array.length()-1){
	            	
	            	setMapCenter(lp, zoom);
	            }
				// var circle = new BMap.Circle(lp,1000,{fillColor:"blue", strokeWeight: 1 ,fillOpacity: 0.3, strokeOpacity: 0.3,enableEditing:true});
				// map.addOverlay(circle);
				
				//将当前marker状态保存为marker的一个属性
				// Map<String, float> marker[i] = new HashMap<String,float>();
				// marker.put("markerStatus", marker_status);
				
				
				//判断 marker_status 状态添加不同覆盖物  
				//   橘红色--选中--icon_marker_orange
				//0：蓝色--正常--icon_marker_blue
				//1：灰色--报警--icon_marker_gray				
				if(marker_status == 1){				
					overlayOptions= new MarkerOptions().position(lp)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marker_gray)).zIndex(15);
				} else {
					overlayOptions= new MarkerOptions().position(lp)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marker_blue)).zIndex(15);
				}								
	         	mBaiduMap.addOverlay(overlayOptions);
	            makers[i]=(Marker) (mBaiduMap.addOverlay(overlayOptions));
//	            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
	            Bundle bundle=new Bundle();
	            bundle.putSerializable("info", i);  
				bundle.putSerializable("markerStatus", marker_status);  
	            makers[i].setExtraInfo(bundle); 
				//保存marker图标状态
				// Bundle markerStatus=new Bundle();
	            // markerStatus.putSerializable("markerStatus", marker_status);  
	            // makers[i].setExtraInfo(markerStatus);
	         }
	         
		} catch (JSONException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		//设置地图 Marker 覆盖物点击事件监听者
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
//				int info = (int) marker.getExtraInfo().get("info");  
//				InfoWindow mInfoWindow;  
//				TextView location = new TextView(getApplicationContext());
//				location.setBackgroundResource(R.drawable.icon_marker_orange); 
//				location.setText(info);
				info = Integer.parseInt(marker.getExtraInfo().get("info").toString());  
				TextView  tvCode,tvPointCategroy,btnDetail,btnNevigation,btnPrevious,btnNext;
				LinearLayout linearLayout_lp,linearLayout_pl;
		        tvCode = (TextView) findViewById(R.id.tv_code_lps);
				tvPointCategroy = (TextView) findViewById(R.id.tv_pointcategory_lps);
				btnDetail = (TextView) findViewById(R.id.tv_detail_lps);
				btnNevigation = (TextView) findViewById(R.id.tv_nevigation_lps);
				btnPrevious = (TextView) findViewById(R.id.tv_previous_lps);
				btnNext = (TextView) findViewById(R.id.tv_next_lps);
				linearLayout_lp = (LinearLayout) findViewById(R.id.linearLayout_lp);
				linearLayout_pl = (LinearLayout) findViewById(R.id.linearLayout_pl);
				linearLayout_lp.setVisibility(View.VISIBLE);
				linearLayout_pl.setVisibility(View.VISIBLE);
				//设置点击时的覆盖物
				
				// public void setMarkerStatus(){
					// Map<String, String> marker = new HashMap<String,String>();
					// marker.put("markerId", "123");
					// marker.put("markerIcon", "ssdasdawdqw");
					// marker.get("markerId");
				// }
				// setMarkerStatus();
				
				// BitmapDescriptor currentMarkerIcon = BitmapDescriptorFactory.getIcon();
				// public BitmapDescriptor getIcon();
				// Log.d(LTAG, "currentMarkerIcon: " + currentMarkerIcon); 
				// overlayOptions= new MarkerOptions().position(lp)
					// .icon(BitmapDescriptorFactory
							// .fromResource(R.drawable.icon_marker_blue)).zIndex(15);
				BitmapDescriptor bd = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marker_orange);
				if(markertemp != null)
				{
					BitmapDescriptor nul;
					//获取marker状态
					// float marker_status = 1;
					// float marker_status = Float.parseFloat(markertemp.getString("marker_status"));
					int judgeStatus = Integer.parseInt(markertemp.getExtraInfo().get("markerStatus").toString()); 
					// Log.e("map",judgeStatus+"");
					if(judgeStatus == 1){
						nul = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marker_gray);
					}
					else
					{
						nul = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marker_blue);
					}
					markertemp.setIcon(nul);
				}
				marker.setIcon(bd);
				marker.setToTop();
				markertemp = marker;
				//如果当前marker的状态为1，则markerStatus置为1
				// marker_status = Integer.parseInt(marker.getExtraInfo().get("markerStatus").toString());  
				// if(marker_status == 1){
					// markerStatus = 1;
				// }else{
					// markerStatus = 0;
				// }
				
				JSONArray array;
				//监听'详情'点击按钮--读取按钮
				btnDetail.setOnClickListener(new View.OnClickListener() {
					 public void onClick(View v) {
						 JSONArray array;
						 try {
							array = new JSONArray(address);
							JSONObject obj;
							obj = array.getJSONObject(info);
							code=obj.getString("city");
							// System.out.println(code);
					  		Intent Intent = new Intent(); 
					  		Bundle bundle=new Bundle();
					  		bundle.putString("info", code);
					  		Intent.putExtras(bundle);
					  		setResult(2, Intent);
					  		finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					 }
				});
				//监听'导航'点击按钮--设置按钮
				btnNevigation.setOnClickListener(new View.OnClickListener() {
					 public void onClick(View v) {
						 JSONArray array;
						 try {
							array = new JSONArray(address);
							JSONObject obj;
							obj = array.getJSONObject(info);
							code=obj.getString("city");
							System.out.println(code);
					  		Intent Intent = new Intent(); 
					  		Bundle bundle=new Bundle();
					  		bundle.putString("info", code);
					  		Intent.putExtras(bundle);
					  		setResult(3, Intent);
					  		finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					 }
				});
				//监听'上一个'点击按钮
				btnPrevious.setOnClickListener(new View.OnClickListener() {
					 public void onClick(View v) {
						 JSONArray array;
						 	try {
								array = new JSONArray(address);
								JSONObject obj;
								OverlayOptions overlayOptions = null;  
								if(info==0){
									
									
										info=array.length()-1;
										BitmapDescriptor bd = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_orange);
										if(markertemp != null)
										{
											BitmapDescriptor nul;
											int judgeStatus = Integer.parseInt(markertemp.getExtraInfo().get("markerStatus").toString()); 
											if(judgeStatus == 1){
												nul = BitmapDescriptorFactory
													.fromResource(R.drawable.icon_marker_gray);
											}
											else
											{
												nul = BitmapDescriptorFactory
													.fromResource(R.drawable.icon_marker_blue);
											}
											markertemp.setIcon(nul);
										}
										makers[info].setIcon(bd);
										makers[info].setToTop();
										markertemp=makers[info];
										
										
										obj = array.getJSONObject(info);
										System.out.println(info);
								        float lon = Float.parseFloat(obj.getString("lon"));
							            float lat = Float.parseFloat(obj.getString("lat"));
							            TextView  tvCode,tvPointCategroy;
							            tvCode = (TextView) findViewById(R.id.tv_code_lps);
										tvPointCategroy = (TextView) findViewById(R.id.tv_pointcategory_lps);
							            tvCode.setText(obj.getString("title"));
							            tvPointCategroy.setText("路灯");
										LatLng dw = new LatLng(lat,lon);
										setMapCenter(dw, zoom);
									
								}else{
									info--;
									
									
									BitmapDescriptor bd = BitmapDescriptorFactory
											.fromResource(R.drawable.icon_marker_orange);
									if(markertemp != null)
									{
										BitmapDescriptor nul;
										int judgeStatus = Integer.parseInt(markertemp.getExtraInfo().get("markerStatus").toString()); 
										if(judgeStatus == 1){
											nul = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_gray);
										}
										else
										{
											nul = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_blue);
										}
										markertemp.setIcon(nul);
									}
//									makers[info].setIcon(nul);
									makers[info].setIcon(bd);
									makers[info].setToTop();
									markertemp=makers[info];
									
									
									obj = array.getJSONObject(info);
							        System.out.println(info);
							        float lon = Float.parseFloat(obj.getString("lon"));
						            float lat = Float.parseFloat(obj.getString("lat"));
						            TextView  tvCode,tvPointCategroy;
						            tvCode = (TextView) findViewById(R.id.tv_code_lps);
									tvPointCategroy = (TextView) findViewById(R.id.tv_pointcategory_lps);
						            tvCode.setText(obj.getString("title"));
						            tvPointCategroy.setText("路灯");
									LatLng dw = new LatLng(lat,lon);
									setMapCenter(dw, zoom);
								}
						        
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					 }
				});
				//监听'下一个'点击按钮
				btnNext.setOnClickListener(new View.OnClickListener() {
					 public void onClick(View v) {
						 JSONArray array;
						 	try {
								array = new JSONArray(address);
								JSONObject obj;
//								OverlayOptions overlayOptions = null;
								if(info==array.length()-1){
										info=0;
										
										
										obj = array.getJSONObject(info);
										System.out.println(info);
								        float lon = Float.parseFloat(obj.getString("lon"));
							            float lat = Float.parseFloat(obj.getString("lat"));
							            TextView  tvCode,tvPointCategroy;
							            tvCode = (TextView) findViewById(R.id.tv_code_lps);
										tvPointCategroy = (TextView) findViewById(R.id.tv_pointcategory_lps);
							            tvCode.setText(obj.getString("title"));
							            tvPointCategroy.setText("路灯");
										LatLng dw = new LatLng(lat,lon);
										setMapCenter(dw, zoom);
										
										
										BitmapDescriptor bd = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_orange);
										if(markertemp != null)
										{
											BitmapDescriptor nul;
											int judgeStatus = Integer.parseInt(markertemp.getExtraInfo().get("markerStatus").toString()); 
											if(judgeStatus == 1){
												nul = BitmapDescriptorFactory
													.fromResource(R.drawable.icon_marker_gray);
											}
											else
											{
												nul = BitmapDescriptorFactory
													.fromResource(R.drawable.icon_marker_blue);
											}
											markertemp.setIcon(nul);
										}
										makers[info].setIcon(bd);
										makers[info].setToTop();
										markertemp=makers[info];

										
									
								}else{
									info++;
									
									
									BitmapDescriptor bd = BitmapDescriptorFactory
											.fromResource(R.drawable.icon_marker_orange);
									if(markertemp != null)
									{
										BitmapDescriptor nul;
										int judgeStatus = Integer.parseInt(markertemp.getExtraInfo().get("markerStatus").toString()); 
										if(judgeStatus == 1){
											nul = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_gray);
										}
										else
										{
											nul = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_marker_blue);
										}
										markertemp.setIcon(nul);
									}
									makers[info].setIcon(bd);
									makers[info].setToTop();
									markertemp=makers[info];
									
									
									obj = array.getJSONObject(info);
									System.out.println(info);
							        float lon = Float.parseFloat(obj.getString("lon"));
						            float lat = Float.parseFloat(obj.getString("lat"));
						            TextView  tvCode,tvPointCategroy;
						            tvCode = (TextView) findViewById(R.id.tv_code_lps);
									tvPointCategroy = (TextView) findViewById(R.id.tv_pointcategory_lps);
						            tvCode.setText(obj.getString("title"));
						            tvPointCategroy.setText("路灯");
									LatLng dw = new LatLng(lat,lon);
									setMapCenter(dw, zoom);
									
								}
						       
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					 }
				});
				try {
					array = new JSONArray(address);
					JSONObject obj;
					OverlayOptions overlayOptions = null;  
			             obj = array.getJSONObject(info);
			             obj.getString("lon");
			             tvCode.setText(obj.getString("title"));
			             tvPointCategroy.setText("路灯");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
		});
//         JSONArray array = new JSONArray(address);
         
//         JSONArray jsonArray = new JSONArray(address); 
//         JSONObject jObj = null;
//         for(int i=0;i<address.length();i++){
//        	
//         }
         
//        mBaidumap.setOnMapClickListener(this);
        
//        JSONArray jsonArray = new JSONArray(address);
//        JSONObject jsonArray = JSONObject.fromObject(address);
         System.out.println(address);
//       JSONArray jsonArray = JSONArray.fromObject(address);
//       JSONObject jObj = null;
//       System.out.println(address);
//       for(int i=0; i< jsonArray.size();i++){
//    	   jObj = jsonArray.getJSONObject(i);
//    	   jObj.getInt("lat");
//    	   jObj.getString("lon");
//    	   System.out.println(jObj.getInt("lat"));
//       }
//       jsonArray.getJSONObject(index)
//        for(int i=0;i<address.length();i++){
//        	JSONObject jsonobject2=jsonArray.getJSONObject(i);
////        	System.out.println(jsonobject2);
//        }
        // 初始化搜索模块，注册事件监听
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(this);  
        //39°54′27  116°23′17
//        LatLng lp = new LatLng(25.9769250000,119.2204810000);
//    	mBaiduMap.addOverlay(new MarkerOptions().position(lp)
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.icon_marker_orange)).zIndex(15));
//        new AlertDialog.Builder(this) 
//        .setTitle("地址详情：")
//        .setMessage(address)
//        .show();
         
         //设置导航栏颜色
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {  
             
             getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
             getWindow().setStatusBarColor(this.getResources().getColor(R.color.blue));
         }             
        
    }
	
	/**
	 * 发起路线规划搜索示例
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		 
		 mBaidumap.clear();
		 
		//重置浏览节点的路线数据
		route = null;
		  
		// 处理搜索按钮响应
		 
	  
		SharedPreferences settings = getSharedPreferences("setting", 0); 			
		String lat1 = settings.getString("lat1", ""); // 本地地址
		String lon1 = settings.getString("lon1", "");//本地城市
		
		String lat2 = settings.getString("lat2", ""); //标记地址
		String lon2 = settings.getString("lon2", ""); //标记城市 
		
		String loccity = settings.getString("loccity", ""); //城市  
		 
//		 Log.e("lat1","起点lat"+lat1);
//		 Log.e("lon1","起点lon"+lon1);
//		 Log.e("lat2","终点lat"+lat2);
//		 Log.e("lon2","终点lon"+lon2);
//		 Log.e("loccity","城市"+loccity);
		  
		
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
	  
		    LatLng ll = new LatLng(Float.parseFloat(lat1), Float.parseFloat(lon1));
		    LatLng l2 = new LatLng(Float.parseFloat(lat2), Float.parseFloat(lon2)); 
	        PlanNode stNode = PlanNode.withLocation(ll);
	        PlanNode enNode = PlanNode.withLocation(l2); 
		
		// 实际使用中请对起点终点城市进行正确的设定
		if (mBtnDrive.equals(v)) {
			 
			 mSearch.drivingSearch((new DrivingRoutePlanOption())
	                    .from(stNode)
	                    .to(enNode)); 

		} else if (mBtnTransit.equals(v)) {
			 	 
			mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .city(loccity)
                    .to(enNode));
			 
		} else if (mBtnWalk.equals(v)) {
			 
			 mSearch.walkingSearch((new WalkingRoutePlanOption())
	                    .from(stNode)
	                    .to(enNode));
		} 
	}
//	public void finish() 
//	{
//		setResult(RESULT_OK);
//
//		super.finish();
//
//		CommonUtils.stopService(this.getApplicationContext(), RfidService.class);
//
//		CommonUtils.stopService(this.getApplicationContext(), BlueToothService.class);
//
//	}
//是否显示底部菜单
	protected void showBaseView(boolean visiable) {
		
		if (visiable){
			layoutBase.setVisibility(View.VISIBLE);
		}
			
		else
			layoutBase.setVisibility(View.GONE);
	}
	
	/** 设置底部布局 */
	protected void setBaseView(View view) {
		layoutBase.removeAllViews();
		layoutBase.addView(view);
	}
	protected void setMapCenter(LatLng latLng, float zoom) {
		// 定义地图状态
		MapStatus mMapStatus;
		if(!isLocationClientStop)
		{
			isLocationClientStop = true ;
			mMapStatus = new MapStatus.Builder().target(latLng).zoom(zoom).build();
		}
		else
		{
			mMapStatus = new MapStatus.Builder().target(latLng).build();
		}
		
		
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaidumap.setMapStatus(mMapStatusUpdate);		
	}
	
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
				
				mLocClient.start();
 	}
 
	
    //手动定位
	public void onClickdingwei() { 
		// 开启定位图层
				isRequest = true;
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
        
          //经纬度存储          
            Context ctx = BaiduMapActivity.this;   
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
        popupText = new TextView(BaiduMapActivity.this);
//        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }
 

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //获取步行线路规划结果  
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到步行路径规划的结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
          
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            //routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

  //获取公交换乘路径规划结果  
    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到公交路径规划的结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
           
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            //routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

  //获取驾车线路规划结果  
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
    	 
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapActivity.this, "抱歉，未找到驾车路径规划的结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	
            nodeIndex = -1; 
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            //routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener((OnMarkerClickListener)overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
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

    //获取地理编码
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		 
		
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			geomSearch.geocode(new GeoCodeOption().city(city).address(address));
//			Log.e(LTAG,"获取地理编码结果 ，未能找到结果"+result.error); 
			return;
		}
//		mBaiduMap.clear();
		
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marker_orange)).zIndex(15));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		
		 String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		  Log.e(LTAG,address+"的地理编码:"+strInfo); 
		   
		    double latitude  = result.getLocation().latitude; // 纬度
            double longitude = result.getLocation().longitude;// 经度
      	   
            SharedPreferences settings = getSharedPreferences("setting",0);
  			SharedPreferences.Editor editor = settings.edit();
  			editor.putString("lat2", latitude+"");  //经纬度存储
  			editor.putString("lon2", longitude+""); 
  			editor.commit(); 
	}

	//获取反向地理编码
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaiduMapActivity.this, "获取反向地理编码结果，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marker_orange)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		Toast.makeText(BaiduMapActivity.this, result.getAddress(),
				Toast.LENGTH_LONG).show();

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


    