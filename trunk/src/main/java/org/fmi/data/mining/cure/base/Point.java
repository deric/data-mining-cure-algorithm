package org.fmi.data.mining.cure.base;

import java.util.Iterator;
import java.util.Vector;

import org.fmi.data.mining.cure.helpers.Number;

/**
 * Class representing the concept of the point in the mathematics.
 * 
 * @author diana
 *
 * @param <T>
 * 				The type of the points.
 */
public class Point {

	/**
	 * The coordinates of the point.
	 */
	private Vector<Double> coordinates;
	
	/**
	 * Constructs a point with given coordinates.
	 * 
	 * @param coordinates
	 * 					The coordinates of the point.
	 */
	public Point(Double...coordinates) {
		this.coordinates = new Vector<Double>();
		for(Double coordinate : coordinates) {
			this.coordinates.add(coordinate);
		}
	}
	
	/**
	 * Constructs a point with given coordinates.
	 * 
	 * @param coordinates
	 * 					The coordinates of the point.
	 */
	public Point(Vector<Double> coordinates) {
		this.coordinates = new Vector<Double>(coordinates);
	}
	
	/**
	 * The number of coordinates of the current point.
	 * 
	 * @return
	 * 			The dimension of the point.
	 */
	public int getDimension() {
		return this.coordinates.size();
	}
	
	/**
	 * Sets a given coordinate of the current point with a new value.
	 * 
	 * @param place
	 * 				The place of the coordinate to be changed.
	 * @param value
	 * 				The new value of the coordinate.
	 */
	public void setCoordinate(int place, double value) {
		if (place >= 0 && place < getDimension()) {
			this.coordinates.set(place, value);
		}
	}
	
	/**
	 * Gets the coordinate at a specified position.
	 * 
	 * @param place
	 * 				The specified place.
	 * @return
	 * 				The coordinate.
	 */
	public Double getCoordinate(int place) {
		if (place >= 0 && place < getDimension()) {
			return this.coordinates.get(place);
		}
		return null;
	}
	
	/**
	 * Finds the distance between two points.
	 * 
	 * @param otherPoint
	 * 					The other point.
	 * @return
	 * 					The distance between the current and the other point.
	 */

	public double distance(Point otherPoint) {
		return euclideanDistance(otherPoint);

	}
	
	private double euclideanDistance(Point otherPoint) {
		if (getDimension() != otherPoint.getDimension()) {
			return Double.NaN; //throw exception...
		}
		double sum = 0;
		int dimension = getDimension();
		for(int i = 0; i < dimension; ++i) {
			sum += Math.pow(getCoordinate(i) - otherPoint.getCoordinate(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	public Vector<Double> directionTo(Point otherPoint) {
		if (getDimension() != otherPoint.getDimension()) {
			return null; //throw exception...
		}
		
		Vector<Double> result = new Vector<Double>();
		
		double euclDist = euclideanDistance(otherPoint);
		int dimension = getDimension();
		for(int i = 0; i < dimension; i++) {
			result.add((otherPoint.getCoordinate(i) - getCoordinate(i)) / euclDist);
		}
		
		return result;
	}
	
	/**
	 * Moves the point with value param.
	 * 
	 * @param moveVector
	 * 			vector showing the direction and distnace the point will be moved by
	 * @return
	 * 			New point with moved coordinates,
	 */

	public Point move(Vector<Double> moveVector) {
		if(moveVector.size() != this.getDimension()) {
			//throw exception or something
			return null;
		}
		Vector<Double> newCoordinates = new Vector<Double>();
		int dimension = getDimension();
		for(int i = 0; i < dimension; i++) {
			newCoordinates.add(getCoordinate(i) + moveVector.get(i));
		}
		return new Point(newCoordinates);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Point)) {
			return false;
		}
		Point point = (Point) obj;
		if (point.getDimension() != getDimension()) {
			return false;
		}
		for (int i = 0; i < point.getDimension(); ++i) {
			if(! point.getCoordinate(i).equals(getCoordinate(i))) {
				return false;
			}
		}
		return true;
	}

}
