package com.tyl.framework.util.device;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tyl.framework.log.L;

import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephoneUtil {

	private static final String TAG = TelephoneUtil.class.getSimpleName();

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        L.i(TAG, " IMEI：" + imei);
        return imei;
    }

    /**
     * 获取手机信息
     */
    public static String printTelephoneInfo(Context context) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------  手机信息  " + time + "  --------------------");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        //IMSI前面三位460是国家号码，其次的两位是运营商代号，00、02是中国移动，01是联通，03是电信。
        String providerName = null;
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providerName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providerName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providerName = "中国电信";
            }
        }
        sb.append(providerName + "  手机号：" + tm.getLine1Number() + " IMSI是：" + IMSI);
        sb.append("\nDeviceID(IMEI)       :" + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion:" + tm.getDeviceSoftwareVersion());
        sb.append("\ngetLine1Number       :" + tm.getLine1Number());
        sb.append("\nNetworkCountryIso    :" + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator      :" + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName  :" + tm.getNetworkOperatorName());
        sb.append("\nNetworkType          :" + tm.getNetworkType());
        sb.append("\nPhoneType            :" + tm.getPhoneType());
        sb.append("\nSimCountryIso        :" + tm.getSimCountryIso());
        sb.append("\nSimOperator          :" + tm.getSimOperator());
        sb.append("\nSimOperatorName      :" + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber      :" + tm.getSimSerialNumber());
        sb.append("\ngetSimState          :" + tm.getSimState());
        sb.append("\nSubscriberId         :" + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber      :" + tm.getVoiceMailNumber());
        return sb.toString();

    }
    
    /**
	 * 检查sim的状态
	 * @author gdpancheng@gmail.com  
	 * @return String
	 */
	public static String getSimState(Context context) {
		TelephonyManager mTm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (mTm.getSimState()) {
			case android.telephony.TelephonyManager.SIM_STATE_UNKNOWN:
				return "未知SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
			case android.telephony.TelephonyManager.SIM_STATE_ABSENT:
				return "没插SIM卡_" + android.telephony.TelephonyManager.SIM_STATE_ABSENT;
			case android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED:
				return "锁定SIM状态_需要用户的PIN码解锁_" + android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
			case android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED:
				return "锁定SIM状态_需要用户的PUK码解锁_" + android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
			case android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED:
				return "锁定SIM状态_需要网络的PIN码解锁_" + android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
			case android.telephony.TelephonyManager.SIM_STATE_READY:
				return "就绪SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_READY;
			default:
				return "未知SIM状态_" + android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;

		}
	}
	
	/**
	 * 获取手机信号状态
	 * @author gdpancheng@gmail.com  
	 * @return String
	 */
	public static String getPhoneType(Context context) {
		TelephonyManager mTm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (mTm.getPhoneType()) {
		case android.telephony.TelephonyManager.PHONE_TYPE_NONE:
			return "PhoneType: 无信号_" + android.telephony.TelephonyManager.PHONE_TYPE_NONE;
		case android.telephony.TelephonyManager.PHONE_TYPE_GSM:
			return "PhoneType: GSM信号_" + android.telephony.TelephonyManager.PHONE_TYPE_GSM;
		case android.telephony.TelephonyManager.PHONE_TYPE_CDMA:
			return "PhoneType: CDMA信号_" + android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
		default:
			return "PhoneType: 无信号_" + android.telephony.TelephonyManager.PHONE_TYPE_NONE;
		}
	}


}
