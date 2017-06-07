package com.titan.util;

import android.text.TextUtils;

/**
 * Created by Whs on 2016/12/12 0012
 */
public class TitanTextUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNumber(String mobiles) {
    /*
    移动：134、135、136、137、138、139、147、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    虚拟运营商: 171
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][34587]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else return mobiles.matches(telRegex);
    }
    /**
     * 验证电话号码
     */
    public static boolean isPhoneNumber(String number){
        String telRegex = "\\d{3}-\\d{8}|\\d{4}-\\{7,8}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number))
            return false;
        else return number.matches(telRegex);
    }
}
