package leoh.screenshot.protector.navigation;

/**
 * copy from https://github.com/gyf-dev/ImmersionBar
 */
class Constants {
    /**
     * 小米导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_MIUI = "force_fsg_nav_bar";
    /**
     * 小米导航栏手势导航情况下，是否隐藏手势提示线,0：显示 1：隐藏
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE = "hide_gesture_line";
    /**
     * 华为导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_EMUI = "navigationbar_is_min";
    /**
     * VIVO导航栏显示隐藏标识位 0-三按钮导航，1-经典三段式，2-全屏手势
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_VIVO = "navigation_gesture_on";
    /**
     * OPPO导航栏显示隐藏标识位 0-三按钮导航，1-手势导航，2-上划手势，3-侧滑手势
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_OPPO = "hide_navigationbar_enable";
    /**
     * SAMSUNG导航栏显示隐藏标识位 0-三按钮导航 1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG = "navigation_bar_gesture_while_hidden";
    /**
     * 三星导航栏手势导航情况下,手势类型 0：三段式线条 1：单线条
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE = "navigation_bar_gesture_detail_type";
    /**
     * 三星导航栏手势导航情况下，是否隐藏手势提示线,0：隐藏 1：显示
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE = "navigation_bar_gesture_hint";
    /**
     * SAMSUNG导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD = "navigationbar_hide_bar_enabled";
    /**
     * 默认手势导航 0-三按钮导航，1-双按钮导航，2-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_DEFAULT = "navigation_mode";
}
