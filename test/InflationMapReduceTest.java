import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class InflationMapReduceTest {

	private Map<String, Double> daily  = new HashMap<String, Double>();
	
	@Before
	public void setup() {
		CSVParser<Entry<String,Double>> csv = new CSVParser<Entry<String,Double>>() {
			@Override
			public Entry<String, Double> parse(String line) {
				String[] strings = line.split(",",-1);
				final String date = strings[0];
				final Double inflationValue = "".equals(strings[1])?0D:Double.valueOf(strings[1]);
				return new Entry<String, Double>() {
					@Override public String getKey() { return date; }
					@Override public Double getValue() { return inflationValue; }
					@Override public Double setValue(Double value) { throw new IllegalArgumentException(); }
				};
			}
		};
		
		List<Entry<String, Double>> dailyEntries = csv.read("csv/Argentina_monthly_series.csv", true);
		daily.clear();
		for(Entry<String, Double> e : dailyEntries) {
			daily.put(e.getKey(), e.getValue());
		}
	}
	
	@Test
	public void testReduceToMonthly() {
		Map<String, Double> monthly = new InflationMapReduce().reduce(daily, InflationMapReduce.Period.month);
		for(Entry<String, Double> m : monthly.entrySet()) {
			System.out.println(m.getKey() + "\t" + m.getValue());
		}
	}
	
	@Test
	public void testReduceToYearly() {
		Map<String, Double> yearly = new InflationMapReduce().reduce(daily, InflationMapReduce.Period.year);
		for(Entry<String, Double> y : yearly.entrySet()) {
			System.out.println(y.getKey() + "\t" + y.getValue());
		}
	}
}
