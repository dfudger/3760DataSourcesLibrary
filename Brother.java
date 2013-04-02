import java.util.ArrayList;
import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

public class Brother implements Parser {
	public ArrayList<Hashtable<Integer, Object>> parse() {
		/* Open or access internet.xml */
		/* parse it for data*/
		/* store the info found in the database tags (as a string) into the Hashtable as an object*/
		/* return the ArrayList with the data inside*/

		/* USE THIS FOR CONNECTING TO SERVER - ALSO, SWAP location VARIABLE ABOVE*/
		/*String[]  cmd = { "/bin/sh", "-c", "scp cis3760@general.uoguelph.ca:/users/windtalk/internet.xml ~" };
		String[] cmdDummy = { "ls" };
    	Process proc;
    	int     ch;

        try {
			proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
        } catch (Exception e) {
        	e.printStackTrace();
        }*/

        BufferedReader in = null;
        try{
        	in = new BufferedReader(new FileReader("internet.xml"));
        } catch(FileNotFoundException e){
        	System.out.println("File not found");
        	return null;
        }
		String line;
		String prevLine = null;
		int rraCount = 0;
		String database = "";
		ArrayList<Hashtable<Integer, Object>> retVal;
		Hashtable<Integer, Object> current = new Hashtable<Integer, Object>();

		retVal = new ArrayList<Hashtable<Integer, Object>>();
		try{
			while ((line = in.readLine()) != null) {
				if (line.trim().equals("<rra>")) {
					rraCount++;
					database = "";
					while ((line = in.readLine()) != null) {
						if (line.trim().equals("<database>")) {
							while ((line = in.readLine()) != null) {
								if (line.trim().equals("</database>")) {
									database = addTimestamp(prevLine);
									break;
								}
								prevLine = line;
							}
							current.put(rraCount - 1, database);
							break;
						}
					}
				}
			}
			retVal.add(current);
		} catch(IOException e){
			System.out.println("IO error");
			return null;
		}

		return retVal;

	}

	public String addTimestamp(String line){
		line = line.replace("<!--", "<timestamp>");
		line = line.replace("-->", "</timestamp>");
		return line;
	}

	public boolean validate(String xml) {
		try {
			StringReader reader = new StringReader(xml);  
			URL xsdResource = getClass().getResource("bigbrother.xsd");  
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);  
			Schema schema = factory.newSchema(xsdResource);  

			Validator val = schema.newValidator();  
			val.validate(new StreamSource(reader));
			return true;

		} catch (Exception e) {
			System.out.println("The xml string is NOT valid");
			System.out.println("Reason: " + e.getLocalizedMessage());
			return false;
		}
	}

	public String getFieldFile() {
		return "BigBrotherFields.txt";
	}

	public static void main(String[] args) {
		ArrayList<Hashtable<Integer, Object>> data;
		Brother big = new Brother();
		System.out.println("Start");
		data = big.parse();
		String dto = null;

		DTOFactory factory = new DTOFactory();
		try{
			dto = factory.makeDTO(data, big.getFieldFile());
			if(big.validate(dto)){
				System.out.println(dto);
			}
		}catch(Exception e){
			System.out.println("Issues making the DTO");
		}
		System.out.println("Done");
	}
}