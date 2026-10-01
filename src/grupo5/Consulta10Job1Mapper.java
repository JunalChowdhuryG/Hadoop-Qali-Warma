package grupo5;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 10 - JOB 1 (Grupo 5, MapReduce encadenado)
 * Igual que la Consulta 9 pero agrupando por UnidadTerritorial en vez de
 * Distrito.
 */
public class Consulta10Job1Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String unidadTerritorial = campos[6].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		output.collect(new Text(unidadTerritorial), new IntWritable(nroUsuarios));
	}
}
