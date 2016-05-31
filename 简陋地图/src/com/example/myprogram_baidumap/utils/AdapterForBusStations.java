package com.example.myprogram_baidumap.utils;

import java.util.List;

import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.example.myprogram_baidumap.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterForBusStations extends BaseAdapter{
	Context context;
	List<BusStation> list;
	
	public AdapterForBusStations(Context context, List<BusStation> list) {
		super();
		this.context = context;
		this.list = list;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public BusStation getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		Holder holder = null;
		if (v== null) {
			 v = View.inflate(context, R.layout.item_for_bus_stations, null);
			holder=new Holder();
			holder.id=(TextView) v.findViewById(R.id.listview_item_number);
			holder.title=(TextView) v.findViewById(R.id.listview_item_title);
			v.setTag(holder);	
		}else{
			holder = (Holder) v.getTag();
		}
		BusStation busStation = list.get(position);
		holder.id.setText(position+1+"");
		holder.title.setText(busStation.getTitle());
		
		return v;
	}
	class Holder{
		TextView id;
		TextView title;
		
	}
}
