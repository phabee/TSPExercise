package ch.zhaw.ciel.mse.alg.tsp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 *
 *         A Traveling Salesman Problem instance.
 */
public class Instance {
	private final static int NAME_INDEX = 0, COMMENT_INDEX = 1;

	private final String name;
	private final String comment;

	private final List<Point> points;
	private Point[] pointsArr = null;

	private double dima[][] = null;

	/**
	 * Load a TSP instance from a given file.
	 * 
	 * @param filePath The path to the file.
	 * @return The loaded TSP instance.
	 * @throws IOException If an exception occurs while reading the file.
	 */
	public static Instance load(Path filePath) throws IOException {
		try (BufferedReader tspFileReader = Files.newBufferedReader(filePath, Charset.forName("UTF-8"))) {
			List<String> lines = Files.readAllLines(filePath);
			List<String> header = lines.subList(0, 2);

			List<Point> points = lines.subList(2, lines.size()).stream().map(line -> line.split("\t"))
					.map(splits -> new Point(Integer.parseInt(splits[0]), Double.parseDouble(splits[1]),
							Double.parseDouble(splits[2])))
					.collect(Collectors.toList());

			String name = header.get(NAME_INDEX).split("\t")[1];
			String comment = header.get(COMMENT_INDEX).split("\t")[1];

			return new Instance(name, comment, points);
		}
	}

	/**
	 * Create a new TSP instance.
	 * 
	 * @param name    The name of this instance.
	 * @param comment A comment about this instance.
	 * @param points  The points.
	 */
	public Instance(String name, String comment, List<Point> points) {
		super();
		this.name = name;
		this.comment = comment;
		this.points = points;
		this.pointsArr = points.toArray(new Point[points.size()]);
		Arrays.sort(pointsArr, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
		initializeDima();
	}

	private void initializeDima() {
		if (points.size() > 10000) {
			return;
		}
		System.out.println("   Initialization of dima-calculation started...");
		Point[] array = points.toArray(new Point[points.size()]);
		Arrays.sort(array, (p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

		this.dima = new double[array.length][array.length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (i <= j) {
					this.dima[i][j] = Utils.euclideanDistance2D(array[i], array[j]);
				} else {
					this.dima[i][j] = this.dima[j][i];
				}
			}
		}
		System.out.println("   Dima-calculation complete!");
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public List<Point> getPoints() {
		return points;
	}

	public List<Point> clonePointList() {
		ArrayList<Point> clonedPoints = new ArrayList<Point>(getPoints().size());

		for (Point p : getPoints()) {
			clonedPoints.add(p);
		}
		return clonedPoints;
	}

	public double getDistance(int i, int j) {
		if (dima != null) {
			return dima[i][j];
		} else {
			return Utils.euclideanDistance2D(pointsArr[i], pointsArr[j]);
		}
	}
}