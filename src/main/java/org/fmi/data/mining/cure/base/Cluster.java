package org.fmi.data.mining.cure.base;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 *	Represents the idea of the cluster in the algorithm.
 *
 * @author diana
 *
 * @param <T>
 * The type of the points given.
 */
public class Cluster implements Iterable<Point>{

	/**
	 * The points in the cluster.
	 */
	private Set<Point> points;
	
	/**
	 * Constructor of the class.
	 * 
	 * @param points
	 * 				The points in the cluster.
	 */
	public Cluster(Point...points) {
		this.points = new HashSet<Point>();
		for (Point point : points) {
			this.points.add(point);
		}
	}
	
	/**
	 * Constructor of the class.
	 * 
	 * @param points
	 * 				The points in the cluster.
	 */
	public Cluster(Set<Point> points) {
		this.points = new HashSet<Point>(points);
	}
	
	@Override
	public Iterator<Point> iterator() {
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
	public void addPoint(Point point) {
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
	public double distance(Cluster cluster) {
		double min = Double.POSITIVE_INFINITY;
		for (Point point1 : cluster) {
			for (Point point2 : this) {
				double distance = point1.distance(point2);
				if (min > distance) {
					min = distance;
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
	private Point findCentroid() {
		Point result = new Point();
//		for (Point point : this) {
//			if (result.getCoordinates() == null)
//			{
//				result.setCoordinates((Vector<Double>) point.getCoordinates().clone());
//			}
//			else
//			{
//				for(int i = 0; i < point.getCoordinates().size(); i++) 
//				{
//					double coordinate = point.getCoordinate(i);
//					result.setCoordinate(i, point.getCoordinate(i) + coordinate);
//				}
//			}
//		}
//		for(int i = 0; i < result.getCoordinates().size(); i++) 
//		{
//			result.setCoordinate(i, result.getCoordinate(i)/this.getSize());
//		}
		result.calculateAsMeanOf(points);
		return result;
	}
	
	/**
	 * Finds the representatives of the cluster.
	 * 
	 * @return
	 * 			A new cluster consisting of the 
	 * 			representatives of the current one.
	 */
	public Cluster findRepresentatives() {
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
	public Cluster moveCluster(double alpha) {
		Point centroid = findCentroid();
		Cluster newCluster = new Cluster();
		for(Iterator<Point> itr = this.iterator(); itr.hasNext(); ) {
			Point pt = itr.next();
			int dim = pt.getDimension();
			Vector<Double> dirToCent = pt.directionTo(centroid);
			double distToCentr = pt.distance(centroid);
			Vector<Double> moveVector = new Vector<Double>(); 
			for(int i = 0; i < dim; i++) {
				moveVector.add(distToCentr * alpha * dirToCent.get(i));
			}
			newCluster.addPoint(pt.move(moveVector));
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
	public Cluster merge(Cluster cluster) {
		Set<Point> mergedPoints = new HashSet<Point>();
		mergedPoints.addAll(cluster.points);
		mergedPoints.addAll(this.points);
		return new Cluster(mergedPoints);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Cluster)) {
			return false;
		}
		Cluster cluster = (Cluster) obj;
		return this.points.equals(cluster.points);
	}
}
