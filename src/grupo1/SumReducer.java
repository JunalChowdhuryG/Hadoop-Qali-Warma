package grupo1;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;


public class SumReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		Text key = t_key;
		int suma = 0;
		while (values.hasNext()) {
			IntWritable value = (IntWritable) values.next();
			suma += value.get();
		}
		output.collect(key, new IntWritable(suma));
	}
}
