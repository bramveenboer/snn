package nnocr.objects;

import java.util.ArrayList;

public class DataSet extends ArrayList<Sample> {
	protected static final long serialVersionUID = 1L;
	
	protected ArrayList<Sample> samples;
	
	public DataSet() {
		samples = new ArrayList<Sample>();
	}

	public void downSample(int rate) {
		for (Sample sample : this.samples) {
			sample.downSample(rate);
		}		
	}
}
