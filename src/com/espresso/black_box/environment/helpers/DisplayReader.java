package com.espresso.black_box.environment.helpers;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by Karnaukh Roman on 16.08.2016.
 */
public class DisplayReader {

    public static int[] getList() {
        final int[] counts = new int[1];
        onView((CoreMatchers.instanceOf(AdapterView.class))).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        return counts;
    }

    public static int[] getFromList(int adapterView) {
        final int[] counts = new int[1];
        onView(allOf(withId(adapterView), isCompletelyDisplayed())).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        return counts;
    }

//    public static int[] getFromRecycler(int adapterView) {
//        final int[] counts = new int[1];
//        onView(withId(adapterView)).check(matches(new TypeSafeMatcher<View>() {
//            @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
//            @Override
//            public boolean matchesSafely(View view) {
//                RecyclerView recyclerView = (RecyclerView) view;
//                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
//                counts[0] = layoutManager.getItemCount();
//                return true;
//            }
//
//            @Override
//            public void describeTo(Description description) {
//
//            }
//        }));
//        return counts;
//    }

    public static String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    public static String getText(final DataInteraction matcher) {
        final String[] stringHolder = { null };
        matcher.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

}
