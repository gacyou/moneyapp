package com.hagame.e758moneyapp.common;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hagame.e758moneyapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chineseword on 2015/3/2.
 */
public class DayCountListItemAdapter extends BaseAdapter {
	private LayoutInflater myInflater;
	List<tradeAmount> l = new ArrayList<>();
	private Context mContext;
	private Activity mActivity;

	public DayCountListItemAdapter(Activity activity, Context context, List<tradeAmount> l){
		myInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mActivity = activity;
		this.l = l;
	}

	@Override
	public int getCount()
	{
		return l.size();
	}

	@Override
	public Object getItem(int position)
	{
		return l.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewTag viewTag;
		if(convertView == null){
			//取得listItem容器 view
			convertView = myInflater.inflate(R.layout.daycount_adapter, null);

			//建構listItem內容view
			viewTag = new ViewTag(
					(TextView) convertView.findViewById(
							mContext.getResources().getIdentifier("dayCount_tradeDate", "id", mContext.getPackageName())),
					(TextView) convertView.findViewById(
							mContext.getResources().getIdentifier("dayCount_tradeAmt", "id", mContext.getPackageName())),
					(TextView) convertView.findViewById(
							mContext.getResources().getIdentifier("dayCount_tradeAmtG", "id", mContext.getPackageName()))
			);

			//設置容器內容
			convertView.setTag(viewTag);
		}
		else{
			viewTag = (ViewTag) convertView.getTag();
		}

		viewTag.tradeDate.setText(l.get(position).tradeDate);
		viewTag.tradeAmt.setText(String.valueOf(l.get(position).tradeAmt));
		viewTag.tradeAmtG.setText(String.valueOf(l.get(position).tradeAmtG));

		return convertView;
	}

	class ViewTag{
		TextView tradeDate;
		TextView tradeAmt;
		TextView tradeAmtG;

		public ViewTag(TextView tradeDate, TextView tradeAmt, TextView tradeAmtG) {
			this.tradeDate = tradeDate;
			this.tradeAmt = tradeAmt;
			this.tradeAmtG = tradeAmtG;
		}
	}
}
