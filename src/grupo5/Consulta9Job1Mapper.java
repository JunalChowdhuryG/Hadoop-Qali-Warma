package grupo5;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 9 - JOB 1 (Grupo 5, MapReduce encadenado)
 * Calcula el total de NroUsuarios por Distrito. Es insumo del Job 2.
 */
public class Consulta9Job1Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String distrito = campos[3].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		output.collect(new Text(distrito), new IntWritable(nroUsuarios));
	}
}
