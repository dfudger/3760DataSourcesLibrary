import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Retriever {
	private Hashtable<Sources, Parser> sources;
	
	public Retriever() {
		sources = new Hashtable<Sources, Parser>();
		sources.put(Sources.FOOTPRINTS, new Footprints());
	}
	
	public static void main(String[] args){
		
		Retriever test = new Retriever();
		
		ArrayList<Integer> fields = new ArrayList<Integer>();
		for (int i=0; i<=30; i++)
			fields.add(i);
		
		String xmlString = test.retrieve(Sources.FOOTPRINTS, fields);
		System.out.println(xmlString);
	}
	
	public String retrieve(Sources source, ArrayList<Integer> fields) {
		ArrayList<Hashtable<Integer, Object>> records; 
		ArrayList<Hashtable<Integer, Object>> filteredRecords;
		
		records = sources.get(source).parse();
		
		if (fields == null) {
			filteredRecords = records;
		}
		else {
			filteredRecords = new ArrayList<Hashtable<Integer, Object>>();
			for (int i = 0; i < records.size(); i++) {
				Hashtable<Integer, Object> next = new Hashtable<Integer, Object>();
				Hashtable<Integer, Object> currentRecord = records.get(i);
				
				for (int j = 0; j < fields.size(); j++) {
					int fieldNumber = fields.get(j);
					
					if (currentRecord.containsKey(fieldNumber)) {
						next.put(fieldNumber, currentRecord.get(fieldNumber));
					}
				}
				
				filteredRecords.add(next);
			}
		}
		
		String xml = "";
		try {
			xml = DTOFactory.makeDTO(filteredRecords, sources.get(source).getFieldFile()); 
		}
		catch (IOException e){
			xml = "IOException";
		}
		catch (Exception e) {
			xml = "Other Exception";
		}
		
		if (sources.get(source).validate(xml)) {
			return xml;
		}
		
		return null;
	}
}
