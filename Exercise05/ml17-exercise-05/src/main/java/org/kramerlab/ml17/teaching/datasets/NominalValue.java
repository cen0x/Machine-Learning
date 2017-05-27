package org.kramerlab.ml17.teaching.datasets;

/**
 * NominalValue represents a nominal attribute value.
 */
public class NominalValue extends Value {

    private final String value;

    /**
     * @param value nominal attribute value
     */
    public NominalValue(String value) {
	this.value = value;
    }

    /**
     * Returns the nominal value in its raw form.
     */
    public String getValue() {
	return value;
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) {
	    return false;
	} else {
	    NominalValue val = (NominalValue)o;
	    return getValue().equals(val.getValue());
	}
    }

    @Override
    public int hashCode() {
	return value.hashCode();
    }

    @Override
    public String toString() {
	return getValue();
    }
}
