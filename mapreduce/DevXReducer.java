package com.mapreduce.devx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.RandomAccess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DevXReducer extends Reducer<Text ,Text,Text,Text> 
{	 	
 // Create variables for file path
    Path positive_file_path;
    Path negative_file_path;
    Path output_file_path;
    Path keyword_file_path;
    
    // Create variables for buffer
    BufferedReader positive_buff_reader;
    BufferedReader negative_buff_reader;
    BufferedReader keyword_buff_reader; 
    
    // Create variables for calculation
    static Double total_record_count=new Double("0");	
   
    static Double count_neg=new Double("0");
    static Double count_pos=new Double("0");
    static Double count_neu=new Double("0");
   
    static Double percent_neg=new Double("0");
    static Double percent_pos=new Double("0");
    static Double percent_neu=new Double("0");

    Pattern pattrn_matcher;
    Matcher matcher_txt;
    static int new_row=0;
    FSDataOutputStream out_1st,out_2nd;	 

 
	 /**
	 * @param key
	 * @param values
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException
	    {          	
    	// Create configuration for reducer
    	Configuration reduce_config = new Configuration();
    	
        // Load hadoop config files
    	reduce_config.addResource(new Path("/user/hadoop/core-site.xml"));
        reduce_config.addResource(new Path("/user/hadoop/hdfs-site.xml"));
    	
        // Create variables
        String key_word = "";
        String check_keyword=key_word;  
        
    	keyword_file_path=new Path("files/repository/keys.txt");
        FileSystem file_system_read = FileSystem.get(URI.create("files/repository/keys.txt"),new Configuration());
        keyword_buff_reader=new BufferedReader(new InputStreamReader(file_system_read.open(keyword_file_path)));
        
        FileSystem get_filesys = FileSystem.get(reduce_config);
		FileSystem get_filesys_posneg = FileSystem.get(reduce_config);
        
        Path path_output = new Path("/user/sentiment_output_file.txt");
        Path path_output_posneg = new Path("/user/posneg_output_file.txt");
        
        // Get keyword
        while(keyword_buff_reader.ready())
        {
        	key_word=keyword_buff_reader.readLine().trim();
        
        } 
        
        // Check file system
        if (!get_filesys.exists(path_output)) {                       
            out_1st = get_filesys.create(path_output);
            out_2nd = get_filesys_posneg.create(path_output_posneg);
           
        }             
         
        // Check keyword matching using positive and negative dictionaries
        if(check_keyword.equals(key.toString().toLowerCase()))
        {     	
        	for(Text new_tweets:values)
            {	
            	// Load positive word dictionary
				positive_file_path=new Path("/user/map_reduce/pos_words.txt");
                FileSystem filesystem_one = FileSystem.get(URI.create("files/pos_words.txt"),new Configuration());
                positive_buff_reader=new BufferedReader(new InputStreamReader(filesystem_one.open(positive_file_path)));
              
                // Load negative word disctinary
                negative_file_path = new Path("/user/map_reduce/neg_words.txt");
                FileSystem filesystem_two = FileSystem.get(URI.create("files/neg_words.txt"),new Configuration());
                negative_buff_reader =new BufferedReader(new InputStreamReader(filesystem_two.open(negative_file_path)));

                ++total_record_count;

                boolean first_flag=false;
                boolean second_flag=false;

                String all_tweets=new_tweets.toString();              
                String first_regex = "";
                String second_regex = "";

                while(positive_buff_reader.ready())
                {
                    first_regex=positive_buff_reader.readLine().trim();
                    new_row++;           
                    pattrn_matcher = Pattern.compile(first_regex, Pattern.CASE_INSENSITIVE);
                    matcher_txt = pattrn_matcher.matcher(all_tweets);
                    first_flag=matcher_txt.find();

                    if(first_flag)
                    {  
                    	out_2nd.writeBytes(all_tweets);
                        context.write(new Text(first_regex),new Text(all_tweets));                  
                        break;
                    }       
                }
                while(negative_buff_reader.ready())
                {
                    new_row++;                    
                    second_regex=negative_buff_reader.readLine().trim();
                    pattrn_matcher = Pattern.compile(second_regex, Pattern.CASE_INSENSITIVE);
                    matcher_txt = pattrn_matcher.matcher(all_tweets);
                    second_flag=matcher_txt.find();
                    if(second_flag)
                    {                    	
                    	out_2nd.writeBytes(all_tweets);
                        context.write(new Text(second_regex),new Text(all_tweets));
                        break;
                    }

                }
                if(first_flag&second_flag)
                {
                    ++count_neu;
                }
                else
                {
                    if(first_flag)
                    {
                        ++count_pos;
                    }
                    if(second_flag)
                    {
                        ++count_neg;
                    }
                    if(first_flag==false&second_flag==false)
                    {
                        ++count_neu;
                    }
                }
                // Close buffers
                negative_buff_reader.close();
                positive_buff_reader.close();

            }
        	
        	// Calculate percent values
            percent_pos=count_pos/total_record_count*100;
            percent_neg=count_neg/total_record_count*100;
            percent_neu=count_neu/total_record_count*100;

          try{        	  
				// Write to the files
        	    out_1st.writeBytes("\n"+key_word);
				out_1st.writeBytes(","+total_record_count);
				out_1st.writeBytes(","+percent_neg);
				out_1st.writeBytes(","+percent_pos);
				out_1st.writeBytes(","+percent_neu);
				
				// Close file systems
				out_1st.close();
				get_filesys.close();
          }catch(Exception e){
        	  e.printStackTrace();
          }	
        }        
    }
}
