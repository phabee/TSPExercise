package ch.zhaw.ciel.mse.alg.tsp.utils;

import java.util.ArrayList;
import java.util.List;

public class TSPHelper {

	public static List<Point> buildTourFromIndices(Point[] points, List<Integer> tour) {
		int[] tourArray = toArray(tour);
		return buildTourFromIndices(points, tourArray);
	}

	private static List<Point> buildTourFromIndices(Point[] points, int[] tourArray) {
		// Walk along next indices to build solution.
		List<Point> solution = new ArrayList<Point>(points.length);
		for (int i = 0; i < points.length; i++) {
			solution.add(points[tourArray[i]]);
		}
		return solution;
	}

	private static int[] toArray(List<Integer> nextIndices) {
		int[] retVal = new int[nextIndices.size()];
		int idx = 0;
		for (Integer integer : nextIndices) {
			retVal[idx] = integer;
			idx++;
		}
		return retVal;
	}

}
