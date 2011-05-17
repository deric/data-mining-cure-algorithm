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
public class Point<T extends Number> {

	/**
	 * The coordinates of the point.
	 */
	private Vector<T> coordinates;
	
	/**
	 * Constructs a point with given coordinates.
	 * 
	 * @param coordinates
	 * 					The coordinates of the point.
	 */
	public Point(T...coordinates) {
		this.coordinates = new Vector<T>();
		for(T coordinate : coordinates) {
			this.coordinates.add(coordinate);
		}
	}
	
	/**
	 * Constructs a point with given coordinates.
	 * 
	 * @param coordinates
	 * 					The coordinates of the point.
	 */
	public Point(Vector<T> coordinates) {
		this.coordinates = new Vector<T>(coordinates);
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
	public void setCoordinate(int place, T value) {
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
	public T getCoordinate(int place) {
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
	@SuppressWarnings("unchecked")
	public T distance(Point<T> otherPoint) {
		if (getDimension() != otherPoint.getDimension()) {
			return null;
		}
		T result = null;
		for(int i = 0; i < getDimension(); ++i) {
			result = (T) result.plus(getCoordinate(i).
					mult(otherPoint.getCoordinate(i)));
		}
		return (T) (result == null ? null : result.sqrt());
	}
	
	/**
	 * Moves the point with value param.
	 * 
	 * @param value
	 * 			The parameter with which the point will be moved.
	 * @return
	 * 			New point with moved coordinates,
	 */
	@SuppressWarnings("unchecked")
	public Point<T> move(Vector<T> values) {
		if(values.size() != this.getDimension()) {
			//throw exception or something
			return null;
		}
		Vector<T> newCoordinates = new Vector<T>();
		Iterator<T> itr = values.iterator();
		for(T coordinate : this.coordinates) {
			newCoordinates.add((T) coordinate.plus(itr.next()));
		}
		return new Point<T>(newCoordinates);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Point)) {
			return false;
		}
		Point<T> point = (Point<T>) obj;
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
