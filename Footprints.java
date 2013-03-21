import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Footprint implements Parser {
	@Override
	private ArrayList<Hashtable<Integer, Object>> Parse() {
	}

	@Override
	private boolean Validate(String file) {
		static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
		static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);

		factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

		// Set the schema file
		factory.setAttribute(JAXP_SCHEMA_SOURCE, new File("footprints.xsd"));

		try {
			DocumentBuilder parser = factory.newDocumentBuilder();

			// Parse the file. If errors found, they will be printed.
			parser.parse("footprints.xml");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private Hashtable<Integer, String> getFieldFile(String file) {

	}
}
