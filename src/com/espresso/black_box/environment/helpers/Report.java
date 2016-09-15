package com.espresso.black_box.environment.helpers;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static com.espresso.black_box.environment.addition.Actions.logAction;
import static com.espresso.black_box.environment.helpers.screenshot.Shot.screenShotPath;


/**
 * Created by Roman Karnaukh on 04.07.2016.
 */
public class Report {
    public static LinkedHashSet<String> SCREENSHOTS_ARRAY = new LinkedHashSet<String>();
    public static HashMap<String, String> LINKS_MAP = new HashMap<>();
    public static HashMap<String, String> EXCEPTIONS = new HashMap<>();
    public static HashMap<String, String> COLORS = new HashMap<>();
    public static boolean MAKE_SCREENSHOTS = false;
    public static String CLASS_NAME;
    public static String CANONICAL_CLASS_NAME;
    public static String TEST_METHOD_NAME;
    public static int ALL_TESTS;
    public static int FILED_TESTS;

    public static void clearScreenshotsArray() throws Exception {
        SCREENSHOTS_ARRAY.clear();
    }

    public static void optimizeScreenshotsArray() throws Exception {
        ArrayList<String> for_remove = new ArrayList<>();
        int line_count = SCREENSHOTS_ARRAY.size();
        int index = 0;

        for(String line : SCREENSHOTS_ARRAY) {
            if(line.contains(".png")){
                File path = screenShotPath(line);
                if (!(path.exists() && path.isFile())) {
                    if(!(index == line_count-1)){
                        for_remove.add(line);
                    }
                }
            }
            index++;
        }

        for(String remove : for_remove){
            SCREENSHOTS_ARRAY.remove(remove);
            logAction("SCREENSHOT " + remove + " REMOVED");
        }

    }

    public static int getTestsCount(){
        int testsCount = 0;
        for(String line: SCREENSHOTS_ARRAY){
            if(!line.contains(".png")){
                testsCount ++;
            }
        }
        ALL_TESTS = ALL_TESTS + testsCount;
        return testsCount;
    }

    public static int getTestsCountWithErrors(){
        int filedTests = EXCEPTIONS.size();
        FILED_TESTS = FILED_TESTS + filedTests;
        return filedTests;
    }

    public static void clearExceptions(){
        EXCEPTIONS.clear();
    }

    public static String traceToStr(StackTraceElement[] err){
        String stackTrace = "";
        for( StackTraceElement element :err){
            stackTrace = stackTrace + element.toString() + "\n";
        }

        return stackTrace;
    }

    public static void saveReport(String report, String links) throws Exception {
        File reportsDirectory = new File(Environment.getExternalStorageDirectory() +
                "/Espresso-data/reports/");
        reportsDirectory.mkdirs();

        File file = new File(reportsDirectory, "/" + CLASS_NAME +".html");
        File fileRes = new File(reportsDirectory, "/index.html");
        writeToFile(file, report);
        writeToFile(fileRes, links);
    }

    public static void writeToFile(File file, String text) throws Exception{
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(text.getBytes());
        } finally {
            stream.close();
        }
    }

    public static void createReport() throws Exception {
        ListToHtmlTransformer rendererList = new ListToHtmlTransformer();

        optimizeScreenshotsArray();
        String renderedHtmlShots = rendererList.render( SCREENSHOTS_ARRAY );
        String renderedHtmlLinks = rendererList.renderLinks( LINKS_MAP );

        saveReport(renderedHtmlShots, renderedHtmlLinks);
        clearScreenshotsArray();
        clearExceptions();
    }

    public static class ListToHtmlTransformer {

        public String render( java.util.Collection< String > reports ) throws Exception{
            int failedTests = getTestsCountWithErrors();
            int allTests = getTestsCount();
            String backGroundColor = getColour(allTests, failedTests);
            COLORS.put(CLASS_NAME, backGroundColor);
            LINKS_MAP.put(CLASS_NAME,
                    "<td><font color=\"red\"><b> failed: " + failedTests + " </b></font></td>" +
                            "<td><font color=\"green\"><b> success: " +( allTests - failedTests )+" </b></font></td>" +
                            "<td><font color=\"black\"><b> total: " + allTests + " </b></font></td>");

            StringBuilder html = new StringBuilder();
            html.append( "<!doctype html>\n" );
            html.append( "<html lang='en'>\n" );

            html.append( "<head>\n" );
            html.append( "<meta charset='utf-8'>\n" );
            html.append( "<title>"+CLASS_NAME+" Report</title>\n" );
            html.append( "<style type=\"text/css\">\n" +
                    "/* скрываем чекбоксы и блоки с содержанием */\n" +
                    ".hide,\n" +
                    ".hide + label ~ div{\n" +
                    "    display: none;\n" +
                    "}\n" +
                    "/* вид текста label */\n" +
                    ".hide + label {\n" +
                    "    padding: 0;\n" +
                    "    color: green;\n" +
                    "    cursor: pointer;\n" +
                    "    display: inline-block;\n" +
                    "}\n" +
                    "/* вид текста label активном переключателе */\n" +
                    ".hide:checked + label {\n" +
                    "    color: red;\n" +
                    "    border-bottom: 0;\n" +
                    "}\n" +
                    "/* когда чекбокс активен показываем блоки с содержанием  */\n" +
                    ".hide:checked + label + div {\n" +
                    "    display: block; \n" +
                    "    background: #efefef;\n" +
                    "    -moz-box-shadow: inset 3px 3px 10px #7d8e8f;\n" +
                    "    -webkit-box-shadow: inset 3px 3px 10px #7d8e8f;\n" +
                    "    box-shadow: inset 3px 3px 10px #7d8e8f;\n" +
                    "    margin-top: 10px;\n" +
                    "    margin-left: 20px;\n" +
                    "    padding: 10px;\n" +
                    "    /* чуточку анимации при появлении */\n" +
                    "     -webkit-animation:fade ease-in 0.5s; \n" +
                    "     -moz-animation:fade ease-in 0.5s;\n" +
                    "     animation:fade ease-in 0.5s; \n" +
                    "}\n" +
                    "/* анимация при появлении скрытых блоков */\n" +
                    "@-moz-keyframes fade {\n" +
                    "    from { opacity: 0; }\n" +
                    "to { opacity: 1 }\n" +
                    "}\n" +
                    "@-webkit-keyframes fade {\n" +
                    "    from { opacity: 0; }\n" +
                    "to { opacity: 1 }\n" +
                    "}\n" +
                    "@keyframes fade {\n" +
                    "    from { opacity: 0; }\n" +
                    "to { opacity: 1 }   \n" +
                    "}\n" +
                    ".hide + label:before {\n" +
                    "    background-color: #1e90ff;\n" +
                    "    color: #fff;\n" +
                    "    content: \"+\";\n" +
                    "    display: block;\n" +
                    "    float: left;\n" +
                    "    font-size: 12px; \n" +
                    "    font-weight: bold;\n" +
                    "    height: 16px;\n" +
                    "    line-height: 16px;\n" +
                    "    margin: 0px 5px;\n" +
                    "    text-align: center;\n" +
                    "    width: 16px;\n" +
                    "    -webkit-border-radius: 50%;\n" +
                    "    -moz-border-radius: 50%;\n" +
                    "    border-radius: 50%;\n" +
                    "}\n" +
                    ".hide:checked + label:before {\n" +
                    "    content: \"\\2212\";\n" +
                    "}\n" +
                    "/* demo box position */\n" +
                    ".demo {\n" +
                    "   \n" +
                    "}"+
                    "  </style>\n" );
            html.append( "<style type=\"text/css\">\n" +
                    "   .filed { \n" +
                    "    padding: 5px;\n" +
                    "    padding-right: 20px; \n" +
                    "    border: solid 1px black; \n" +
                    "    height: auto;\n" +
                    "    width: 25%;\n" +
                    "    border: none;\n" +
                    "    -webkit-appearance: none;\n" +
                    "    background: #ED9199;\n" +
                    "    box-shadow: 0 0 10px rgba(0, 0, 0, .5);\n" +
                    "    border-radius: 7px 7px 7px 7px;\n" +
                    "    font-weight: bold;"+
                    "   }\n" +
                    "    " +
                    "  </style>" );

            html.append( "<style type=\"text/css\">\n" +
                    "   .topbutton {\n" +
                    "width:100px;\n" +
                    "border:2px solid #ccc;\n" +
                    "background:#f7f7f7;\n" +
                    "text-align:center;\n" +
                    "padding:10px;\n" +
                    "position:fixed;\n" +
                    "bottom:50px;\n" +
                    "right:50px;\n" +
                    "cursor:pointer;\n" +
                    "color:#333;\n" +
                    "font-family:verdana;\n" +
                    "font-size:12px;\n" +
                    "border-radius: 5px;\n" +
                    "-moz-border-radius: 5px;\n" +
                    "-webkit-border-radius: 5px;\n" +
                    "-khtml-border-radius: 5px;\n" +
                    "}\n" +
                    "  </style>" );

 html.append( "<style type=\"text/css\">\n" +
                    "   .mainbutton {\n" +
                    "width:100px;\n" +
                    "border:2px solid #0B84DA;\n" +
                    "background:#B2ECFA;\n" +
                    "text-align:center;\n" +
                    "padding:10px;\n" +
                    "position:fixed;\n" +
                    "bottom:100px;\n" +
                    "right:50px;\n" +
                    "cursor:pointer;\n" +
                    "color:#333;\n" +
                    "font-family:verdana;\n" +
                    "font-size:12px;\n" +
                    "border-radius: 5px;\n" +
                    "-moz-border-radius: 5px;\n" +
                    "-webkit-border-radius: 5px;\n" +
                    "-khtml-border-radius: 5px;\n" +
                    "}\n" +
                    "  </style>" );

            html.append( "</head>\n" );

            html.append( "<body>\n" );
            html.append( "<h1>List of Screenshots in "+CLASS_NAME+"</h1>\n" );
            html.append( "<a href=\"#\" title=\"Вернуться к началу\" class=\"topbutton\">Наверх</a>\n" );
            html.append( "<a href=\"index.html\" title=\"Go home\" class=\"mainbutton\">Main</a>\n" );

            if(!(EXCEPTIONS.size()==0)){

                html.append( "<div class=\"filed\">\n" );
                for (Map.Entry<String, String> entry : EXCEPTIONS.entrySet()){
                    html.append( "<a href=\"#"+entry.getKey()+"\" style=\"color:#fff\">"+entry.getKey()+"</a><br>" );
                }
                html.append( "</div>\n" );
            }

            for ( String report : reports ) {
                if(report.contains(".png")){
                    html.append( "<img src=\"screenshots/" + report + "\" alt=\"Screenshot\" width=\"250\">\n" );
                }else{
                    html.append("<a name=\""+ report +"\"></a>");
                    html.append( "<h3>In Test: </h3><h2><b>" + report + "</b><name=\""+ report +"\"></h2>\n" );

                    if(EXCEPTIONS.get(report) == null) {
                        html.append("<hr align=\"center\" width=\"100%\" size=\"2\" color=\"#73EDB5\" /><br>\n");
                    }else{
                        html.append("<hr align=\"center\" width=\"100%\" size=\"2\" color=\"#EC8396\" />\n");
                        html.append("<div class=\"demo\">\n" +
                                "    <input type=\"checkbox\" id=\""+report+"*\" class=\"hide\">\n" +
                                "    <label for=\""+report+"*\"></label> \n" +
                                "    <div>\n");
                        html.append( "<h5>" + EXCEPTIONS.get(report) + "</h5>" );
                        html.append("</div>\n</div><br>");
                    }
                }
            }
            html.append( "</body>\n" );
            html.append( "</html>" );
            return html.toString();
        }

        public String renderLinks( HashMap< String, String > reports ) {
            StringBuilder html = new StringBuilder();
            html.append( "<!doctype html>\n" );
            html.append( "<html lang='en'>\n" );

            html.append( "<head>\n" );
            html.append( "<meta charset='utf-8'>\n" );
            html.append( "<title>Test Links</title>\n" );
            html.append( "<style type=\"text/css\">\n" +
                    "   .info { \n" +
                    "    width: auto; \n" +
                    "    padding: 5px;\n" +
                    "    padding-right: 20px; \n" +
                    "    border: solid 1px black; \n" +
                    "    color: #fff;\n" +
                    "    height: auto;\n" +
                    "    width: 30%;\n" +
                    "    border: none;\n" +
                    "    -webkit-appearance: none;\n" +
                    "    background: #52b99b;\n" +
                    "    box-shadow: 0 0 10px rgba(0, 0, 0, .5);\n" +
                    "    border-radius: 7px 7px 7px 7px;\n" +
                    "    font-weight: bold;"+
                    "   }\n" +
                    "  </style>" );
            html.append( "</head>\n" );

            html.append( "<body>\n" );
            html.append( "<h1>Result Report</h1>\n" );
            html.append( deviceInfo() );
            html.append( "<br>\n");
            html.append( testsResult() );
            html.append( "<p><table>\n" );

            for (Map.Entry<String, String> entry : reports.entrySet()){
                html.append( "<tr >\n" );
                html.append( "<td> <label "+ getLabelStyle(COLORS.get(entry.getKey()))+" > </label></td>\n" );
                html.append("<td><a href=\"" + entry.getKey() + ".html\">" + entry.getKey() + " </td>" +entry.getValue() + "<a/>\n");
                html.append( "</tr>\n" );

            }

            html.append( "</table>\n" );
            html.append( "</body>\n" );
            html.append( "</html>" );
            return html.toString();
        }

    }

    public static String deviceInfo(){
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"info\">");
        html.append( "<h2> " + Build.MANUFACTURER.toUpperCase() + " "+ Build.MODEL + "</h2>" +
                "<h3> OS API Level: " + Build.VERSION.SDK_INT+"\n</h3>" );
        html.append( "</div>\n");
        return html.toString();
    }

    public static String testsResult(){
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"info\" align=\"center\">");
        html.append( "filed: " + FILED_TESTS );
        html.append( " success: " + (ALL_TESTS - FILED_TESTS) );
        html.append( " total: " + ALL_TESTS);
        html.append( "</div>\n");
        return html.toString();
    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    public static String getLabelStyle(String color){
        String style = "style = \"\n" +
                "     background-color: "+color+";\n" +
                "     color: #fff;\n" +
                "     display: block;\n" +
                "     float: left;\n" +
                "     font-size: 12px; \n" +
                "     font-weight: bold;\n" +
                "     height: 16px;\n" +
                "     line-height: 16px;\n" +
                "     margin: 0px 5px;\n" +
                "     text-align: center;\n" +
                "     width: 16px;\n" +
                "    -webkit-border-radius: 50%;\n" +
                "    -moz-border-radius: 50%;\n" +
                "    border-radius: 50%\"\n";
        return style;
    }

    public static String getColour(int allTests, int filedTests){
        String color = "#52b99b";
        if (!(filedTests == 0)) {
            double result = (double)filedTests / (double)allTests;
            logAction("Result " + result);
            if (isBetween(result, 0.76, 1)) {
                color = "#F86DA0";
            } else if (isBetween(result, 0.51, 0.75)) {
                color = "#F8AA6D";
            } else if (isBetween(result, 0.26, 0.50)) {
                color = "#F8D86D";
            } else if (isBetween(result, 0.01, 0.25)) {
                color = "#D4F86D";
            }
        }
        return color;
    }
}
