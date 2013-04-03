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

public class SurveyParser implements Parser {
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

				System.out.println(table.size() + "  " + line.toString());
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

	public String getFieldFile() {
		return "HDIFields.txt";
	}

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