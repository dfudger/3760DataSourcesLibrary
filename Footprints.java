import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.sql.*;

public class Footprints implements Parser {

	@Override
	public boolean validate(String file) {
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

	@Override
	public String getFieldFile() {
		return "FootprintsFields.txt";
	}

	@Override
	public ArrayList<Hashtable<Integer, Object>> parse() {

		ArrayList<Hashtable<Integer, Object>> data = new ArrayList<Hashtable<Integer, Object>>();
		Hashtable<String, Integer> fields = null;
		
		try {
			fields = mapNamesField();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// 1. Connect to the Footprints DB ==== IMPOSSIBLE TO DO
		/********************************************************/

		// 2. Fill the hashtable with the data found === FAKE DATA FOR DEMO

		/* TICKETS */

		Hashtable<Integer, Object> ticket1 = new Hashtable<Integer, Object>();
		ticket1.put(0, "7103");
		ticket1.put(fields.get("mrPRIORITY"), "1");
		ticket1.put(fields.get("mrSTATUS"), "Closed");
		ticket1.put(fields.get("mrUPDATEDATE"), "19/07/2010  11:57:14");
		ticket1.put(fields.get("mrSUBMITDATE"), "01/11/2007  08:43:09");
		ticket1.put(5, "0x00000000007BD392");
		ticket1.put(fields.get("Category"), "Accounts");
		ticket1.put(fields.get("Sub__ucategory"), "NULL");
		ticket1.put(fields.get("Type__bof__bIssue"), "Incident");
		ticket1.put(fields.get("Billing__bCode"), "NULL");
		ticket1.put(fields.get("Source"), "Phone");
		ticket1.put(fields.get("Closure__bCode"), "NULL");
		ticket1.put(fields.get("Impact"), "Individual");
		ticket1.put(fields.get("Billing__bEstimate"), "NULL");
		ticket1.put(fields.get("Type__bof__bwork__border"), "NULL");
		ticket1.put(fields.get("Sub__ucategory__bII"), "NULL");
		ticket1.put(fields.get("Software__bProduct"), "NULL");
		ticket1.put(fields.get("Due__bDate"), "NULL");
		ticket1.put(fields.get("Student__bLocation"), "NULL");
		ticket1.put(fields.get("Billing__bRequired"), "NULL");
		ticket1.put(fields.get("Submit__bto__bBilling"), "NULL");

		Hashtable<Integer, Object> ticket2 = new Hashtable<Integer, Object>();
		ticket2.put(0, "14603");
		ticket2.put(fields.get("mrPRIORITY"), "1");
		ticket2.put(fields.get("mrSTATUS"), "Closed");
		ticket2.put(fields.get("mrUPDATEDATE"), "19/07/2010  11:57:14");
		ticket2.put(fields.get("mrSUBMITDATE"), "13/03/2008  10:14:42");
		ticket2.put(5, "0x00000000007BD3A6");
		ticket2.put(fields.get("Category"), "Software__bDistribution__fELMS");
		ticket2.put(fields.get("Sub__ucategory"), "Media__bCreation");
		ticket2.put(fields.get("Type__bof__bIssue"), "Incident");
		ticket2.put(fields.get("Billing__bCode"), "NULL");
		ticket2.put(fields.get("Source"), "Email");
		ticket2.put(fields.get("Closure__bCode"), "Solved");
		ticket2.put(fields.get("Impact"), "Individual");
		ticket2.put(fields.get("Billing__bEstimate"), "NULL");
		ticket2.put(fields.get("Type__bof__bwork__border"), "NULL");
		ticket2.put(fields.get("Sub__ucategory__bII"), "NULL");
		ticket2.put(fields.get("Software__bProduct"), "NULL");
		ticket2.put(fields.get("Due__bDate"), "NULL");
		ticket2.put(fields.get("Student__bLocation"), "IT__bHelp__bDesk");
		ticket2.put(fields.get("Billing__bRequired"), "NULL");
		ticket2.put(fields.get("Submit__bto__bBilling"), "NULL");

		Hashtable<Integer, Object> ticket3 = new Hashtable<Integer, Object>();
		ticket3.put(0, "15021");
		ticket3.put(fields.get("mrPRIORITY"), "1");
		ticket3.put(fields.get("mrSTATUS"), "_DELETED_");
		ticket3.put(fields.get("mrUPDATEDATE"), "01/06/2011  11:56:59");
		ticket3.put(fields.get("mrSUBMITDATE"), "18/03/2008  09:54:06");
		ticket3.put(5, "0x00000000007BD3A7");
		ticket3.put(fields.get("Category"), "Software__bDistribution__fELMS");
		ticket3.put(fields.get("Sub__ucategory"), "Distributed__bSoftware");
		ticket3.put(fields.get("Type__bof__bIssue"), "NULL");
		ticket3.put(fields.get("Billing__bCode"), "NULL");
		ticket3.put(fields.get("Source"), "NULL");
		ticket3.put(fields.get("Closure__bCode"), "NULL");
		ticket3.put(fields.get("Impact"), "Individual");
		ticket3.put(fields.get("Billing__bEstimate"), "NULL");
		ticket3.put(fields.get("Type__bof__bwork__border"), "NULL");
		ticket3.put(fields.get("Sub__ucategory__bII"), "NULL");
		ticket3.put(fields.get("Software__bProduct"), "SAS");
		ticket3.put(fields.get("Due__bDate"), "NULL");
		ticket3.put(fields.get("Student__bLocation"), "NULL");
		ticket3.put(fields.get("Billing__bRequired"), "NULL");
		ticket3.put(fields.get("Submit__bto__bBilling"), "NULL");

		/* HISTORY */

		Hashtable<Integer, Object> history1 = new Hashtable<Integer, Object>();
		history1.put(21, "1");
		history1.put(fields.get("mrSEQUENCE"), "1");
		history1.put(fields.get("mrFIELDNAME"), "mrSTATUS");
		history1.put(fields.get("mrNEWFIELDVALUE"), "Open");
		history1.put(fields.get("mrOLDFIELDVALUE"), "");
		history1.put(26, "12/06/2007  08:43:04");

		Hashtable<Integer, Object> history2 = new Hashtable<Integer, Object>();
		history2.put(21, "1");
		history2.put(fields.get("mrSEQUENCE"), "2");
		history2.put(fields.get("mrFIELDNAME"), "mrSTATUS");
		history2.put(fields.get("mrNEWFIELDVALUE"), "_DELETED_");
		history2.put(fields.get("mrOLDFIELDVALUE"), "Open");
		history2.put(26, "20/07/2007  11:16:49");

		Hashtable<Integer, Object> history3 = new Hashtable<Integer, Object>();
		history3.put(21, "10");
		history3.put(fields.get("mrSEQUENCE"), "19");
		history3.put(fields.get("mrFIELDNAME"), "mrSTATUS");
		history3.put(fields.get("mrNEWFIELDVALUE"), "Open");
		history3.put(fields.get("mrOLDFIELDVALUE"), "");
		history3.put(26, "25/07/2007  19:00:37");

		Hashtable<Integer, Object> history4 = new Hashtable<Integer, Object>();
		history4.put(21, "10");
		history4.put(fields.get("mrSEQUENCE"), "20");
		history4.put(fields.get("mrFIELDNAME"), "mrSTATUS");
		history4.put(fields.get("mrNEWFIELDVALUE"), "_DELETED_");
		history4.put(fields.get("mrOLDFIELDVALUE"), "Open");
		history4.put(26, "25/07/2007  19:01:11");

		/* TIME TRACKING */

		Hashtable<Integer, Object> timetracking1 = new Hashtable<Integer, Object>();
		timetracking1.put(27, "43863");
		timetracking1.put(fields.get("mrGENERATION"), "1");
		timetracking1.put(fields.get("mrTIMESPENT"), "15m");
		timetracking1.put(fields.get("mrTIMEDATE"), "02/07/2009  13:12:58");

		Hashtable<Integer, Object> timetracking2 = new Hashtable<Integer, Object>();
		timetracking2.put(27, "44009");
		timetracking2.put(fields.get("mrGENERATION"), "2");
		timetracking2.put(fields.get("mrTIMESPENT"), "15m");
		timetracking2.put(fields.get("mrTIMEDATE"), "07/07/2009  11:21:13");

		Hashtable<Integer, Object> timetracking3 = new Hashtable<Integer, Object>();
		timetracking3.put(27, "44545");
		timetracking3.put(fields.get("mrGENERATION"), "1");
		timetracking3.put(fields.get("mrTIMESPENT"), "1800m");
		timetracking3.put(fields.get("mrTIMEDATE"), "15/07/2009  17:01:05");

		Hashtable<Integer, Object> timetracking4 = new Hashtable<Integer, Object>();
		timetracking4.put(27, "45677");
		timetracking4.put(fields.get("mrGENERATION"), "3");
		timetracking4.put(fields.get("mrTIMESPENT"), "5m");
		timetracking4.put(fields.get("mrTIMEDATE"), "10/08/2009  16:15:27");

		data.add(ticket1);
		data.add(ticket2);
		data.add(ticket3);

		data.add(history1);
		data.add(history2);
		data.add(history3);
		data.add(history4);

		data.add(timetracking1);
		data.add(timetracking2);
		data.add(timetracking3);
		data.add(timetracking4);

		// 3. Return the array of hashtable
		return data;
	}

	private Hashtable<String, Integer> mapNamesField()
			throws FileNotFoundException, IOException {
		BufferedReader in;
		String line;
		Hashtable<String, Integer> retVal;

		in = new BufferedReader(new FileReader("FootprintsFields.txt"));
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
