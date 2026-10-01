package grupo5;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Reducer del Job 1 para las Consultas 9 y 10. Es identico en concepto al
 * SumReducer del Grupo 1 -- suma NroUsuarios por llave -- se reutiliza para
 * ambas consultas porque no depende de si la llave es Distrito o
 * UnidadTerritorial.
 */
public class SumReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		int suma = 0;
		while (values.hasNext()) {
			suma += values.next().get();
		}
		output.collect(t_key, new IntWritable(suma));
	}
}
