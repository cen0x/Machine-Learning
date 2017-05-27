package org.kramerlab.ml17.teaching.datasets;

/**
 * Attribute contains the meta information of an attribute belonging to a
 * dataset.
 */
public abstract class Attribute {

    private String name;

    /**
     * @param name unique attribute name
     */
    public Attribute(String name) {
	this.name = name;
    }

    /**
     * @return unique attribute name
     */
    public String getName() {
	return name;
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) {
	    return false;
	} else {
	    Attribute att = (Attribute)o;
	    return getName().equals(att.getName());
	}
    }

    @Override
    public int hashCode() {
	return getName().hashCode();
    }

    @Override
    public String toString() {
	return "@attribute " + name;
    }
}
