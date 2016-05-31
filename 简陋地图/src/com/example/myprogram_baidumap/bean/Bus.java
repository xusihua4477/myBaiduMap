package com.example.myprogram_baidumap.bean;

import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="bus")
public class Bus {
	int id;
	@Column(column="name")
	String name;
	@Column(column="time")
	String time;
	@Column(column="busStation")
	String busStation;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBusStation() {
		return busStation;
	}
	public void setBusStation(String busStation) {
		this.busStation = busStation;
	}
	public Bus(int id, String name, String time, String busStation) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.busStation = busStation;
	}
	public Bus() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {

		StringBuffer sb=new StringBuffer();
		sb.append(this.getName()).append("\n")
		
		.append(this.getTime()+"  ").append(this.getBusStation())
		.append("\n").append("\n");
		return  sb.toString();
	}
	
	
	
	
}
