package grupo5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Reducer del Job 2, compartido por las Consultas 9 y 10.
 * Para cada llave de zona (Distrito o UnidadTerritorial segun la consulta)
 * recibe una mezcla de:
 *  - un valor "TOTAL:total"  -> viene de la salida del Job 1 (ya agregado)
 *  - varios "DATA:codigoIIEE,institucionEducativa,nroUsuarios" -> vienen del
 *    dataset original
 * Primero separa ambos tipos, y luego calcula, para cada registro
 * individual, que proporcion decimal representa sobre el total de su zona.
 */
public class PropZonaReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	public void reduce(Text t_key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		List<String> datos = new ArrayList<String>();
		long total = -1;

		while (values.hasNext()) {
			String v = values.next().toString();
			if (v.startsWith("TOTAL:")) {
				total = Long.parseLong(v.substring("TOTAL:".length()));
			} else if (v.startsWith("DATA:")) {
				datos.add(v.substring("DATA:".length()));
			}
		}

		if (total <= 0) {
			return; // no llego el total para esta zona (no deberia pasar)
		}

		for (String dato : datos) {
			String[] partes = dato.split(",", 3);
			if (partes.length < 3) {
				continue;
			}
			String codigoIIEE = partes[0];
			String institucionEducativa = partes[1];

			int nroUsuarios;
			try {
				nroUsuarios = Integer.parseInt(partes[2]);
			} catch (NumberFormatException e) {
				continue;
			}

			double proporcion = (double) nroUsuarios / total;

			String resultado = String.format(
				"zona=%s;institucion=%s;nroUsuarios=%d;totalZona=%d;proporcion=%.4f",
				t_key.toString(), institucionEducativa, nroUsuarios, total, proporcion
			);

			output.collect(new Text(codigoIIEE), new Text(resultado));
		}
	}
}
