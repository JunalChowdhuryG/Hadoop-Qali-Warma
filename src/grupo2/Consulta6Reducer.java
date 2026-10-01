package grupo2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Recibe todos los NroUsuarios de un mismo Departamento y calcula:
 * - promedio (media aritmetica)
 * - mediana (ordenando los valores)
 * - desviacion estandar poblacional
 * Todo en una sola pasada: se guardan los valores en una lista porque la
 * mediana necesita orden, y de paso se usan para el promedio/desviacion.
 */
public class Consulta6Reducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		List<Integer> datos = new ArrayList<Integer>();
		long suma = 0;

		while (values.hasNext()) {
			int v = values.next().get();
			datos.add(v);
			suma += v;
		}

		int n = datos.size();
		if (n == 0) {
			return;
		}

		double promedio = (double) suma / n;
		// Desviacion estandar poblacional: raiz de la varianza
		double sumaDiferenciasCuadrado = 0.0;
		for (int v : datos) {
			double diferencia = v - promedio;
			sumaDiferenciasCuadrado += diferencia * diferencia;
		}
		double varianza = sumaDiferenciasCuadrado / n;
		double desviacionEstandar = Math.sqrt(varianza);
		// Mediana
		Collections.sort(datos);
		double mediana;
		if (n % 2 == 0) {
			mediana = (datos.get(n / 2 - 1) + datos.get(n / 2)) / 2.0;
		} else {
			mediana = datos.get(n / 2);
		}
		String resultado = String.format(
			"promedio=%.2f;mediana=%.2f;desviacionEstandar=%.2f;n=%d",
			promedio, mediana, desviacionEstandar, n
		);

		output.collect(t_key, new Text(resultado));
	}
}
