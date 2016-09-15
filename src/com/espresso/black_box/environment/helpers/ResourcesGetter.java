package com.espresso.black_box.environment.helpers;

import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.espresso.black_box.environment.addition.Actions.logAction;
import static com.espresso.black_box.environment.addition.Actions.writeFields;

/**
 * Created by Karnaukh Roman on 27.07.2016.
 */

public class ResourcesGetter{
    public static List<String> ids_list = new ArrayList<>();
    public static List<String> strings_list = new ArrayList<>();
    public static List<String> strings_values = new ArrayList<>();
    public static R.id idClass = new R.id();
    public static R.string stringClass = new R.string();
    public static List<String> strings_ = declaredResources(stringClass);

    public static void getResources() throws Exception{
        logAction("getting resources... ");
        List<String> ids = declaredResources(idClass);

        int n = 2131296000;

        while(true){
            try {
                String typeName = R.targetContext.getResources().getResourceTypeName(n);
                String id = R.targetContext.getResources().getResourceEntryName(n);

                   if(typeName.equals("id")){
                       if(!ids.contains(id)){
                           String data = "public static int "+id+" = id(\""+id+"\");\n";
                           ids_list.add(data);
                       }
                   }else if(typeName.equals("string")){
                       if(!strings_.contains(id) && !id.contains(".")){
                           if(!id.contains("google")){
                               if(!Character.isUpperCase(id.charAt(0))){
                                   String data = "public static int "+id+" = string(\""+id+"\");\n";
                                   strings_list.add(data);
                                   strings_.add(id);
                               }

                           }

                       }
                   }
            }catch (android.content.res.Resources.NotFoundException err){            }

            if (n == 2131626327) break;
            n++;
        }

//        writeFields("id_list.txt", ids_list);
//        writeFields("strings_list.txt", strings_list);
//        adb pull "/sdcard/Espresso-data/ids_list.txt" adb pull "/sdcard/Espresso-data/strings_list.txt"
    }

    public static void getStrings() throws Exception{

        for(String string:strings_){
            String targetString;
            try{
                targetString = R.targetContext.getResources().getString(R.string(string));
                strings_values.add(string + "=" +targetString + "\n");
            }catch (Resources.NotFoundException e){

            }

        }

        writeFields("strings.txt", strings_values);
        //        adb pull /sdcard/Espresso-data/strings.txt ~/Documents/auto_testing/
    }


    public static List<String> declaredResources(Object instance){
        List<String> array = new ArrayList<>();
        Object value = "value";

        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                value = field.get(instance);
            }catch (IllegalAccessException ew){

            }

            if (value != null) {
                array.add(field.getName());
            }
        }
        return array;
    }

}
