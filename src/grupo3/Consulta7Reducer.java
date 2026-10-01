package grupo3;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Como es un job de filtro, el Reducer no agrega ni calcula nada -- solo
 * reenvia cada fila completa que el Mapper ya filtro
 */
public class Consulta7Reducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	public void reduce(Text t_key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		while (values.hasNext()) {
			Text fila = values.next();
			output.collect(t_key, fila);
		}
	}
}
