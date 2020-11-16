package ch.zhaw.ciel.mse.alg.tsp.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import ch.zhaw.ciel.mse.alg.tsp.metaheuristics.GreedyInsertion;

public class RandomInstanceRunner {
	

	public static void main(String[] args) throws IOException {
		runRandomInstances(20);
	}

	
	private static void runRandomInstances(int numberOfInstances) throws IOException {
		//Run algorithms on some randomly generated instances

		String pathToSolutions = "RandomInstances_Solutions";
		List<Point> solution = null;

		System.out.println("Generating and solving " + numberOfInstances + " random TSP instances...");
		for (int i = 0; i < numberOfInstances; i++){

			//Generate a random instance.
			int numPoints = 100 * i + 50;
			Instance randomInstance = Utils.getRandomTspInstance("" + i, "random instance with " + numPoints + " points.", numPoints);
			System.out.println("   Random instance number " + i + " generated with " + numPoints + " points.");


			//Get a random path (most likely quite long)
			solution = Utils.getAllPermutations(randomInstance.getPoints()).next();
			Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_randomPath.html"));
			System.out.println("        Random Path has length: " + Utils.euclideanDistance2D(solution));

			//Get the solution obtained with the nearest neighbor heuristic.
			//solution = NearestNeighbor.solve(randomInstance);
			//Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_nearest.html"));
			//System.out.println("        Nearest Neighbor solution has length: " + Utils.euclideanDistance2D(solution));

			//Get the solution obtained with the greedy insertion heuristic.
			solution = GreedyInsertion.solve(randomInstance);
			Printer.writeToSVG(randomInstance, solution, Paths.get(pathToSolutions, i + "_greedy.html"));
			System.out.println("        Greedy Insertion solution has length: " + Utils.euclideanDistance2D(solution));

			System.out.println();

		}
	}
}
