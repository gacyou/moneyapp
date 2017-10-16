package com.hagame.e758moneyapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hagame.e758moneyapp.common.DayCountListItemAdapter;
import com.hagame.e758moneyapp.common.Settings;
import com.hagame.e758moneyapp.common.Utils;
import com.hagame.e758moneyapp.common.tradeAmount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 //
 //                                  _oo8oo_
 //                                 o8888888o
 //                                 88" . "88
 //                                 (| -_- |)
 //                                 0\  =  /0
 //                               ___/'==='\___
 //                             .' \\|     |// '.
 //                            / \\|||  :  |||// \
 //                           / _||||| -:- |||||_ \
 //                          |   | \\\  -  /// |   |
 //                          | \_|  ''\---/''  |_/ |
 //                          \  .-\__  '-'  __/-.  /
 //                        ___'. .'  /--.--\  '. .'___
 //                     ."" '<  '.___\_<|>_/___.'  >' "".
 //                    | | :  `- \`.:`\ _ /`:.`/ -`  : | |
 //                    \  \ `-.   \_ __\ /__ _/   .-` /  /
 //                =====`-.____`.___ \_____/ ___.`____.-`=====
 //                                  `=---=`
 //
 //
 //       ~~~~~~~Powered by https://github.com/ottomao/bugfreejs~~~~~~~
 //
 //                          佛祖保佑         永無bug
 //
 */

public class DayCountFragment extends Fragment {

	Context mContext;
	Activity mActivity;

	LinearLayout loadingView;
	ListView mlv;

	int gameId;
	String sDate;
	String eDate;
	String otp;

	GetDayCountTask mTask;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View V = inflater.inflate(R.layout.activity_daycount, container, false);
		mContext = V.getContext();

		loadingView = (LinearLayout) V.findViewById(R.id.dayCount_loading);
		mlv = (ListView) V.findViewById(R.id.dayCount_list);
		gameId = getArguments().getInt("gameId");
		sDate = getArguments().getString("sDate");
		eDate = getArguments().getString("eDate");
		otp = getArguments().getString("otp");


		return V;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mTask != null){
			return;
		}

		mTask = new GetDayCountTask();
		mTask.execute(gameId);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mTask != null){
			return;
		}

		mTask = new GetDayCountTask();
		mTask.execute(gameId);
	}

	class GetDayCountTask extends AsyncTask<Integer, Void, List<tradeAmount>> {
		@Override
		protected  void onPreExecute(){
			loadingView.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<tradeAmount> doInBackground(Integer... params) {
			List<tradeAmount> r = new ArrayList<>();

			String t = Utils.getTimeStamp();
			String h = "";

			try{
				h = Utils.sha256(sDate + eDate + gameId + otp + t);
			}
			catch(NoSuchAlgorithmException ex){

			}
			catch (UnsupportedEncodingException ex1){

			}

			String url = Settings.dayCountUrl + "?s=" + sDate + "&e=" + eDate + "&g=" + gameId + "&o=" + otp + "&t=" + t + "&h=" + h;


			try {
				String retSrc = Utils.getUrlResponse(url);

				JSONObject json = new JSONObject(retSrc);

				JSONArray amt = new JSONArray(json.getString("tradeAmt"));
				JSONArray amtG = new JSONArray(json.getString("tradeAmtG"));

				for(int i = 0; i < amt.length(); ++i){
					tradeAmount tmp = new tradeAmount();
					String tDateRipOff = amt.getJSONObject(i).getString("TradeDate").replace("/Date(", "").replace(")/", "");
					Long timeInMillis = Long.valueOf(tDateRipOff);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(timeInMillis);
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
					String tDate = df.format(calendar.getTime());
					tmp.tradeDate = tDate.toString();
					tmp.tradeAmt = amt.getJSONObject(i).getInt("totalAmount");
					tmp.tradeAmtG = 0;
					r.add(tmp);

				}

				for(int i = 0; i < amt.length(); ++i){
					String tDateRipOff = amt.getJSONObject(i).getString("TradeDate").replace("/Date(", "").replace(")/", "");
					for(int j = 0; j < amtG.length(); ++j){
						String tDateRipOffG = amtG.getJSONObject(j).getString("TradeDate").replace("/Date(", "").replace(")/", "");
						if (tDateRipOff.equals(tDateRipOffG)){
							r.get(i).tradeAmtG = amtG.getJSONObject(j).getDouble("Amount");
						}
					}
				}
			} catch (Exception ex) {

			}

			return r;
		}

		@Override
		protected void onPostExecute(List<tradeAmount> result) {

			loadingView.setVisibility(View.GONE);

			DayCountListItemAdapter a = new DayCountListItemAdapter(mActivity, mContext, result);
			mlv.setAdapter(a);

			mTask = null;
		}
	}

}
