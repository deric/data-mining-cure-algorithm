//package org.fmi.data.mining.cure;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.fmi.data.mining.cure.base.Cluster;
//import org.fmi.data.mining.cure.base.Point;
//import org.fmi.data.mining.cure.helpers.DoubleNum;
//
//public class Cure {
//	
//	public static void cureAlgor(double alphaN,int k, Set<Point> points) {
//		double alpha = alphaN;
//		//init clusters
//		List<Cluster> originalClusters = new ArrayList<Cluster>();
//		for (Point point : points) {
//			originalClusters.add(new Cluster(point));
//		}
//		//find the pair min clusters
//		Integer firstCluster = 0;
//		Integer secondCluster = 0;
//		findMinDistClusters(originalClusters, firstCluster, secondCluster);
//		Cluster Ck = originalClusters.get(firstCluster);
//		Cluster Cl = originalClusters.get(secondCluster);
//		//merge clusters
//		Cluster Cm = Ck.merge(Cl);
//		originalClusters.set(firstCluster, Cm);
//		originalClusters.remove(secondCluster);
//		while(originalClusters.size() > k) {
//			//representatives
//			Cluster representatives = Cm.findRepresentatives();
//			//move cluster
//			Cluster movedRepresentatives = representatives.moveCluster(alpha);
//			//find min clusters
//			List<Cluster> allClusterRepresentatives = new ArrayList<Cluster>();
//			for (int i = 0; i < originalClusters.size(); ++i) {
//				if (i != firstCluster) {
//					allClusterRepresentatives.add(originalClusters.get(i).findRepresentatives());
//				}
//			}
//			int minCl = findMinDistCluster(allClusterRepresentatives, movedRepresentatives);
//			originalClusters.set(firstCluster, Cm.merge(originalClusters.get(minCl)));
//			originalClusters.remove(minCl);
//		}
//	}
//
//	private static int findMinDistCluster(List<Cluster> clusters, Cluster cluster) {
//		double minDistance = Double.POSITIVE_INFINITY;
//		int min = -1;
//		for (int i = 0; i < clusters.size(); ++ i) {
//			Cluster cl = clusters.get(i);
//				double distance = cl.distance(cluster);
//				if (minDistance > distance) {
//					min = i;
//					minDistance = distance;
//				}
//		}
//		return min;
//	}
//	private static void findMinDistClusters(List<Cluster> clusters,
//			Integer firstCluster, Integer secondCluster) {
//		double minDistance = Double.POSITIVE_INFINITY;
//		for(int i = 0; i < clusters.size(); ++ i) {
//			for(int j = i + 1; j < clusters.size(); ++ j) {
//				double distance = 
//					clusters.get(i).distance(clusters.get(j));
//				if (minDistance > distance) {
//					firstCluster = new Integer(i);
//					secondCluster = new Integer(j);
//					minDistance = distance;
//				}
//			}
//		}
//	}
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) {
//		Set<Point> points = new HashSet<Point>();
//		//init points
//		//......
//		//cureAlgor(alphaN,k, points);
//			
//	}
//}
