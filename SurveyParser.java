import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.BufferedWriter; 
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

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

				token = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				for (int i = 0; i < token.length; i++) {
					if (i != 10) {
						if (token[i].trim().equals("TRUE") || token[i].trim().equals("FALSE")) {
							table.put(tokens, token[i].toLowerCase());
						} else {
							table.put(tokens, token[i]);
						}
						tokens += 1;
					}
				}

				list.add(table);

				if (lines > 5) {
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Parsing Error: " + e.getMessage());
			System.exit(1);
		}

		return list;
	}

	public boolean validate(String xml) {
		Source xmlFile = new StreamSource(new File("hdi.xml"));
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File("hdi.xsd"));
			Validator validator = schema.newValidator();
		  	validator.validate(xmlFile);
		  	return true;
		} catch (Exception e) {
			System.out.println(xmlFile.getSystemId() + " is NOT valid");
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

		DTOFactory factory = new DTOFactory();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("hdi.xml"));
			String output = factory.makeDTO(list, parser.getFieldFile());
			bw.write(output, 0, output.length());
			bw.close();
			System.out.println(parser.validate("hdi.xml"));
		} catch (Exception e) {
			System.err.println("Factory reported error");
			System.exit(1);
		}
	}
}