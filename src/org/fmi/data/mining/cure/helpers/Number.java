package org.fmi.data.mining.cure.helpers;

/**
 * Base interface for all the comparable types of numbers.
 * 
 * @author diana
 *
 */
public interface Number {
	
	/**
	 * Plus operation.
	 * 
	 * @param n
	 * 			The other value.
	 * @return
	 * 		 NEW number with the value of the current one plus the value
	 * 		of the n param.
	 */
	Number plus(Number n);
	
	/**
	 * Multiply operation.
	 * 
	 * @param n
	 * 			The other value.
	 * @return
	 * 		 NEW number with the value of the current one multiplied with the value
	 * 		of the n param.
	 */
	Number mult(Number n);
	
	/**
	 * Sqrt value of the number.
	 * 
	 * @return
	 * 			The sqrt.
	 */
	Number sqrt();
	
	
	/**
	 * For all comparable numbers. 
	 * 
	 * @param n
	 * 			The "compared with" object.
	 * 
	 * @return
	 * 			True if the current one is greater that the n param.
	 */
	boolean isGreaterThan(Number n);
}
