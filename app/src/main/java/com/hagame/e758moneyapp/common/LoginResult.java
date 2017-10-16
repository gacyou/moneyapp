package com.hagame.e758moneyapp.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chineseword on 2015/2/26.
 */
public class LoginResult implements Serializable {
	public int ReturnMsgNo;
	public String ReturnMsg;
	public String uName;
	public String otp;
	public List<games> g;
	public List<games> d;
}
