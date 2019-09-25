package frc.robot;

public class Utils {
    public static boolean isDataInRange(double value, double min, double max) {
        if(min > max) {
            double temp = max;
            max = min;
            min = temp;
        }

        return (value > min || value == min) && (value < max || value == max);
    }
} 