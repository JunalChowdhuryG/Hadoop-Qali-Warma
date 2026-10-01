package grupo7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Consulta 14 (Grupo 7 - Regresion Lineal simple, 1 feature)
 *
 * args[0] = dataset original en HDFS
 * args[1] = directorio BASE donde se guarda la salida de cada epoca
 */
public class Consulta14Driver {

	private static final int NUM_EPOCAS = 40;
	private static final double TASA_APRENDIZAJE = 0.1;
	private static final int NUM_PESOS = 2;

	public static void main(String[] args) {
		double[] pesos = new double[NUM_PESOS];

		System.out.println("epoca\tmse\trmse\tpesos");

		try {
			for (int epoca = 0; epoca < NUM_EPOCAS; epoca++) {
				String outputDir = args[1] + "/epoch_" + epoca;

				JobConf job_conf = new JobConf(Consulta14Driver.class);
				job_conf.setJobName("Consulta14_RegLineal_Epoca" + epoca);
				job_conf.set("pesos", pesosToString(pesos));

				job_conf.setOutputKeyClass(Text.class);
				job_conf.setOutputValueClass(DoubleWritable.class);

				job_conf.setMapperClass(grupo7.Consulta14Mapper.class);
				job_conf.setReducerClass(grupo7.SumDoubleReducer.class);
				job_conf.setNumReduceTasks(1);

				job_conf.setInputFormat(TextInputFormat.class);
				job_conf.setOutputFormat(TextOutputFormat.class);

				FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
				FileOutputFormat.setOutputPath(job_conf, new Path(outputDir));

				JobClient.runJob(job_conf);

				Map<String, Double> resultados = leerResultados(outputDir + "/part-00000");

				double n = resultados.get("n");
				double mse = resultados.get("sse") / n;
				double rmse = Math.sqrt(mse);

				for (int i = 0; i < NUM_PESOS; i++) {
					double gradPromedio = resultados.get("grad_" + i) / n;
					pesos[i] = pesos[i] - TASA_APRENDIZAJE * gradPromedio;
				}

				System.out.println(epoca + "\t" + mse + "\t" + rmse + "\t" + pesosToString(pesos));
			}

			System.out.println("Pesos finales: " + pesosToString(pesos));
			System.out.println("Nota: el target y los pesos estan en la escala NroUsuarios/1000.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String pesosToString(double[] pesos) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pesos.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(pesos[i]);
		}
		return sb.toString();
	}

	private static Map<String, Double> leerResultados(String rutaHdfs) throws IOException {
		Map<String, Double> mapa = new HashMap<String, Double>();
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(rutaHdfs);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
		String linea;
		while ((linea = br.readLine()) != null) {
			String[] partes = linea.split("\t");
			if (partes.length == 2) {
				mapa.put(partes[0], Double.parseDouble(partes[1]));
			}
		}
		br.close();
		fs.close();
		return mapa;
	}
}
