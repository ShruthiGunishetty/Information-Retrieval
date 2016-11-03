import java.util.Comparator;
import java.util.Map;
class ValueComp implements Comparator<String> {
	// this is used for comparing values within a map. used to find out the most freqently occuring term with high frequency values

	Map<String,Integer> val_b;

	public ValueComp(Map<String,Integer> val_b) {
		this.val_b = val_b;
	}

	
	public int compare(String a, String b) {
		if (val_b.get(a) >= val_b.get(b)) {
			return -1;
		} else {
			return 1;
		}
	}
}


public class Document_info_len_max {
	// all the details related to a document are stored in this class
	int documentId;
	int maximumTermFrequency;
	String highest_freq_stem;
	int length_of_document;
	public void setDoclen(int length_of_document) {
		this.length_of_document = length_of_document;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setMostFrequentStem(String highest_freq_stem) {
		this.highest_freq_stem = highest_freq_stem;
	}
	public String getMostFrequentStem() {
		return highest_freq_stem;
	}
	public int getMax_tf() {
		return maximumTermFrequency;
	}
	
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public int getDoclen() {
		return length_of_document;
	}
	
	public void setMax_tf(int maximumTermFrequency) {
		this.maximumTermFrequency = maximumTermFrequency;
	}
}

