package com.espresso.black_box.environment.addition;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.espresso.black_box.environment.data.Consts;
import com.espresso.black_box.environment.helpers.DisplayReader;
import com.espresso.black_box.environment.helpers.Matchers;
import com.espresso.black_box.environment.helpers.Report;
import com.espresso.black_box.environment.helpers.screenshot.Shot;

import junit.framework.AssertionFailedError;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.espresso.black_box.environment.helpers.R.getString;
import static com.espresso.black_box.environment.helpers.screenshot.Shot.SCREENSHOT_SAVED;
import static java.lang.Integer.parseInt;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;

@TargetApi(Build.VERSION_CODES.KITKAT)
public  class Actions {
    private static final String SERVICE_PACKAGE = "com.espresso.black.tests.showtoast";
    public static SharedPreferences sPref;
    public static long startTime = 0;

    public static String dateTwoWeeksBeforeFormatted = generateStringDate(true, 14);
    public static String dateWeekBeforeFormatted = generateStringDate(true, 7);
    public static String currentDateFormatted = generateStringDate(true, 0);
    static String TAG = "ROBO_message";


    public static String getShort(String account) {
        String shortNum;
        if (account.length() == 16) {
            shortNum = account.substring(12, 16);
        } else {
            shortNum = account.substring(10, 14);
        }
        assertTrue(shortNum.length() == 4);
        return shortNum;
    }

    public static void logAction(Object message) {
        Log.d(TAG, message.toString());
    }

    public static void clickOnString(int id, boolean isString) throws Exception {
        if (isString) {
            onView(withText(id)).perform(click());
        } else {
            onView(withText(containsString(getString(id)))).perform(click());
        }
        Shot.makeScreenshot();
    }

    public static void clickOnString(int id) throws Exception {
        clickOnString(id, false);
    }

    public static void clickOnStringWithoutShot(int id) {
        onView(withText(containsString(getString(id)))).perform(click());
    }

    public static void clickOnView(int id) throws Exception {
        onView(allOf(withId(id), isCompletelyDisplayed())).perform(click());
        Shot.makeScreenshot();
    }

    public static void clickOnSubmenuItem(int withText) throws Exception {
        try {
            onView(withText(withText)).perform(click());
        } catch (RuntimeException err) {
            err.printStackTrace();
        }
        Shot.makeScreenshot();
    }

    public static void clickOnViewWithoutShot(int id) {
        onView(withId(id)).perform(click());
    }

    public static void clickOnViewWithScrolling(int id) throws Exception {
        clickOnViewWithScrolling(id, 0, null);
    }

    public static void clickOnViewWithScrolling(int id, int withText) throws Exception {
        clickOnViewWithScrolling(id, withText, null);
    }

    public static void clickOnViewWithScrolling(int id, String withString) throws Exception {
        onView(allOf(withId(id), withText(withString))).perform(scrollTo(), click());
    }

    public static void clickOnViewWithScrolling(int id, int withText, String withString) throws Exception {
        try{
            if (withText == 0) {
                onView(withId(id)).perform(scrollTo(), click());
            } else {
                onView(allOf(withId(id), withText(withText))).perform(scrollTo(), click());
            }
        }catch (PerformException err){
            clickOnView(id);
        }
        Shot.makeScreenshot();
    }

    public static void clickOnText(String name) throws Exception {
        clickOnText(name, true);
    }

    public static void clickOnText(String name, boolean makeShot) throws Exception {
        onView(withText(containsString(name))).perform(click());
        if (makeShot) {
            Shot.makeScreenshot();
        }
    }

    public static void clickOnTextWithScrolling(String name, boolean makeShot) throws Exception {
        onView(withText(containsString(name))).perform(scrollTo(), click());
        if (makeShot) {
            Shot.makeScreenshot();
        }
    }

    public static void longClickOnText(String name) throws Exception {
        onView(withText(containsString(name))).perform(longClick());
        Shot.makeScreenshot();
    }

    public static void longClickOnView(int withId) throws Exception {
        onView(withId(withId)).perform(longClick());
        Shot.makeScreenshot();
    }

    public static void clickOnDescriptionText(String description) throws Exception {
        onData(withContentDescription(containsString(description))).perform(click());
        Shot.makeScreenshot();
    }


    public static void clickOnTextInSpinner(int spinnerId, String[] text) throws Exception {
        clickOnViewWithoutShot(spinnerId);
        int index = 0;
        while (true) {
            try {
                onData(AllOf.allOf(anything(),
                        hasToString(containsString(text[index]))))
                        .perform(click());
                break;
            } catch (PerformException err) {
                index++;
                if (index == text.length) break;
            }
        }
        onView(withId(spinnerId)).check(matches(withSpinnerText(containsString(text[index]))));
    }

    public static void clickOnTextInSpinner(int spinnerId, String selectionText) throws Exception {
        clickOnViewWithoutShot(spinnerId);
        getSpinnerPosition(selectionText);
    }

    public static void getPosition(int position) throws Exception {
        onData(anything())
                .inAdapterView(CoreMatchers.instanceOf(AdapterView.class))
                .atPosition(position)
                .perform(click());
        Shot.makeScreenshot();
    }

    public static void scrollToView(int id, boolean makeShot) throws Exception {
        onView(withId(id))
                .perform(scrollTo());
        if (makeShot) {
            Shot.makeScreenshot();
        }
    }

    public static void scrollToView(int id) throws Exception {
        scrollToView(id, true);
    }

    public static void scrollToView(Matcher<View> view) throws Exception {
        onView(view)
                .perform(scrollTo());
    }

    public static void getPosition(int position, boolean takeShot) throws Exception {
        getPosition(HashMap.class, position);
        if (takeShot) {
            Shot.makeScreenshot();
        }
    }

    public static void getPosition(Class clazz, int position) throws Exception {
        try {
            onData(CoreMatchers.instanceOf(clazz))
                    .atPosition(position)
                    .perform(click());
        } catch (RuntimeException err) {
            err.printStackTrace();
        }
    }

    public static boolean getSpinnerPosition(String text) throws Exception {
        int position = 0;
        int[] list = DisplayReader.getList();
        boolean result = true;

        try {
            onData(AllOf.allOf(org.hamcrest.Matchers.anything(), hasToString(containsString(text))))
                    .perform(click());
        } catch (PerformException err) {

            while (true) {
                try {
                    onData(anything())
                            .onChildView(withText(containsString(text)))
                            .atPosition(position)
                            .perform(click());
                    break;
                } catch (NoMatchingViewException er) {
                    position++;
                    if (position == list[0]) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }


    public static void getPosition(String clazz, int adapterView, int position) throws Exception {
        onData(CoreMatchers.instanceOf(Class.forName(clazz)))
                .inAdapterView(withId(adapterView))
                .atPosition(position)
                .perform(click());
        Shot.makeScreenshot();
    }

    public static void getPosition(Matcher<View> childView, int adapterView, int position) {
        onData(anything())
                .inAdapterView(withId(adapterView))
                .atPosition(position)
                .onChildView(childView)
                .perform(click());
    }

    public static void displayLastElement(int inAdapterView, int lastElement) throws Exception {
        if (lastElement == 0) {
            lastElement = DisplayReader.getFromList(inAdapterView)[0] - 4;
        }

        onData(anything())
                .inAdapterView(AllOf.allOf(withId(inAdapterView), isDisplayed()))
                .atPosition(lastElement)
                .check(matches(isDisplayed()));
    }

    public static void displayLastElement(int inAdapterView) throws Exception {
        displayLastElement(inAdapterView, 0);
    }


    public static void setCalendar(int buttonId, boolean before, long days) throws Exception {
        int[] payDate = generateIntegerCalendar(generateStringDate(before, days));
        /*set date format is yyyy.mm.DD*/
        setCalendar(buttonId,
                payDate[2],
                payDate[1],
                payDate[0]);
    }

    public static void setCalendar(int buttonId, int year, int month, int day) throws Exception {
        clickOnView(buttonId);
        logAction("year " + year + " month " + month + " day " + day);
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(year, month, day));
        clickOnView(android.R.id.button1);
    }

    public static void setTimes(int buttonId, boolean before, int minutes) throws Exception {
        setTimes(buttonId, generateTime(before, minutes));
    }

    public static void setTimes(int buttonId, int[] time) throws Exception {
        clickOnView(buttonId);
        onView(isAssignableFrom(TimePicker.class)).perform(setTime(time[0], time[1]));
        clickOnView(android.R.id.button1);
    }

    public static String addZero(int number) {
        String num = "";
        if (number < 10) {
            num = "0" + number;
        } else {
            num = "" + number;
        }
        return num;
    }

    public static void spinnerWithItemsMustBe(ArrayList<String> itemsArray, String[] withItems) {

        int n = 0;
        while (true) {
            String withCard = withItems[n];
            assertTrue(itemsArray.indexOf(withCard) > -1);
            n++;
            if (withItems.length == n) break;
        }
    }

    public static void spinnerMustBeWithoutItems(ArrayList<String> itemsArray, String[] withoutItems) {
        int i = 0;
        while (true) {
            String exceptCard = withoutItems[i];
            assertTrue(itemsArray.indexOf(exceptCard) == -1);
            i++;
            if (withoutItems.length == i) break;
        }
    }


    public static void clickOnSiblingText(int siblingId, String siblingText) {
        onView(AllOf.allOf(withId(siblingId),
                hasSibling(withText(containsString(siblingText))))).perform(click());
    }

    public static void setText(int id, String text) {
        onView(withId(id))
                .perform(Matchers.setTextFast(text), closeSoftKeyboard());
    }

    public static void substituteText(int id, String text) {
        onView(withId(id))
                .perform(replaceText(text), closeSoftKeyboard());
    }

    public static void setTextWithScrollingTo(int id, String text) {
        onView(withId(id))
                .perform(scrollTo(), replaceText(text));
    }

    public static void enterText(int id, String text) {
        onView(withId(id))
                .perform(typeText(text), closeSoftKeyboard());
    }

    /* если поле не принимает невалидные символы,
    * они могут игнорироваться либо генерировать RuntimeException
    * если валидации не будет и текст будет введен с лишними сиволами
    * сгенирируется AssertionError*/
    public static void validateTextField(int textFieldId, String notValidText, String textMustContain) {
        try {
            onView(withId(textFieldId))
                    .perform(typeText(notValidText), closeSoftKeyboard())
                    .check(matches(withText(textMustContain)));
        } catch (RuntimeException err) {
            setText(textFieldId, textMustContain);
        }
    }

    public static void matchViewContainsString(int id, String match) {
        onView(withId(id)).check(matches(withText(containsString(match))));
    }

    public static void matchAnyObjectHasDescription(String description) {
        onView(withContentDescription(description)).check(matches(isDisplayed()));
    }

    public static void swipeLeftOnView(int id) throws Exception {
        onView(withId(id)).perform(swipeLeft());
        Clock.sleep(350);
        Shot.makeScreenshot();
    }

    public static void makeDisplayed(int inAdapterView, int atPosition) throws Exception {
        onData(anything())
                .inAdapterView(AllOf.allOf(withId(inAdapterView), isDisplayed()))
                .atPosition(atPosition)
                .check(matches(isDisplayed()));
    }

    public static boolean mathPresent(Matcher<View> matcher) {
        boolean result = true;
        try {
            onView(matcher).check(matches(isDisplayed()));
        } catch (NoMatchingViewException err) {
            result = false;
        }
        return result;
    }

    public static boolean matchTextPresent(int stringId) throws Exception {
        boolean result = true;
        try {
            onView(withText(stringId)).check(matches((withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))));
        } catch (NoMatchingViewException | AssertionFailedError err) {
            result = false;
        }
        return result;
    }

    public static boolean matchTextPresent(String text) throws Exception {
        boolean result = true;
        try {
            onView(withText(containsString(text))).check(matches(isDisplayed()));
        } catch (NoMatchingViewException err) {
            result = false;
        }
        return result;
    }

    public static boolean matchOneOfTextsPresent(String[] textArray) throws Exception {
        boolean result = true;
        for (String line : textArray) {
            try {
                onView(withText(containsString(line))).check(matches(isDisplayed()));
                result = true;
                break;
            } catch (NoMatchingViewException err) {
                err.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    public static boolean matchViewPresent(int viewId) {
        return matchViewPresent(viewId, true);
    }

    public static boolean matchViewPresent(int viewId, boolean useScrolling) {
        boolean result = true;
        try {
            if (useScrolling) {
                onView(withId(viewId)).perform(scrollTo()).check(matches(isDisplayed()));
            } else {
                onView(withId(viewId)).check(matches(isDisplayed()));
            }
        } catch (NoMatchingViewException | AssertionError err) {
            result = false;
        }
        return result;
    }


    public static void matchViewVisible(int withId) throws Exception {
        onView(AllOf.allOf(withId(withId),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).
                check(matches(isDisplayed()));
    }

    public static void matchViewGone(int withId) {
        onView(AllOf.allOf(withId(withId),
                withEffectiveVisibility(ViewMatchers.Visibility.GONE))).
                check(matches(not(isDisplayed())));
    }

    public static boolean matchViewNotPresent(int viewId) throws Exception {
        boolean result = true;
        try {
            onView(withId(viewId)).check(matches(not(isDisplayed())));
            result = true;
        } catch (AssertionError err) {
            result = false;
        }
        return result;
    }

    public static boolean waitForViewGone(int viewId) throws Exception {
        if (matchViewPresent(viewId)) {
            while (true) {
                try {
                    onView(withId(viewId)).check(matches(not(isDisplayed())));
                    break;
                } catch (AssertionError err) {
                    Clock.sleep(WaitFor.MEDIUM_CYCLE);
                }
            }
        }
        return true;
    }

    public static void matchViewsPresent(int[] viewsIdArray) throws Exception {
        for (int id : viewsIdArray) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    public static void matchViewsWithText(int[] viewsIdArray, Object[] names) throws Exception {
        for (int i = 0; i < viewsIdArray.length; i++) {
            if (names[i].getClass() == Integer.class) {
                matchViewWithText(viewsIdArray[i], Integer.parseInt(names[i].toString()));
            } else {
                matchViewWithText(viewsIdArray[i], names[i].toString());
            }
        }
    }

    public static void matchViewWithText(int id, String text) {
        onView(allOf(withId(id), withText(containsString(text))))
                .check(matches(isDisplayed()));
    }

    public static void matchViewWithText(int id, int textId) {
        onView(allOf(withId(id), withText(textId)))
                .check(matches(isDisplayed()));
    }

    public static void swipeRightOnView(int id) throws InterruptedException {
        onView(withId(id)).perform(swipeRight());
        Clock.sleep(300);
    }

    public static void swipeRightOnView(int id, int times) throws InterruptedException {
        if(times == 1){
            swipeRightOnView(times);
            return;
        }
        while (!(times == 0)) {
            onView(withId(id)).perform(swipeRight());
            times--;
        }

    }


    /**
     * ----------------FOR TOAST-------------------
     */
    public static Matcher<Root> isToast() {
        return new Matchers.ToastMatcher();
    }

    public static void isToastMessageDisplayed(String textId) {
        onView(withText(textId)).inRoot(isToast()).check(matches(isDisplayed()));
    }

   //-------------other helper methods ------------//

    /**
     * Use generateStringDate for generate calendar date in format dd.MM.yyyy
     * for example today date is 12.06.2016 (generateStringDate(false, 0))
     * generateStringDate(false, 15) return date 15 days more than the current
     * specifically, 27.06.2016
     * generateStringDate(true, 15) return date 15 days less than the current
     * specifically, 29.05.2016
     */

    public static String generateStringDate(boolean before, long days) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date();
        if (before) {
            Date date = new Date(d.getTime() - days * 24 * 3600 * 1000);
            return dateFormat.format(date);
        } else {
            Date date = new Date(d.getTime() + days * 24 * 3600 * 1000);
            return dateFormat.format(date);
        }
    }


    public static int[] generateTime(boolean before, int minutes) {
        DateFormat minuteFormat = new SimpleDateFormat("mm");
        DateFormat hourFormat = new SimpleDateFormat("HH");
        int time[] = {0, 0};
        Date d = new Date();

        if (before) {
            Date date = new Date(d.getTime() - minutes * 60 * 1000);
            time[0] = Integer.parseInt(hourFormat.format(date));
            time[1] = Integer.parseInt(minuteFormat.format(date));
            return time;
        } else {
            Date date = new Date(d.getTime() + minutes * 60 * 1000);
            time[0] = Integer.parseInt(hourFormat.format(date));
            time[1] = Integer.parseInt(minuteFormat.format(date));
            return time;
        }
    }

    /**
     * FOR CALENDAR
     * int[] calendar =  generateIntegerCalendar(generateStringDate(false, 15))
     * int date  = calendar[0]
     * int month  = calendar[1]
     * int year  = calendar[2]
     */
    public static int[] generateIntegerCalendar(String stringDate) {
        String strArray[] = stringDate.split("\\.");
        int[] array = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            array[i] = parseInt(strArray[i]);
            System.out.println(strArray[i]);
        }
        return array;
    }


    // ---- for @Before -------



    static String[] getPackageVersion(String packageName){
        PackageInfo pInfo = null;
        try {
            pInfo = WatchManClassConsole.mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            logAction(e.getMessage());
        }
        String version = pInfo.versionName;
        int verCode = pInfo.versionCode;
        return new String[]{version, String.valueOf(verCode)};
    }

    private static void setVersionCode(){
        Consts.OtherData.currentVersionCode = Integer.parseInt(getPackageVersion(WatchManClassConsole.packageName)[1]);
    }

    public static boolean isOld(){
        return Consts.OtherData.currentVersionCode <= Consts.OtherData.oldVerCode;
    }

    // ---- for @After -------
    public static void actionsInTheEnd() throws Exception {
        if(Report.MAKE_SCREENSHOTS) {
            while (!SCREENSHOT_SAVED) {
                Clock.sleep(200);
            }
        }
    }

    public static void intentFragment(Context context, String fragmentClassName) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent("ua.your.package.FragmentEnvironment");
        bundle.putString("fragment", fragmentClassName);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }



    public static void setStartTime() {
        startTime = System.nanoTime() / 1000000;//milliseconds
        saveTime(startTime);
    }

    static void saveTime(long time) {
        sPref = WatchManClassConsole.mActivity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong("time", time);
        ed.commit();
    }

    static long loadTime() {
        long lastLoginTime;
        sPref = WatchManClassConsole.mActivity.getPreferences(Activity.MODE_PRIVATE);
        lastLoginTime = sPref.getLong("time", 0);
        return lastLoginTime;
    }

    public static void getDeviceInfo() {
        String s = "Debug-infos:"
                + "OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
                + " OS API Level: " + android.os.Build.VERSION.SDK_INT
                + " Device: " + android.os.Build.DEVICE
                + "Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        System.out.println("ROBO message - " + s);

        if (android.os.Build.PRODUCT.equals("nakasi")) {
            //сравнение не работает с MODEL and MANUFACTURER
            System.out.println("ROBO message - ASUS TABLET ");
        } else {
            System.out.println("ROBO message - perhaps it is SMARTPHONE ");
        }
    }



    public static void writeToFile(String filename, String data) {
        if (createFolder()) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Environment.getExternalStorageDirectory() + "/Espresso-data/" + filename), "utf-8"))) {
                writer.write(data);
            } catch (IOException e) {
            }
        } else {
        }
    }

    public static void writeFields(String filename, List<String> fields) throws Exception {
        if (createFolder()) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Environment.getExternalStorageDirectory() + "/Espresso-data/" + filename), "utf-8"))) {

                for (String text : fields) {
                    writer.write(text);
                }
            } catch (IOException e) {
            }
        } else {
        }
    }


    public static boolean createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Espresso-data");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

    public static int takePosition(Matcher<View> childView, int startPosition) {
        int position = startPosition;
        int list = DisplayReader.getList()[0];

        while (true) {
            try {
                onData(org.hamcrest.Matchers.anything())
                        .atPosition(position)
                        .onChildView(childView)
                        .check(matches(isDisplayed()));
                break;
            } catch (NoMatchingViewException err) {
                if (position == list) break;
                position++;
            }
        }

        return position;
    }

    public static int takePosition(Matcher<View> childView) {
        return takePosition(childView, 0);
    }

    public static void clickOnAutoCompleteText(String lookingText) throws Exception {
        onData((hasToString(containsString(lookingText))))
                .inRoot(RootMatchers.isTouchable())
                .perform(click());
    }


    public static void showInToast(final String message) {
        Intent intent = new Intent(SERVICE_PACKAGE);
        intent.putExtra("message", message);
        intent.putExtra("type", "toast");
        WatchManClassConsole.mContext.startService(intent);
    }

    public static void makeNotify(String message) {
        Intent intent = new Intent(SERVICE_PACKAGE);
        intent.putExtra("message", message);
        intent.putExtra("type", "notify");
        WatchManClassConsole.mContext.startService(intent);
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}


