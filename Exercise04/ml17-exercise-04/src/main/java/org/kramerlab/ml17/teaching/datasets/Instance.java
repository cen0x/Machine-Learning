package org.kramerlab.ml17.teaching.datasets;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>Instance</code> represents an instance of a dataset.
 *
 * Its values are stored as a
 * mapping that can be extended using the method <code>addValue</code>, and
 * read using the methods <code>hasAttribute</code> and <code>getValue</code>.
 */
public class Instance {

    private HashMap<Attribute, Value> values;

    public Instance() {
	  this.values = new HashMap<Attribute, Value>();
    }

    /**
     * Maps the attribute <code>att</code> to the value <code>val</code>.
     * If the attribute is already mapped, it will be overwritten.
     *
     * @param att attribute to be mapped
     * @param val value to which the attribute is mapped
     */
    public void addValue(Attribute att, Value val) {
	values.put(att, val);
    }

    /**
     * Checks whether the given attribute is mapped to a value.
     *
     * @param att attribute of interest
     */
    public boolean hasAttribute(Attribute att) {
	return values.containsKey(att);
    }

    /**
     * Returns the value of the attribute <code>att</code> it is
     * exists. Otherwise, null is returned, which corresponds to a missing
     * value.
     *
     * @return the value of the given attribute
     */
    public Value getValue(Attribute att) {
	return values.get(att);
    }

    /**
     * Creates a string representation that only contains the given attributes.
     *
     * @param attributes list of attributes that should be considered for
     * constructing the string representation
     * @return the string representation
     */
    public String toString(List<Attribute> attributes) {
	String inst = "";
	for (Attribute att : attributes) {
	    if (hasAttribute(att)) {
		inst += getValue(att).toString() + ",";
	    } else {
		inst += ",";
	    }
	}
	final int lastComma = inst.length() - 1;
	return inst.substring(0, lastComma);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Instance) {
          Instance i = (Instance) o;
          if (i.values.size() == this.values.size()) {
            for (Map.Entry<Attribute, Value> kv: this.values.entrySet()) {
                Attribute key = kv.getKey();
                Value v = kv.getValue();
                if (!i.values.containsKey(key)) {
                    return false;
                } else if (!i.values.get(key).equals(v)) {
                    return false;
                }
            }
            return true;
          }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public boolean equalsExcept(Instance other, Attribute classAttribute) {
      if (other.values.size() == this.values.size()) {
        for (Map.Entry<Attribute, Value> kv: this.values.entrySet()) {
            Attribute key = kv.getKey();
            Value v = kv.getValue();
            if (!key.equals(classAttribute)) {
                if (!other.values.containsKey(key)) {
                    return false;
                } else if (!other.values.get(key).equals(v)) {
                    return false;
                }
            }
        }
        return true;
      }
      return false;
    }

}
