package ch.zhaw.ciel.mse.alg.tsp.metaheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.zhaw.ciel.mse.alg.tsp.utils.Instance;
import ch.zhaw.ciel.mse.alg.tsp.utils.Point;
import ch.zhaw.ciel.mse.alg.tsp.utils.TSPHelper;
import ch.zhaw.ciel.mse.alg.tsp.utils.Utils;

public class PilotMethod {
	/**
	 * Solve TSP instance using Pilot method together with Nearest Neighbor 
	 * 
	 * @param instance
	 * @return
	 */
	public static List<Point> solve(Instance instance) {
		Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
		Arrays.sort(points, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
		
		List<Integer> tour = new ArrayList<Integer>();
		// start at point 1
		tour.add(0);
		while (tour.size() < points.length) {
			int nextPoint = findNextBestPilot(points, tour);
			tour.add(nextPoint);
		}
		tour.add(tour.get(0));

		List<Point> solution = TSPHelper.buildTourFromIndices(points, tour);
		return solution;
	}

	private static int findNextBestPilot(Point[] points, List<Integer> tour) {
		double bestCost = Double.MAX_VALUE;
		int bestStop = -1;
		for (int i = 0; i < points.length; i++) {
			// only check unvisited nodes as next candidates
			if (!tour.contains(i)) {
				List<Integer> tmpTour = new ArrayList<Integer>(tour);
				tmpTour.add(i);
				List<Integer> tmpTourInt = completeNearestNeighborTour(points, tmpTour);
				double cost = Utils.euclideanDistance2D(points, tmpTourInt);
				if (cost < bestCost) {
					bestCost = cost;
					bestStop = i;
				}
			}
		}
		assert bestStop != -1;
		return bestStop;
	}

	private static List<Integer> completeNearestNeighborTour(Point[] points, List<Integer> tour) {
		List<Integer> tmpTour = new ArrayList<Integer>(tour);
		// as long as not all points have been visited
		while(tmpTour.size() < points.length) {
			int bestNextStop = NearestNeighbor.findNextNearest(tmpTour, points);
			assert bestNextStop != -1;
			tmpTour.add(bestNextStop);
		}
		return tmpTour;
	}
}
