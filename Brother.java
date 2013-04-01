import java.util.ArrayList;
import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

public class Brother implements Parser {
	//String location = "/home/cis3760/files/";
	String location = "/Users/mattstark/Documents/Project/files/";
	public ArrayList<Hashtable<Integer, Object>> parse() {
		/* Open or access internet.xml */
		/* parse it for data*/
		/* store the info found in the database tags (as a string) into the Hashtable as an object*/
		/* return the ArrayList with the data inside*/

		/* USE THIS FOR CONNECTING TO SERVER - ALSO, SWAP location VARIABLE ABOVE*/
		/*String[]  cmd = { "/bin/sh", "-c", "scp cis3760@general.uoguelph.ca:/users/windtalk/internet.xml ~/files/" };
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
        	in = new BufferedReader(new FileReader(location + "internet.xml"));
        } catch(FileNotFoundException e){
        	System.out.println("File not found");
        	return null;
        }
		String line;
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
					System.out.println(line);
					while ((line = in.readLine()) != null) {
						if (line.trim().equals("<database>")) {
							System.out.println(line);
							while ((line = in.readLine()) != null) {
								if (line.trim().equals("</database>")) {
									break;
								}
								database = database + line;
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

	public boolean validate(String xml) {
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(location + "bigbrother.xsd"));
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

	public static void main(String[] args) {
		ArrayList<Hashtable<Integer, Object>> data;
		Brother big = new Brother();
		System.out.println("Start");
		data = big.parse();
		
		DTOFactory factory = new DTOFactory();
		try{
			System.out.println(factory.makeDTO(data, big.getFieldFile()));
		}catch(Exception e){
			System.out.println("Issues making the DTO");
		}
		System.out.println("Done");
	}
}