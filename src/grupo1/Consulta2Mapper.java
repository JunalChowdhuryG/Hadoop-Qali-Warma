package grupo1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 2 (Grupo 1 - 2+ campos)
 * Pregunta: ¿Cuantas IIEE hay por Provincia y Modalidad de Atencion
 * (RACIONES vs PRODUCTOS)?
 * Key de salida: "PROVINCIA,MODALIDAD_ATENCION"
 * Value de salida: 1 por cada fila (el Reducer cuenta)
 */
public class Consulta2Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

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

		String provincia = campos[2].trim();
		String modalidadAtencion = campos[17].trim();

		String llaveCompuesta = provincia + "," + modalidadAtencion;
		output.collect(new Text(llaveCompuesta), one);
	}
}
