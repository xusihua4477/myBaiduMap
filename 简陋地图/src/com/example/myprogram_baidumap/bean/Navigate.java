package com.example.myprogram_baidumap.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "navigate")
public class Navigate {

	int id;
	@Column(column = "placeFrom")
	String placeFrom;
	@Column(column = "placeTo")
	String placeTo;

	@Column(column = "description")
	String description;

	public Navigate(int id, String placeFrom, String placeTo, String description) {
		super();
		this.id = id;
		this.placeFrom = placeFrom;
		this.placeTo = placeTo;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlaceFrom() {
		return placeFrom;
	}

	public void setPlaceFrom(String placeFrom) {
		this.placeFrom = placeFrom;
	}

	public String getPlaceTo() {
		return placeTo;
	}

	public void setPlaceTo(String placeTo) {
		this.placeTo = placeTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Navigate() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		StringBuffer sb2=new StringBuffer();
			
			sb2.append("Form:"+this.getPlaceFrom()+"To:"+this.getPlaceTo()).append("\n")
			
			.append(this.getDescription()+"  ")
			.append("\n");
		return sb2.toString();
	}
	
	
	
}
