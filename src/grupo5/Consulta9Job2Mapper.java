package grupo5;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 9 - JOB 2 (Grupo 5, MapReduce encadenado)
 * Este Mapper recibe lineas de DOS fuentes distintas a la vez (se agregan
 * ambas rutas de entrada en el Driver):
 *  1) La salida del Job 1: "Distrito\tTotal"  -> se emite como "TOTAL:total"
 *  2) El dataset original: separado por ";"   -> se emite como "DATA:..."
 * En ambos casos la llave de salida es el Distrito, para que el Reducer
 * reciba juntos el total de la zona y todos sus registros individuales.
 */
public class Consulta9Job2Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		String linea = value.toString();

		// Caso 1: linea de la salida del Job 1 ("Distrito<TAB>Total")
		String[] partesTab = linea.split("\t");
		if (partesTab.length == 2) {
			try {
				long total = Long.parseLong(partesTab[1].trim());
				output.collect(new Text(partesTab[0].trim()), new Text("TOTAL:" + total));
				return;
			} catch (NumberFormatException e) {
				// no era una linea de totales valida; se intenta como dataset
			}
		}

		// Caso 2: linea del dataset original (separada por ";")
		if (key.get() == 0) {
			return; // encabezado del CSV original
		}
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String distrito = campos[3].trim();
		String codigoIIEEQW = campos[7].trim();
		String institucionEducativa = campos[10].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		String dato = "DATA:" + codigoIIEEQW + "," + institucionEducativa + "," + nroUsuarios;
		output.collect(new Text(distrito), new Text(dato));
	}
}
