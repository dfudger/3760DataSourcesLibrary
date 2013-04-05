package datasources;

import java.util.*;
import java.util.logging.XMLFormatter;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.*;
import java.net.URL;
import javax.xml.transform.stream.StreamSource;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;

/**
 * This class extends the Parser interface and is responsible for retrieving, parsing, and validating HDI.
 */
public class SurveyParser implements Parser {

	/**
     * This method is responsible for retrieving and parsing HDI. As of today, it gets static data.
     * @return ArrayList<Hashtable<Integer, Object>>, an arraylist to be given to the DTOFactory
     */
	public ArrayList<Hashtable<Integer, Object>> parse() {
		ArrayList<Hashtable<Integer, Object>> list = new ArrayList<Hashtable<Integer, Object>>();
        
		try {
			BufferedReader br = new BufferedReader(new FileReader("HDI_Raw.csv"));
			String line = "";
			String[] token;
			int lines = 0;
			int tokens = 0;
            
			while ((line = br.readLine()) != null) {
				lines += 1;
				tokens = 0;
				Hashtable<Integer, Object> table = new Hashtable<Integer, Object>();
                
				token = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
				for (int i = 0; i < token.length; i++) {
					if (i != 10) {
						if (!token[i].equals("")) {
							if (token[i].trim().equals("TRUE") || token[i].trim().equals("FALSE")) {
								table.put(tokens, token[i].toLowerCase());
							} else {
								table.put(tokens, token[i]);
							}
						} else {
							table.put(tokens, "");
						}
						tokens += 1;
					}
				}
                
				if (table.size() > 0) {
					list.add(table);
				}
                
				if (lines > 1000) {
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Parsing Error: " + e.getMessage());
			return null;
		}
        
		return list;
	}
    
	/**
     * This method is responsible for validating HDI.
     * @param xml a String representing the DTO given to the Data Warehouse
     * @return boolean, whether or not the String is valid
     */
	public boolean validate(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			URL xsdResource = getClass().getResource("hdi.xsd");
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
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
    
    /**
     * This method returns the name of the field enumeration file for HDI.
     * @return String, the name of the field enumeration file
     */
	public String getFieldFile() {
		return "HDIFields.txt";
	}
    
    /**
     * This method is a testing harness for this class.
     * It accurately simulates a call from the Data Warehouse.
     * @param args command line arguments, which are not used
     */
	public static void main(String[] args) {
		SurveyParser parser = new SurveyParser();
		ArrayList<Hashtable<Integer, Object>> list = parser.parse();
        
		try {
			Retriever r = new Retriever();
			BufferedWriter bw = new BufferedWriter(new FileWriter("hdi.xml"));
			String output = r.retrieve(Sources.HDI, null);
			bw.write(output, 0, output.length());
			bw.close();
		} catch (Exception e) {
			System.err.println("Factory reported error");
			System.exit(1);
		}
	}
}