/**
 * 
 */
package com.example.myprogram_baidumap.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.myprogram_baidumap.Application_Map;
import com.example.myprogram_baidumap.MainActivity;
import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.bean.Shop;
import com.example.myprogram_baidumap.utils.Contants;
import com.example.myprogram_baidumap.utils.NetConnectionUtil;
import com.example.myprogram_baidumap.view.Advertisement;

/**
 * @author Administrator
 * 
 */

public class FragmentResearch extends Fragment implements OnClickListener{
	/**
	 * ���ؼ����Զ���ؼ���
	 */
	Advertisement adv;
	MainActivity mainActivity;
	LocationClient mLocationClient;
	double latitude;
	double longitude;
	EditText et_research_research;
	MapView mapView_resourch;
	Button btn_research_research;
	LinearLayout llayoutForshowMap;
	public TextView tvShopName;
	public TextView tvShopTel;
	public TextView tvShopAdress;
	public TextView tvShopInternet;
	BaiduMap mapForResourch;
	PoiSearch poiSearch;

	private Shop shop;
	public LinearLayout shopMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(mainActivity.getApplicationContext());

		View view = View
				.inflate(mainActivity, R.layout.fragment_research, null);
		poiSearch = PoiSearch.newInstance();
		setOnGetPoiSearchResultListener();
		
		return view;

	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		
	
		shopMenu = (LinearLayout) view.findViewById(R.id.shopMenu);
		tvShopName = (TextView) view.findViewById(R.id.tvShopName);
		tvShopTel = (TextView) view.findViewById(R.id.tvShopTel);
		tvShopTel.setOnClickListener(this);
		tvShopAdress = (TextView) view.findViewById(R.id.tvShopAdress);
		tvShopInternet = (TextView) view.findViewById(R.id.tvShopInternet);
		tvShopTel.setOnClickListener(this);
		tvShopAdress.setOnClickListener(this);
		tvShopInternet.setOnClickListener(this);
		et_research_research = (EditText) view
				.findViewById(R.id.et_resourch_research);
		btn_research_research = (Button) view
				.findViewById(R.id.btn_research_research);
		mapView_resourch = (MapView) view
				.findViewById(R.id.mapView_resourchFragment_resourch);
		llayoutForshowMap = (LinearLayout) view
				.findViewById(R.id.llayout_resourch_forshowMap);

		mapForResourch = mapView_resourch.getMap();
		setadvtisement(view);
		setListener();
	}

	private void setadvtisement(View v) {
		// TODO Auto-generated method stub
		adv = (Advertisement) v.findViewById(R.id.adv);
		adv.setImgs(Contants.Adv_imgs1);
		adv.setUrls(Contants.Adv_urls1);
		
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		mapForResourch.setOnMapTouchListener(new OnMapTouchListener() {
			
			@Override
			public void onTouch(MotionEvent event) {
				// TODO Auto-generated method stub
				shopMenu.setVisibility(View.GONE);
			}
		});
		setonBtn_research_research();
	}

	

	public void setonBtn_research_research() {
		btn_research_research.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int netStatus = NetConnectionUtil.getNetStatus(mainActivity);
				if (netStatus == 0) {
					Toast.makeText(mainActivity, "û�����������粻ͨ��", 2000).show();

					return;
				}
				mainActivity.showProgressDialog("������");
				getMyLocation();
				// ����һ���ܱ߼���

				LatLng latLng = new LatLng(latitude, longitude);
				poiSearch.searchNearby(new PoiNearbySearchOption()
						.keyword(et_research_research.getText().toString())

						.location(latLng).pageNum(1).pageCapacity(40)
						.radius(1000 * 10));

			}
		});
	}

	public void setOnGetPoiSearchResultListener() {
		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {


					@Override
					public void onGetPoiResult(PoiResult poiResult) {
						// TODO Auto-generated method stub
						mainActivity.hideProgressDialog();
						
						if (poiResult.error != PoiResult.ERRORNO.NO_ERROR) {
							return;
						}
						// �������뷨
						InputMethodManager imm = (InputMethodManager) mainActivity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						// ��ʾ�����������뷨
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						// 1)����Poioverlay�ĸ�����
						CustomPoiOverlay overlay = new CustomPoiOverlay(
								mapForResourch);
						// 2)�������
						overlay.setData(poiResult);
						// 3)��������ӵ���ͼ��
						overlay.addToMap();
						// 4)��������
						overlay.zoomToSpan();
						// #1 ��ÿһ��poioverlay���marker��click�¼�
						mapForResourch.setOnMarkerClickListener(overlay);
					}

					@Override
					public void onGetPoiDetailResult(
							PoiDetailResult poiDetailResult) {
						// TODO Auto-generated method stub


						if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {// �����ɹ�
							String address = poiDetailResult.getAddress();
							double price = poiDetailResult.getPrice();
							String name = poiDetailResult.getName();
							String time = poiDetailResult.getShopHours();
							String url = poiDetailResult.getDetailUrl();
							LatLng point = poiDetailResult.getLocation();
							String location = "γ��:" + point.latitude + " ����:"
									+ point.longitude;
							String tel = poiDetailResult.getTelephone();
							String message = "����:" + name + "\n" + "���ڵص�:"
									+ address + "\n" + "��ַ:" + url + "\n"
									+ "Ӫҵʱ��:" + time + "\n" + "�˾�����:" + price
									+ "\n" + "�绰:" + tel + "\n" + "���ڵ�ַλ��:"
									+ location;

							String[] tels = tel.split(",");
							;
							shop = new Shop(name, tels, address, url);
							showShop();
						}
					}
				});
	}

	protected void showShop() {
		// TODO Auto-generated method stub
		if (shopMenu.getVisibility()==View.GONE) {
			shopMenu.setVisibility(View.VISIBLE);
		}
		tvShopName.setText(shop.getName()+"");
		tvShopAdress.setText(shop.getAdress()+"");
		tvShopInternet.setText(shop.getUrl()+"");
		String[] tel = shop.getTel();
		tvShopTel.setText(tel[0]);
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		SDKInitializer.initialize(mainActivity.getApplicationContext());
		initView(getView());
		
		getMyLocation();
	}

	private void getMyLocation() {
		// TODO Auto-generated method stub
		int netStatus = NetConnectionUtil.getNetStatus(mainActivity);
		if (netStatus == 0) {
			Toast.makeText(mainActivity, "û�����������粻ͨ��", 2000).show();

			return;
		}
		mLocationClient = new LocationClient(
				mainActivity.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// ������������
		option.setCoorType("bd09ll");
		// �����Ƿ���Ҫ��ַ��Ϣ��Ĭ��Ϊ�޵�ַ
		option.setIsNeedAddress(true);
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		// �����綨λʱ���Ƿ���Ҫ�豸����
		option.setNeedDeviceDirect(true);
		// ����ɨ��������λ�Ǻ���,30�����һ��
		option.setScanSpan(30000);

		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				// TODO Auto-generated method stub
				if (bdLocation == null) {
					Toast.makeText(mainActivity, "��Ǹδ�ܼ���������", 3000).show();
					return;
				}

				longitude = bdLocation.getLongitude();
				latitude = bdLocation.getLatitude();
				String street = bdLocation.getStreet();
				Log.i("main", "��λ��õ�street" + street);
				Application_Map instance = Application_Map.getInstance();
				instance.data_forApplication.setMy_current_latitude(latitude);
				instance.data_forApplication.setMy_current_longitude(longitude);
			}
		});

		//
		// ��ʼ��λ����
		mLocationClient.start();
		// ����λ���첽���أ������locationListener�л�ȡ.
		mLocationClient.requestLocation();
	}

	class CustomPoiOverlay extends PoiOverlay {

		public CustomPoiOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		// #2
		@Override
		public boolean onPoiClick(int index) {
			// Toast.makeText(MainActivity.this, "ѡ�е�������:"+index, 1).show();
			// ������������Ķ���
			PoiInfo info = getPoiResult().getAllPoi().get(index);
			poiSearch.searchPoiDetail(new PoiDetailSearchOption()
					.poiUid(info.uid));
			return true;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		  Intent intent;
		switch (id) {
		case R.id.tvShopAdress:
			
			break;
		case R.id.tvShopInternet:
			Uri uri = Uri.parse(shop.getUrl());  
           intent = new Intent();  
          intent.setAction(Intent.ACTION_VIEW);  
          intent.setData(uri);  
       startActivity(intent);  
			break;
		case R.id.tvShopTel:
			String[] tels = shop.getTel();
			 String tel = tels[0].replaceFirst("-", "");
			  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+
					  tel));
			 startActivity(intent);
			 System.out.println("tvShopTel");
			 System.out.println("tvShopTel");
			break;

		default:
			break;
		}
	}
}
