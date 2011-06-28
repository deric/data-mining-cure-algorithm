
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.fmi.data.mining.cure.base.Cluster;


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
	
	private Instances m_instances;

	private double m_ColFactor = 0.1;
	
	private boolean m_ClassifyOnlyInput = true;
	
	private boolean m_SeparateRepPoints = true;

	/** the distance function used. */
	protected DistanceFunction m_DistanceFunction = new EuclideanDistance();
	
	private List<Cluster> clusters = new Vector<Cluster>(); 
	
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
		result.enable(Capability.NUMERIC_ATTRIBUTES);
		result.enable(Capability.MISSING_VALUES);

		return result;
	}

	public void buildClusterer(Instances data) throws Exception {
		getCapabilities().testWithFail(data);
		
		m_instances = data;
	    int nInstances = m_instances.numInstances();
	    if (nInstances == 0) {
	      return;
	    }
	    m_DistanceFunction.setInstances(m_instances);
		
	    clusters.clear();
		
		for(int i = 0; i < data.numInstances(); i++) {
			Instances instance = new Instances(data, i, 1);
			clusters.add(new Cluster(instance, m_RepObj, m_ColFactor, m_DistanceFunction.getInstances(), m_DistanceFunction));
		}
		
		for(;m_NumClusters < clusters.size();) {
			double min = Double.POSITIVE_INFINITY;
			int mergeInd1 = 0;
			int mergeInd2 = 0;
			
			for(int i = 0; i < clusters.size(); i++) {
				for(int j = i + 1; j < clusters.size(); j++) {
					double dist = clusters.get(i).distance(clusters.get(j));
					if(dist < min) {
						mergeInd1 = i;
						mergeInd2 = j;
						min = dist;
					}
				}
			}
			
			Cluster cl1 = clusters.remove(mergeInd1);
			Cluster cl2 = clusters.remove(mergeInd2 - 1);
			Cluster mcl = cl1.merge(cl2);
			clusters.add(mcl);
		}
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

		if(m_SeparateRepPoints) {
			for(int i = 0; i < clusters.size(); i++) {
				if(clusters.get(i).isRepPoint(instance)) {
//					System.out.println("Cluster: " + i);
					return i * 2 + 1; //number after the last cluster
				}
			}
		}
		
		if(m_ClassifyOnlyInput) {
			for(int i = 0; i < clusters.size(); i++) {
				if(clusters.get(i).contains(instance)) return i * (m_SeparateRepPoints ? 2 : 1);
			}
			
			Exception up = new Exception("Cannot");
			throw up;
		}
		else {
			double min = Double.POSITIVE_INFINITY;
			int closestClusterInd = 0;
			for(int i = 0; i < clusters.size(); i++) {
				double distance = clusters.get(i).distance(instance);
				if(distance < min) {
					closestClusterInd = i;
					min = distance;
				}
			}
			return closestClusterInd * (m_SeparateRepPoints ? 2 : 1);
		}
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
    m_DistanceFunction = df;
  }

  public String classifyOnlyInputTipText() {
	  return "If true than an instance will be classified to a given cluster" +
	  	"only if it is identical to an instance in the input data.";
  }
  
  public boolean getClassifyOnlyInput() {
	  return m_ClassifyOnlyInput;
  }

  public void setClassifyOnlyInput(boolean m_ClassOnlyInput) {
	  this.m_ClassifyOnlyInput = m_ClassOnlyInput;
  }

  public String separateRepPointsTipText() {
	  return "Classify representative points in a seperate cluster so that they are clearly visible";
  }
  
  public boolean getSeparateRepPoints() {
	  return m_SeparateRepPoints;
  }

  public void setSeparateRepPoints(boolean m_SeparateRepPoints) {
	  this.m_SeparateRepPoints = m_SeparateRepPoints;
  }

/**
   * Returns the number of clusters.
   *
   * @return the number of clusters generated for a training dataset.
   * @throws Exception if number of clusters could not be returned
   * successfully
   */
  public int numberOfClusters() throws Exception {
    return clusters.size() * (m_SeparateRepPoints ? 2 : 1);
  }

  public Enumeration listOptions () {
    Vector result = new Vector();

    result.addElement(new Option(
                                 "\tnumber of clusters.\n"
                                 + "\t(default 2).",
                                 "N", 1, "-N <num>"));
    result.addElement(new Option(
                                 "\tNumber of representative objects.\n",
                                 "R", 1, "-R"));
    
    result.addElement(new Option(
            "\tCollapse factor.\n",
            "C", 1, "-C"));    
    
    result.add(new Option(
                          "\tDistance function to use.\n"
                          + "\t(default: weka.core.EuclideanDistance)",
                          "A", 1,"-A <classname and options>"));
    
    result.add(new Option(
            "\tClassify only input.\n"
            + "\t(default: true)",
            "O", 0,"-O"));


    return  result.elements();
  }

  /**
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions (String[] options)
    throws Exception {

    String optionString = Utils.getOption('N', options);
    if (optionString.length() != 0) {
      setNumClusters(Integer.parseInt(optionString));
    }
    
    optionString = Utils.getOption("R", options);
    if (optionString.length() != 0) {
      setRepObj(Integer.parseInt(optionString));
    }
    
    optionString = Utils.getOption("C", options);
    if (optionString.length() != 0) {
      setColFactor(Integer.parseInt(optionString));
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
    
    m_ClassifyOnlyInput = Utils.getFlag("O", options);
    
    m_SeparateRepPoints = Utils.getFlag("P", options);

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

    result.add("-R");
    result.add("" + getRepObj());
    
    result.add("-C");
    result.add("" + getColFactor());
    
    result.add("-A");
    result.add((m_DistanceFunction.getClass().getName() + " " +
                Utils.joinOptions(m_DistanceFunction.getOptions())).trim());

    if(m_ClassifyOnlyInput) result.add("-O");
    
    if(m_SeparateRepPoints) result.add("-P");
    
    return (String[]) result.toArray(new String[result.size()]);
  }

  /**
   * return a string describing this clusterer
   *
   * @return a description of the clusterer as a string
   */
  public String toString()
  {
	  String returnString = "";
	  
	  int i;
	  for(i = 0; i < clusters.size(); i++) {
		  returnString += "Cluster: " + i + "\n";
		  returnString += clusters.get(i).toString(0);
	  }
	  
	  returnString += "\n\n";
	  
	  return returnString; 
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
