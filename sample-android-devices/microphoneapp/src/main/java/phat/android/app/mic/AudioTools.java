/*
 * Based on code from https://github.com/DjDCH/SoundStreamVisualizer/blob/master/src/main/java/com/djdch/dev/soundstreamvisualizer/util/SoundTools.java
 * Modified by Pablo Campillo-Sanchez <pabcampi@ucm.es>
 */

package phat.android.app.mic;

public class AudioTools {

	public static String doubleToString(double value) {
		long v = Math.round(value * 100);
		return String.valueOf(((double) v) / 100.0f);
	}

	public static double volumeRMS(byte[] raw, int offset, int size) {
		double sum = 0d;
		if (raw.length == 0) {
			return sum;
		} else {
			for (int ii = offset; ii < size; ii += 2) {
				sum += (raw[ii] | raw[ii + 1]);
			}
		}
		double average = sum / size;

		double sumMeanSquare = 0d;
		for (int ii = offset; ii < size; ii += 2) {
			sumMeanSquare += Math.pow((raw[ii] | raw[ii + 1]) - average, 2d);
		}
		double averageMeanSquare = sumMeanSquare / size;
		double rootMeanSquare = Math.pow(averageMeanSquare, 0.5d);

		return rootMeanSquare;
	}

}
