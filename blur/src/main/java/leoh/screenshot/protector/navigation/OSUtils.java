package leoh.screenshot.protector.navigation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * copy from https://github.com/gyf-dev/ImmersionBar
 */
public class OSUtils {
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";


    /**
     * 是否是小米手机
     * Is xiao mi boolean.
     *
     * @return the boolean
     */
    public static boolean isXiaoMi() {
        return Build.MANUFACTURER.toLowerCase().contains("xiaomi");
    }

    /**
     * 是否是华为手机
     * Is hua wei boolean.
     *
     * @return the boolean
     */
    public static boolean isHuaWei() {
        return Build.MANUFACTURER.toLowerCase().contains("huawei");
    }

    /**
     * 是否是Oppo手机
     * Is oppo boolean.
     *
     * @return the boolean
     */
    public static boolean isOppo() {
        return Build.MANUFACTURER.toLowerCase().contains("oppo");
    }

    /**
     * 是否是Vivo手机
     * Is vivo boolean.
     *
     * @return the boolean
     */
    public static boolean isVivo() {
        return Build.MANUFACTURER.toLowerCase().contains("vivo");
    }

    /**
     * 是否是Samsung手机
     * Is samsung boolean.
     *
     * @return the boolean
     */
    public static boolean isSamsung() {
        return Build.MANUFACTURER.toLowerCase().contains("samsung");
    }

    /**
     * 判断是否为miui
     * Is miui boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME);
        return !TextUtils.isEmpty(property);
    }

    /**
     * 判断是否为emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME);
        return !TextUtils.isEmpty(property);
    }

    /**
     * 得到emui的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_NAME) : "";
    }

    /**
     * 判断是否为emui3.1版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        return "EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1");
    }

    /**
     * 判断是否为emui3.0版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_0() {
        String property = getEMUIVersion();
        return property.contains("EmotionUI_3.0");
    }

    /**
     * 判断是否为emui3.x版本
     * Is emui 3 x boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_x() {
        return OSUtils.isEMUI3_0() || OSUtils.isEMUI3_1();
    }

    /**
     * 判断是否为ColorOs
     * Is emui boolean.
     *
     * @return the boolean
     */
    public static boolean isColorOs() {
        String property = getSystemProperty("ro.build.version.opporom");
        return !TextUtils.isEmpty(property);
    }

    public static boolean isPixel() {
        return Build.MANUFACTURER.equalsIgnoreCase("Google");
    }

    /**
     * 判断是否为FuntouchOs或者是否为OriginOs
     * Is emui boolean.
     *
     * @return the boolean
     */
    public static boolean isFuntouchOrOriginOs() {
        String property = getSystemProperty("ro.vivo.os.version");
        return !TextUtils.isEmpty(property);
    }

    @SuppressLint("PrivateApi")
    private static String getSystemProperty(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method method = clz.getMethod("get", String.class, String.class);
            return (String) method.invoke(clz, key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}