package app;

public interface Constants {
    static final String TIMES_PRESSED_FILE_NAME = "storage/timesPressed.txt";
    static final String PAUSES_FILE_NAME = "storage/pauses.txt";
    static double studentCriterion(int i) {
        if(i <= 0) {
            return -1;
        } else if(i > 0 && i <= 10) {
            return 2.228;
        } else if(i > 10 && i <= 20) {
            return 2.086;
        } else if(i > 20 && i <= 30) {
            return 2.042;
        } else if(i > 30 && i <= 40) {
            return 2.018;
        } else if(i > 40 && i <= 50) {
            return 1.999;
        } else if(i > 50 && i <= 60) {
            return 1.987;
        } else {
            return 1.96;
        }
    }
}
