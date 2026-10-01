package grupo2;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 6 (Grupo 2 - promedio, mediana, desviacion estandar)
 * Pregunta: Cual es el promedio, la mediana y la desviacion estandar de
 * NroUsuarios (ninos atendidos) por Departamento?
 */
public class Consulta6Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String departamento = campos[1].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		output.collect(new Text(departamento), new IntWritable(nroUsuarios));
	}
}
