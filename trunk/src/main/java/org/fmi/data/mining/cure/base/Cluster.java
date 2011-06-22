package org.fmi.data.mining.cure.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;


/**
 *	Represents the idea of the cluster in the algorithm.
 *
 * @author diana
 *
 * @param <T>
 * The type of the points given.
 */
public class Cluster implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Cluster leftParent;
	private Cluster rightParent;
	
	protected DistanceFunction distanceFunction;
	
	private int repPointNum = 5;
	
	private double alpha = 0.1;
	
	/**
	 * The points in the cluster.
	 */
	private Instances points;
	
//	private Instances repPoints;
//	
//	private Instances colRepPoints;
	
	public Cluster getLeftChild() {
		return leftParent;
	}

	public Cluster getRightChild() {
		return rightParent;
	}

//	/**
//	 * Constructor of the class.
//	 * 
//	 * @param points
//	 * 				The points in the cluster.
//	 */
//	public Cluster(Point...points) {
//		this.points = new HashSet<Point>();
//		for (Point point : points) {
//			this.points.add(point);
//		}
//	}
	
	/**
	 * Constructor of the class.
	 * 
	 * @param points
	 * 				The points in the cluster.
	 */
	public Cluster(Instances points, int repPointNum, double alpha, Instances in, DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
		this.distanceFunction.setInstances(in);
		this.repPointNum = repPointNum;
		this.alpha = alpha;
		this.points = new Instances(points);
//		repPoints = new Instances(points, repPointNum);
	}
	
//	@Override
//	public Iterator<Point> iterator() {
//		return points.iterator();
//	}
	
	/**
	 * The size of the cluster - the number of the points in it.
	 * 
	 * @return
	 * 			The number of the points in the cluster.
	 */
	public int getSize() {
		return this.points.numInstances();
	}
	
//	/**
//	 * Adds a point to the given cluster.
//	 * 
//	 * @param point
//	 * 				The point to be addes.
//	 */
//	public void addPoint(Point point) {
//		this.points.add(point);
//		
//		centroidDirty = true;
//	}
	
	/**
	 * Finds the distance between two the cluster and another one.
	 * 
	 * @param cluster
	 * 					The other cluster.
	 * @return
	 * 					The distance between the clusters.
	 */
	public double distance(Cluster cluster) {
		Instances colRepPointsThis = getColRepPoints();
		Instances colRepPointsOther = cluster.getColRepPoints();
		
		double min = Double.POSITIVE_INFINITY;
		Instance point1;
		for (int i = 0; i < colRepPointsThis.numInstances(); i++) {
			point1 = colRepPointsThis.get(i);
			Instance point2;
			for (int j = 0; j < colRepPointsOther.numInstances(); j++) {
				point2 = colRepPointsOther.get(j);
				double distance = distanceFunction.distance(point1, point2);
				if (min > distance) {
					min = distance;
				}
			}
		}
		return min;
	}
	

	/**
	 * holds the centroid point
	 */
	private Instance centroid = null;
	/**
	 * Finds the centroid of the cluster.
	 * 
	 * @return
	 * 			The centroid of the cluster.
	 */
	private Instance findCentroid() {
		double [] attrs = new double[points.numAttributes()];
		
		if (centroid != null) return centroid;
		
		for(int i = 0; i < points.numAttributes(); i++) {
			attrs[i] = points.meanOrMode(i);
		}
		centroid = new DenseInstance(1, attrs);
		centroid.setDataset(points);
		
		return centroid;
	}
	
//	/**
//	 * Finds the representative points of the cluster.
//	 * 
//	 * @return
//	 * 			A new cluster consisting of the 
//	 * 			representatives of the current one.
//	 */
//	public Cluster findRepresentatives() {
//		int representativesCount = (int) (this.getSize()*representativesPercent/100);
//		representativesCount = (representativesCount > 1) ? representativesCount : 1;
//		
//		Cluster result = new Cluster();
//		
//		findCentroid();
//		Point point = findTheMostDistantPointFrom(centroid, this, result);
//		result.addPoint(point);
//		while (result.points.size() < representativesCount)
//		{
//			point = findTheMostDistantPointFrom(point, this, result);
//			result.addPoint(point);
//		}
//		return result;
//	}
	
	private Instances getRepPoints() {
		//if(repPoints != null) return repPoints;

		Instances repPoints = new Instances(points);
		
		Instance prevPoint = findCentroid();
		Set<Integer> usedInd = new HashSet<Integer>();
		for(int i = 0; i < repPointNum; i++) {
			double maxDistance = -1;
			int distInstanceInd = 0;
			for(int j = 0; j < points.numInstances(); j++) {
				Instance curInstance = points.get(j);
				double curDistance = distanceFunction.distance(prevPoint, curInstance);
				if(curDistance > maxDistance && !usedInd.contains(j)) {
					distInstanceInd = j;
					maxDistance = curDistance;
				}
			}
			if(repPoints.size() <= i) {
				repPoints.add(i, points.get(distInstanceInd));
			} else {
				repPoints.set(i, points.get(distInstanceInd));
			}
			usedInd.add(distInstanceInd);
			prevPoint = points.get(distInstanceInd);
		}
		
		return repPoints;
	}
	
//	/**
//	 * Finds the the most distant point from a cluster
//	 * to a given point. And excludes the points which
//	 * are present in the second cluster
//	 * 
//	 * @param point
//	 * 				The seed point
//	 * @param inCluster
//	 * 					The other cluster to search from
//	 * @param notInCluster
//	 * 					The exclusive cluster which 
//	 * 					contains the points which we should 
//	 * 					avoid
//	 * @return
//	 * 			A point which is most distant from 
//	 * 			the seed point
//	 */
//	public Point findTheMostDistantPointFrom(Point point, Cluster inCluster, Cluster notInCluster) 
//	{
//		Point result = point;
//		double distance;
//		double maxDistance = 0;
//		for (Point testPoint : inCluster)
//		{
//			if (!notInCluster.points.contains(testPoint))
//			{
//				distance = testPoint.distance(point);
//				if (distance > maxDistance)
//				{
//					result = testPoint;
//				}
//			}
//		}
//		return result;
//	}
	
//	/**
//	 * Moves the cluster with the given alpha distance.
//	 * 
//	 * @param alpha
//	 * 			Fixed parameter.
//	 * @return
//	 * 			Another cluster, with points that are moved with alpha/
//	 */
//	public Cluster moveCluster(double alpha) {
//		findCentroid();
//		Cluster newCluster = new Cluster();
//		for(Iterator<Point> itr = this.iterator(); itr.hasNext(); ) {
//			Point pt = itr.next();
//			int dim = pt.getDimension();
//			Vector<Double> dirToCent = pt.directionTo(centroid);
//			double distToCentr = pt.distance(centroid);
//			Vector<Double> moveVector = new Vector<Double>(); 
//			for(int i = 0; i < dim; i++) {
//				moveVector.add(distToCentr * alpha * dirToCent.get(i));
//			}
//			newCluster.addPoint(pt.move(moveVector));
//		}
//		return newCluster;
//	}
	
	private Instances getColRepPoints() {
//		if(colRepPoints != null) return colRepPoints;
		
		
		Instances repPoints = getRepPoints();
		Instance centroid = findCentroid();
		
		Instances colRepPoints = new Instances(repPoints);
		
		for(int i = 0; i < repPoints.numInstances(); i++) {
			Instance curInstance = colRepPoints.get(i);
			double distToCenter = distanceFunction.distance(curInstance, centroid);
			for(int j = 0; j < curInstance.numAttributes(); j++) {
				double curVal = curInstance.value(j);
				double centrVal = centroid.value(j);
				double newVal = curVal + alpha * distToCenter * (curVal > centrVal ? -1 : 1);
				curInstance.setValue(j, newVal);
			}
		}
		
		return colRepPoints;
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
		Instances mergedInstances = new Instances(points);
		mergedInstances.addAll(cluster.points);
		Cluster newCluster = new Cluster(mergedInstances, repPointNum, alpha, distanceFunction.getInstances(), distanceFunction);
		
		return newCluster;
	}
	
	public boolean contains(Instance inst) {		
		for(int i = 0; i < points.numInstances(); i++) {
			if(distanceFunction.distance(points.instance(i), inst) == 0) return true;			
		}
		
		return false;
	}
	
	public String toString() {
		String returnString = "(";
		
		centroid = findCentroid();
		
		int i;
		for(i = 0; i < centroid.numAttributes() - 1; i++) {
			returnString += centroid.value(i) + ",";
		}
		returnString += centroid.value(i);
		if(leftParent != null) returnString += leftParent.toString();
		if(rightParent != null) returnString += rightParent.toString();
		returnString += ")";
		
		return returnString;
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
