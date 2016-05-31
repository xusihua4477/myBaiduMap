package com.example.myprogram_baidumap.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.PoiInfo.POITYPE;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.myprogram_baidumap.MainActivity;
import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.bean.Bus;
import com.example.myprogram_baidumap.utils.AdapterForBusStations;
import com.example.myprogram_baidumap.utils.NetConnectionUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentBus extends Fragment {
	TextView tvBusNumber;
	TextView tv_bus_destination;
	TextView tv_bus_time;
	AdapterForBusStations adapterForBusStationsListview;
	SuggestionSearch suggestionSearch;
	EditText et_busSearch_City;
	AutoCompleteTextView autoET_busSearch_bus;
	View view_bus_layout;
	PoiSearch poiSearch;
	BusLineSearch busLineSearch;
	//RadioButton radio_button_listForbus;
	ListView listviewForBusStations;
	MainActivity mainActivity;
	ListView lvForBusChoosen;
	ArrayAdapter<String> adapterForBusChoosen;
	/** 存储着公交站点信息 */
	List<BusStation> stationList;
	/** 输入内容后根据内容搜索到的所有公交PoiInfo */
	List<PoiInfo> listForOptions;

	Button btn_save;
	private String time;
	private String busName;
	protected PoiInfo poiInfo_userChoose;
	private ProgressDialog pd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view_bus_layout = inflater.inflate(R.layout.fragment_bus, null);

		ViewUtils.inject(this, view_bus_layout);
	
		return view_bus_layout;

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mainActivity = (MainActivity) activity;
		pd = new ProgressDialog(mainActivity);
		pd.setCancelable(true);
	}
	public void hideProgressDialog() {
	
	pd.hide();
		
		
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initview();

	}

	private void setadapter(List<BusStation> station_List) {
		// TODO Auto-generated method stub
		adapterForBusStationsListview = new AdapterForBusStations(mainActivity,
				station_List);

		listviewForBusStations.setAdapter(adapterForBusStationsListview);

	}
@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	
}
	private void initview() {
		// TODO Auto-generated method stub
		View view = getView();
		btn_save = (Button) view.findViewById(R.id.btn_search_bus_save);
		et_busSearch_City = (EditText) view.findViewById(R.id.et_search_City);
		autoET_busSearch_bus = (AutoCompleteTextView) view
				.findViewById(R.id.autotv_search_bus);
	/*	radio_button_listForbus = (RadioButton) view
				.findViewById(R.id.radio_button_listForbus);*/
		lvForBusChoosen=(ListView) view.findViewById(R.id.lvForBusChoosenForBusFragment);
		listviewForBusStations = (ListView) view
				.findViewById(R.id.listViewForBusStations);
		tvBusNumber = (TextView) view.findViewById(R.id.tv_bus_number);
		tv_bus_destination = (TextView) view
				.findViewById(R.id.tv_bus_destination);
		tv_bus_time = (TextView) view.findViewById(R.id.tv_bus_time);
		poiSearch = PoiSearch.newInstance();
		busLineSearch = BusLineSearch.newInstance();
		listForOptions = new ArrayList<PoiInfo>();
		setonGetPoiResult();

		setBusLineResultListener();
		setListviewForBusStations();
		setAutoEditText();
		setBtnSaveListener();
		setonListForChoose_Item_click_Listener();
	}

	private void setListviewForBusStations() {
		// TODO Auto-generated method stub
		listviewForBusStations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		
		
		});
	}

	private void setonListForChoose_Item_click_Listener() {
		// TODO Auto-generated method stub
		lvForBusChoosen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 poiInfo_userChoose = listForOptions.get(position);
			autoET_busSearch_bus.setText(poiInfo_userChoose.name);
			lvForBusChoosen.setVisibility(View.GONE);

			if (NetConnectionUtil.getNetStatus(mainActivity) == NetConnectionUtil.NET_NO_CONNECTION) {
				Toast.makeText(mainActivity, "请连接网络", 3000).show();
				return;
			}
				showProgressDialog("搜索中");
			//	mainActivity.hideProgressDialog();
					busLineSearch.searchBusLine(new BusLineSearchOption()
							.city(poiInfo_userChoose.city)

							.uid(poiInfo_userChoose.uid));
					return;
				
			}
		});
	}

	private void setBtnSaveListener() {
		// TODO Auto-generated method stub
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (stationList == null) {
					Toast.makeText(mainActivity, "无内容可保存", 3000).show();
					return;
				}

				DbUtils dbUtils = DbUtils.create(getActivity()
						.getApplicationContext(), "bus.db");
				try {
					dbUtils.createTableIfNotExist(Bus.class);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				Bus bus = new Bus();
				bus.setName(busName);
				bus.setTime(time);
				StringBuffer sb=new StringBuffer();
				
				for (int i = 0; i < stationList.size(); i++) {
					BusStation busStation = stationList.get(i);
					sb.append((i+1)+"  ");
				sb.append(busStation.getTitle()).append(",");
				
				}
				
			String station = sb.toString();
			station=station.substring(0, station.lastIndexOf(","));
				bus.setBusStation(station);
				
				System.out.println(station);
		try {
			
			Selector selector=Selector.from(bus.getClass());
			selector.where(WhereBuilder.b("name", "=", bus.getName()));
			Bus bus2 = dbUtils.findFirst(selector);
			if (bus2!=null) {
				Toast.makeText(mainActivity, "该记录已经存在", 3000).show();
			}else{
			  
				
					dbUtils.save(bus);
					mainActivity.updateFavariteFragmentData();
					Toast.makeText(mainActivity, "保存成功", 3000).show();
					System.out.println("保存成功!");}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void setAutoEditText() {

		setAutoEditText_busNumberListener();
		// setsuggestionSearchListener();
	}

	/*
	 * private void setsuggestionSearchListener() { // TODO Auto-generated
	 * method stub suggestionSearch.setOnGetSuggestionResultListener(new
	 * OnGetSuggestionResultListener() {
	 * 
	 * @Override public void onGetSuggestionResult(SuggestionResult
	 * suggestionResult) { // TODO Auto-generated method stub if
	 * (suggestionResult!=null && suggestionResult.getAllSuggestions()!=null) {
	 * List<SuggestionInfo> allSuggestions_list =
	 * suggestionResult.getAllSuggestions(); for(SuggestionInfo
	 * suggestionInfo:allSuggestions_list) { String key = suggestionInfo.key;
	 * System.out.println("district:"+suggestionInfo.district);
	 * 
	 * //向适配器添加数据 adapterForAutoEText.add(key); }
	 * adapterForAutoEText.notifyDataSetChanged(); } } }); }
	 */
	private void setAutoEditText_busNumberListener() {
		// TODO Auto-generated method stub
		autoET_busSearch_bus.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (NetConnectionUtil.getNetStatus(mainActivity) == NetConnectionUtil.NET_NO_CONNECTION) {
					Toast.makeText(mainActivity, "请连接网络", 3000).show();

					return;
				}
			
					lvForBusChoosen.setVisibility(View.VISIBLE);
					
			
					
			
			
				
				poiSearch.searchInCity(new PoiCitySearchOption().city(
						et_busSearch_City.getText().toString()).keyword(
						s.toString()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void showProgressDialog(String message) {
	
		pd.setMessage(message);
		pd.show();
		
		
		
	}
	private void setBusLineResultListener() {
		// TODO Auto-generated method stub
		busLineSearch
				.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {

					@Override
					public void onGetBusLineResult(BusLineResult busLineResult) {
						// TODO Auto-generated method stub
						hideProgressDialog();
						if (busLineResult == null  ) {
							Toast.makeText(mainActivity, "onGetBusLineResult 没有查询到结果", 3000)
									.show();
							return;
						}
				
							/* 列表显示各个站点 */
							Log.i("main",
									"getBusLineName:"
											+ busLineResult.getBusLineName());
							setBusListTitile(busLineResult);
							stationList = busLineResult.getStations();
							setadapter(stationList);

						
					}
				});
	}

	public void setBusListTitile(BusLineResult busLineResult) {
		busName = busLineResult.getBusLineName();
		tvBusNumber.setText(busLineResult.getBusLineName());
		tv_bus_destination.setText("请注意发车时间和发车方向");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm");
		String start_time = simpleDateFormat.format(busLineResult
				.getStartTime());

		String end_time = simpleDateFormat.format(busLineResult.getEndTime());
		time = "首:" + start_time + "末:" + end_time;
		tv_bus_time.setText(time);
	}

	private void setonGetPoiResult() {
		// TODO Auto-generated method stub

		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult poiResult) {
						// TODO Auto-generated method stub
			/*			if (poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
							
							return;
						}*/
						hideProgressDialog();
						
						if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
							List<PoiInfo> poiInfo_list = poiResult.getAllPoi();
							
							if (poiInfo_list.size()<1) {
							/*	Toast.makeText(mainActivity, "搜索不到结果,换个关键字试试",3000).show();
								autoET_busSearch_bus.setText("");
								*/
								Log.i("main", "关键字不够精确,换个关键字试试");
								return;
							}
							
							adapterForBusChoosen = new ArrayAdapter<String>(
									mainActivity,
									android.R.layout.simple_dropdown_item_1line);
		
							listForOptions=new ArrayList<PoiInfo>();
							for (PoiInfo poiInfo : poiInfo_list) {

								if (poiInfo.type == POITYPE.BUS_LINE
										|| poiInfo.type == POITYPE.SUBWAY_LINE) {
									Log.i("main", "poiInfo.name:"
											+ poiInfo.name);
									adapterForBusChoosen.add(poiInfo.name);
									listForOptions.add(poiInfo);
								}
							}
						
						if (listForOptions.size()<1) {
							mainActivity.show_Toast("关键字不够精确,换个关键字试试");
							return;
						}
							lvForBusChoosen.setAdapter(adapterForBusChoosen);
						//	adapterForBusChoosen.notifyDataSetInvalidated();

						}
					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {
						// TODO Auto-generated method stub
						hideProgressDialog();
					}
				});
	}

	
}
