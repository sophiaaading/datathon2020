import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class exercise2 extends Configured implements Tool {

    public static class Map1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
        //declare private variable for comparison

        public void configure(JobConf job) {
            //configure is used for various methods. Most notably for changing the byte offset of a line
            //There is no need to change settings here. Leaving this in will not hurt, and it can be removed.
        }

        protected void setup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
        public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {

            String line = value.toString();
            String[] arr_line = line.split("\\s+");
            String num_volumes = arr_line[3];
            int num_volumes_int = Integer.parseInt(num_volumes.trim());

            output.collect(new Text(""), new DoubleWritable(num_volumes_int));
        }

        protected void cleanup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
    }
    public static class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
        //declare private variable for comparison
        //public static final int missing_info = 9999;

        public void configure(JobConf job) {//configure is used for various methods. Most notably for changing the byte offset of a line
            //There is no need to change settings here. Leaving this in will not hurt, and it can be removed.
        }

        protected void setup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
        public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {

            String line = value.toString();
            String[] arr_line = line.split("\\s+");

            String num_volumes = arr_line[4];
            int num_volumes_int = Integer.parseInt(num_volumes);

            output.collect(new Text(""), new DoubleWritable(num_volumes_int));

        }

        protected void cleanup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
    }
    public static class Reduce extends MapReduceBase implements Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        public void configure(JobConf job) {
        }

        protected void setup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }

        public void reduce(Text key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException
        {

            double sum = 0;
            double sum_of_squares = 0;
            double total = 0;
            double sd = 0;

            while (values.hasNext()) {
                double volume_next = values.next().get();
                sum += volume_next;
                sum_of_squares += Math.pow(volume_next, 2);
                total += 1;
            }

            sd = Math.sqrt(((1/total)*(sum_of_squares)) - Math.pow(((1/total)*sum),2));
            output.collect(key, new DoubleWritable(sd));

        }
        protected void cleanup(OutputCollector<Text, DoubleWritable> output) throws IOException, InterruptedException {
        }
    }
    public int run(String[] args) throws Exception {
        JobConf conf = new JobConf(getConf(), exercise2.class);
        conf.setJobName("exercise2");

        // added setNumReduceTasks
        conf.setNumReduceTasks(1);

        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(DoubleWritable.class);
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
        int res = ToolRunner.run(new Configuration(), new exercise2(), args);
        System.exit(res);
    }
}