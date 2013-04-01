import java.util.ArrayList;
import java.util.HashTable;
import java.io.File;
import java.io.BufferedReader;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

public class Brother implements Parser {
	public ArrayList<Hashtable<Integer, Object>> parse() {
		/* Open or access internet.xml */
		/* parse it for data*/
		/* store the info found in the database tags (as a string) into the Hashtable as an object*/
		/* return the ArrayList with the data inside*/

		BufferedReader in = new BufferedReader(new File("internet.xml"));
		String line;
		int rraCount = 0;
		String database = "";
		ArrayList<Hashtable<Integer, Object>> retVal;
		Hashtable<Integer, Object> current;

		retVal = new ArrayList<Hashtable<Integer, Object>>();

		while ((line = in.readLine()) != null) {
			if (line.equals("<rra>")) {
				rraCount++;
				current = new Hashtable<Integer, Object>();
				database = "";
				while ((line = in.readLine()) != null) {
					if (line.equals("<database>")) {
						while ((line = in.readLine()) != null) {
							if (line.equals("</database>")) {
								break;
							}
							database = database + line;
						}
						current.put(rraCount - 1, database);
						retVal.add(current);
						break;
					}
				}
			}
		}

		return retVal;

	}

	public boolean validate(String xml) {
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource("bigbrother.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public String getFieldFile() {
		return "BigBrotherFields.txt";
	}
}