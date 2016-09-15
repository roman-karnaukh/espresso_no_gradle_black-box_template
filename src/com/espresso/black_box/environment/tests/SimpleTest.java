package com.espresso.black_box.environment.tests;

import com.espresso.black_box.environment.addition.Actions;
import com.espresso.black_box.environment.addition.Clock;
import com.espresso.black_box.environment.addition.WatchManClassConsole;

import org.junit.Test;

/**
 * Created by Karnaukh Roman on 13.09.2016.
 */
public class SimpleTest extends WatchManClassConsole {

    @Test
    public void makeNotify() {
        Clock.sleep(2000);
        Actions.makeNotify("Test Notify");
    }


}
