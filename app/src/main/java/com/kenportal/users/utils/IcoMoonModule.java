package com.kenportal.users.utils;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * Created by kranti on 11/06/2016.
 */
public class IcoMoonModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "fonts/icomoon.ttf";
    }

    @Override
    public Icon[] characters() {
        return IcoMoonIcons.values();
    }
}
