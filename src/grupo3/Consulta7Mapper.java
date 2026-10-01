package grupo3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 7 (Grupo 3 - busqueda de subtexto en varios campos)
 * Pregunta: Que registros de IIEE contienen el subtexto "SAN" en su nombre
 * de institucion educativa, su direccion, o su centro poblado?
 */
public class Consulta7Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	// Cambiar este valor para buscar otro subtexto sin tocar el resto del codigo
	private static final String SUBTEXTO = "SAN";

	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}
		//String SUBTEXTO = "SAN";
		String codigoIIEEQW = campos[7].trim();
		String institucionEducativa = campos[10].trim().toUpperCase();
		String direccionIIEE = campos[13].trim().toUpperCase();
		String centroPoblado = campos[5].trim().toUpperCase();

		boolean coincide = institucionEducativa.contains(SUBTEXTO)
			|| direccionIIEE.contains(SUBTEXTO)
			|| centroPoblado.contains(SUBTEXTO);

		if (coincide) {
			output.collect(new Text(codigoIIEEQW), value);
		}
	}
}
