package com.titan.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Whs on 2016/12/12 0012
 */
public class TitanTextUtils {
    /**
     * 验证手机格式
     * 移动：134、135、136、137、138、139、147、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 虚拟运营商: 171
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNumber(String mobiles) {
        if (TextUtils.isEmpty(mobiles))
            return false;
        String telRegex = "[1][34587]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        Pattern pattern= Pattern.compile(telRegex);
        Matcher matcher=pattern.matcher(mobiles);
        return matcher.matches();
    }
    /**
     * 验证电话号码
     */
    public static boolean isPhoneNumber(String number){


        if (TextUtils.isEmpty(number))
            return false;
       // String telRegex = "\\d{3}-\\d{8}|\\d{4}-\\{7,8}";

        String PhoRegex ="(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}";
        Pattern pattern=Pattern.compile(PhoRegex);
        Matcher matcher=pattern.matcher(number);
        return matcher.matches();
    }
}
