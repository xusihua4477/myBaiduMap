package com.example.myprogram_baidumap.fragment;

import java.util.List;

import com.baidu.lbsapi.auth.i;
import com.example.myprogram_baidumap.MainActivity;
import com.example.myprogram_baidumap.R;
import com.example.myprogram_baidumap.bean.Bus;
import com.example.myprogram_baidumap.bean.Navigate;
import com.example.myprogram_baidumap.utils.AdapterForShowShouCang;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentFavourate extends Fragment {
	private View v;
MainActivity mainActivity;
TextView tvShow;
public ListView lvForShoucang;
public AdapterForShowShouCang adapterForShowShouCang;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		 v=inflater.inflate(R.layout.fragment_favourate,null);
		System.out.println("FragmentFavourate.onCreateView");
		System.out.println("FragmentFavourate.onCreateView");
		System.out.println("FragmentFavourate.onCreateView");
		 lvForShoucang=(ListView) v.findViewById(R.id.lvForShoucang);
		  adapterForShowShouCang = new AdapterForShowShouCang(mainActivity);
		 lvForShoucang.setAdapter(adapterForShowShouCang);
		 adapterForShowShouCang.notifyDataSetChanged();
		return v;

	}
@Override
public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	
	
	System.out.println("FragmentFavourate onPause()");
	System.out.println("FragmentFavourate onPause()");
	System.out.println("FragmentFavourate onPause()");
}
@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	
	System.out.println("FragmentFavourate onDestroy");
	System.out.println("FragmentFavourate onDestroy");
	System.out.println("FragmentFavourate onDestroy");
}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		System.out.println("FragmentFavourate onAttach");
		System.out.println("FragmentFavourate onAttach");
		System.out.println("FragmentFavourate onAttach");
		mainActivity=(MainActivity) activity;
		// adapterForShowShouCang.notifyDataSetChanged();
		
		
	}
@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	System.out.println("FragmentFavourate onActivityCreated");
	System.out.println("FragmentFavourate onActivityCreated");
	System.out.println("FragmentFavourate onActivityCreated");
	 adapterForShowShouCang = new AdapterForShowShouCang(mainActivity);
	 lvForShoucang.setAdapter(adapterForShowShouCang);
	 adapterForShowShouCang.notifyDataSetChanged();
}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("FragmentFavourate onResume");
		System.out.println("FragmentFavourate onResume");
		System.out.println("FragmentFavourate onResume");
		
	}
}
