package com.example.myprogram_baidumap.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myprogram_baidumap.R;

public class Advertisement extends RelativeLayout implements OnClickListener {
	public ImageView iv1;
	public ImageView iv2;
	public ImageView iv3;
	private View view;
	String urls[];
	int ImgId[];
	private Context context;

	public Advertisement(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	public Advertisement(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		this.context = context;
		initView();
	}

	public Advertisement(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		view = View.inflate(getContext(), R.layout.guanggao, this);

		iv1 = (ImageView) view.findViewById(R.id.iv1);
		iv2 = (ImageView) view.findViewById(R.id.iv2);
		iv3 = (ImageView) view.findViewById(R.id.iv3);

		
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);
	}

	public void setImgs(int ImgId[]) {
		this.ImgId=ImgId;
		iv1.setImageResource(ImgId[0]);
		iv2.setImageResource(ImgId[1]);
		iv3.setImageResource(ImgId[2]);
	}
	public void setUrls(String[] urls) {
		this.urls=urls;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Uri uri=null;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv1:
			 uri = Uri.parse(urls[0]);

	
			break;
		case R.id.iv2:
			 uri = Uri.parse(urls[1]);
			break;
		case R.id.iv3:
			 uri = Uri.parse(urls[2]);
			break;

		default:
			break;
		}
		intent.setAction(Intent.ACTION_VIEW);

		intent.setData(uri);
		context.startActivity(intent);
	}

}
