package grupo7;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 13 (Grupo 7 - regresion)
 * Pregunta: se puede predecir el NroUsuarios de una IIEE a partir de su
 * Departamento, NivelEducativo y ModalidadAtencion?
 */
public class Consulta13Mapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {

	private double[] pesos = new double[4];

	@Override
	public void configure(JobConf job) {
		String pesosStr = job.get("pesos", "0,0,0,0");
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
		String nivelEducativo = campos[11].trim();
		String modalidadAtencion = campos[17].trim();

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
		int nivelCode = FeatureEncoder.codigoNivelEducativo(nivelEducativo);

		double x0 = 1.0;
		double x1 = depCode / 25.0;
		double x2 = nivelCode / 3.0;
		double x3 = modalidadAtencion.equals("RACIONES") ? 1.0 : 0.0;

		double y = nroUsuarios / 1000.0; // target escalado

		double h = pesos[0] * x0 + pesos[1] * x1 + pesos[2] * x2 + pesos[3] * x3; // sin sigmoide

		double error = h - y;

		output.collect(new Text("grad_0"), new DoubleWritable(error * x0));
		output.collect(new Text("grad_1"), new DoubleWritable(error * x1));
		output.collect(new Text("grad_2"), new DoubleWritable(error * x2));
		output.collect(new Text("grad_3"), new DoubleWritable(error * x3));

		double errorCuadrado = error * error;
		output.collect(new Text("sse"), new DoubleWritable(errorCuadrado)); // suma de errores al cuadrado

		output.collect(new Text("n"), new DoubleWritable(1.0));
	}
}
