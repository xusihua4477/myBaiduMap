 package com.example.myprogram_baidumap.bean;

import java.util.List;

import com.baidu.mapapi.search.route.TransitRouteLine;

public class Bus_plan {
	String from,to;
	 List<TransitRouteLine> routeLines_list;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public List<TransitRouteLine> getRouteLines_list() {
		return routeLines_list;
	}
	public void setRouteLines_list(List<TransitRouteLine> routeLines_list) {
		this.routeLines_list = routeLines_list;
	}
	
}
