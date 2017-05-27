package org.kramerlab.ml17.teaching.datasets;

/**
 * NumericAttribute represents attributes taking floating point numbers as
 * values.
 *
 * In future versions, NumericAttribute could provide methods for specifying
 * and checking the range of allowed values.
 */
public class NumericAttribute extends Attribute {

    public NumericAttribute(String name) {
	super(name);
    }
}
