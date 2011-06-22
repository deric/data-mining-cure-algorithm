/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package weka.clusterers;

import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.core.Capabilities.Capability;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;


public class CureAlgorithm
extends AbstractClusterer 
implements OptionHandler, NumberOfClustersRequestable, WeightedInstancesHandler {

	/** for serialization */
	static final long serialVersionUID = -3235809600124455376L;

	/**
	 * number of clusters to generate
	 */
	private int m_NumClusters = 2;

	private int m_RepObj = 5;

	private double m_ColFactor = 0.1;

	/**
	 * The number of instances in each cluster
	 */
	private int [] m_ClusterSizes;

	/** the distance function used. */
	protected DistanceFunction m_DistanceFunction = new EuclideanDistance();

	/**
	 * the default constructor
	 */
	public CureAlgorithm() {
		super();

	}

	/**
	 * Returns a string describing this clusterer
	 * @return a description of the evaluator suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String globalInfo() {
		return "Cluster data using the CURE.";
	}

	/**
	 * Returns default capabilities of the clusterer.
	 *
	 * @return      the capabilities of this clusterer
	 */
	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();
		result.disableAll();
		result.enable(Capability.NO_CLASS);

		// attributes
		result.enable(Capability.NOMINAL_ATTRIBUTES);
		result.enable(Capability.NUMERIC_ATTRIBUTES);
		result.enable(Capability.MISSING_VALUES);

		return result;
	}

	public void buildClusterer(Instances data) throws Exception {
		getCapabilities().testWithFail(data);

		ReplaceMissingValues m_ReplaceMissingFilter = new ReplaceMissingValues();
		Instances instances = new Instances(data);

		instances.setClassIndex(-1);
		m_ReplaceMissingFilter.setInputFormat(instances);
		instances = Filter.useFilter(instances, m_ReplaceMissingFilter);


	}


	/**
	 * Classifies a given instance.
	 *
	 * @param instance the instance to be assigned to a cluster
	 * @return the number of the assigned cluster as an interger
	 * if the class is enumerated, otherwise the predicted value
	 * @throws Exception if instance could not be classified
	 * successfully
	 */
	@Override
	public int clusterInstance(Instance instance) throws Exception {
		return 0;
	}

	/**
	 * Returns the tip text for this property
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String numClustersTipText() {
		return "set number of clusters";
	}

	/**
	 * set the number of clusters to generate
	 *
	 * @param n the number of clusters to generate
	 * @throws Exception if number of clusters is negative
	 */
	public void setNumClusters(int n) throws Exception {
		if (n <= 0) {
			throw new Exception("Number of clusters must be > 0");
		}
		m_NumClusters = n;
	}

	/**
	 * gets the number of clusters to generate
	 *
	 * @return the number of clusters to generate
	 */
	public int getNumClusters() {
		return m_NumClusters;
	}

  	public String repObjTipText() {
  		return "Number of representetive objects.";  		
  	}
  
	public int getRepObj() {
		return m_RepObj;
	}
	
	public void setRepObj(int m_RepObj) {
		this.m_RepObj = m_RepObj;
	}
	
	public String colFactorTipText() {
		return "Collapse factor (alpha).";
	}
	
	public double getColFactor() {
		return m_ColFactor;
	}
	
	public void setColFactor(double m_ColFactor) {
		this.m_ColFactor = m_ColFactor;
	}

/**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   *         		displaying in the explorer/experimenter gui
   */
  public String distanceFunctionTipText() {
    return "The distance function to use for instances comparison " +
      "(default: weka.core.EuclideanDistance). ";
  }

  /**
   * returns the distance function currently in use.
   *
   * @return the distance function
   */
  public DistanceFunction getDistanceFunction() {
    return m_DistanceFunction;
  }

  /**
   * sets the distance function to use for instance comparison.
   *
   * @param df the new distance function to use
   * @throws Exception if instances cannot be processed
   */
  public void setDistanceFunction(DistanceFunction df) throws Exception {
    if(!(df instanceof EuclideanDistance) &&
       !(df instanceof ManhattanDistance))
      {
        throw new Exception("Greda.");
      }
    m_DistanceFunction = df;
  }

  /**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   * @throws Exception if number of clusters could not be returned
   * successfully
   */
  public int numberOfClusters() throws Exception {
    return m_NumClusters;
  }

  public Enumeration listOptions () {
    Vector result = new Vector();

    result.addElement(new Option(
                                 "\tnumber of clusters.\n"
                                 + "\t(default 2).",
                                 "N", 1, "-N <num>"));
    result.addElement(new Option(
                                 "\tReplace missing values with mean/mode.\n",
                                 "M", 0, "-M"));

    result.add(new Option(
                          "\tDistance function to use.\n"
                          + "\t(default: weka.core.EuclideanDistance)",
                          "A", 1,"-A <classname and options>"));

    result.add(new Option(
                          "\tMaximum number of iterations.\n",
                          "I",1,"-I <num>"));

    result.addElement(new Option(
                                 "\tNumber of executions of the K-means subalgorithm.\n",
                                 "X", 1, "-X"));

    result.addElement(new Option(
                                 "\tWay to choose the cluster to split.\n",
                                 "W", 1, "-W"));


    return  result.elements();
  }
  /**
   * Parses a given list of options. <p/>
   *
   <!-- options-start -->
   * Valid options are: <p/>
   *
   * <pre> -N &lt;num&gt;
   *  number of clusters.
   *  (default 2).
   * </pre>
   *
   * <pre> -V
   *  Display std. deviations for centroids.
   * </pre>
   *
   * <pre> -M
   *  Replace missing values with mean/mode.
   * </pre>
   *
   * <pre> -S &lt;num&gt;
   *  Random number seed.
   *  (default 10)
   * </pre>
   *
   * <pre> -A &lt;classname and options&gt;
   *  Distance function to be used for instance comparison
   *  (default weka.core.EuclidianDistance)
   * </pre>
   *
   * <pre> -I &lt;num&gt;
   *  Maximum number of iterations of the K-means subalgorithm.
   * </pre>
   *
   * <pre> -O
   *  Preserve order of instances.
   * </pre>
   *
   * <pre> -X
   *  Number of executions of the K-means subalgorithm at each splitting.
   * </pre>
   *
   * <pre> -W
   *  Way to choose the cluster to split.
   * </pre>
   *
   <!-- options-end -->
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions (String[] options)
    throws Exception {

    String optionString = Utils.getOption('N', options);

    if (optionString.length() != 0) {
      setNumClusters(Integer.parseInt(optionString));
    }

    String distFunctionClass = Utils.getOption('A', options);
    if(distFunctionClass.length() != 0) {
      String distFunctionClassSpec[] = Utils.splitOptions(distFunctionClass);
      if(distFunctionClassSpec.length == 0) {
        throw new Exception("Invalid DistanceFunction specification string.");
      }
      String className = distFunctionClassSpec[0];
      distFunctionClassSpec[0] = "";

      setDistanceFunction( (DistanceFunction)
                           Utils.forName( DistanceFunction.class,
                                          className, distFunctionClassSpec) );
    }
    else {
      setDistanceFunction(new EuclideanDistance());
    }

  }

  /**
   * Gets the current settings of BisectingKMeans
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  public String[] getOptions () {
    int       	i;
    Vector    	result;
    String[]  	options;

    result = new Vector();

    result.add("-N");
    result.add("" + getNumClusters());

    result.add("-A");
    result.add((m_DistanceFunction.getClass().getName() + " " +
                Utils.joinOptions(m_DistanceFunction.getOptions())).trim());

    return (String[]) result.toArray(new String[result.size()]);
  }

  /**
   * return a string describing this clusterer
   *
   * @return a description of the clusterer as a string
   */
  public String toString()
  {
        String resultString = new String();
        resultString = resultString.concat("Number of clusters: ");
        resultString = resultString.concat(m_NumClusters + "\n");
        resultString = resultString.concat("\n Cluster centroids:\n");
        for (int i = 0; i < m_NumClusters; ++i){
            
        }
        resultString = resultString.concat("\nCluster errors:\n");
        for (int i = 0; i < m_NumClusters; ++i){
        	
        }
        resultString = resultString.concat("\n");
        for (int i = 0; i < m_NumClusters; i++){
        	
        }
        resultString = resultString.concat("\n");
        return resultString;
  }

  /**
   * Returns the revision string.
   *
   * @return		the revision
   */
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1000 $");
  }

  public static void main (String[] argv) {
    runClusterer(new CureAlgorithm(), argv);
  }

}
