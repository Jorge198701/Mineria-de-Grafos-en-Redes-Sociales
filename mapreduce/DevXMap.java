package com.mapreduce.devx; 
import java.io.BufferedReader; 
import java.io.InputStreamReader;
import java.net.URI; import java.util.StringTokenizer;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
 
public class DevXMap extends Mapper<LongWritable, Text, Text,Text> {
 // Create Path, BufferedReader and Text variables 
Path file_path; 
BufferedReader buffer_reader; 
Text tweet_values = new Text();
public void map(LongWritable key, Text value, Context context) {
	try{	
map_config = new Configuration(); 
// Load Hadoop core files in configuration 
map_config.addResource(new Path("/user/hadoop/core-site.xml")); map_config.addResource(new Path("/user/hadoop/hdfs-site.xml"));	
// Create variables 
String searchkeyword = ""; 
// Open file from the file path 
file_path=new Path("files/repository/keys.txt"); 
FileSystem file_system = FileSystem.get(URI.create("files/repository/keys.txt"),
new Configuration()); 
// Load buffer reader 
buffer_reader=new BufferedReader(new InputStreamReader(file_system.open(file_path))); while(buffer_reader.ready()) {
 searchkeyword=buffer_reader.readLine().trim(); 
}
 // Get key value final 
Text key_value = new Text(searchkeyword); 
// Check value and take decision 
if(value == null) { 
return;
 } else{
	StringTokenizer string_tokens = new StringTokenizer(value.toString(),","); 
int count = 0; 
while(string_tokens.hasMoreTokens()) 
{
 count ++;
 if(count <=1) continue;
 String new_tweet_value = string_tokens.nextToken().toLowerCase().trim().replaceAll("\\*",""); if(new_tweet_value.contains(searchkeyword.toLowerCase().trim()))
 {	tweet_values.set(new_tweet_value);
 context.write(key_value,tweet_values);
 }	
}
 
}
 } catch(Exception e){	
e.printStackTrace();	
}
 
}
 }
