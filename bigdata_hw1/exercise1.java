import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class exercise1 extends Configured implements Tool {

    public static class Map1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
        //declare private variable for comparison

        public void configure(JobConf job) {//configure is used for various methods. Most notably for changing the byte offset of a line
            //There is no need to change settings here. Leaving this in will not hurt, and it can be removed.
        }

        protected void setup(OutputCollector<Text, IntWritable> output) throws IOException, InterruptedException {
        }
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

            String line = value.toString();
            String[] arr_line = line.split("\\s+");

            int check_year = Integer.parseInt(arr_line[1]);

            try {
                String word = arr_line[0];
                String year = arr_line[1];
                String num_volumes = arr_line[3];

                int num_volumes_int = Integer.parseInt(num_volumes);

                if (word.contains("nu")) {
                    String k = year + ", nu, ";
                    output.collect(new Text(k),  new IntWritable(num_volumes_int));
                }
                else if (word.contains("chi")) {
                    String k = year + ", chi, ";
                    output.collect(new Text(k),  new IntWritable(num_volumes_int));
                }
                else if (word.contains("haw")) {
                    String k = year + ", haw, ";
                    output.collect(new Text(k),  new IntWritable(num_volumes_int));
                }
            }
            catch (Exception e) {
			}


        }

        protected void cleanup(OutputCollector<Text, IntWritable> output) throws IOException, InterruptedException {
        }
    }
    public static class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
        //declare private variable for comparison

        public void configure(JobConf job) {//configure is used for various methods. Most notably for changing the byte offset of a line
            //There is no need to change settings here. Leaving this in will not hurt, and it can be removed.
        }

        protected void setup(OutputCollector<Text, IntWritable> output) throws IOException, InterruptedException {
        }
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

            String line = value.toString();
            String[] arr_line = line.split("\\s+");

            int check_year = Integer.parseInt(arr_line[2]);
            String word1 = arr_line[0];
            String word2 = arr_line[1];
            String year = arr_line[2];
            String num_volumes = arr_line[4];
            int num_volumes_int = Integer.parseInt(num_volumes);

            //first word
            try {
                if (word1.contains("nu")) {
                    String k = year + ", nu, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }
                else if (word1.contains("chi")) {
                    String k = year + ", chi, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }
                else if (word1.contains("haw")) {
                    String k = year + ", haw, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }

                //second word
                if (word2.contains("nu")) {
                    String k = year + ", nu, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }
                else if (word2.contains("chi")) {
                    String k = year + ", chi, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }
                else if (word2.contains("haw")) {
                    String k = year + ", haw, ";
                    output.collect(new Text(k), new IntWritable(num_volumes_int));
                }
            } 
            catch (Exception e) {	
			}
        }

        protected void cleanup(OutputCollector<Text, IntWritable> output) throws IOException, InterruptedException {
        }
    }
    
    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, DoubleWritable> {

        public void configure(JobConf job) {
        }

        protected void setup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }

        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException
        {

            double sum = 0.0;
            double total = 0.0;
            double average = 0.0;

            while (values.hasNext()) {
                sum += values.next().get();
                total += 1;
            }
            average = sum/total;
            output.collect(key, new DoubleWritable(average));

        }
        protected void cleanup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
    }
    public int run(String[] args) throws Exception {
        JobConf conf = new JobConf(getConf(), exercise1.class);
        conf.setJobName("exercise1");

        // added setNumReduceTasks
        conf.setNumReduceTasks(1);

        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(IntWritable.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(DoubleWritable.class);

        // conf.setMapperClass(Map.class);
        conf.setMapperClass(Map1.class);
        conf.setMapperClass(Map2.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        MultipleInputs.addInputPath(conf, new Path(args[0]), TextInputFormat.class,Map1.class);
        MultipleInputs.addInputPath(conf, new Path(args[1]), TextInputFormat.class,Map2.class);
        FileOutputFormat.setOutputPath(conf, new Path(args[2]));

        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new exercise1(), args);
        System.exit(res);
    }
}