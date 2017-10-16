package com.hagame.e758moneyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hagame.e758moneyapp.common.LoginResult;
import com.hagame.e758moneyapp.common.Settings;
import com.hagame.e758moneyapp.common.Utils;
import com.hagame.e758moneyapp.common.games;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

public class SplashScreen extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static Context mContext;
	private static Activity mActivity;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		mContext = this;
		mActivity = this;

		mRequestQueue = Volley.newRequestQueue(this);
		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
			private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
			public void putBitmap(String url, Bitmap bitmap) {
				mCache.put(url, bitmap);
			}
			public Bitmap getBitmap(String url) {
				return mCache.get(url);
			}
		});

		NetworkImageView img = (NetworkImageView) findViewById(R.id.cover_ImgN);

		img.setImageUrl("http://image.1758play.com/cpi/icon/1758play.png", mImageLoader);

		String i = Utils.getIdentifier(mContext);
		String t = Utils.getTimeStamp();
		String h = "";

		try{
			h = Utils.sha256(i + t);
		}
		catch(NoSuchAlgorithmException ex){

		}
		catch (UnsupportedEncodingException ex1){

		}

		String url = Settings.loginUrl + "?i=" + i +"&t=" + t +"&h=" + h;

		Log.d("gegegeggegegegege", "onCreate: " + url);

		new LoginTask().execute(url);
	}

	private class LoginTask extends AsyncTask<String, Integer, LoginResult> {
		@Override
		protected LoginResult doInBackground(String... params) {
			LoginResult r = new LoginResult();

			try {
				Thread.sleep(1000);
				String retSrc = Utils.getUrlResponse(params[0]);

				JSONObject json = new JSONObject(retSrc);

				int ReturnMsgNo = json.getInt("ReturnMsgNo");

				if (ReturnMsgNo != 1)
				{
					r.ReturnMsgNo = ReturnMsgNo;
					r.ReturnMsg = json.getString("ReturnMsg");
					return r;
				}

				r.otp = json.getString("otp");
				r.uName = json.getString("uName");

				JSONArray g = new JSONArray(json.getString("games"));

				List<games> tmp = new ArrayList<games>();

				for(int i = 0; i < g.length(); ++i){
					games t = new games();
					t.gameId  = g.getJSONObject(i).getInt("GameId");
					t.gameName  = g.getJSONObject(i).getString("Name");
					tmp.add(t);
				}
				r.ReturnMsgNo = ReturnMsgNo;
				r.g = tmp;
                r.d = tmp;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return r;
		}

		@Override
		protected void onPostExecute(LoginResult result) {
			if (result.ReturnMsgNo != 1) {
				new AlertDialog.Builder(mContext)
						.setIconAttribute(android.R.attr.alertDialogIcon)
						.setTitle("登入有誤")
						.setCancelable(false)
						.setMessage("此手機無法登入: " + result.ReturnMsg + "，請洽系統單位。")
						.setPositiveButton("確定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						})
						.show();
			} else {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("LoginResult", result);
				mActivity.startActivity(intent);

				finish();
			}
		}
	}



}
