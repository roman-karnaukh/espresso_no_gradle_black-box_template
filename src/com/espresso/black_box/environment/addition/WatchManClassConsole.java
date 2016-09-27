package com.espresso.black_box.environment.addition;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.espresso.black_box.environment.helpers.Report;
import com.espresso.black_box.environment.helpers.screenshot.Shot;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.espresso.black_box.environment.addition.Actions.makeNotify;
import static com.espresso.black_box.environment.addition.Actions.logAction;
import static com.espresso.black_box.environment.addition.Actions.showInToast;
import static com.espresso.black_box.environment.helpers.Report.CANONICAL_CLASS_NAME;
import static com.espresso.black_box.environment.helpers.Report.CLASS_NAME;
import static com.espresso.black_box.environment.helpers.Report.EXCEPTIONS;
import static com.espresso.black_box.environment.helpers.Report.TEST_METHOD_NAME;
import static com.espresso.black_box.environment.helpers.Report.traceToStr;
import static com.espresso.black_box.environment.helpers.ResourcesGetter.getResources;
import static com.espresso.black_box.environment.helpers.ResourcesGetter.getStrings;

/**
 * Created by Karnaukh Roman on 07.07.2016.
 */

public class WatchManClassConsole {
    public static Context mContext = InstrumentationRegistry.getContext();
    public static Activity mActivity;
    private String packageActivity = "your.package.name.MainActivity";
    public static String packageName = "your.package.name";

    @Rule
//    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
//            MainActivity.class);


    public ActivityTestRule<?> mActivityRule = newActivityTestRule(packageActivity);

    private ActivityTestRule newActivityTestRule(String className) {
        return new ActivityTestRule(activityClass(className));
    }

    private static Class<? extends Activity> activityClass(String className) {
        try {
            return (Class<? extends Activity>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Rule
    public TestWatcher watcher = new TestWatcher() {

        @Override
        public Statement apply(Statement base, Description description) {
            CLASS_NAME = description.getTestClass().getSimpleName();
            CANONICAL_CLASS_NAME = description.getTestClass().getCanonicalName();
            TEST_METHOD_NAME = description.getMethodName();
            return super.apply(base, description);
        }

        @Override
        protected void succeeded(Description description) {
            logAction(description.getDisplayName() + " " + "success!");
            makeNotify(CLASS_NAME+"."+TEST_METHOD_NAME + " Success!");
        }

         @Override
         protected void failed(Throwable e, Description description) {
            logAction(description.getDisplayName() + " " + e.getClass().getSimpleName());

             if(!(e.getMessage() == null)){
                 EXCEPTIONS.put(TEST_METHOD_NAME, e.getMessage());
             }else{
                 EXCEPTIONS.put(TEST_METHOD_NAME, e.getMessage() + "\n" + traceToStr(e.getStackTrace()));
             }

             makeNotify(CLASS_NAME+"."+TEST_METHOD_NAME + " Filed");
         }
    };


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
        showInToast(CLASS_NAME + "." + TEST_METHOD_NAME);
//        getResources();
//        getStrings();
    }

    @After
    public void exit() throws Exception{
        Shot.makeScreenshotFromDevice();
    }

    @AfterClass
    public static void generateReport() throws Exception {
        Report.createReport();
    }

}
