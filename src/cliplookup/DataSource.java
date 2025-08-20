package cliplookup;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class DataSource {
	private List<String[]> records;	
	private List<ClipInfo> clipInfoList;
	public DataSource (String dataFile) throws IOException, CsvException {
	        FileReader filereader = new FileReader(dataFile); 
	        CSVReader csvReader = new CSVReader(filereader);
			records = csvReader.readAll();
			csvReader.close();
			//parse(dataFile);
			//remove title row.
			records.remove(records.get(0));
			validateData();
		
	}
	
    public void parse(String dataFile){
        try{
            CSVReader reader=
                    new CSVReaderBuilder(new FileReader(dataFile)).
                            withSkipLines(1). // Skiping firstline as it is header
                            build();
            clipInfoList=reader.readAll().stream().map(data-> mapRowToClipInfo(data)).collect(Collectors.toList());
            clipInfoList.forEach(System.out::println);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

	private ClipInfo mapRowToClipInfo(String[] data) {
		ClipInfo clipInfo= new ClipInfo();
		clipInfo.setLastName(data[0]);
		clipInfo.setFirstName(data[1]);
		clipInfo.setDisplayName(data[2]);
		clipInfo.setClipTitle(data[3]);
		clipInfo.setTheme(data[4]);
		clipInfo.setPhotoLink(data[5]);
		clipInfo.setState(data[6]);
		clipInfo.setCommunity(data[7]);
		clipInfo.setClipLink(data[8]);
//		clipInfo.setMaidenName(data[9]);
//		clipInfo.setSecondFamilyName(data[10]);
		clipInfo.setDateOfInterview(data[9]);
		return clipInfo;
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
	public ClipInfo getClipLocator ( String clip) {
		for (String[] row : records) {
			if ( row[3].equals(clip)) {
				return mapRowToClipInfo(row);
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
	
	public void validateData() {
		for (String[] row : records) {
			String clipLink = row[8];
			File clipFile = new File(clipLink);
			if ( ! clipFile.exists()) {
				System.out.println("Bad Clip Link: "+clipLink+" for record of: "+row[2]);
			}
		}
		for (String[] row : records) {
			String photoLink = row[5];
			File photoFile = new File(photoLink);
			if ( ! photoFile.exists()) {
				System.out.println("Bad Photo Link: "+photoLink+" for record of: "+row[2]);
			}
		}

	}
}
