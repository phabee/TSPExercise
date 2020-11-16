package ch.zhaw.ciel.mse.alg.tsp.metaheuristics;

import java.util.Arrays;
import java.util.List;

import ch.zhaw.ciel.mse.alg.tsp.utils.Instance;
import ch.zhaw.ciel.mse.alg.tsp.utils.Point;
import ch.zhaw.ciel.mse.alg.tsp.utils.Utils;

/**
 * create an initial solution by NN and improve by 2-opt
 * @author fabian.leuthold
 *
 */
public class MultiNearestNeighbor {

	public static List<Point> solve(Instance instance) {
		Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
		Arrays.sort(points, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

		double minCost = Double.MAX_VALUE;
		List<Point> bestSolution = null;
		// start with initial solution from NN
		for (int i = 0; i < points.length; i++) {
			List<Point> solution = NearestNeighbor.solve(instance, i);
			double cost = Utils.euclideanDistance2D(solution);
			if (cost < minCost) {
				minCost = cost;
				bestSolution = solution;
			}
		}
		return bestSolution;
	}

}
