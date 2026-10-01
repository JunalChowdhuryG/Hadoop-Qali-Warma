package grupo7;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 14 (Grupo 7 - regresion)
 * Pregunta: cuanto del NroUsuarios se puede predecir usando SOLO el
 * Departamento? Se compara contra la Consulta 13 (que usa 3 features) para
 * ver si agregar mas variables mejora la prediccion.
 *
 * Features: x0=bias(1), x1=Departamento/25
 * Target: y = NroUsuarios/1000
 */
public class Consulta14Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {

	private double[] pesos = new double[2];

	@Override
	public void configure(JobConf job) {
		String pesosStr = job.get("pesos", "0,0");
		String[] partes = pesosStr.split(",");
		for (int i = 0; i < pesos.length && i < partes.length; i++) {
			pesos[i] = Double.parseDouble(partes[i]);
		}
	}

	public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
		if (key.get() == 0) {
			return;
		}

		String linea = value.toString();
		String[] campos = linea.split(";", -1);
		if (campos.length != 18) {
			return;
		}

		String departamento = campos[1].trim();

		int nroUsuarios;
		try {
			nroUsuarios = Integer.parseInt(campos[14].trim());
		} catch (NumberFormatException e) {
			return;
		}

		Integer depCode = FeatureEncoder.codigoDepartamento(departamento);
		if (depCode == null) {
			return;
		}

		double x0 = 1.0;
		double x1 = depCode / 25.0;

		double y = nroUsuarios / 1000.0;

		double h = pesos[0] * x0 + pesos[1] * x1;

		double error = h - y;

		output.collect(new Text("grad_0"), new DoubleWritable(error * x0));
		output.collect(new Text("grad_1"), new DoubleWritable(error * x1));

		double errorCuadrado = error * error;
		output.collect(new Text("sse"), new DoubleWritable(errorCuadrado));

		output.collect(new Text("n"), new DoubleWritable(1.0));
	}
}
