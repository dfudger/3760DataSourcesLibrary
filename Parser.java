import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public interface Parser {
	public ArrayList<Hashtable<Integer, Object>> parse();
	public String getFieldFile();
	public boolean validate(String xml);
}
