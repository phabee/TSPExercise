package ch.zhaw.ciel.mse.alg.tsp.metaheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.zhaw.ciel.mse.alg.tsp.utils.Instance;
import ch.zhaw.ciel.mse.alg.tsp.utils.Point;
import ch.zhaw.ciel.mse.alg.tsp.utils.TSPHelper;

public class NearestNeighbor {

	public static List<Point> solve(Instance instance) {
		Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
		Arrays.sort(points, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

		List<Integer> tour = new ArrayList<Integer>();
		tour.add(0);
		while (tour.size() < points.length) {
			int nextPoint = findNextNearest(tour, points, instance);
			tour.add(nextPoint);
			System.out.println("size = " + tour.size());
		}
		tour.add(tour.get(0));

		List<Point> solution = TSPHelper.buildTourFromIndices(points, tour);
		return solution;
	}

	private static int findNextNearest(List<Integer> tour, Point[] points, Instance instance) {
		// get current location
		Integer currentLocation = tour.get(tour.size() - 1);
		double minDist = Double.MAX_VALUE;
		Integer bestNextStop = null;
		for (int i = 0; i < points.length; i++) {
			// only test unvisited points
			if (!tour.contains(i)) {
//				double dist = Utils.euclideanDistance2D(points[currentLocation], points[i]);
				double dist = instance.getDistance(currentLocation, i);
				if (dist < minDist) {
					minDist = dist;
					bestNextStop = i;
				}
			}
		}
		if (bestNextStop == null) {
			throw new IllegalStateException("findNextNearest() didn't succeed to determine next stop.");
		}
		return bestNextStop;
	}
}
