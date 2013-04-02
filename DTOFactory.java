import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class DTOFactory {
	private static Hashtable<Integer, String> mapFieldNames(String fieldFile) throws FileNotFoundException, IOException {
		BufferedReader in;		
		String line;
		Hashtable<Integer, String> retVal;

		in = new BufferedReader(new FileReader(fieldFile));
		retVal = new Hashtable<Integer, String>();

		while ((line = in.readLine()) != null) {
			String tokens[] = line.split(" ");

			if (tokens.length == 1) {
				continue;
			}
			else if (tokens.length == 2) {
				retVal.put(Integer.parseInt(tokens[0]), tokens[1]);
			}
		}

		return retVal;
	}

	private static Hashtable<Integer, String> mapObjectNames(String fieldFile) throws FileNotFoundException, IOException {
		BufferedReader in;		
		String line;
		Hashtable<Integer, String> retVal;

		in = new BufferedReader(new FileReader(fieldFile));
		retVal = new Hashtable<Integer, String>();
		String objectName = "";

		while ((line = in.readLine()) != null) {
			String tokens[] = line.split(" ");

			if (tokens.length == 1) {
				objectName = tokens[0];
			}
			else if (tokens.length > 1){
				retVal.put(Integer.parseInt(tokens[0]), objectName);
			}
		}

		return retVal;
	}

	public static String makeDTO(ArrayList<Hashtable<Integer, Object>> records, String fieldFile) throws FileNotFoundException, IOException {
		Hashtable<Integer, String> fieldNames;
		Hashtable<Integer, String> objectNames;
		String DTO;

		fieldNames = DTOFactory.mapFieldNames(fieldFile);
		objectNames = DTOFactory.mapObjectNames(fieldFile);
		DTO = "<DTO>\n";
		
		for (int i = 0; i < records.size(); i++) {
			int firstField;
			String objectName;
			Hashtable<Integer, Object> record; 
			
			record = records.get(i);
			firstField = record.keySet().iterator().next();
			
			List<Integer> v = new ArrayList<Integer>(record.keySet());
		   	Collections.sort(v);
			
			objectName = objectNames.get(firstField);
			
			DTO = DTO + "\t<" + objectName + ">\n";

			for(Integer fieldNumber : v){
				String fieldName = fieldNames.get(fieldNumber);

				DTO = DTO + "\t\t<" + fieldName + ">";
				DTO = DTO + record.get(fieldNumber).toString();
				DTO = DTO + "</" + fieldName + ">\n";
			}

			DTO = DTO + "\t</" + objectName + ">\n";
		}
		
		DTO = DTO + "</DTO>";

		return DTO;
	}
}
