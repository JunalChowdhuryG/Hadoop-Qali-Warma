package grupo6;

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
 * Consulta 11 (Grupo 6 - clasificacion con Regresion Logistica)
 *
 * Cada epoca de entrenamiento es UN job de MapReduce completo (Consulta11Mapper
 * + SumDoubleReducer). Este Driver corre las epocas EN SECUENCIA: despues de
 * cada job, lee de HDFS los gradientes y la perdida ya sumados, actualiza los
 * pesos en memoria de Java, y los pasa a la siguiente epoca via job_conf.set.
 * Al final imprime la tabla epoca / loss / accuracy / pesos, que es la data
 * cruda para la tabla comparativa de metricas ML del informe.
 *
 * args[0] = dataset original en HDFS
 * args[1] = directorio BASE donde se guarda la salida de cada epoca
 *           (se crean subcarpetas epoch_0, epoch_1, ... epoch_N)
 */
public class Consulta11Driver {

	private static final int NUM_EPOCAS = 30;
	private static final double TASA_APRENDIZAJE = 5.0;
	private static final int NUM_PESOS = 4;

	public static void main(String[] args) {
		double[] pesos = new double[NUM_PESOS]; // arranca en 0.0, 0.0, 0.0, 0.0

		System.out.println("epoca\tloss\taccuracy\tpesos");

		try {
			for (int epoca = 0; epoca < NUM_EPOCAS; epoca++) {
				String outputDir = args[1] + "/epoch_" + epoca;

				JobConf job_conf = new JobConf(Consulta11Driver.class);
				job_conf.setJobName("Consulta11_LogReg_Epoca" + epoca);
				job_conf.set("pesos", pesosToString(pesos));

				job_conf.setOutputKeyClass(Text.class);
				job_conf.setOutputValueClass(DoubleWritable.class);

				job_conf.setMapperClass(grupo6.Consulta11Mapper.class);
				job_conf.setReducerClass(grupo6.SumDoubleReducer.class);
				job_conf.setNumReduceTasks(1);

				job_conf.setInputFormat(TextInputFormat.class);
				job_conf.setOutputFormat(TextOutputFormat.class);

				FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
				FileOutputFormat.setOutputPath(job_conf, new Path(outputDir));

				JobClient.runJob(job_conf);

				Map<String, Double> resultados = leerResultados(outputDir + "/part-00000");

				double n = resultados.get("n");
				double lossPromedio = resultados.get("loss") / n;
				double accuracy = resultados.get("correctos") / n;

				for (int i = 0; i < NUM_PESOS; i++) {
					double gradPromedio = resultados.get("grad_" + i) / n;
					pesos[i] = pesos[i] - TASA_APRENDIZAJE * gradPromedio;
				}

				System.out.println(epoca + "\t" + lossPromedio + "\t" + accuracy + "\t" + pesosToString(pesos));
			}

			System.out.println("Pesos finales: " + pesosToString(pesos));

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
