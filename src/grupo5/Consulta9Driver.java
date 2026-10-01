package grupo5;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 * args[0] = dataset original en HDFS (ej. /input_dir/dataQaliWarma.csv)
 * args[1] = directorio INTERMEDIO donde queda la salida del Job 1
 *           (tambien sirve de segunda entrada para el Job 2)
 * args[2] = directorio de salida FINAL del Job 2
 */
public class Consulta9Driver {
	public static void main(String[] args) {
		JobClient my_client = new JobClient();

		//  JOB 1: total de NroUsuarios por Distrito
		JobConf job_conf1 = new JobConf(Consulta9Driver.class);
		job_conf1.setJobName("Consulta9_Job1_TotalPorDistrito");
		job_conf1.setOutputKeyClass(Text.class);
		job_conf1.setOutputValueClass(IntWritable.class);
		job_conf1.setMapperClass(grupo5.Consulta9Job1Mapper.class);
		job_conf1.setReducerClass(grupo5.SumReducer.class);
		job_conf1.setInputFormat(TextInputFormat.class);
		job_conf1.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(job_conf1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job_conf1, new Path(args[1]));
		my_client.setConf(job_conf1);

		//  JOB 2: proporcion de cada IIEE sobre el total de su Distrito
		JobClient my_client2 = new JobClient();
		JobConf job_conf2 = new JobConf(Consulta9Driver.class);
		job_conf2.setJobName("Consulta9_Job2_ProporcionPorDistrito");
		job_conf2.setOutputKeyClass(Text.class);
		job_conf2.setOutputValueClass(Text.class);
		job_conf2.setMapperClass(grupo5.Consulta9Job2Mapper.class);
		job_conf2.setReducerClass(grupo5.PropZonaReducer.class);
		job_conf2.setInputFormat(TextInputFormat.class);
		job_conf2.setOutputFormat(TextOutputFormat.class);
		// El Job 2 lee DOS fuentes: el dataset original y la salida del Job 1
		FileInputFormat.addInputPath(job_conf2, new Path(args[0]));
		FileInputFormat.addInputPath(job_conf2, new Path(args[1]));
		FileOutputFormat.setOutputPath(job_conf2, new Path(args[2]));
		my_client2.setConf(job_conf2);

		try {
			JobClient.runJob(job_conf1);
			JobClient.runJob(job_conf2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
