import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.sql.*;

/* Without implementing the Parse interface (test puprose only) */

public class Footprint {

	private boolean Validate(String file) {
		String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
		String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

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

	private static String getFieldFile() {
		return "FootprintsField.txt";
	}

	public static void main(String[] args) {
		String connectionUrl = "jdbc:sqlserver://sunshine.cfs.uoguelph.ca;" +
				   "databaseName=Footprints_view;user=fpviewer;password=fpviewer;";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(connectionUrl);
			System.out.println("Connection succeeded");
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private ArrayList<Hashtable<Integer, Object>> parse()
			throws FileNotFoundException, IOException {

		Hashtable<String, Integer> fields = mapNamesField();
		// 1. Connect to the Footprints DB
		// 2. Fill the hashtable with the data found
		// 3. Return the array of hashtable
		return null;
	}

	private static Hashtable<String, Integer> mapNamesField()
			throws FileNotFoundException, IOException {
		BufferedReader in;
		String line;
		Hashtable<String, Integer> retVal;

		in = new BufferedReader(new FileReader("FootprintsField.txt"));
		retVal = new Hashtable<String, Integer>();

		while ((line = in.readLine()) != null) {
			String tokens[] = line.split(" ");

			if (tokens.length == 1) {
				continue;
			} else if (tokens.length == 2) {
				retVal.put(tokens[1], Integer.parseInt(tokens[0]));
			}
		}

		return retVal;
	}
}
