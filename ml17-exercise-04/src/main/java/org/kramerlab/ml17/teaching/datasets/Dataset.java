package org.kramerlab.ml17.teaching.datasets;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A dataset consists of three components: a name, a list of attibutes, and a
 * list of instances, where the instances may only use the attribute specified
 * by the list of attributes.
 */
public class Dataset {

    private String name;
    private List<Attribute> attributes;
    private List<Instance> instances;

    public Dataset() {
	this.attributes = new ArrayList<Attribute>();
	this.instances = new ArrayList<Instance>();
    }

    public Dataset(String name) {
	this();
	this.name = name;
    }

    /**
     * @return the name of the dataset
     */
    public String getName() {
	return name;
    }

    /**
     * @param name of the dataset
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the list of attributes that are associated with this dataset
     */
    public List<Attribute> getAttributes() {
	return attributes;
    }

    /**
     * Associates attribute <code>att</code> with the dataset.
     * @attribute att attribute that is supposed to be associated with the
     * dataset
     */
    public void addAttribute(Attribute att) {
	attributes.add(att);
    }

    /**
     * @return the number of instances that are contained in the dataset
     */
    public int getNumberOfInstances() {
	return instances.size();
    }

    /**
     * @return the instance at index <code>index</code>
     */
    public Instance getInstance(int index) {
	return instances.get(index);
    }

    /**
     * @return all instances as list
     */
    public List<Instance> getInstances() {
        List<Instance> result = new ArrayList<>();
        int n = getNumberOfInstances();
        for (int i = 0; i < n; i++) {
          result.add(getInstance(i));
        }
        return result;
    }

    /**
     * @return adds instance <code>inst</code>
     */
    public void addInstance(Instance inst) {
	instances.add(inst);
    }

    /**
     * Parses the given ARFF file and adds the attributes and instances to the
     * dataset.
     *
     * @param file ARFF file describing the dataset
     */
    public void load(File file) throws Exception {

	String line = null;
	BufferedReader r = new BufferedReader(new FileReader(file));

	boolean headerSection = true;
	while ((line = r.readLine()) != null) {
	    line = line.trim();

	    // parse line
	    if (headerSection) {  // header section
		if (line.startsWith("@relation")) {
		    String name = line.split(" ")[1];
		    setName(name);
		} else if (line.startsWith("@attribute")) {
		    addAttribute(parseAttribute(line));
		}
	    } else {              // data section
		Instance inst = parseInstance(line);
		if (inst != null) {
		    addInstance(inst);
		}
	    }

	    // check whether data section started
	    if (line.startsWith("@data")) {
		headerSection = false;
	    }
	}

	r.close();
    }

    /**
     * Parses the given ARFF file and adds the attributes and instances to the
     * dataset.
     *
     * @param inputStream input stream with the dataset
     */
    public void load(InputStream inputStream) throws Exception {

  String line = null;
  BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

  boolean headerSection = true;
  while ((line = r.readLine()) != null) {
      line = line.trim();

      // parse line
      if (headerSection) {  // header section
    if (line.startsWith("@relation")) {
        String name = line.split(" ")[1];
        setName(name);
    } else if (line.startsWith("@attribute")) {
        addAttribute(parseAttribute(line));
    }
      } else {              // data section
    Instance inst = parseInstance(line);
    if (inst != null) {
        addInstance(inst);
    }
      }

      // check whether data section started
      if (line.startsWith("@data")) {
    headerSection = false;
      }
  }

  r.close();
    }

    /*
     * Parses attribute information from <code>line</code>. A string of the form
     * @attribute NAME TYPE
     * @attribute NAME {VAL1, VAL2, ...}
     * is expected. The former is regarded as numeric attribute, the latter as
     * nominal attribute.
     */
    private Attribute parseAttribute(String line) {
	line = line.trim();

	int space1 = line.indexOf(" ");
	int space2 = line.indexOf(" ", space1 + 1);
	String name = line.substring(space1 + 1, space2);
	String type = line.substring(space2 + 1);

	Attribute att = null;
	if (type.indexOf("{") == -1) {  // if attribute is numeric
	    att = new NumericAttribute(name);
	} else {                        // if attribute is nominal
	    type = type.replace("{", "");
	    type = type.replace("}", "");
	    type = type.replace(" ", "");
	    String[] vals = type.split(",");
	    att = new NominalAttribute(name);
	    for (int i = 0; i < vals.length; i++) {
		((NominalAttribute)att).addValue(new NominalValue(vals[i]));
	    }
	}

	return att;
    }

    /*
     * Parses instance information from <code>line</code>.
     */
    private Instance parseInstance(String line) {

	// catch empty line
	line = line.trim();
	if (line.length() == 0) {
	    return null;
	}

	Instance inst = new Instance();
	String[] vals = line.split(",");
	if (vals.length != getAttributes().size()) {
	    return null;

	} else {
	    // Read the value of each attribute. It is assumed that ordering of
	    // the values corresponds to the ordering of the attributes
	    // specified in the header.
	    for (int i = 0; i < getAttributes().size(); i++) {
		Value val = null;
		Attribute att = getAttributes().get(i);
		if (att instanceof NominalAttribute) {
		    val = new NominalValue(vals[i]);
		} else if (att instanceof NumericAttribute) {
		    try {
			val = new NumericValue(Double.parseDouble(vals[i]));
		    } catch (NumberFormatException ex) {
			val = null;
		    }
		}
		inst.addValue(att, val);
	    }
	}

	return inst;
    }

    @Override
    public String toString() {

	String result = "@relation " + getName() + "\n";
	for (Attribute att : getAttributes()) {
	    result += att.toString() + "\n";
	}

	for (int i = 0; i < getNumberOfInstances(); i++) {
	    result += getInstance(i).toString(getAttributes()) + "\n";
	}
	return result;
    }

    public static void main(String[] args) {
	File f = new File("weather.nominal.arff");

	try {
	    Dataset dataset = new Dataset();
	    dataset.load(f);
	    System.out.println(dataset.toString());
	} catch (Exception ex) {
	    // todo
	    ex.printStackTrace();
	}
    }
}
