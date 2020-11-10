package ch.zhaw.ciel.mse.alg.tsp.metaheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.zhaw.ciel.mse.alg.tsp.utils.Point;
import ch.zhaw.ciel.mse.alg.tsp.utils.Instance;
import ch.zhaw.ciel.mse.alg.tsp.utils.Utils;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 * 
 * The greedy insertion meta heuristic to find a solution for a given TSP.
 * The tour starts at the {@link Point} with the lowest id and the points are inserted 
 * by ascending id.
 * For positions with an equal cost, the first such position is taken.
 */
public class RandomSampling {
	/**
	 * Solve the given TSP using greedy insertion.
	 * @param instance The instance to solve.
	 * @return A list of points. Each point in the instance appears exactly once.
	 */
	public static List<Point> solve(Instance instance) {
		Point[] points = instance.getPoints().toArray(new Point[instance.getPoints().size()]);
		Arrays.sort(points, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
		
		List<Integer> nextIndices = new ArrayList<Integer>();
				
		for (int i = 0; i < points.length; i++) {
			Integer nextRandomIndex = getNextRandomIndex(points, nextIndices);
			nextIndices.add(nextRandomIndex);
		}
		int[] intNextIndices = toArray(nextIndices);
		
		List<Point> solution = buildTourFromIndices(points, intNextIndices);
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


	private static Integer getNextRandomIndex(Point[] points, List<Integer> nextIndices) {
		// calculate set of indexes not visited 
		Set<Integer> allIdx = getIntegerSet(points.length);
		allIdx.removeAll(nextIndices);
		// now get random entry from set
		Integer retVal = (int) (Math.random()*allIdx.size());
		return retVal;
	}

	private static Set<Integer> getIntegerSet(int length) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i=0; i< length; i++)
			set.add(i);
		return set;
	}


	private static List<Point> buildTourFromIndices(Point[] points, int[] nextIndices) {
		//Walk along next indices to build solution.
		List<Point> solution = new ArrayList<>(points.length);
		int j = 0;
		for(int i = 0; i < points.length; i++){
			solution.add(points[j]);
			j = nextIndices[j];
		}
		return solution;
	}

	private static void findBestInsertPosition(Point[] points, int[] nextIndices) {
		//Find the best position to insert for each remaining point
		for(int i = 2; i < points.length; i++){
			double lowestDistanceIncrease = Double.POSITIVE_INFINITY;
			int lowestDistanceIncreaseIdx = -1;
			
			for(int j = 0; j < i; j++){
				//Increased cost of tour if point i is inserted in place j
				double distanceIncrease = Utils.euclideanDistance2D(points[j], points[i]) + Utils.euclideanDistance2D(points[i], points[nextIndices[j]]) - Utils.euclideanDistance2D(points[j], points[nextIndices[j]]);
				if (distanceIncrease < lowestDistanceIncrease){
					lowestDistanceIncrease = distanceIncrease;
					lowestDistanceIncreaseIdx = j;
				}
			}
			
			nextIndices[i] = nextIndices[lowestDistanceIncreaseIdx];
			nextIndices[lowestDistanceIncreaseIdx] = i;
		}
	}

}
