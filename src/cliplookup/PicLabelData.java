package cliplookup;

public class PicLabelData implements Comparable<PicLabelData>{
	public PicLabelData(String labelText, String imageURL) {
		this.labelText = labelText;
		this.imageURL = imageURL;
	}
	public String imageURL;
	public String labelText;
	
	public String toString() {
		return labelText;
	}

	@Override
	public int compareTo(PicLabelData o) {
		// TODO Auto-generated method stub
		return labelText.compareTo(o.labelText);
	}

}
