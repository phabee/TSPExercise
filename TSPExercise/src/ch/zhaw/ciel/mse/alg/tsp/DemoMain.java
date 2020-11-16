package ch.zhaw.ciel.mse.alg.tsp;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import ch.zhaw.ciel.mse.alg.tsp.metaheuristics.MultiNearestNeighbor;
import ch.zhaw.ciel.mse.alg.tsp.utils.Instance;
import ch.zhaw.ciel.mse.alg.tsp.utils.Point;
import ch.zhaw.ciel.mse.alg.tsp.utils.Printer;
import ch.zhaw.ciel.mse.alg.tsp.utils.Utils;

public class DemoMain {

	public static void main(String[] args) throws IOException {
		runSingleTSPInstance("pr1002");
	}

	private static void runSingleTSPInstance(String instanceName) throws IOException {

		String solutionName = instanceName;

		String pathToInstances = "TSP_Instances";
		String pathToSolutions = "TSP_Solutions";

		String instanceFilenameExtension = ".tsp";
		String solutionFilenameExtension = ".html";

		String pathToInstance = pathToInstances + java.io.File.separator + instanceName + instanceFilenameExtension;
		String pathToSolution = pathToSolutions + java.io.File.separator + solutionName + solutionFilenameExtension;

		System.out.println("Loading instance " + instanceName + "...");
		Instance instance = Instance.load(Paths.get(pathToInstance));

		System.out.println("Instance has " + instance.getPoints().size() + " points.");

		System.out.println("Start generating a solution..."); 
		List<Point> solution;
		
//		solution = NearestNeighbor.solve(instance);
//		solution = RandomSampling.solve(instance);
//		solution = GreedyInsertion.solve(instance);
//		solution = PilotMethod.solve(instance);
		solution = MultiNearestNeighbor.solve(instance);
		System.out.println("Solution for " + instanceName + " has length: " + Utils.euclideanDistance2D(solution));
		System.out.println();

		// Generate Visualization of Result, will be stored in directory pathToSolutions
		Printer.writeToSVG(instance, solution, Paths.get(pathToSolution));
	}

}
