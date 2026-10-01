package grupo2;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class Consulta6Driver {
	public static void main(String[] args) {
		JobClient my_client = new JobClient();
		JobConf job_conf = new JobConf(Consulta6Driver.class);

		job_conf.setJobName("Consulta6_PromedioMedianaDesviacionPorDepartamento");

		// Tipos de salida
		job_conf.setOutputKeyClass(Text.class);
		job_conf.setOutputValueClass(Text.class);

		// Tipos INTERMEDIOS que produce el Mapper 
		job_conf.setMapOutputKeyClass(Text.class);
		job_conf.setMapOutputValueClass(IntWritable.class);

		job_conf.setMapperClass(grupo2.Consulta6Mapper.class);
		job_conf.setReducerClass(grupo2.Consulta6Reducer.class);

		job_conf.setInputFormat(TextInputFormat.class);
		job_conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(job_conf, new Path(args[1]));

		my_client.setConf(job_conf);
		try {
			JobClient.runJob(job_conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
