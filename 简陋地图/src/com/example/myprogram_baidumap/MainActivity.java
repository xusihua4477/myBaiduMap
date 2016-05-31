package com.example.myprogram_baidumap;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.example.myprogram_baidumap.fragment.FragmentBus;
import com.example.myprogram_baidumap.fragment.FragmentFavourate;
import com.example.myprogram_baidumap.fragment.FragmentNavigation;
import com.example.myprogram_baidumap.fragment.FragmentResearch;
import com.example.myprogram_baidumap.utils.AdapterForShowShouCang;
import com.example.myprogram_baidumap.utils.NetConnectionUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	@ViewInject(R.id.ll_menu)
	LinearLayout linearlayout_menu;
	@ViewInject(R.id.content_llayout)
	LinearLayout linearlayout_main;
	int PrePosition = 0;
	public Fragment[] fragments = new Fragment[4];
	FragmentManager fragmentManager;

public	 ProgressDialog pd;
	BaiduMap baiduMap;
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.main);
		CheckNetConnection();
		ViewUtils.inject(this);
		initData();
		initView();
		setMenuListener();
		/** 初始化首页面 */

	}

	private void initView() {
		// TODO Auto-generated method stub
		if (pd==null) {
			pd = new ProgressDialog(MainActivity.this);
			pd.setCancelable(true);
		}
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			beginTransaction.add(R.id.content_llayout, fragments[i],
					fragments[i].getTag()).hide(fragments[i]);

		}

		// beginTransaction.replace(R.id.content_llayout, fragments[0]);

		PrePosition = 0;
		/*
		 * beginTransaction.replace(R.id.content_llayout, fragments[0]);
		 */
		beginTransaction.show(fragments[0]);
		beginTransaction.commit();
	}

	/**
	 * 
	 */
	private void initData() {
		init_fragment();

	}

	public void showProgressDialog(String message) {
		
		
		pd.setMessage(message);
		pd.show();
		
		
		
	}
	public void show_Toast(String message) {
		
	Toast.makeText(this, message, 3000).show();
		
		
	}
	public void hideProgressDialog() {
	
	pd.hide();
		
		
		
	}
	private void init_fragment() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		fragments[0] = new FragmentNavigation();
		fragments[1] = new FragmentBus();
		fragments[2] = new FragmentResearch();
		fragments[3] = new FragmentFavourate();

	}

	/** 为主菜单设置监听 */

	private void setMenuListener() {
		// TODO Auto-generated method stub
		for (int i = 0; i < linearlayout_menu.getChildCount(); i++) {
			linearlayout_menu.getChildAt(i).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String tag1 = v.getTag().toString();
							int tag = Integer.parseInt(tag1);

							// 设置选中的当前项变色
							v.setBackgroundColor(Color.WHITE);

							/*
							 * v.setBackgroundDrawable(getResources().getColor(R.
							 * id.))
							 */

							// 设置选中的项目的前一个项目颜色还原
							TextView textView = (TextView) linearlayout_menu
									.getChildAt(PrePosition);
							textView.setBackgroundColor(Color
									.parseColor("#66ff99"));

							transaction = fragmentManager.beginTransaction();
							for (int i = 0; i < fragments.length; i++) {
								transaction.hide(fragments[i]);
							}

							// beginTransaction.replace(R.id.content_llayout,
							// fragments[0]);

							/*
							 * beginTransaction.replace(R.id.content_llayout,
							 * fragments[0]);
							 */

							transaction.show(fragments[tag]);

							transaction.commit();
							PrePosition = tag;

						}

					});
		}
	}

	private void CheckNetConnection() {
		// TODO Auto-generated method stub

		int netStatus = NetConnectionUtil.getNetStatus(MainActivity.this);

	}

	public void updateFavariteFragmentData() {
		FragmentFavourate fragment = (FragmentFavourate) this.fragments[3];
		fragment.lvForShoucang.setAdapter(new AdapterForShowShouCang(this));

	}
	@Override
	public boolean onCreateOptionsMenu(Menu m) {
		// TODO Auto-generated method stub
		m.add("联系作者");
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		int id = item.getOrder();
		switch (id) {
		case 1:
			break;

		case 0:
			Intent i=new Intent(this, AboutAuther.class);
			startActivity(i);
			
			
			break;
		}
		
		return true;
	}
}
