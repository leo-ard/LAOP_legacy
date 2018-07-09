package org.lrima.utils;

public class Random {
	
	public static int getRandomIntegerValue(int min, int max) {
		return (int) (Math.random()*(max-min)+min);
	}
	
	public static int getRandomIntegerValue(int max) {
		return (int) (Math.random()*max);
	}
	
	public static float getRandomFloatValue(float max) {
		return (float) (Math.random()*max);
	}
	
	public static float getRandomFloatValue(float min, float max) {
		return (float) (Math.random()*(max-min)+min);
	}
	
	public static double getRandomDoubleValue(double max) {
		return (double) (Math.random()*max);
	}
	
	public static double getRandomDoubleValue(double min, double max) {
		return (double) (Math.random()*(max-min)+min);
	}

}
