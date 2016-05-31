package com.example.myprogram_baidumap.utils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.fragment.FragmentNavigation;

public class AdapterForRouteBusPlan  extends BaseAdapter{
	Context context;
	List<String>list;
	FragmentNavigation fragmentNavigation;

	public AdapterForRouteBusPlan(Context context, List<String> list,
			FragmentNavigation fragmentNavigation) {
		super();
		this.context = context;
		this.list = list;
		this.fragmentNavigation = fragmentNavigation;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (v==null) {
			v=View.inflate(context, R.layout.adapter_for_bus_plan, null);
			Holer holer=new Holer();
			holer.tvItem_luxian= (TextView)	v.findViewById(R.id.tv_for_adapterBus_Plan);
		   v.setTag(holer);
		}
		LinearLayout llauout_navigite_btns = (LinearLayout) v.findViewById(R.id.llauout_navigite_btns);
		for (int i = 0; i < llauout_navigite_btns.getChildCount(); i++) {
			View button = llauout_navigite_btns.getChildAt(i);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					
					InterFaceForBusPlan interFaceForBusPlan;
					interFaceForBusPlan= fragmentNavigation ;
					int button_position = Integer.parseInt(view.getTag().toString()) ;
					interFaceForBusPlan.SetButtons_Onclick(position, button_position);
					
				}
			});
		}
		
		Holer holder= (Holer) v.getTag();
		holder.tvItem_luxian.setText(list.get(position));
		return v;
	}

	class Holer{
		TextView tvItem_luxian;
		
	}

	

}
