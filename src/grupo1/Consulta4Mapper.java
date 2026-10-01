package grupo1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 4 (Grupo 1 - 2+ campos)
 * Pregunta: ¿Cuantas IIEE hay por Distrito y Nivel Educativo?
 * Key de salida: "DISTRITO,NIVEL_EDUCATIVO"
 * Value de salida: 1 por cada fila
 */
public class Consulta4Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);

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
		String nivelEducativo = campos[11].trim();

		String llaveCompuesta = distrito + "," + nivelEducativo;
		output.collect(new Text(llaveCompuesta), one);
	}
}
