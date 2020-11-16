package ch.zhaw.ciel.mse.alg.tsp.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BinaryOperator;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 * 
 *         Some utilities for meta heuristics.
 */
public class Utils {
	/**
	 * Get the Euclidean distance between two points in 2D.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double euclideanDistance2D(Point a, Point b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	/**
	 * Get the cost for a whole trip using the Euclidean distance. The way from the
	 * last point in the list to the start is included.
	 * 
	 * @param points
	 * @return
	 */
	public static double euclideanDistance2D(List<Point> points) {
		double totalDistance = 0;

		Point currentPoint = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			Point nextPoint = points.get(i);
			totalDistance += euclideanDistance2D(currentPoint, nextPoint);
			currentPoint = nextPoint;
		}

		totalDistance += euclideanDistance2D(currentPoint, points.get(0));

		return totalDistance;
	}

	/**
	 * the same as euclideanDistance2D, however it takes an integer list containing
	 * the indices of the points in the sequence to be visited.
	 * 
	 * @param points
	 * @param tmpTourInt
	 * @return
	 */
	public static double euclideanDistance2D(Point[] points, List<Integer> tmpTourInt) {
		List<Point> pointList = new ArrayList<Point>();
		for (int stop : tmpTourInt) {
			pointList.add(points[stop]);
		}
		return euclideanDistance2D(pointList);
	}

	/**
	 * Get the point with the lowest id.
	 * 
	 * @param points
	 * @return
	 */
	public static Point getLowestIdPoint(Collection<Point> points) {
		return points.stream().reduce(null, new BinaryOperator<Point>() {

			@Override
			public Point apply(Point t, Point u) {
				if (t == null) {
					return u;
				} else if (u == null) {
					return t;
				} else {
					if (t.getId() < u.getId()) {
						return t;
					} else {
						return u;
					}
				}
			}
		});
	}

	/**
	 * Generate a random instance.
	 * 
	 * @param name      The name of the generated instance.
	 * @param comment   The comment for the generated instance.
	 * @param numPoints The amount of points to generate randomly.
	 * @return An instance with given name and comment and randomly distributed
	 *         points.
	 */
	public static Instance getRandomTspInstance(String name, String comment, int numPoints) {
		int maxX = 2000;
		int maxY = 2000;

		Random rand = new SecureRandom();

		List<Point> points = new ArrayList<>(numPoints);
		int id = 0;

		for (int i = 0; i < numPoints; i++) {
			int x = rand.nextInt(maxX);
			int y = rand.nextInt(maxY);

			points.add(new Point(id, x, y));
			id += 1;
		}

		return new Instance(name, comment, points);
	}

	/**
	 * Get an iterator over all possible distinct permutations in lexicographical
	 * order (by {@link Point} id) </br>
	 * [1,3,2] -> [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
	 * 
	 * @param points The points to get permutations for.
	 * @return An iterator over all possible permutations. Starts with the first
	 *         permutation in lexicographical order.
	 */
	public static Iterator<List<Point>> getAllPermutations(Collection<Point> points) {
		return new PermutationIterator(points);
	}

	/**
	 * Get an iterator over all possible distinct permutations in lexicographical
	 * order starting with the given permutation (by {@link Point} id) </br>
	 * [1,3,2] -> [[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1],[1,2,3]]
	 * 
	 * @param points The points to get permutations for.
	 * @return An iterator over all possible permutations. Starts with the given
	 *         permutation.
	 */
	public static Iterator<List<Point>> getAllPermutations(List<Point> points) {
		return new PermutationIterator(points);
	}

	/**
	 * 
	 * @author Marek Arnold (arnd@zhaw.ch)
	 * 
	 *         Iterator over all possible permutations in lexicographical order.
	 */
	public static class PermutationIterator implements Iterator<List<Point>> {
		private final List<Point> points;

		private List<Point> nextPermutation;

		/**
		 * Instantiate a new {@link Iterator} beginning with the first permutation in
		 * lexicographical order.
		 * 
		 * @param points The points to generate permutations for.
		 */
		public PermutationIterator(Collection<Point> points) {
			this.points = new ArrayList<>(points);
			this.points.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
			this.nextPermutation = this.points;
		}

		/**
		 * Instantiate an {@link Iterator} beginning with the given permutation.
		 * 
		 * @param points The points in the permutation to begin with.
		 */
		public PermutationIterator(List<Point> points) {
			this.points = new ArrayList<>(points);
			this.nextPermutation = this.points;
		}

		@Override
		public boolean hasNext() {
			return nextPermutation != null;
		}

		@Override
		public List<Point> next() {
			List<Point> res = nextPermutation;
			nextPermutation = getNextPermutation(nextPermutation);

			// All permutations visited, therefore stop iterator.
			if (nextPermutation.equals(points)) {
				nextPermutation = null;
			}

			return res;
		}

		private static List<Point> getNextPermutation(List<Point> points) {
			List<Point> result = new ArrayList<>(points);

			int k = result.size() - 2;
			while (result.get(k).getId() >= result.get(k + 1).getId()) {
				k--;
				if (k < 0) {
					result.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
					return result;
				}
			}
			int l = result.size() - 1;
			while (result.get(k).getId() >= result.get(l).getId()) {
				l--;
			}
			Point p = result.get(k);
			result.set(k, result.get(l));
			result.set(l, p);

			int length = result.size() - (k + 1);
			for (int i = 0; i < length / 2; i++) {

				p = result.get(k + 1 + i);
				result.set(k + 1 + i, result.get(result.size() - i - 1));
				result.set(result.size() - i - 1, p);
			}
			return result;
		}
	}

}
