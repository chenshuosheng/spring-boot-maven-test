package css.common.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 字符串处理工具
 * @Author: CSS
 * @Date: 2023/11/24 10:53
 */

@Component
public class MyStringUtils {

    public static boolean isEmpty(String s){
        if(s == null || s.length()<=0 || s.replaceAll(" ","").length()<=0){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }

    public static String getString(String s) {
        return (getString(s, ""));
    }

    public static String getString(String s, String defval) {
        if (isEmpty(s)) {
            return (defval);
        }
        return (s.trim());
    }



    /**
     * 通用方法通过正则表达式获取内容
     * @param str 原字符串
     * @param pattern 正则
     * @param sort 获取第几个
     * @return 通过正则获得的字符串
     */
    public static String pattern(String str, String pattern, int sort) {
        int i = 0;
        String rs = "";
        Pattern patternTitle = Pattern.compile(pattern);
        Matcher matcherTitle = patternTitle.matcher(str);
        while (matcherTitle.find()) {
            if (i == sort) {
                rs = matcherTitle.group();
                break;
            }
            i++;
        }
        return rs;
    }

    /**
     * 将特殊的空格和\r\n\b\t转成英文空格
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        if (str == null) {
            return "";
        }

        for ( int i=0; i<str.length(); i++ ) {
            if( Character.isWhitespace(str.charAt(i)) ) {
                str.replace(str.charAt(i), " ".charAt(0));
            }
        }
        //&#12288;中文全角空格
        //&#160;普通的英文半角空格
        str = str.replaceAll("&#160;| ", " ")
                .replaceAll("&#12288;|　", " ")
                .replaceAll("&#8194;| ", " ")
                .replaceAll("&#8195;| ", " ")
                .replaceAll("&#8197;| ", " ")
                .replaceAll("[\r\n\b\t]", " ");

        return str;
    }
}
