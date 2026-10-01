package grupo4;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 8 (Grupo 4 - maximo y minimo agrupado)
 * Pregunta: Cual es la IIEE con mayor y con menor NroUsuarios atendidos
 * en cada Departamento?
 *
 * El value que se emite empaqueta 3 datos separados por coma:
 * NroUsuarios,InstitucionEducativa,CodigoIIEEQW
 */
public class Consulta8Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String departamento = campos[1].trim();
		String institucionEducativa = campos[10].trim();
		String codigoIIEEQW = campos[7].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		String paquete = nroUsuarios + "," + institucionEducativa + "," + codigoIIEEQW;
		output.collect(new Text(departamento), new Text(paquete));
	}
}
