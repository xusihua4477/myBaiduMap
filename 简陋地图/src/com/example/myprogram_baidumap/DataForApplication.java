package com.example.myprogram_baidumap;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.myprogram_baidumap.bean.Bus_plan;

public class DataForApplication {
		public List<BusStation> stationList;
		public Bus_plan bus_plan;
	static	 double My_current_latitude;
		static double My_current_longitude;


	

		public static double getMy_current_latitude() {
			return My_current_latitude;
		}

		public static void setMy_current_latitude(double my_current_latitude) {
			My_current_latitude = my_current_latitude;
		}

		public static double getMy_current_longitude() {
			return My_current_longitude;
		}

		public static void setMy_current_longitude(double my_current_longitude) {
			My_current_longitude = my_current_longitude;
		}

		public Bus_plan getBus_plan() {
			return bus_plan;
		}

		public void setBus_plan(Bus_plan bus_plan) {
			this.bus_plan = bus_plan;
		}

		public List<BusStation> getStationList() {
			return stationList;
		}

		public void setStationList(List<BusStation> stationList) {
			this.stationList = stationList;
		}
		
		
		
}
