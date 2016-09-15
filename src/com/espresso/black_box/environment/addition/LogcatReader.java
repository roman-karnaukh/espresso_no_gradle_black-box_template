package com.espresso.black_box.environment.addition;

import com.espresso.black_box.environment.data.Consts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Karnaukh Roman on 15.08.2016.
 */
public class LogcatReader {
    public static void clearLogcat() throws Exception{
        new ProcessBuilder()
                .command("logcat", "-c")
                .redirectErrorStream(true)
                .start();
    }

    public static String readLogcat(String filter, long timeout) {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + timeout;

         while (readLogcat(filter).length() == 0){
             Clock.sleep(100);
             if(System.currentTimeMillis() >= endTime) break;
         }

        return readLogcat(filter);
    }

    public static String readLogcat(String filter){
        return readLogcat(Consts.Servers.RESPONSE_FILTER, filter);
    }

    public static String readLogcat(String mainFilter, String filter){
        StringBuilder log = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains(mainFilter)){
                    if(line.contains(filter)){
                        log.append(line + "\n");
                    }
                }
            }

        }
        catch (IOException e) {}

        return log.toString();
    }
}
