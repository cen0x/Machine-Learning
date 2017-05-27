package org.kramerlab.ml17.teaching.datasets;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * NominalAttribute is an attribute having a finite number of fixed values. The
 * set of values can changed using the method <code>addValue</code> and read by
 * the methods <code>getValue</code> and <code>getNumberOfValues</code>.
 */
public class NominalAttribute extends Attribute {

    private List<NominalValue> values;

    /**
     * @param name unique attribute name
     */
    public NominalAttribute(String name) {
	super(name);

	values = new ArrayList<NominalValue>();
    }

    /**
     * Adds given value to the set of values.
     *
     * @param val value that is supposed to be added
     */
    public void addValue(NominalValue val) {
	if (!values.contains(val)) {
	    values.add(val);
	}
    }

    /**
     * Returns the value at the given index.
     *
     * @param index attribute index
     */
    public NominalValue getValue(int index) {
	return values.get(index);
    }

    /**
     * @return number of available attribute values
     */
    public int getNumberOfValues() {
	return values.size();
    }

    public List<NominalValue> getValues() {
        ArrayList<NominalValue> res = new ArrayList<>();
        for (NominalValue v: values) {
            res.add(v);
        }
        return res;
    }

    @Override
    public String toString() {
	String att = "@attribute " + getName() + " {";
	for (int i = 0; i < getNumberOfValues(); i++) {
	    att += getValue(i).getValue() + ",";
	}
	final int lastComma = att.length() - 1;
	att = att.substring(0, lastComma);
	att += "}";

	return att;
    }

}
