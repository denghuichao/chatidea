package com.github.denghuichao.chatcode.util;

import java.awt.Color;

/**
 * @author huichaodeng
 * @version 1.0.0
 * @since 2023/11/09 17:24
 */
public class ColorUtils {
    public static Color[] calculateMessageBackground(Color systemColor) {
        Color myColor;
        Color otherColor;

        if (isLightColor(systemColor)) { // 浅色背景
            myColor = new Color(99, 118, 145);
            otherColor = new Color(244, 246, 248);
        } else { // 深色背景
            myColor = new Color(86, 98, 115);
            otherColor = new Color(46, 47, 48);
        }

        return new Color[]{myColor, otherColor};
    }

    public static boolean isLightColor(Color color) {
        int[] rgb = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
        // 根据RGB值判断颜色是浅色还是深色
        int brightness = (int) Math.sqrt(
                rgb[0] * rgb[0] * .299 +
                        rgb[1] * rgb[1] * .587 +
                        rgb[2] * rgb[2] * .114
        );
        return brightness > 130;
    }
}
