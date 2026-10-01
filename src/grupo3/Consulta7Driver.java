package grupo3;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class Consulta7Driver {
	public static void main(String[] args) {
		JobClient my_client = new JobClient();
		JobConf job_conf = new JobConf(Consulta7Driver.class);

		job_conf.setJobName("Consulta7_BusquedaSubtextoSAN");

		job_conf.setOutputKeyClass(Text.class);
		job_conf.setOutputValueClass(Text.class);

		job_conf.setMapperClass(grupo3.Consulta7Mapper.class);
		job_conf.setReducerClass(grupo3.Consulta7Reducer.class);

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
