package ch.zhaw.ciel.mse.alg.tsp.utils;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 * 
 * A point in an euclidean 2D space.
 */
public class Point {
	private final int id;
	private final double x, y;
	
	/**
	 * Instantiate a new point with the given coordinates.
	 * @param id The id of this point. Ids among points of the same problem should be distinct.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Point(int id, double x, double y) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
