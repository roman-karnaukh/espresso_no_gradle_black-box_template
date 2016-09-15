package com.espresso.black_box.environment.addition;

import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;

import junit.framework.AssertionFailedError;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.espresso.black_box.environment.helpers.R.getString;
import static com.espresso.black_box.environment.helpers.Report.CANONICAL_CLASS_NAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Karnaukh Roman on 03.08.2016.
 */

public class WaitFor {

    public static int LARGE_TIMEOUT = 20000;
    public static int  BIG_TIMEOUT = 4000;
    public static int  MEDIUM_TIMEOUT = 2000;
    public static int  SMALL_TIMEOUT = 1000;
    public static int  SHORT_CYCLE = 200;
    public static int  MEDIUM_CYCLE = 250;
    public static int  LONG_CYCLE = 500;

    public  static  boolean waitForView(int view, int time) throws InterruptedException {
        int timeToBreak = time;
        int timeOfCycle = SHORT_CYCLE;
        boolean result;

        while(true) {
            try {
                onView(withId(view)).check(matches(isDisplayed()));
                result = true;
                break;
            } catch (NoMatchingViewException | AssertionFailedError |AmbiguousViewMatcherException err) {
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if(timeToBreak <= 0){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public  static  boolean waitForDescriptionIn(String description, int time) throws InterruptedException {
        int timeToBreak = time;
        int timeOfCycle = LONG_CYCLE;
        boolean result;

        while(true) {
            try {
                onView(withContentDescription(description)).check(matches(isDisplayed()));
                result = true;
                break;
            } catch (NoMatchingViewException|AmbiguousViewMatcherException err) {
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if(timeToBreak <= 0){
                    result =false;
                    break;
                }
            }
        }
        return result;
    }

    public  static  boolean waitForString(int string, int time) throws InterruptedException {
        int timeToBreak = time;
        int timeOfCycle = MEDIUM_CYCLE;
        boolean result;

        while(true) {
            try {
                onView(withText(containsString(getString(string)))).check(matches(isDisplayed()));
                result = true;
                break;
            } catch (NoMatchingViewException err) {
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if(timeToBreak <= 0){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public  static boolean waitForText(String string, int time) throws InterruptedException {
        int timeToBreak = time;
        int timeOfCycle = MEDIUM_CYCLE;
        boolean result = true;

        while(true) {
            try {
                onView(withText(containsString(string))).check(matches(isDisplayed()));
                result = true;
                break;
            } catch (Exception err) {
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if(timeToBreak <= 0){
                    result = false;
                    break;
                }
            } catch (Throwable e){
                break;
            }
        }
        return result;
    }

    public  static  boolean waitForTextNotIsDisplayed(String string, int time) throws InterruptedException {

        int timeToBreak = time;
        int timeOfCycle = MEDIUM_CYCLE;
        boolean result;

        while(true) {
            try {
                onView(withText(string)).check(matches(not(isDisplayed())));
                result = true;
                break;
            } catch (Exception err) {
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if(timeToBreak <= 0){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public  static  void waitForOneOfSeveralViews(int view, int view2, int time) throws InterruptedException {
        int timeToBreak = time;
        int timeOfCycle = MEDIUM_CYCLE;
        while(true) {
            try {
                onView(withId(view)).check(matches(isDisplayed()));
                break;
            } catch (NoMatchingViewException err) {
                try {
                    onView(withId(view2)).check(matches(isDisplayed()));
                    break;
                } catch (NoMatchingViewException err2){
                    Clock.sleep(timeOfCycle);
                    timeToBreak -= timeOfCycle;
                    if(timeToBreak <= 0) break;
                }
            }
        }
    }

    public  static boolean waitForToast(String message, int timeout) throws Exception {
        int timeToBreak = timeout;
        int timeOfCycle = SHORT_CYCLE;
        boolean result;

        while (true) {
            try {
                onView(withText(containsString(message))).inRoot(Actions.isToast()).check(matches(isDisplayed()));
                result = true;
                System.out.println("ROBO message - find toast \"" + message +"\"");
                break;
            } catch (NoMatchingRootException err) {
                System.out.println("ROBO message -  cannot find toast \"" + message + "\" ");
                System.out.println("ROBO message -  " + err );
                Clock.sleep(timeOfCycle);
                timeToBreak -= timeOfCycle;
                if (timeToBreak <= 0) {
                    result = false;
                    break;
                }
            }
        }
        if(CANONICAL_CLASS_NAME.contains("negative")){
            waitForToastGone(message);
        }
        return result;
    }

    public static void waitForToastGone(String message) throws Exception {
        Clock.sleep(MEDIUM_CYCLE);
        while (true) {
            try {
                onView(withText(containsString(message))).inRoot(Actions.isToast()).check(matches(org.hamcrest.Matchers.not(isDisplayed())));
                break;
            } catch (AssertionError err) {
                Clock.sleep(MEDIUM_CYCLE);
            }catch(NoMatchingRootException|NoMatchingViewException e){
                break;
            }
        }
    }


    public  static  void waitForTextPresent(int id) throws Exception {
        waitForString(id, SMALL_TIMEOUT);
    }
}
