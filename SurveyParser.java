import java.io.BufferedReader; 
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

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
				Hashtable<Integer, Object> table = new Hashtable<Integer, Object>();

				token = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				for (int i = 0; i < token.length; i++) {
					table.put(i, token[i]);
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
		return true;
	}

	public String getFieldFile() {
		return "HDIFields.txt";
	}

	public static void main(String[] args) {
		SurveyParser parser = new SurveyParser();
		ArrayList<Hashtable<Integer, Object>> list = parser.parse();
		DTOFactory factory = new DTOFactory();
		try {
			System.out.println(factory.makeDTO(list, parser.getFieldFile()));
		} catch (Exception e) {
			System.err.println("Factory reported error");
			System.exit(1);
		}
	}
}