package com.example.myprogram_baidumap.bean;

public class Shop {
	String name;
	String tel[];
	String adress;
	String url;
	double latitude;
	double longitude;

	public Shop() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Shop(String name, String[] tel, String adress, String url) {
		super();
		this.name = name;
		this.tel = tel;
		this.adress = adress;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTel() {
		return tel;
	}

	public void setTel(String[] tel) {
		this.tel = tel;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
