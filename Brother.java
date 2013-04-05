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

/**
* This Class extends the Parser interface and is responsible for retrieving, parsing and validating the data for Big Brother.
* @author Matt Stark and Lawrence Wong
*/
public class Brother implements Parser {
	private Integer fileCount = 0;
	/**
	* This method is responsible for parsing the internet.xml file that is retrieved from the general server. This method calls the method resposible for retrieving the file.
	* @return ArrayList<Hashtable<Integer, Object>> This contains the records within from Big Brother.
	*/
	public ArrayList<Hashtable<Integer, Object>> parse() {
		BufferedReader in = null;
		String line;
		String prevLine = null;
		int rraCount = 0;
		String database = "";
		ArrayList<Hashtable<Integer, Object>> retVal = new ArrayList<Hashtable<Integer, Object>>();
		Hashtable<Integer, Object> current = new Hashtable<Integer, Object>();
		fileCount = 0;
		getFile();
		if(fileCount > 25){
			return retVal;
		}
        
        try{
        	in = new BufferedReader(new FileReader("internet.xml"));
        } catch(FileNotFoundException e){
        	System.out.println("File not found");
        	return null;
        }

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

	/**
	* This method is responsible for retrieving the file from the general server. This method is called by parse.
	*/
	private void getFile(){
		fileCount++;
		if(fileCount > 25){
			return;
		}
		String[]  cmd = { "/bin/sh", "-c", "scp cis3760@general.uoguelph.ca:/users/windtalk/internet.xml ~" };
    	Process proc;

        try {
			proc = Runtime.getRuntime().exec(cmd);
			Worker worker = new Worker(proc);
  			worker.start();
  			worker.join(3000);
  			if(worker.exit == false){
  				worker.interrupt();
  				proc.destroy();
  				getFile();
  			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	/**
	* This method is called by parse and is used to change the comment beside the row, into it's own timestamp tag so it can be more effectively parsed by the Data Warehouse.
	* @return String The new string with the timestamp.
	* @param line This is the line that requires the swap from comment to timestamp tag.
	*/
	private String addTimestamp(String line){
		line = line.replace("<!--", "<timestamp>");
		line = line.replace("-->", "</timestamp>");
		return line;
	}

	/**
	* This method is used to validate the DTO with the Big Brother xsd file.
	* @return boolean A boolean value based on whether the String was validated.
	* @param xml This is the dto string that requires the validation.
	*/
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

	/**
	* This method returns the field enumeration file specific to Big Brother
	* @return String This is the file name.
	*/
	public String getFieldFile() {
		return "BigBrotherFields.txt";
	}

	/**
	* This method is used as a testing harness to be used to simulate the Retriever requesting Big Brother parsing and validation.
	* @param args This is an array of Strings that are the command line arguments when calling the program to be run.
	*/
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

	/**
	* This class was built inside the Brother class and extends Thread. This is used for the timeouts and multithreading that is required in case the file download fails.
	* @author Matt Stark
	*/
	private static class Worker extends Thread {
		private final Process process;
		private boolean exit = false;
		/**
		* This is the constructor. It copies over the process parameter to being a class variable of Worker.
		* @param process This is the process that you want to have threaded
		*/
		private Worker(Process process) {
			this.process = process;
		}
		/**
		* This is the method that is run when the program signals for the thread to start. It calls the waitFor method in order to monitor and keep track of the progress of the execution of the process. The class variable exit is set to true if the waitFor finishes.
		*/
		public void run() {
			try { 
				process.waitFor();
				exit = true;
			} catch (InterruptedException ignore) {
				return;
			}
		}  
	}
}

