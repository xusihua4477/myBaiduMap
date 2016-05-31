package com.example.myprogram_baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ShowPointsOnActivity extends Activity {
	
	private BaiduMap baidumap_forNavigate;
	private MapView mapView_nagigate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	
	setContentView(R.layout.activity_show_points);
	init();
	setData();
	}

	private void setData() {
		// TODO Auto-generated method stub
		MapStatus mapStatus = baidumap_forNavigate.getMapStatus();
		String string = mapStatus.toString();

	
		int Traffic_tools_type =(int) getIntent().getIntExtra("Traffic_tools_type", 3);
		// Toast.makeText(mainActivity, "mapStatus"+string, 3000);
		if (Traffic_tools_type == 1) {
			TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(
					baidumap_forNavigate);
		
			transitRouteOverlay.setData(Application_Map.transitRouteLine);
			transitRouteOverlay.addToMap();
			transitRouteOverlay.zoomToSpan();

		} else if (Traffic_tools_type == 2) {

			DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
					baidumap_forNavigate);
			drivingRouteOverlay.setData(Application_Map.drivingRouteLine);
	
			drivingRouteOverlay.addToMap();

			drivingRouteOverlay.zoomToSpan();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		mapView_nagigate = (MapView)findViewById(R.id.mapView_nagigate);
		baidumap_forNavigate = mapView_nagigate.getMap();
		
		
		
	}
	
	
	public void returnBack(View view){
		
		finish();
		
	}
	
	
	

}
