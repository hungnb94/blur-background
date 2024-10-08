package leoh.screenshot.protector.navigation;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

/**
 * copy from https://github.com/gyf-dev/ImmersionBar
 */
class GestureUtils {

    /**
     * 获取全面屏相关信息
     *
     * @param context Context
     * @return FullScreenBean
     */
    public static GestureBean getGestureBean(Context context) {
        GestureBean gestureBean = new GestureBean();
        if (context != null && context.getContentResolver() != null) {
            ContentResolver contentResolver = context.getContentResolver();
            NavigationBarType navigationBarType = NavigationBarType.UNKNOWN;
            int type = -1;
            boolean isGesture = false;
            boolean checkNavigation = false;
            if (OSUtils.isHuaWei() || OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x()) {
                    type = Settings.System.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_EMUI, -1);
                } else {
                    type = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_EMUI, -1);
                }
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isXiaoMi() || OSUtils.isMIUI()) {
                type = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_MIUI, -1);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                    int i = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE, -1);
                    checkNavigation = i != 1;
                }
            } else if (OSUtils.isVivo() || OSUtils.isFuntouchOrOriginOs()) {
                type = Settings.Secure.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_VIVO, -1);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES_THREE_STAGE;
                    isGesture = true;
                } else if (type == 2) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isOppo() || OSUtils.isColorOs()) {
                type = Settings.Secure.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_OPPO, -1);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1 || type == 2 || type == 3) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isSamsung()) {
                type = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG, -1);
                if (type != -1) {
                    if (type == 0) {
                        navigationBarType = NavigationBarType.CLASSIC;
                        isGesture = false;
                    } else if (type == 1) {
                        isGesture = true;
                        int gestureType = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE, 1);
                        if (gestureType == 1) {
                            navigationBarType = NavigationBarType.GESTURES;
                        } else {
                            navigationBarType = NavigationBarType.GESTURES_THREE_STAGE;
                        }
                        int hide = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE, 1);
                        checkNavigation = hide == 1;
                    }
                } else {
                    type = Settings.Global.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD, -1);
                    if (type == 0) {
                        navigationBarType = NavigationBarType.CLASSIC;
                        isGesture = false;
                    } else if (type == 1) {
                        navigationBarType = NavigationBarType.GESTURES;
                        isGesture = true;
                    }
                }
            }
            if (type == -1) {
                type = Settings.Secure.getInt(contentResolver, Constants.IMMERSION_NAVIGATION_BAR_MODE_DEFAULT, -1);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.DOUBLE;
                    isGesture = false;
                } else if (type == 2) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                    checkNavigation = true;
                }
            }
            gestureBean.isGesture = isGesture;
            gestureBean.checkNavigation = checkNavigation;
            gestureBean.type = navigationBarType;
        }
        return gestureBean;
    }

    static class GestureBean {
        /**
         * 是否有手势操作
         */
        public boolean isGesture = false;
        /**
         * 需要校验导航栏高度，需要检查的机型，小米，三星，原生
         */
        public boolean checkNavigation = false;
        /**
         * 手势类型
         */
        public NavigationBarType type;

        @NonNull
        @Override
        public String toString() {
            return "GestureBean{" +
                    "isGesture=" + isGesture +
                    ", checkNavigation=" + checkNavigation +
                    ", type=" + type +
                    '}';
        }
    }
}
