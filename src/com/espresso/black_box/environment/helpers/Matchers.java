package com.espresso.black_box.environment.helpers;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Roman Karnaukh on 02.06.2016.
 */

public final class Matchers {
    private Matchers() {
    }

    /**
     * for clicking on element after taking it position
     */
    public static ViewAction clickXY(final float x, final float y) {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};
                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    public static ViewAction setTextFast(final String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TextView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(text);
            }
        };
    }

    public static ViewAction showBottomSheet() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(LinearLayout.class));
            }

            @Override
            public String getDescription() {
                return "Drag bottom sheet";
            }

            @Override
            public void perform(UiController uiController, View view) {
                LinearLayout bottomSheet =  (LinearLayout) view;
//                BottomSheetBehavior mBottomSheetBehavior;
//
//                mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        };
    }


    public static View returnView(Matcher<View> matcher) {
        View[] viewHolder = new View[] { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "getting view by matcher ";
            }

            @Override
            public void perform(UiController uiController, View view){
                viewHolder[0] = view;
            }
        });
        return viewHolder[0];
    }


    public static class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    // windowToken == appToken means this window isn't contained by any other windows.
                    // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                    return true;
                }
            }
            return false;
        }

    }

    /**Click by bounds / coordinates*/
    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

//    public static Matcher<View> withInputLayoutHintText(Matcher<String> stringMatcher) {
//        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
//            @Override
//            public boolean matchesSafely(TextInputLayout view) {
//                CharSequence hint = view.getHint().toString();
//                return stringMatcher.matches(hint);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with InputLayout hint text: " + stringMatcher.toString());
//            }
//        };
//    }
//
//
//   public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
//        return new TypeSafeMatcher<View>() {
//
//            @Override
//            public boolean matchesSafely(View view) {
//                if (!(view instanceof TextInputLayout)) {
//                    return false;
//                }
//
//                CharSequence error = ((TextInputLayout) view).getError();
//
//                if (error == null) {
//                    return false;
//                }
//
//                String hint = error.toString();
//
//                return expectedErrorText.equals(hint);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//            }
//        };
//   }

    public static Matcher<Object> backgroundShouldHaveColor(int expectedColor) {
        return buttonShouldHaveBackgroundColor(equalTo(expectedColor));
    }

    private static Matcher<Object> buttonShouldHaveBackgroundColor(final Matcher<Integer> expectedObject) {
        final int[] color = new int[1];
        return new BoundedMatcher<Object, TextView>( TextView.class) {
            @Override
            public boolean matchesSafely(final TextView t) {

                color[0] = t.getCurrentTextColor();


                if( expectedObject.matches(color[0])) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(final Description description) {
                // Should be improved!
                description.appendText("Color did not match "+color[0]);
            }
        };
    }

}








