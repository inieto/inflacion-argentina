

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CSVParser<T> {
	
	public List<T> read(String filePath, boolean hasHeader) {
		List<T> result = new ArrayList<T>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line;
			boolean readHeader = false;
			while ((line = br.readLine()) != null) {
				if(hasHeader && !readHeader) {
					line = br.readLine();
					if (line == null) break;
				}
				result.add(parse(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public abstract T parse (String line);

}
