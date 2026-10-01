package grupo5;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 * args[0] = dataset original en HDFS
 * args[1] = directorio INTERMEDIO (salida del Job 1 / 2da entrada del Job 2)
 * args[2] = directorio de salida FINAL
 */
public class Consulta10Driver {
	public static void main(String[] args) {
		JobClient my_client = new JobClient();

		JobConf job_conf1 = new JobConf(Consulta10Driver.class);
		job_conf1.setJobName("Consulta10_Job1_TotalPorUnidadTerritorial");
		job_conf1.setOutputKeyClass(Text.class);
		job_conf1.setOutputValueClass(IntWritable.class);
		job_conf1.setMapperClass(grupo5.Consulta10Job1Mapper.class);
		job_conf1.setReducerClass(grupo5.SumReducer.class);
		job_conf1.setInputFormat(TextInputFormat.class);
		job_conf1.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(job_conf1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job_conf1, new Path(args[1]));
		my_client.setConf(job_conf1);

		JobClient my_client2 = new JobClient();
		JobConf job_conf2 = new JobConf(Consulta10Driver.class);
		job_conf2.setJobName("Consulta10_Job2_ProporcionPorUnidadTerritorial");
		job_conf2.setOutputKeyClass(Text.class);
		job_conf2.setOutputValueClass(Text.class);
		job_conf2.setMapperClass(grupo5.Consulta10Job2Mapper.class);
		job_conf2.setReducerClass(grupo5.PropZonaReducer.class);
		job_conf2.setInputFormat(TextInputFormat.class);
		job_conf2.setOutputFormat(TextOutputFormat.class);
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
