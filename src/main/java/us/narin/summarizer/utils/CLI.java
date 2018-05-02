package us.narin.summarizer.utils;

import org.json.JSONArray;
import us.narin.summarizer.Summarizer;

/**
 * Created by endlessdev on 7/28/17.
 */
public class CLI {
    public static void main(String[] args) {
        System.out.println(new JSONArray(new Summarizer(args[0]).summarize()));
    }
}
