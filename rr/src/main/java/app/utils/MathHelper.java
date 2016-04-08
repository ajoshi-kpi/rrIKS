package app.utils;

import java.util.List;

public class MathHelper {

    public static double calculateExpectation(List<Long> numbers) {
        double expectation = 0.0;
        for (Long number : numbers) {
            expectation += number;
        }
        expectation /= numbers.size();
        return expectation;
    }

    public static double calculateDispersion(List<Long> numbers) {
        double dispersion = 0.0;
        for (Long number : numbers) {
            dispersion += Math.pow((number - calculateExpectation(numbers)), 2.0);
        }
        dispersion /= numbers.size();
        return Math.sqrt(dispersion);
    }

}
