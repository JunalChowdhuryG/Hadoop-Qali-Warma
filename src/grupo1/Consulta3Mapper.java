package grupo1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 3 (Grupo 1 - 2+ campos)
 * Pregunta: ¿Cual es el total de NroUsuarios atendidos por Unidad
 * Territorial y por Item (comite de compra/producto)?
 * Key de salida: "UNIDAD_TERRITORIAL,ITEM"
 * Value de salida: NroUsuarios de esa fila
 */
public class Consulta3Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

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
		String item = campos[16].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		String llaveCompuesta = unidadTerritorial + "," + item;
		output.collect(new Text(llaveCompuesta), new IntWritable(nroUsuarios));
	}
}
