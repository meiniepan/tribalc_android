package com.gs.buluo.app.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hjn on 2016/11/10.
 */
public class CommonUtils {
    public static boolean checkPhone(String area, String phone, Context context){
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(context, "手机号不能为空!");
            return false;
        }
        Pattern p;
        switch (area){
            case "86":
                p= Pattern.compile("1[3|4|5|7|8|][0-9]{9}");  //中国
                break;
            case "1":
                p=Pattern.compile("[0-9]{10}");   //美国
                break;
            case "886":
                p=Pattern.compile("09[0-9]{8}||9[0-9]{8}");  //台湾
                break;
            case "852":
                p=Pattern.compile("[5|6|9|][0-9]{7}");  //香港  目前8位
                break;
            case "81":
                p=Pattern.compile("0[8|9|]0[0-9]{8}");  //日本
                break;
            default:
                p= Pattern.compile("\\d+"); //哪国都不是 纯数字就过
        }
        Matcher m = p.matcher(phone);

        if (!m.matches()||phone.length()!=11) {
            ToastUtils.ToastMessage(context, "请输入正确的手机号!");
            return false;
        }
        return true;
    }

    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var5) {
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            if(Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }
}
