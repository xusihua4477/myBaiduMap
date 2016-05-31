package com.example.myprogram_baidumap.fragment;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.myprogram_baidumap.Application_Map;
import com.example.myprogram_baidumap.MainActivity;
import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.ShowPointsOnActivity;
import com.example.myprogram_baidumap.bean.Navigate;
import com.example.myprogram_baidumap.utils.AdapterForRouteBusPlan;
import com.example.myprogram_baidumap.utils.AdapterForShowShouCang;
import com.example.myprogram_baidumap.utils.InterFaceForBusPlan;
import com.example.myprogram_baidumap.utils.NetConnectionUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnItemSelected;

public class FragmentNavigation extends Fragment implements InterFaceForBusPlan {
	static final int BY_BUS = 1;
	static final int BY_TAXI = 2;
	MainActivity mainActivity;
	Boolean autoTvChufadiIsCompelte=false;
	Boolean autoTvMudidiIsCompelte=false;
	MapView mapView_nagigate;
	BaiduMap baidumap_forNavigate;
	LocationClient mLocationClient;
	Button btn_navigate_search, btn_navigate_exchange,
			btn_navigate_autoLocation;
	TextView tv_navigate_show;
	EditText et_navigate_mudidi;
	BDLocation bdlocation_chufadi;
	AutoCompleteTextView autotv_navigate_mudi_position,
			autoTv_navigate_chufadi;
	PoiSearch poiSearch;
	SuggestionSearch suggestionSearch;
	EditText et_navigate_midi_city;
	ArrayAdapter<String> adapter;
	RadioButton rdButton_byBus;

	RoutePlanSearch routePlanSearch;
	ListView lvForAdapterBusplan;
	LinearLayout lLayoutForNivigate_baiduMap;
	/** 出发地的radioGroup */
	RadioGroup rg_chufa;
	/** 出发地的radioButton(自动定位当前位置) */
	RadioButton rd_chufa_auto;
	/** 出发地的radioButton(自己输入位置) */
	RadioButton rd_chufa_cutomer;

	/** 根据用户输入的目的地的文字而产生的搜索检验列表 */
	List<SuggestionInfo> suggestions_listForMudidi;
	List<SuggestionInfo> suggestions_listForChufadi;

	ListView lvForChufadiAdapter;
	ListView lvForMudidiAdapter;
	boolean isAutoLocation;
	/** 保存着搜索得到的公交方案 */
	List<TransitRouteLine> routeLines_list;
	/** 保存处理好了的公交方案以便适配器调用 */
	ArrayList<String> listForbusPlan;
	ArrayList<String> listForCarPlan;
	Button btn_navigate_return;
	LinearLayout ll_navigate_hideForMapRoom;

	/** 保存着搜索得到的所有驾车方案 */
	List<DrivingRouteLine> driverRouteLines;
	RadioButton rdButton_byCar;
	protected SuggestionInfo suggestionInfo_userChoosenChuFadi;
	protected SuggestionInfo suggestionInfo_userChoosenMudidi;
	static int Traffic_tools_type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(mainActivity.getApplicationContext());
		View view = View.inflate(mainActivity, R.layout.fragment_navigation,
				null);
	System.out.println("FragmentNavigation onCreateView");
	System.out.println("FragmentNavigation onCreateView");
	System.out.println("FragmentNavigation onCreateView");
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
System.out.println("navigteFragment onAttach");
System.out.println("navigteFragment onAttach");
System.out.println("navigteFragment onAttach");
		mainActivity = (MainActivity) activity;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		System.out.println("navigteFragment onActivityCreated");
		System.out.println("navigteFragment onActivityCreated");
		System.out.println("navigteFragment onActivityCreated");
		initView(view);

		//

	}
@Override
public void onHiddenChanged(boolean hidden) {
	// TODO Auto-generated method stub
	super.onHiddenChanged(hidden);
	System.out.println("navigteFragment onActivityCreated");
	System.out.println("navigteFragment onActivityCreated");
	System.out.println("navigteFragment onActivityCreated");
	
	
}



	private void initView(View view) {
		// TODO Auto-generated method stub
		initAboutMap(view);
		lvForChufadiAdapter = (ListView) view
				.findViewById(R.id.lvForChufadiAdapter);
		lvForChufadiAdapter.setVisibility(View.GONE);
		lvForMudidiAdapter = (ListView) view
				.findViewById(R.id.lvForMudididiAdapter);
		lvForMudidiAdapter.setVisibility(View.GONE);
		
		btn_navigate_autoLocation = (Button) view
				.findViewById(R.id.btn_nagigate_autoSearch);
		// btn_navigate_exchange = (Button)
		// view.findViewById(R.id.btn_exchange_position);
		btn_navigate_search = (Button) view
				.findViewById(R.id.btn_nagigate_search);
		tv_navigate_show = (TextView) view.findViewById(R.id.tv_nagigate_show);

		et_navigate_midi_city = (EditText) view
				.findViewById(R.id.et_navigate_midi_city);
		autoTv_navigate_chufadi = (AutoCompleteTextView) view
				.findViewById(R.id.et_navigate_chufadi);
		rdButton_byBus = (RadioButton) view
				.findViewById(R.id.radio_button_bytakeBUS);
		rdButton_byCar = (RadioButton) view
				.findViewById(R.id.radio_button_bytakeTaxi);
		suggestions_listForChufadi = new ArrayList<SuggestionResult.SuggestionInfo>();
		lvForAdapterBusplan = (ListView) view
				.findViewById(R.id.lvForApaterBusPlan);
		lLayoutForNivigate_baiduMap = (LinearLayout) view
				.findViewById(R.id.lLayoutForNivigate_baiduMap);
		btn_navigate_return = (Button) view
				.findViewById(R.id.btn_navigate_return);
		ll_navigate_hideForMapRoom = (LinearLayout) view
				.findViewById(R.id.ll_navigate_hideForMapRoom);
		initAutotv(view);
		setRadioButtons(view);
		/** 设置默认焦点 */
		setFocus();
		setonChufadiTextviewFocusChange();
		setonMudidiTextviewFocusChange();
		setListener();
	}

	public void btn_navigate_re_resourch(View view) {

		autoTv_navigate_chufadi.setText("");
		autotv_navigate_mudi_position.setText("");

	}

	private void setRadioButtons(View view) {
		// TODO Auto-generated method stub
		rg_chufa = (RadioGroup) view.findViewById(R.id.radioGroupForChufadi);
		rd_chufa_auto = (RadioButton) view
				.findViewById(R.id.rd_chufa_choose_dangqian);
		rd_chufa_cutomer = (RadioButton) view
				.findViewById(R.id.rd_chufa_choose_customer);
	}

	private void setFocus() {
		// TODO Auto-generated method stub
		rdButton_byBus.setFocusable(true);
		rdButton_byBus.setFocusableInTouchMode(true);
		rdButton_byBus.requestFocus();
		rdButton_byBus.requestFocusFromTouch();

	}

	private void initAutotv(View view) {
		// TODO Auto-generated method stub
		autotv_navigate_mudi_position = (AutoCompleteTextView) view
				.findViewById(R.id.autotv_navigate_mudi_position);
		autotv_navigate_mudi_position.setThreshold(2);
		int width2 = autotv_navigate_mudi_position.getWidth();
		autotv_navigate_mudi_position.setDropDownHeight(300);
		autotv_navigate_mudi_position.setDropDownWidth(width2 - 20);

		autoTv_navigate_chufadi.setThreshold(2);
		autoTv_navigate_chufadi.setDropDownHeight(500);
		int width = autoTv_navigate_chufadi.getWidth();
		autoTv_navigate_chufadi.setDropDownWidth(width - 20);
	}

	private void initAboutMap(View view) {
		// TODO Auto-generated method stub

		mapView_nagigate = (MapView) view.findViewById(R.id.mapView_nagigate);
		baidumap_forNavigate = mapView_nagigate.getMap();
		suggestionSearch = SuggestionSearch.newInstance();
		routePlanSearch = RoutePlanSearch.newInstance();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		seton_btn_navigate_autoLocation();
		seton_btn_navigate_search();
		setonAutotv_navigate_mudi_position_textchange();
		setonSuggestionSearch_result();
		setonChufadiRadioGroup_CheckedChangeListener();
		setonAutoTv_chufadi_textchange_listener();
		setonRoutePlanSearch_GetResult_listener();
		seton_btn_navigate_return_onclicklistener();

		setonlvForMudidiAdapter_itemListener();
		setonlvForChufadiAdapter_itemListener();
	}

	private void setonlvForChufadiAdapter_itemListener() {
		// TODO Auto-generated method stub
		lvForChufadiAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				suggestionInfo_userChoosenChuFadi = suggestions_listForChufadi
						.get(position);
				String key = suggestionInfo_userChoosenChuFadi.key;
				autoTv_navigate_chufadi.setText(key);
				autoTvChufadiIsCompelte = true;
				lvForChufadiAdapter.setVisibility(View.GONE);
				lvForChufadiAdapter.setAdapter(null);
				changeFocusOnResearchButton();
			}
		});
	}

	protected void changeFocusOnResearchButton() {
		// TODO Auto-generated method stub
 btn_navigate_search.requestFocus();
	}

	
	private void setonlvForMudidiAdapter_itemListener() {
		lvForMudidiAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				suggestionInfo_userChoosenMudidi = suggestions_listForMudidi
						.get(position);
				String key = suggestionInfo_userChoosenMudidi.key;
				autotv_navigate_mudi_position.setText(key);
				autoTvMudidiIsCompelte = false;
				lvForMudidiAdapter.setVisibility(View.GONE);

			}

		});
	}

	public void seton_btn_navigate_return_onclicklistener() {
		btn_navigate_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lLayoutForNivigate_baiduMap.setVisibility(View.GONE);
				lvForAdapterBusplan.setVisibility(View.VISIBLE);
				ll_navigate_hideForMapRoom.setVisibility(View.VISIBLE);
			}
		});
	}

	public void setonRoutePlanSearch_GetResult_listener() {
		routePlanSearch
				.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

					@Override
					public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

					}

					@Override
					public void onGetTransitRouteResult(
							TransitRouteResult transitRouteResult) {
						// TODO Auto-generated method stub
						mainActivity.hideProgressDialog();
					
						
						if (rdButton_byCar.isChecked()) {
							Traffic_tools_type = BY_TAXI;
							return;
						}

						if (transitRouteResult == null
								|| transitRouteResult.getRouteLines() == null) {
							tv_navigate_show.setText("检索失败,请更换搜索内容再试!");
							return;
						}

						StringBuffer sb = null;

						routeLines_list = transitRouteResult.getRouteLines();
						// 遍历每一个公交方案
						listForbusPlan = new ArrayList<String>();

						for (int i = 0; i < routeLines_list.size(); i++) {
							sb = new StringBuffer();
							TransitRouteLine transitRouteLine = routeLines_list
									.get(i);
							sb.append("方案" + (i + 1) + ":");
							int distance = transitRouteLine.getDistance();
							sb.append("距离" + distance / 1000f + "千米");

							// String start =
							// transitRouteLine.getStarting().getTitle();
							// sb.append("方案1transitRouteLine.getStarting()"+start).append("总行程:"+distance)
							List<TransitStep> Step_list = transitRouteLine
									.getAllStep();
							sb.append("需耗时" + transitRouteLine.getDuration()
									/ 60 + "分钟");
							sb.append("\n");
							for (int y = 0; y < Step_list.size(); y++) {
								TransitStep step = Step_list.get(y);

								// sb.append(step.getEntrace().getTitle()+"出发");
								String instructions = step.getInstructions();

								sb.append((y + 1) + ".").append(instructions);
								sb.append("\n");

							}
							listForbusPlan.add(sb.toString());
						}

						SetAdapterForBusPlan(listForbusPlan, routeLines_list);

					}

					@Override
					public void onGetDrivingRouteResult(
							DrivingRouteResult drivingRouteResult) {
						// TODO Auto-generated method stub
						mainActivity.hideProgressDialog();
						if (rdButton_byBus.isChecked()) {
							Traffic_tools_type = BY_BUS;
							return;
						}
						if (drivingRouteResult == null
								|| drivingRouteResult.getRouteLines() == null) {
							autotv_navigate_mudi_position
									.setText("检索失败,请更换搜索内容再试!");
							return;
						}
						if (drivingRouteResult.getRouteLines().size() == 0) {

							Toast.makeText(mainActivity,
									"检索失败,输入地址不够精准请更换搜索内容再试", 3000).show();
							autotv_navigate_mudi_position.setText("");
							autoTv_navigate_chufadi.setText("");
							autoTvChufadiIsCompelte = false;
							autoTvMudidiIsCompelte = false;
							return;
						}
						StringBuffer sb = new StringBuffer();
						listForbusPlan = new ArrayList<String>();
						driverRouteLines = drivingRouteResult.getRouteLines();
						for (int i = 0; i < driverRouteLines.size(); i++) {
							DrivingRouteLine drivingRouteLine = driverRouteLines
									.get(i);
							sb.append("方案" + (i + 1) + ":");
							int distance = drivingRouteLine.getDistance();
							sb.append("距离" + distance / 1000f + "千米");
							List<DrivingStep> Step_list = drivingRouteLine
									.getAllStep();
							sb.append("需耗时" + drivingRouteLine.getDuration()
									/ 60);
							sb.append("\n");
							for (int y = 0; y < Step_list.size(); y++) {
								DrivingStep step = Step_list.get(y);

								// sb.append(step.getEntrace().getTitle()+"出发");
								String instructions = step.getInstructions();

								sb.append((y + 1) + ".").append(instructions);
								sb.append("\n");

							}
							listForbusPlan.add(sb.toString());
						}

						SetAdapterForBusPlan(listForbusPlan, routeLines_list);

					}
				});
	}

	public void SetAdapterForBusPlan(ArrayList<String> listForbusPlan,
			List<TransitRouteLine> routeLines_list) {
		// TODO Auto-generated method stub

		AdapterForRouteBusPlan adapterForRouteBusPlan = new AdapterForRouteBusPlan(
				mainActivity, listForbusPlan, this);
		lvForAdapterBusplan.setAdapter(adapterForRouteBusPlan);
		adapterForRouteBusPlan.notifyDataSetChanged();
	}

	public void setonAutoTv_chufadi_textchange_listener() {
		
		autoTv_navigate_chufadi.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
		/*		if (s == null || s == "") {

					return;

				}*/
				if (NetConnectionUtil.getNetStatus(mainActivity) == NetConnectionUtil.NET_NO_CONNECTION) {
					Toast.makeText(mainActivity, "网络无连接", 3000).show();
					autoTv_navigate_chufadi.setText("");
				}
				
	
				
			
				
					lvForChufadiAdapter.setVisibility(View.VISIBLE);
					autoTvChufadiIsCompelte=false;
	
			

				if (rd_chufa_auto.isChecked()) {
					return;
				}
				// 次该用户选择了手动输入
			

			
				suggestionSearch.requestSuggestion(new SuggestionSearchOption()

				.city("中国").keyword(s.toString()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setonChufadiRadioGroup_CheckedChangeListener() {
		// TODO Auto-generated method stub
		rg_chufa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (rd_chufa_auto.getId() == checkedId) {
					int netStatus = NetConnectionUtil
							.getNetStatus(mainActivity);
					if (netStatus == 0) {
						Toast.makeText(mainActivity, "没有联网或网络不通畅", 2000).show();
						rd_chufa_auto.setChecked(false);
						rd_chufa_cutomer.setChecked(false);
						return;
					}
		
					getMyLocation();
					isAutoLocation = true;
					return;
				}

				if (rd_chufa_cutomer.getId() == checkedId) {
					autoTv_navigate_chufadi.setText("");

					isAutoLocation = false;
				}

			}
		});
	}

	private void setonSuggestionSearch_result() {
		// TODO Auto-generated method stub
		suggestionSearch
				.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {

					@Override
					public void onGetSuggestionResult(SuggestionResult results) {
						// TODO Auto-generated method stub
						
						if (results == null) {
							System.out.println("onGetSuggestionResult 为null");
							return;
						}

						if (results.getAllSuggestions() == null) {
						if ( ! isAutoLocation) {
						}
					
							return;
						}

						List<SuggestionInfo> allSuggestions = results
								.getAllSuggestions();

						List<String> list = new ArrayList<String>();

						List<SuggestionInfo> suggestions_list = results
								.getAllSuggestions();
						for (int i = 0; i < suggestions_list.size(); i++) {

							SuggestionInfo info = suggestions_list.get(i);

							list.add(info.city + info.key);
						}
					
						adapter = new ArrayAdapter<String>(mainActivity,
								android.R.layout.simple_dropdown_item_1line,
								list);
						// 判断用户是在出发地还是在目的地的文本框输入内容
						// 是那个文本框就给那个设置适配器
						if (autoTv_navigate_chufadi.isFocused()) {
							Log.i("main",
									"autoTv_navigate_chufadi.isActivated()");
							lvForChufadiAdapter.setAdapter(adapter);
							// 默认suggestions给了目的地;此刻焦点在出发地文本框上,说明应该将数据存到出发地的list中
							suggestions_listForChufadi = suggestions_list;
						} else {
							suggestions_listForMudidi = suggestions_list;
							lvForMudidiAdapter.setAdapter(adapter);
						}

						adapter.notifyDataSetChanged();
					}
				});
	}

	public void setonAutotv_navigate_mudi_position_textchange() {
		autotv_navigate_mudi_position.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
	/*			if (s == "" || s == null) {
					Log.i("main", "s为空,退出TextChanged");
					return;
				}*/
	
					lvForMudidiAdapter.setVisibility(View.VISIBLE);
					
					autoTvMudidiIsCompelte=false;
			
				suggestionSearch.requestSuggestion(new SuggestionSearchOption()
						.city("中国").keyword(s.toString()));

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

	public void seton_btn_navigate_search() {
		btn_navigate_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.showProgressDialog("拼命加载中");
				startSearch();

			}
		});
	}

	protected void startSearch() {
		String city_mudi = null;
		String place_mudi = null;
		String city_chufa = null;
		String place_chufa = null;
		// TODO Auto-generated method stub
		/*
		 * String chufadi = autoTv_navigate_chufadi.getText().toString().trim();
		 * String mudidi = autotv_navigate_mudi_position.getText().toString()
		 * .trim();
		 * 
		 * 
		 * for (SuggestionInfo info : suggestions_listForMudidi) { if
		 * ((info.city + info.key).equals(mudidi)) {
		 * System.out.println("suggestions_listForMudidi is ok"); city_mudi =
		 * info.city; place_mudi = info.key;
		 * 
		 * break; } }
		 */

		if (isAutoLocation) {
			city_chufa = bdlocation_chufadi.getCity();
			place_chufa = bdlocation_chufadi.getAddrStr();
		} else {
			/*
			 * for (SuggestionInfo info : suggestions_listForChufadi) { if
			 * ((info.city + info.key).equals(chufadi)) { city_chufa =
			 * info.city; place_chufa = info.key;
			 * 
			 * break; }}
			 */
			city_chufa = suggestionInfo_userChoosenChuFadi.city;
			place_chufa = suggestionInfo_userChoosenChuFadi.key;

		}

		city_mudi = suggestionInfo_userChoosenMudidi.city;
		place_mudi = suggestionInfo_userChoosenMudidi.key;
		if (city_mudi == null || place_mudi == null) {
			autotv_navigate_mudi_position.setError("请从下拉列表中选择,不支持自定义");

			return;
		}
		if (city_chufa == null || place_chufa == null) {

			autoTv_navigate_chufadi.setError("请从下拉列表中选择或自动定位,不支持自定义");

			return;
		}
		Application_Map instance = Application_Map.getInstance();
		/*
		 * instance.data_forApplication.bus_plan.setFrom(city_chufa+place_chufa);
		 * instance.data_forApplication.bus_plan.setTo(city_mudi+place_mudi);
		 */

		PlanNode planNode_from = PlanNode.withCityNameAndPlaceName(city_chufa,
				place_chufa);
		PlanNode planNode_to = PlanNode.withCityNameAndPlaceName(city_mudi,
				place_mudi);

		if (rdButton_byBus.isChecked()) {
			routePlanSearch.transitSearch(new TransitRoutePlanOption()
					.city(city_chufa).from(planNode_from).to(planNode_to));
		} else {
			routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(
					planNode_from).to(planNode_to));
		}

	}

	public void seton_btn_navigate_autoLocation() {
		btn_navigate_autoLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//	getMyLocation();

			}

		});
	}

	/** 调用定位功能,并在结果监听中把定位信息显示在出发地text中 */
	public void getMyLocation() {
		int netStatus = NetConnectionUtil.getNetStatus(mainActivity);
		if (netStatus == 0) {
			Toast.makeText(mainActivity, "没有联网或网络不通畅", 2000).show();
			autoTv_navigate_chufadi.setText("");
			rd_chufa_auto.setChecked(false);
			rd_chufa_cutomer.setChecked(false);
			return;
		}
		System.out.println("onclick");
		mLocationClient = new LocationClient(
				mainActivity.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// 设置坐标类型
		option.setCoorType("bd09ll");
		// 设置是否需要地址信息，默认为无地址
		option.setIsNeedAddress(true);
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		// 在网络定位时，是否需要设备方向
		option.setNeedDeviceDirect(true);
		// 设置扫描间隔，单位是毫秒
		// option.setScanSpan(5000);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new MyLocationListener());
		//
		// 开始定位功能
		mLocationClient.start();
		// 请求定位，异步返回，结果在locationListener中获取.
		mLocationClient.requestLocation();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdlocation) {
			if (bdlocation != null) {
				StringBuffer sb = new StringBuffer(256);

				sb.append("time : ");
				sb.append(bdlocation.getTime());
				sb.append("\nerror code : ");
				sb.append(bdlocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(bdlocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(bdlocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(bdlocation.getRadius());
				if (bdlocation.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nspeed : ");
					sb.append(bdlocation.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(bdlocation.getSatelliteNumber());
					sb.append("TypeGpsLocation");
				} else if (bdlocation.getLocType() == BDLocation.TypeNetWorkLocation) {
					sb.append("\naddr : " + "NetWorkLocation");
					sb.append(bdlocation.getAddrStr());
				}

				Log.i("main", sb.toString());
				tv_navigate_show.setText(sb.toString());
				bdlocation_chufadi = bdlocation;
				autoTv_navigate_chufadi.setText(bdlocation.getAddrStr());

			} else {
				Log.i("main", "bdlocation=null");
			}

		}
	}

	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView_nagigate.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView_nagigate.onPause();
		System.out.println("FragmentNavigation onPause");
		System.out.println("FragmentNavigation onPause");
		lvForChufadiAdapter.setVisibility(View.GONE);
		lvForMudidiAdapter.setVisibility(View.GONE);
		
	//	lvForAdapterBusplan.setVisibility(View.GONE);
	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("FragmentNavigation onDestroy");
		System.out.println("FragmentNavigation onDestroy");
		System.out.println("FragmentNavigation onDestroy");
		mapView_nagigate.onDestroy();
	}
	@Override
	public void SetButtons_Onclick(int Listview_positon, int button_positon) {
		// TODO Auto-generated method stub
		Log.i("main", "button_positon:" + button_positon);

		switch (button_positon) {
		// 单击了查看地图的按钮
		case 0:
			// 显示百度地图所在的布局
			if (rdButton_byBus.isChecked()) {
				Traffic_tools_type = BY_BUS;
			} else {
				Traffic_tools_type = BY_TAXI;

			}
			 
			if (Traffic_tools_type == BY_BUS) {
				/*TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(
						baidumap_forNavigate);*/
				TransitRouteLine	 transitRouteLine = routeLines_list
						.get(Listview_positon);
				Application_Map.transitRouteLine=transitRouteLine;
	/*
				transitRouteOverlay.setData(transitRouteLine);
				transitRouteOverlay.addToMap();
				transitRouteOverlay.zoomToSpan();
*/
			} else if (Traffic_tools_type == BY_TAXI) {
				DrivingRouteLine drivingRouteLine = driverRouteLines
						.get(Listview_positon);
				Application_Map.drivingRouteLine=drivingRouteLine;
			/*	DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						baidumap_forNavigate);
				drivingRouteOverlay.setData(driverRouteLines
						.get(Listview_positon));*/
				
			/*	Log.i("main",
						"drivingRouteLine.getTitle()"
								+ drivingRouteLine.getTitle());
				drivingRouteOverlay.addToMap();

				drivingRouteOverlay.zoomToSpan();*/
			}
			Intent i =new Intent(mainActivity, ShowPointsOnActivity.class);
			i.putExtra("Traffic_tools_type", BY_BUS);
			startActivity(i);
			
			/*lLayoutForNivigate_baiduMap.setVisibility(View.VISIBLE);
			// 隐藏listview;
			ll_navigate_hideForMapRoom.setVisibility(View.GONE);
			lvForAdapterBusplan.setVisibility(View.GONE);*/
			MapStatus mapStatus = baidumap_forNavigate.getMapStatus();
			String string = mapStatus.toString();

		
			
			
			// Toast.makeText(mainActivity, "mapStatus"+string, 3000);
			

			break;
		// 单击了短信收藏按钮,把当前路线方案发送到短信
		case 1:
			String plan = listForbusPlan.get(Listview_positon);

			Uri uri = Uri.parse("smsto:");
			Intent smsIntent = new Intent(Intent.ACTION_VIEW, uri);
			smsIntent.putExtra("sms_body", plan);
			smsIntent.setType("vnd.android-dir/mms-sms");

			mainActivity.startActivityForResult(smsIntent, 1002);
			break;
		case 2:
			//单击了保存按钮
			String plan1 = listForbusPlan.get(Listview_positon);
			String chufadi = autoTv_navigate_chufadi.getText().toString();
			String mudidi = autotv_navigate_mudi_position.getText().toString();
			savePlanOnDatabase(plan1, chufadi, mudidi);
			
			break;
		default:
			break;
		}
	}

	private void savePlanOnDatabase(String plan1, String chufadi, String mudidi) {
		DbUtils dbUtils=DbUtils.create(getActivity().getApplicationContext(),"daohang.db");
		
		try {
			dbUtils.createTableIfNotExist(Navigate.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Navigate niNavigate=new Navigate();
		niNavigate.setDescription(plan1);
		niNavigate.setPlaceFrom(chufadi);
		niNavigate.setPlaceTo(mudidi);
		Selector selector=Selector.from(Navigate.class).where(WhereBuilder.b("placeFrom", "=", chufadi))
				.and("description", "=", plan1);
		
		try {
		
			Navigate query = dbUtils.findFirst(selector);
			
			if (query!=null) {
				Toast.makeText(mainActivity, "该记录已经存在", 3000).show();
				return;
			}
			
			dbUtils.save(niNavigate);
			refreshShouCangFragmentData();
			Toast.makeText(mainActivity, "已经保存到收藏夹", 3000).show();
			return;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mainActivity, "查询失败", 3000).show();
			return;
		}
	}

	private void refreshShouCangFragmentData() {
		// TODO Auto-generated method stub
		MainActivity activity = (MainActivity) getActivity();
		FragmentFavourate fragment = (FragmentFavourate) activity.fragments[3];
		fragment.lvForShoucang.setAdapter(new AdapterForShowShouCang(activity));
	
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i("main", "FragmentNavigation onstop");
		super.onStop();
		System.out.println("FragmentNavigation onStop");
		System.out.println("FragmentNavigation onStop");
		System.out.println("FragmentNavigation onStop");
	}

	private void setonChufadiTextviewFocusChange() {
		autoTv_navigate_chufadi
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							Log.i("main", "ChufadiTextview获得焦点");
						}
						
						if (hasFocus && autoTvChufadiIsCompelte==false) {
							Log.i("main", "hasFocus");
							lvForChufadiAdapter.setVisibility(View.VISIBLE);
						}

					}
				});
	}

	private void setonMudidiTextviewFocusChange() {
		// TODO Auto-generated method stub
		autotv_navigate_mudi_position
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						lvForChufadiAdapter.setVisibility(View.GONE);
						if (hasFocus  && autoTvMudidiIsCompelte==false) {
							Log.i("main", "hasFocus");
							if (adapter!=null) {
								adapter.clear();
							}
							//lvForMudidiAdapter.setVisibility(View.VISIBLE);
						}

					}
				});
	}

}
