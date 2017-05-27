package org.kramerlab.ml17.teaching.datasets;

/**
 * NumericValue represents a numeric attribute value.
 */
public class NumericValue extends Value {

    private double value;

    /**
     * @param value numeric attribute value
     */
    public NumericValue(double value) {
	this.value = value;
    }

    /**
     * Returns the numeric value in its raw form.
     */
    public double getValue() {
	return value;
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) {
	    return false;
	} else {
	    NumericValue val = (NumericValue)o;
	    return getValue() == val.getValue();
	}
    }

    /**
     * Checks whether the numeric value is strictly smaller than
     * <code>val</code>.
     */
    public boolean smallerThan(NumericValue val) {
	if (val == null) {
	    return false;
	} else {
	    return getValue() < val.getValue();
	}
    }

    /**
     * Checks whether the numeric value is strictly greater than
     * <code>val</code>.
     */
    public boolean greaterThan(NumericValue val) {
	if (val == null) {
	    return false;
	} else {
	    return getValue() > val.getValue();
	}
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
	return Double.toString(getValue());
    }
}
