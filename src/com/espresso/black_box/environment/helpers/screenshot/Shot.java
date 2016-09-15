//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.espresso.black_box.environment.helpers.screenshot;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.uiautomator.UiDevice;
import android.view.View;

import com.espresso.black_box.environment.helpers.R;
import com.espresso.black_box.environment.helpers.Report;

import org.hamcrest.Matcher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.espresso.black_box.environment.addition.Actions.getRandomNumberInRange;
import static com.espresso.black_box.environment.addition.Actions.isToast;
import static com.espresso.black_box.environment.addition.Actions.logAction;
import static com.espresso.black_box.environment.addition.WaitFor.waitForView;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.containsString;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public final class Shot {
    public static boolean SCREENSHOT_SAVED = false;

    public static void takeRootedScreenshot() throws IOException, InterruptedException {
        logAction("Try to get device screenshot");
        Process sh = Runtime.getRuntime().exec("su", null,null);
        OutputStream os = sh.getOutputStream();
        os.write(("/system/bin/screencap -p " + Environment.getExternalStorageDirectory().getPath()+ "/Pictures/test"+getRandomNumberInRange(1, 15)+".png").getBytes("ASCII"));
        os.flush();

        os.close();
        sh.waitFor();
    }

    public static void saveScreenshot(final File file) throws Exception {
        final Bitmap bitmap;
        if(waitForView(R.id.decor_content_parent, 250)){
            try{
                bitmap = getBitmapFromView(R.id.decor_content_parent);
                saveBitmap(bitmap, file);
            }catch (NoMatchingViewException err){

            }
        }else{
            try {
                bitmap = getBitmapFromView(android.R.id.content);
                saveBitmap(bitmap, file);
            }catch (NoMatchingViewException err){

            }
        }

    }


    static void saveBitmap(final Bitmap bitmap, final File file) throws Exception {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                try{
//                    Bitmap bitmap_for_save = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
//
//                    BufferedOutputStream fos1 = null;
//
//                    try {
//                        fos1 = new BufferedOutputStream(new FileOutputStream(file));
//
//                        bitmap_for_save.compress(CompressFormat.PNG, 90, fos1);
//                    } finally {
//                        bitmap_for_save.recycle();
//                        if(fos1 != null) {
//                            fos1.close();
//                        }
//
//                    }
//                }catch ( Exception ex ){
//                    ex.printStackTrace();
//                }
//            }
//        });
//        t.start();
        SaveBitmapTaskParams saveBitmapTaskParams = new SaveBitmapTaskParams(bitmap, file);
        save_bitmap saveBitmap = new save_bitmap();
        saveBitmap.execute(saveBitmapTaskParams);


    }


    private static class SaveBitmapTaskParams {
        Bitmap bitmap;
        File file;

        SaveBitmapTaskParams(Bitmap bitmap, File file) {
            this.bitmap = bitmap;
            this.file = file;
        }
    }

    static class save_bitmap extends AsyncTask<SaveBitmapTaskParams, Void, Void> {
        @Override
        protected Void doInBackground(SaveBitmapTaskParams... params) {
            Bitmap bitmap = params[0].bitmap;
            File file = params[0].file;
            try{
                Bitmap bitmap_for_save = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                BufferedOutputStream fos1 = null;

                try {
                    fos1 = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap_for_save.compress(CompressFormat.PNG, 90, fos1);
                } finally {
                    bitmap_for_save.recycle();
                    if(fos1 != null) {
                        fos1.close();
                    }

                }
            }catch ( Exception ex ){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SCREENSHOT_SAVED = false;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SCREENSHOT_SAVED = true;
        }
    }

    public static Bitmap getBitMapFromToast(String message){

        final Bitmap[] bitmapHolder = {null};
        onView(withText(containsString(message))).
                inRoot(isToast())
//                .check(matches(isDisplayed()))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(View.class);
                    }

                    @Override
                    public String getDescription() {
                        return "getting bitmap from Toast";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        Bitmap bitmap;
                        view.setDrawingCacheEnabled(true);
                        bitmap = Bitmap.createBitmap(view.getDrawingCache());
                        view.setDrawingCacheEnabled(false);
                        bitmapHolder[0] = bitmap;
                    }
                });
        return bitmapHolder[0];
    }

    public static Bitmap getBitMapFromViewWithClass(Class withName){

        final Bitmap[] bitmapHolder = {null};
//        onView(allOf(withClassName(containsString(withName.getSimpleName()))))
        onView(instanceOf(withName))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(View.class);
                    }

                    @Override
                    public String getDescription() {
                        return "getting bitmap from Toast";
                    }

                    @Override
                    public void perform(UiController uiController, View view){
                        Bitmap bitmap;
                        view.setDrawingCacheEnabled(true);
                        bitmap = Bitmap.createBitmap(view.getDrawingCache());
                        view.setDrawingCacheEnabled(false);
                        bitmapHolder[0] = bitmap;
                    }
                });
        return bitmapHolder[0];
    }


    public static Bitmap getBitmapFromView(int id) {
        final Bitmap[] bitmapHolder = {null};
        onView(withId(id))
                .check(matches(isDisplayed()))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(View.class);
                    }

                    @Override
                    public String getDescription() {
                        return "getting bitmap from View";
                    }

                    @Override
                    public void perform(UiController uiController, View view){
                        Bitmap bitmap;
                        view.setDrawingCacheEnabled(true);
                        bitmap = Bitmap.createBitmap(view.getDrawingCache());
                        view.setDrawingCacheEnabled(false);
                        bitmapHolder[0] = bitmap;
                    }
                });
        return bitmapHolder[0];
    }

    public static void makeScreenshot() throws Exception {
        if(Report.MAKE_SCREENSHOTS){
            makeScreenshot(true);
        }
    }


    public static void makeScreenshot(Class clazz)throws Exception {
        if(Report.MAKE_SCREENSHOTS){
            makeScreenshot(true, clazz);
        }
    }

    public static String screenshotFileName(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss");
        String filename = "shot_at_" +  format.format(date)+ "-" + getRandomNumberInRange(0, 10) + ".png";
        return  filename;
    }



    public static File screenShotPath(String filename){
        File screenshotsDirectory = new File(Environment.getExternalStorageDirectory() +
                "/Espresso-data/reports/screenshots/");
        screenshotsDirectory.mkdirs();
        File path = new File(screenshotsDirectory + "/" + filename);
        return path;
    }

    public static void makeScreenshot(boolean notUse) throws Exception {
        final String filename = screenshotFileName();
        File path = screenShotPath(filename);
        saveScreenshot(path);
        addToReport(path,filename);
    }

    public static void makeScreenshot(int viewId) throws Exception {
        final String filename = screenshotFileName();
        File path = screenShotPath(filename);
        saveBitmap(getBitmapFromView(viewId), path);
        addToReport(path,filename);
    }

    public static void makeScreenshot(boolean notUse, Class clazz) throws Exception {
        final String filename = screenshotFileName();
        File path = screenShotPath(filename);
        saveBitmap(getBitMapFromViewWithClass(clazz), path);
        addToReport(path,filename);
    }

    public static void makeScreenshotFromDevice(){
        try{
            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            final String filename = screenshotFileName();
            File path = screenShotPath(filename);
            mDevice.takeScreenshot(path);
            addToReport(path,filename);
        }catch (Exception err){
            try{
                takeRootedScreenshot();
            }catch (IOException | InterruptedException err2){

            }
        }
    }

    public static void addToReport(File path, String filename) {
        if(!Report.SCREENSHOTS_ARRAY.contains(Report.TEST_METHOD_NAME)){
            Report.SCREENSHOTS_ARRAY.add(Report.TEST_METHOD_NAME);
        }

//        if (path.exists() && path.isFile()) {
            Report.SCREENSHOTS_ARRAY.add(filename);
//        }
    }

}
