package org.fmi.data.mining.cure.base;

import java.awt.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.fmi.data.mining.cure.helpers.Number;

/**
 *	Represents the idea of the cluster in the algorithm.
 *
 * @author diana
 *
 * @param <T>
 * The type of the points given.
 */
public class Cluster<T extends Number> implements Iterable<Point<T>>{

	/**
	 * The points in the cluster.
	 */
	private Set<Point<T>> points;
	
	/**
	 * Constructor of the class.
	 * 
	 * @param points
	 * 				The points in the cluster.
	 */
	public Cluster(Point<T>...points) {
		this.points = new HashSet<Point<T>>();
		for (Point<T> point : points) {
			this.points.add(point);
		}
	}
	
	/**
	 * Constructor of the class.
	 * 
	 * @param points
	 * 				The points in the cluster.
	 */
	public Cluster(Set<Point<T>> points) {
		this.points = new HashSet<Point<T>>(points);
	}
	
	@Override
	public Iterator<Point<T>> iterator() {
		return points.iterator();
	}
	
	/**
	 * The size of the cluster - the number of the points in it.
	 * 
	 * @return
	 * 			The number of the points in the cluster.
	 */
	public int getSize() {
		return this.points.size();
	}
	
	/**
	 * Adds a point to the given cluster.
	 * 
	 * @param point
	 * 				The point to be addes.
	 */
	public void addPoint(Point<T> point) {
		this.points.add(point);
	}
	
	/**
	 * Finds the distance between two the cluster and another one.
	 * 
	 * @param cluster
	 * 					The other cluster.
	 * @return
	 * 					The distance between the clusters.
	 */
	//very dummy!!!!!!!!!!!!!!!!
	public T distance(Cluster<T> cluster) {
		T min = null;
		for (Point<T> point1 : cluster) {
			for (Point<T> point2 : this) {
				T distance = point1.distance(point2);
				if (min == null) {
					min = distance;
				} else {
					if (min.isGreaterThan(distance)) {
						min = distance;
					}
				}
			}
		}
		return min;
	}
	
	/**
	 * Finds the centroid of the cluster.
	 * 
	 * @return
	 * 			The centroid of the cluster.
	 */
	private Point<T> findCentroid() {
		//TODO:Svetlio
		return null;
	}
	
	/**
	 * Finds the representatives of the cluster.
	 * 
	 * @return
	 * 			A new cluster consisting of the 
	 * 			representatives of the current one.
	 */
	public Cluster<T> findRepresentatives() {
		//TODO: Svetlio
		return null;
	}
	
	/**
	 * Moves the cluster with the given alpha distance.
	 * 
	 * @param alpha
	 * 			Fixed parameter.
	 * @return
	 * 			Another cluster, with points that are moved with alpha/
	 */
	public Cluster<T> moveCluster(T alpha) {
		//TODO:Georgi - use Point.move and Cluster.findCentroid
		Cluster<T> newCluster = new Cluster<T>();
		for(Iterator<Point<T>> itr = this.iterator(); itr.hasNext(); ) {
			Point<T> pt = itr.next();
			int dim = pt.getDimension();
			Vector<T> moveVector = new Vector<T>();
			for(int i; i < dim; i++) {
				
			}
			newCluster.addPoint();
		}
		return newCluster;
	}
	
	/**
	 * Merges two clusters together.
	 * 
	 * @param cluster
	 * 					The other cluster.
	 * @return
	 * 					New cluster consisting of all the points
	 * 					in the current cluster plus the ones in the other cluster.
	 */
	public Cluster<T> merge(Cluster<T> cluster) {
		Set<Point<T>> mergedPoints = new HashSet<Point<T>>();
		mergedPoints.addAll(cluster.points);
		mergedPoints.addAll(this.points);
		return new Cluster<T>(mergedPoints);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Cluster)) {
			return false;
		}
		Cluster<T> cluster = (Cluster<T>) obj;
		return this.points.equals(cluster.points);
	}
}
