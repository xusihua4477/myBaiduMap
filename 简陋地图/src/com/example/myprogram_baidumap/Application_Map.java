package com.example.myprogram_baidumap;

import java.util.List;

import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;

import android.app.Application;
import android.content.Context;

public class Application_Map extends Application {
	public static Application_Map instance;
	public static Context applicationContext;
	public static DataForApplication data_forApplication=new DataForApplication();
	
	public static	DrivingRouteLine drivingRouteLine;
	public static  TransitRouteLine	 transitRouteLine;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this;
		
		applicationContext=this;
		
		
	}
	
	
	public static Application_Map getInstance(){
		
	return	instance;
		
	}
	
	public List<BusStation> getStationList() {
		return data_forApplication.getStationList();
	}

	public void setStationList(List<BusStation> stationList) {
		data_forApplication.setStationList(stationList);
	}
	

}
