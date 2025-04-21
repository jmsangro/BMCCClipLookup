package cliplookup;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.opencsv.CSVReader;

public class DataSource {
	private List<String[]> records;	
	public DataSource () {
		try {
			String dataFile = "./Test Data.csv";
	        FileReader filereader = new FileReader(dataFile); 
	        try (CSVReader csvReader = new CSVReader(filereader)) {
				records = csvReader.readAll();
				//remove title row.
				records.remove(records.get(0));
			}

		}
		catch (Exception e) {
			System.err.println(e);
		}		
	}
	public Collection<String> getTowns(String s) {
		Set<String> names = new TreeSet<String>();
		for (String[] row : records) {
			if (row[6] != null && row[6].equals(s)) {
			 names.add(row[7]);
			}
		}
		return names;
	}
	public Collection<String> getPersonsFromTown(String value) {
		Set<String> names = new TreeSet<String>();
		for (String[] row : records) {
			if (row[7] != null && row[7].equals(value)) {
			 names.add(row[2]);
			}
		}
		return names;
		
	}
	public Collection<PicLabelData> getPicLabelsFromTown(String value) {
		Set<PicLabelData> names = new TreeSet<PicLabelData>();
		for (String[] row : records) {
			if (row[7] != null && row[7].equals(value)) {
				PicLabelData pld = new PicLabelData(row[2],row[5]);
			 names.add(pld);
			}
		}
		return names;
		
	}
	
	public Collection<String> getClipsOfPerson(String value) {
		// TODO Auto-generated method stub
		ArrayList<String> returnVal = new ArrayList<String>();
		for (String[] row : records) {
			if (row[2] != null && row[2].equals(value)) {
			 returnVal.add(row[3]);
			}
		}
		return returnVal;
	}
	public String getClipLocator ( String clip) {
		for (String[] row : records) {
			if ( row[3].equals(clip)) {
				return row[8];
			}
		}
		return null;
	}
	
	public Collection<String> getPersonsWithSirName(String value) {
		Set<String> names = new TreeSet<String>();
		for (String[] row : records) {
			if (row[0] != null && row[0].equals(value)) {
			 names.add(row[2]);
			}
		}
		return names;
	}
	
	public Collection<String> getAllSirNames() {
		return getUniqueByRow(0);
	}
	
	public Collection<String> getAllStates() {
		return getUniqueByRow(6);
	}
	private Collection<String> getUniqueByRow( int rowIndex) {
		Set<String> names = new TreeSet<String>();
		for (String[] row : records) {
			 names.add(row[rowIndex]);
		}
		return names;
	}
	
	public Collection<String> getClipsOfTheme(String theme) {
		ArrayList<String> returnVal = new ArrayList<String>();
		for (String[] row : records) {
			if (row[4] != null && row[4].equals(theme)) {
			 returnVal.add(row[3]);
			}
		}
		return returnVal;
	}
	public Collection<String> getAllThemes() {
		return getUniqueByRow(4);
	}
	public Collection<PicLabelData> getPicLabelsWithSirName(String value) {
		Set<PicLabelData> names = new TreeSet<PicLabelData>();
		for (String[] row : records) {
			if (row[0] != null && row[0].equals(value)) {
				PicLabelData pld = new PicLabelData(row[2],row[5]);
			 names.add(pld);
			}
		}
		return names;
	}
}
