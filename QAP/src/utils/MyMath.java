package utils;

public class MyMath {

	/**
	 * Method to calculate a linear interpolation between two points
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param val
	 * @return the linear interpolation for val using the given points
	 */
	public static double linearInterpolation(double x1, double y1, double x2, double y2, double val) {
		
		if(x2 == x1)
			return 1.0;
		else
			return y1 + (y2-y1)* ((val-x1)/(x2-x1));
		
	}
}
