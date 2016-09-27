package com.espresso.black_box.environment.helpers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;



/**
 * Created by Karnaukh Roman on 22.07.2016.
 */
public class R {
    public static Context targetContext = InstrumentationRegistry.getTargetContext();
    private static  String packageName = targetContext.getPackageName();

    public static String getString(int id){
        return targetContext.getResources().getString(id);
    }

    public static int id(String s) {
        return targetContext.getResources().getIdentifier(s, "id", packageName);
    }

    public static int string(String s) {
        return targetContext.getResources().getIdentifier(s, "string", packageName);
    }

    public static class id{
        public static int decor_content_parent = id("decor_content_parent");
    }

    public static class string{
        public static int discount_club = string("discount_club");
    }
}
