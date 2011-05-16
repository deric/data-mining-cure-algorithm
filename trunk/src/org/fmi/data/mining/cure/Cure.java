package org.fmi.data.mining.cure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fmi.data.mining.cure.base.Cluster;
import org.fmi.data.mining.cure.base.Point;
import org.fmi.data.mining.cure.helpers.DoubleNum;

public class Cure {
	
	public static void cureAlgor(double alphaN,int k, Set<Point<DoubleNum>> points) {
		DoubleNum alpha = new DoubleNum(alphaN);
		//init clusters
		List<Cluster<DoubleNum>> originalClusters = new ArrayList<Cluster<DoubleNum>>();
		for (Point<DoubleNum> point : points) {
			originalClusters.add(new Cluster<DoubleNum>(point));
		}
		//find the pair min clusters
		Integer firstCluster = 0;
		Integer secondCluster = 0;
		findMinDistClusters(originalClusters, firstCluster, secondCluster);
		Cluster<DoubleNum> Ck = originalClusters.get(firstCluster);
		Cluster<DoubleNum> Cl = originalClusters.get(secondCluster);
		//merge clusters
		Cluster<DoubleNum> Cm = Ck.merge(Cl);
		originalClusters.set(firstCluster, Cm);
		originalClusters.remove(secondCluster);
		while(originalClusters.size() > k) {
			//representatives
			Cluster<DoubleNum> representatives = Cm.findRepresentatives();
			//move cluster
			Cluster<DoubleNum> movedRepresentatives = representatives.moveCluster(alpha);
			//find min clusters
			List<Cluster<DoubleNum>> allClusterRepresentatives = new ArrayList<Cluster<DoubleNum>>();
			for (int i = 0; i < originalClusters.size(); ++i) {
				if (i != firstCluster) {
					allClusterRepresentatives.add(originalClusters.get(i).findRepresentatives());
				}
			}
			int minCl = findMinDistCluster(allClusterRepresentatives, movedRepresentatives);
			originalClusters.set(firstCluster, Cm.merge(originalClusters.get(minCl)));
			originalClusters.remove(minCl);
		}
	}

	private static int findMinDistCluster(List<Cluster<DoubleNum>> clusters, Cluster<DoubleNum> cluster) {
		DoubleNum minDistance = null;
		int min = -1;
		for (int i = 0; i < clusters.size(); ++ i) {
			Cluster<DoubleNum> cl = clusters.get(i);
				DoubleNum distance = 
					cl.distance(cluster);
				if (minDistance == null || (minDistance.isGreaterThan(distance))) {
					min = i;
					minDistance = distance;
				}
		}
		return min;
	}
	private static void findMinDistClusters(List<Cluster<DoubleNum>> clusters,
			Integer firstCluster, Integer secondCluster) {
		DoubleNum minDistance = null;
		for(int i = 0; i < clusters.size(); ++ i) {
			for(int j = i + 1; j < clusters.size(); ++ j) {
				DoubleNum distance = 
					clusters.get(i).distance(clusters.get(j));
				if (minDistance == null || (minDistance.isGreaterThan(distance))) {
					firstCluster = new Integer(i);
					secondCluster = new Integer(j);
					minDistance = distance;
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Set<Point<DoubleNum>> points = new HashSet<Point<DoubleNum>>();
		//init points
		//......
		cureAlgor(alphaN,k, points);
			
	}
}
