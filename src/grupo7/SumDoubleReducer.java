package grupo7;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SumDoubleReducer extends MapReduceBase implements Reducer<Text, DoubleWritable, Text, DoubleWritable> {

	public void reduce(Text t_key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
		double suma = 0.0;
		while (values.hasNext()) {
			suma += values.next().get();
		}
		output.collect(t_key, new DoubleWritable(suma));
	}
}
