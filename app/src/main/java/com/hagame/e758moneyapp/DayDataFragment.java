package com.hagame.e758moneyapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hagame.e758moneyapp.common.DayDataListitemAdapter;
import com.hagame.e758moneyapp.common.Settings;
import com.hagame.e758moneyapp.common.Utils;
import com.hagame.e758moneyapp.common.dayDataAmount;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

public class DayDataFragment extends Fragment {

    Context mContext;
    Activity mActivity;

    ListView mlv;
    LinearLayout loadingView;

    int gameId , outY, outM, outD, rMsNo;
    String rMsg;

    GetDayDataCountTask mTask;

    //
    private TextView tvDate;
    private Button btDate;
    final Calendar c = Calendar.getInstance();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.activity_daydata, container, false);

        mContext = V.getContext();

        mlv = (ListView) V.findViewById(R.id.daydata_list);
        tvDate = (TextView) V.findViewById(R.id.textData);
        btDate = (Button) V.findViewById(R.id.dateButton);
        loadingView = (LinearLayout) V.findViewById(R.id.dayCount_loading);

        gameId = getArguments().getInt("gameId");

        //時間選擇
        btDate.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View V) {

                DatePickerDialog da = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        outY = y;
                        outM = m + 1;
                        outD = d;

                      //  tvDate.setText(y + "-" + (m + 1) + "-" + d);

                        //
                        mTask = new GetDayDataCountTask();
                        mTask.execute(gameId);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                da.show();

            }
        });


        return V;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mTask != null){
            return;
        }

        mTask = new GetDayDataCountTask();
        mTask.execute(gameId);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTask != null){
            return;
        }

        mTask = new GetDayDataCountTask();
        mTask.execute(gameId);

    }

    class GetDayDataCountTask extends AsyncTask<Integer, Void, List<dayDataAmount>> {
        @Override
        protected  void onPreExecute(){
            loadingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<dayDataAmount> doInBackground(Integer... params) {

            List<dayDataAmount> r = new ArrayList<>();

            String h = "";

            String year = Integer.toString(outY);
            String month =  Integer.toString(outM);
            String day =  Integer.toString(outD);



            try{

                //hash = sha256("1758" + year + month + day + "fchnUyNXk7ufSnINfnrPBU8exIxK2N8WzEo4SXWK")
                h = Utils.ssha256("1758" + year + month + day + "fchnUyNXk7ufSnINfnrPBU8exIxK2N8WzEo4SXWK");

            }
            catch(NoSuchAlgorithmException ex){

            }
            catch (UnsupportedEncodingException ex1){

            }

            String url = Settings.dayDataUrl + "?year=" + year + "&month=" + month + "&day=" + day + "&hash=" + h + "&gameId=" + gameId ;
            KLog.d("fafsdfasdfasdff", url);

            try {

                String retSrc = Utils.getUrlResponse(url);

                KLog.d("json", retSrc);

                JSONObject json = new JSONObject(retSrc);

                int MsgNo = json.getInt("ReturnMsgNo");
                String Msg = json.getString("ReturnMsg");


                rMsNo = MsgNo;
                rMsg = Msg;

                    JSONObject data = new JSONObject(json.getString("data"));
                    dayDataAmount d = new dayDataAmount();

                    d.dayData_tradeDate = year + "-" + month + "-" + day;
                    d.addeq = data.getInt("addeq");
                    d.addaccount = data.getInt("addaccount");
                    d.eqDAU = data.getInt("eqDAU");
                    d.accountDAU = data.getInt("accountDAU");
                    d.totalredeem = data.getInt("totalredeem");
                    d.amountredeem = data.getInt("amountredeem");
                    d.arpu = data.getDouble("arpu");
                    d.arppu = data.getDouble("arppu");
                    d.payrate = data.getDouble("payrate");

                    r.add(d);

            } catch (Exception ex) {

            }

            return r;

        }

        @Override
        protected void onPostExecute(List<dayDataAmount> result) {

            loadingView.setVisibility(View.GONE);

            KLog.d("21321", rMsNo);
            KLog.d("21321", rMsg);

            if(rMsNo != 1 && rMsNo != -85)
            {
                Toast toast = Toast.makeText(mContext,
                        rMsg, Toast.LENGTH_SHORT);
                toast.show();
            }

            if(rMsNo == -85)
            {
                Toast toast = Toast.makeText(mContext,
                        "請選擇想要觀看的日期", Toast.LENGTH_SHORT);
                toast.show();
            }

            DayDataListitemAdapter a = new DayDataListitemAdapter(mActivity, mContext, result);
            mlv.setAdapter(a);

            mTask = null;
        }
    }



}

