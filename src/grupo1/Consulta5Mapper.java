package grupo1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 5 (Grupo 1 - 2+ campos)
 * Pregunta: ¿Cual es el total de NroUsuarios atendidos por Comite de
 * Compra y Modalidad de Atencion?
 * Key de salida: "COMITE_COMPRA,MODALIDAD_ATENCION"
 * Value de salida: NroUsuarios de esa fila
 */
public class Consulta5Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String comiteCompra = campos[15].trim();
		String modalidadAtencion = campos[17].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		String llaveCompuesta = comiteCompra + "," + modalidadAtencion;
		output.collect(new Text(llaveCompuesta), new IntWritable(nroUsuarios));
	}
}
