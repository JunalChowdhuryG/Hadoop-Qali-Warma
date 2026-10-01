package grupo1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 1 (Grupo 1 - 2+ campos)
 * Pregunta: ¿Cual es el total de ninos atendidos (NroUsuarios) por
 * Departamento y Nivel Educativo?
 * Key de salida: "DEPARTAMENTO,NIVEL_EDUCATIVO"
 * Value de salida: NroUsuarios de esa fila (el Reducer los suma)
 */
public class Consulta1Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		// Saltar la fila de encabezado
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return; // fila mal formada, se descarta
		}

		String departamento = campos[1].trim();
		String nivelEducativo = campos[11].trim();
		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return; 
		}
		String llaveCompuesta = departamento + "," + nivelEducativo;
		output.collect(new Text(llaveCompuesta), new IntWritable(nroUsuarios));
	}
}
