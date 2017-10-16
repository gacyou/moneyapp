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
 * Created by user on 16/10/3.
 */
public class DayDataListitemAdapter extends BaseAdapter {


    private LayoutInflater myInflater;
    List<dayDataAmount> l = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;

    public DayDataListitemAdapter(Activity activity, Context context, List<dayDataAmount> l){
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
            convertView = myInflater.inflate(R.layout.daydata_adapter, null);

            //建構listItem內容view
            viewTag = new ViewTag(
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("dayData_tradeDate", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("addeqTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("addaccountTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("eqDAUTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("accountDAUTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("totalredeemTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("amountredeemTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("arpuTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("arppuTv", "id", mContext.getPackageName())),
                    (TextView) convertView.findViewById(
                            mContext.getResources().getIdentifier("payrateTv", "id", mContext.getPackageName()))
            );

            //設置容器內容
            convertView.setTag(viewTag);
        }
        else{
            viewTag = (ViewTag) convertView.getTag();
        }

        viewTag.dayData_tradeDate.setText(l.get(position).dayData_tradeDate);
        viewTag.addeqTv.setText(String.valueOf(l.get(position).addeq));
        viewTag.addaccountTv.setText(String.valueOf(l.get(position).addaccount));
        viewTag.eqDAUTv.setText(String.valueOf(l.get(position).eqDAU));
        viewTag.accountDAUTv.setText(String.valueOf(l.get(position).accountDAU));
        viewTag.totalredeemTv.setText(String.valueOf(l.get(position).totalredeem));
        viewTag.amountredeemTv.setText(String.valueOf(l.get(position).amountredeem));
        viewTag.arpuTv.setText(String.valueOf(l.get(position).arpu));
        viewTag.arppuTv.setText(String.valueOf(l.get(position).arppu));
        viewTag.payrateTv.setText(String.valueOf(l.get(position).payrate));

        return convertView;
    }

    class ViewTag{
        TextView dayData_tradeDate;
        TextView addeqTv;
        TextView addaccountTv;
        TextView eqDAUTv;
        TextView accountDAUTv;
        TextView totalredeemTv;
        TextView amountredeemTv;
        TextView arpuTv;
        TextView arppuTv;
        TextView payrateTv;

        public ViewTag(TextView dayData_tradeDate, TextView addeq, TextView addaccount, TextView eqDAU, TextView accountDAU, TextView totalredeem, TextView amountredeem, TextView arpu, TextView arppu, TextView payrate) {
            this.dayData_tradeDate = dayData_tradeDate;
            this.addeqTv = addeq;
            this.addaccountTv = addaccount;
            this.eqDAUTv = eqDAU;
            this.accountDAUTv = accountDAU;
            this.totalredeemTv = totalredeem;
            this.amountredeemTv = amountredeem;
            this.arpuTv = arpu;
            this.arppuTv = arppu;
            this.payrateTv = payrate;
        }
    }

}
