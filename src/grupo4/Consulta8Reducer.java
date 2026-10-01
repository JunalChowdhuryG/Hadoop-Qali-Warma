package grupo4;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class Consulta8Reducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	public void reduce(Text t_key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		int maxUsuarios = Integer.MIN_VALUE;
		int minUsuarios = Integer.MAX_VALUE;
		String iieeMax = "";
		String iieeMin = "";

		while (values.hasNext()) {
			Text value = values.next();
			String[] partes = value.toString().split(",", 3);
			if (partes.length < 3) {
				continue;
			}

			int nroUsuarios;
			try {
				nroUsuarios = Integer.parseInt(partes[0]);
			} catch (NumberFormatException e) {
				continue;
			}
			String nombreIIEE = partes[1];
			String codigoIIEE = partes[2];

			if (nroUsuarios > maxUsuarios) {
				maxUsuarios = nroUsuarios;
				iieeMax = nombreIIEE + " (codigo=" + codigoIIEE + ")";
			}
			if (nroUsuarios < minUsuarios) {
				minUsuarios = nroUsuarios;
				iieeMin = nombreIIEE + " (codigo=" + codigoIIEE + ")";
			}
		}

		String resultado = "max=" + maxUsuarios + ":" + iieeMax
			+ ";min=" + minUsuarios + ":" + iieeMin;

		output.collect(t_key, new Text(resultado));
	}
}
