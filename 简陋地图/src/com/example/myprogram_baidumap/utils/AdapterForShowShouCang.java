package com.example.myprogram_baidumap.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.bean.Bus;
import com.example.myprogram_baidumap.bean.Navigate;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class AdapterForShowShouCang extends BaseAdapter {

	Context context;

	List<Navigate> lists_navigate;
	List<Bus> lists_bus;
	List<String> lists;
	public OnShowMePublished_listener item_onclickLister;

	public interface OnShowMePublished_listener {
		public void btn_delete(String objectid, int position);

	}

	public AdapterForShowShouCang(Context context) {
		super();
		this.context = context;
		lists=new ArrayList<String>();
		setDataForBus();
		setDataForNavigate();
	}

	public AdapterForShowShouCang(Context context, List<Navigate> lists,
			OnShowMePublished_listener item_onclickLister) {
		super();
		this.context = context;
		this.item_onclickLister = item_onclickLister;
		setDataForBus();
		setDataForNavigate();

	}

	private void setDataForNavigate() {
		// TODO Auto-generated method stub

		DbUtils dbUtils_daohang = DbUtils.create(context, "daohang.db");
		try {
			lists_navigate = dbUtils_daohang.findAll(Navigate.class);
			if (lists_navigate==null || lists_navigate.size() <= 0) {
				Log.i("main", "没有导航数据");
				return;
			}

			for (Navigate navigate : lists_navigate) {
				lists.add(navigate.toString());
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
			
			
		}
	}

	private void setDataForBus() {
		// TODO Auto-generated method stub
		DbUtils dbUtils = DbUtils.create(context, "bus.db");
		try {
			dbUtils.createTableIfNotExist(Bus.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			lists_bus = dbUtils.findAll(Bus.class);
			if (lists_bus==null ||lists_bus.size() <= 0) {
				return;
			}
			for (Bus bus : lists_bus) {
				lists.add(bus.toString());

			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_showshoucang, null);
		TextView tv_contentForlv = (TextView) view
				.findViewById(R.id.tv_contentForlv);
		Button btn_del = (Button) view
				.findViewById(R.id.btn_del);
		
		tv_contentForlv.setText(lists.get(position));
		btn_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (lists_bus==null ||lists_bus.size()==0) {
					Navigate navigate = lists_navigate.get(position);
				  	delDateBasedata(navigate);
					lists.remove(position);
					lists_navigate.remove(position);
				
					AdapterForShowShouCang.this.notifyDataSetChanged();
				}else{
					
					if (position>lists_bus.size()) {
						delDateBasedata(lists_navigate.get(position-lists_bus.size()));
						lists_navigate.remove(position-lists_bus.size());
						lists.remove(position);
					} else {
						delDateBasedata(lists_bus.get(position));
						lists_bus.remove(position);

						lists.remove(position);
					}
					AdapterForShowShouCang.this.notifyDataSetChanged();
				}
			}
		});
		return view;
	}

	protected <T> void delDateBasedata(T t) {
		// TODO Auto-generated method stub
		
		if (t instanceof Navigate) {
			Navigate n=(Navigate)t;
			DbUtils dbUtils_daohang = DbUtils.create(context, "daohang.db");
			try {
				dbUtils_daohang.delete(n);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("本地数据库记录删除失败");
			}
		} else {
			
			Bus n=(Bus)t;
			DbUtils dbUtils_bus = DbUtils.create(context, "bus.db");
			try {
				dbUtils_bus.delete(n);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("本地数据库记录删除失败");
			}
		}
		
		
		
	}

}
