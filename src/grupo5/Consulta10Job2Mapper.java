package grupo5;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 10 - JOB 2 (Grupo 5, MapReduce encadenado)
 * Misma logica que Consulta9Job2Mapper, pero la llave de union es
 * UnidadTerritorial (indice 6) en vez de Distrito (indice 3).
 */
public class Consulta10Job2Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		String linea = value.toString();

		String[] partesTab = linea.split("\t");
		if (partesTab.length == 2) {
			try {
				long total = Long.parseLong(partesTab[1].trim());
				output.collect(new Text(partesTab[0].trim()), new Text("TOTAL:" + total));
				return;
			} catch (NumberFormatException e) {
			}
		}

		if (key.get() == 0) {
			return;
		}
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String unidadTerritorial = campos[6].trim();
		String codigoIIEEQW = campos[7].trim();
		String institucionEducativa = campos[10].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		String dato = "DATA:" + codigoIIEEQW + "," + institucionEducativa + "," + nroUsuarios;
		output.collect(new Text(unidadTerritorial), new Text(dato));
	}
}
