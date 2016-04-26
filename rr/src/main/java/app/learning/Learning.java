package app.learning;

import app.Constants;
import app.frame.FrameFactory;

import java.util.ArrayList;
import java.util.List;


public class Learning implements Constants {

    private static List<String> letters;
    private static List<Long> starts;
    private static List<Long> ends;
    private static List<Long> timesPressed;
    private static List<Long> pauses;
    private static final FrameFactory frameFactory;

    static {
        letters = new ArrayList<String>();
        starts = new ArrayList<Long>();
        ends = new ArrayList<Long>();
        timesPressed = new ArrayList<Long>();
        pauses = new ArrayList<Long>();
        frameFactory = new FrameFactory(letters, starts, ends, timesPressed, pauses);
    }

    public static void main(String[] args) {
        frameFactory.getLearningFrame().setVisible(true);
    }
}
