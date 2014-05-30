import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class InflationMapReduce {
	
	public enum Period {day, month, year}
	
	public Map<String, Double> reduce(Map<String, Double> daily, Period p) {
		if (Period.day.equals(p))  return daily;
		if (Period.month.equals(p))return reduce(map(daily));
		if (Period.year.equals(p)) return sum(reduce(map(daily)));
		return daily;
	}

	private Map<String, List<Double>> map(Map<String, Double> daily) {
		Map<String, List<Double>> map = new TreeMap<String, List<Double>>();
		for(Entry<String, Double> e : daily.entrySet()) {
			String key = e.getKey().substring(0,6);// 201412
			if (map.get(key) == null) map.put(key, new ArrayList<Double>());
			map.get(key).add(e.getValue());
		}
		return map;
	}
	
	private Map<String, Double> reduce(Map<String, List<Double>> map) {
		Map<String, Double> reduce = new TreeMap<String,Double>();
		for(Entry<String, List<Double>> e : map.entrySet()) {
			Double avg = 0D;
			for(Double sum : e.getValue()) avg += sum;
			avg /= e.getValue().size();
			reduce.put(e.getKey(), avg);
		}
		return reduce;
	}
	
	private Map<String, Double> sum(Map<String, Double> monthly) {
		Map<String, Double> reduce = new TreeMap<String, Double>();
		for(Entry<String, Double> e : monthly.entrySet()) {
			String key = e.getKey().substring(0,4);// 2014
			if (reduce.get(key) == null) reduce.put(key, 0D);
			reduce.put(key, reduce.get(key) + e.getValue());
		}
		return reduce;
	}
}