package org.fmi.data.mining.cure.helpers;

/**
 * Class representing the double numbers.
 * 
 * @author diana
 *
 */
public class DoubleNum implements Number {

	/**
	 * Value of the number.
	 */
	private double value;
	/**
	 * 
	 */
	private static final double EPS = 0.001;
	
	/**
	 * Constructs double number.
	 * 
	 * @param value
	 * 				The value of the number.
	 */
	public DoubleNum(double value) {
		this.value = value;
	}
	
	@Override
	public Number plus(Number n) {
		if (n instanceof DoubleNum) {
			return new DoubleNum(((DoubleNum)n).value + this.value);
		}
		return null;
	}

	@Override
	public Number mult(Number n) {
		if (n instanceof DoubleNum) {
			return new DoubleNum(((DoubleNum)n).value * this.value);
		}
		return null;
	}

	@Override
	public Number sqrt() {
		return new DoubleNum(Math.sqrt(this.value));
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if(! (obj instanceof DoubleNum)) {
			return false;
		}
		return Math.abs(((DoubleNum) obj).value - this.value) <= EPS;
	}

	@Override
	public boolean isGreaterThan(Number n) {
		return this.value > ((DoubleNum)n).value;
	}
}
