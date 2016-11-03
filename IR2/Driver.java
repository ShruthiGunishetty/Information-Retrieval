import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
public class Driver{
    protected StanfordCoreNLP pipe;
    public Driver() {
        Properties proplist;
        proplist = new Properties();
        proplist.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipe = new StanfordCoreNLP(proplist);
    }
    static  ArrayList<String> stop_lst=new ArrayList<String>();
    static  TreeMap<String,TreeMap<Integer,Integer>> lemma_mapper=new TreeMap<String,TreeMap<Integer,Integer>>();
	static TreeMap<String,TreeMap<Integer,Integer>> stem_mapper=new TreeMap<String,TreeMap<Integer,Integer>>(); 
    static Map<Integer,Document_info_len_max> stem_tf_mapper=new TreeMap<Integer,Document_info_len_max>();
	static TreeMap<String,Integer> lemma_tf_mapper=new TreeMap<String,Integer>();
	static int index1_size=0; 
	static int index2_size=0;
	static int lemma_compr_size=0;
	static int stem_compr_size=0;
	static long timer1=0,timer2=0;
    public List<String> get_lemmas_from_doc(String document)
    {
    	Annotation document1 = new Annotation(document); 
    	String str_temp="";
        List<String> lemmas_doc = new LinkedList<String>();
        this.pipe.annotate(document1);       
        List<CoreMap> sentences = document1.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            	str_temp=token.get(LemmaAnnotation.class);
            	if(!stop_lst.contains(str_temp)){
            	    lemmas_doc.add(str_temp);
            	}         
            }
        }
        return lemmas_doc;
    }
public static void uncmpr_files_gen_meth()throws IOException, FileNotFoundException{
	    long u_time=System.currentTimeMillis();
        Iterator it = stem_mapper.entrySet().iterator(); 
        TreeMap<Integer,Integer> hex=null;
	    Writer wr_out1;
	    Writer output2;
        File version_1 = new File("version2_index");
        File version_2 = new File("version1_index");
        output2 = new BufferedWriter(new FileWriter(version_2));
        wr_out1 = new BufferedWriter(new FileWriter(version_1));
        while (it.hasNext()) {
            Map.Entry<String,TreeMap<Integer,Integer>> map_12 = (Map.Entry<String,TreeMap<Integer,Integer>>)it.next();
            hex=map_12.getValue();
            String k_str_key=map_12.getKey();
            Integer size_map_12=map_12.getValue().size();
            index2_size=index2_size+hex.size()*8+k_str_key.length()+(1)*4;
            
              wr_out1.write(map_12.getKey()+" "+map_12.getValue().size()+" "+map_12.getValue());
              Set<Integer> keys = hex.keySet(); 
              wr_out1.write(" ");
              for(Integer key: keys){
              wr_out1.write(key.toString());
              wr_out1.write(" ");
              } 
              wr_out1.write("\n");
        }
        Iterator itr_tf = stem_tf_mapper.entrySet().iterator();
        while(itr_tf.hasNext()){
       	 Map.Entry<Integer,Document_info_len_max> map_12 = (Map.Entry<Integer,Document_info_len_max>)itr_tf.next();
       	 wr_out1.write(map_12.getValue().getMax_tf()+" ");
         wr_out1.write(map_12.getValue().getDoclen()+" ");
         wr_out1.write("\n");
         long u_time1=System.currentTimeMillis();
         timer1=u_time1-u_time;
        }
        System.out.println(" Created Version 1 !!!!!!!!!!!!!!!!!!!");
        Iterator it1 = lemma_mapper.entrySet().iterator();
        long t_3=0;
        long t_4=0;
        t_3=System.currentTimeMillis();
        while (it1.hasNext()) {
            Map.Entry<String,TreeMap<Integer,Integer>> map_12 = (Map.Entry<String,TreeMap<Integer,Integer>>)it1.next();
            hex=map_12.getValue();
            String k_str_key=map_12.getKey();
            Integer size_map_12=map_12.getValue().size();
            index1_size=index1_size+hex.size()*8+k_str_key.length()+(1)*4;
           
            output2.write(map_12.getKey()+" "+map_12.getValue().size()+" "+map_12.getValue());
            Set<Integer> keys = hex.keySet(); 
            output2.write(" ");
            for(Integer key: keys){
            output2.write(key.toString());
            output2.write(" ");
            } 
            output2.write("\n");
        }
        Iterator itr_tf2 = stem_tf_mapper.entrySet().iterator();
        while(itr_tf2.hasNext()){
       	
       	 Map.Entry<Integer,Document_info_len_max> map_12 = (Map.Entry<Integer,Document_info_len_max>)itr_tf2.next();
       	 output2.write(map_12.getValue().getMax_tf()+" ");
            output2.write(map_12.getValue().getDoclen()+" ");
            output2.write("\n");
        }
        System.out.println("Created Version 2!!!!!!!!!!!!!! ");
        t_4=System.currentTimeMillis();
        timer2=t_4-t_3;
        wr_out1.flush();output2.flush();wr_out1.close();output2.close();
    	
    	
    }

    public static void lemma_compressed_gen_files() throws FileNotFoundException,IOException{
    	TreeMap<Integer,Integer> h_stem_m1;
    	int k=0;
        StringBuffer sb_1=new StringBuffer();
        String compr_str="";
        StringBuffer sb_2=new StringBuffer();
        ArrayList<Integer> pos_1=new ArrayList<Integer>();
  	    File version_1 = new File("version1_compr");
        Writer wr_out1;
        wr_out1 = new BufferedWriter(new FileWriter(version_1));
        Iterator<Map.Entry<String,TreeMap<Integer,Integer>>> it = lemma_mapper.entrySet().iterator(); 
        StringBuffer sb_3=new StringBuffer();
       int get_gamma=0;
       while (it.hasNext()) {
       int i1=0;
       int i2=0;
       Map.Entry<String,TreeMap<Integer,Integer>> map_12 = (Map.Entry<String,TreeMap<Integer,Integer>>)it.next();
       String key=(String)map_12.getKey();
       h_stem_m1=(TreeMap<Integer,Integer>) map_12.getValue();
       String postingFile="";
       sb_2.append(helper1.gammaCode(h_stem_m1.size())+" ");
       sb_3.append("{");
       for(Map.Entry<Integer,Integer> entry : h_stem_m1.entrySet()) {
       i2=i2+1;
       sb_3.append(helper1.gammaCode(entry.getValue())+" ");
       Integer value = entry.getValue();       	 
       Integer key_v = entry.getKey();      	  
       int  u1=key_v-i1;
       i1=key_v ;
       String gc_1;
       gc_1 = helper1.gammaCode(u1);
       if(i2!=1){
			  postingFile=postingFile+" "+ gc_1; 
			  Double len=(double)gc_1.length();
   		      get_gamma=get_gamma+(int)Math.ceil(len/8);
	   }
       else{
              String bin_str1=  Integer.toBinaryString(key_v);
              Double len=(double)bin_str1.length();
              get_gamma=get_gamma+(int)Math.ceil(len/8);
              postingFile=postingFile+bin_str1; 
       }
              		                		  
              		 
       }
       sb_3.append("}");
       if(k%8==0){
       get_gamma=get_gamma+4;
       pos_1.add(compr_str.length());
       Integer length=compr_str.length();
       sb_1.append(length.toString());
       }
       k++;
       compr_str=compr_str+key.length()+key;
       sb_1.append("{");
       sb_1.append(postingFile);
       sb_1.append("}");
      }
       wr_out1.write(compr_str+"\n");
       get_gamma=get_gamma+compr_str.length();
       wr_out1.write(sb_1.toString()+"\n"+sb_2.toString()+"\n"+sb_3.toString());
       Iterator it1 = stem_tf_mapper.entrySet().iterator();
       while(it1.hasNext()){
       	 
       	 Map.Entry<Integer,Document_info_len_max> map_12 = (Map.Entry<Integer,Document_info_len_max>)it1.next();
       	 wr_out1.write(helper1.gammaCode(map_12.getValue().getMax_tf())+" "+helper1.gammaCode(map_12.getValue().length_of_document)+"\n");
          
        }
        wr_out1.flush();
        lemma_compr_size=get_gamma;
        System.out.println("compressed version 1 index (of lemmas ) is generated ");   
  	
  	
  }
    public static void stem_compr_meth() throws IOException,FileNotFoundException{
    	
    	TreeMap<Integer,Integer> tmh;
        String compr_str="";
        StringBuffer sb_1=new StringBuffer();
        ArrayList<Integer> position=new ArrayList<Integer>();
        int k=0;
        StringBuffer sb_2=new StringBuffer();
        int kv_c=0;   
		File version_1 = new File("version2_compr_file");
		StringBuffer sb_3=new StringBuffer();
		 String[] str_arr = new String[4]; 
        Writer wr_out1;
        int get_gamma=4;
        wr_out1 = new BufferedWriter(new FileWriter(version_1));
        Iterator<Map.Entry<String,TreeMap<Integer,Integer>>> it = stem_mapper.entrySet().iterator(); 
        position.add(0); 
        sb_1.append("0");        
        while (it.hasNext()) {
      	  int i1=0;
      	  int f_1=0;
          Map.Entry<String,TreeMap<Integer,Integer>> map_12 = (Map.Entry<String,TreeMap<Integer,Integer>>)it.next();
          String key=(String)map_12.getKey();
          str_arr[kv_c]=key;
          kv_c=kv_c+1;
          tmh=(TreeMap<Integer,Integer>) map_12.getValue();
          sb_2.append(helper1.deltaCode(tmh.size())+" ");
          String postingFile="";
          sb_3.append("{");
          for(Map.Entry<Integer,Integer> entry : tmh.entrySet()) {
          f_1++;
          sb_3.append(helper1.deltaCode(entry.getValue())+" ");
          Integer keyv = entry.getKey();
          Integer value = entry.getValue();
          int  up=keyv-i1;
          i1=keyv ;
          String DeltaCode;
          DeltaCode = helper1.deltaCode(up);
          if(f_1==1){
              			String bin=  Integer.toBinaryString(keyv);
              			Double len=(double)bin.length();
           			    get_gamma=get_gamma+(int)Math.ceil(len/8);
              		    postingFile=postingFile+bin; 
              		}
          else{
              		    Double len=(double)DeltaCode.length();
		                get_gamma=get_gamma+(int)Math.ceil(len/8);
		                postingFile=postingFile+" "+ DeltaCode; 
              		                		
		       }                   		 
            }
            sb_3.append("}");
            sb_1.append("{");sb_1.append(postingFile);sb_1.append("}");
            if(kv_c%4==0){
           	get_gamma=get_gamma+4;
            compr_str=compr_str+helper1.frontCoding_meth(str_arr);
            Integer length=compr_str.length();
            position.add(length);
            sb_1.append(length.toString());
            kv_c=0;
            }                                                      
        }
   
       wr_out1.write(compr_str);wr_out1.write("\n");
       get_gamma=get_gamma+compr_str.length();
        wr_out1.write(sb_1.toString()+"\n"+sb_2.toString()+"\n"+sb_3.toString()+"\n");
        Iterator it2 = stem_tf_mapper.entrySet().iterator();
        while(it2.hasNext()){
       	 Map.Entry<Integer,Document_info_len_max> map_12 = (Map.Entry<Integer,Document_info_len_max>)it2.next();
  
       	 wr_out1.write(helper1.deltaCode(map_12.getValue().getMax_tf())+" "+helper1.deltaCode(map_12.getValue().length_of_document)+"\n");
        }
        wr_out1.flush();
        stem_compr_size=get_gamma;
        System.out.println("compressed version 2 of stems created !!!!!!!!!. ");  
 	
 	
 	
}
    public static void get_lemmas_of_docs(String str_txt,int doc_id_cnter,Driver d){
     List<String> l=d.get_lemmas_from_doc(str_txt);
     doc_id_cnter++;
     int lemma_no_tokens=0;
     Integer tf=1;
   	 Iterator<String> it=l.iterator();
     
     while(it.hasNext()){
    	 lemma_no_tokens++;
    	 String key=(String)it.next();
    	 if(!stop_lst.contains(key)){
    	 if(lemma_mapper.containsKey(key)){
    		 	TreeMap<Integer,Integer> h_stem_m1=lemma_mapper.get(key); 
    		 	if(h_stem_m1.containsKey(doc_id_cnter)){
    			h_stem_m1.put(doc_id_cnter, (h_stem_m1.get(doc_id_cnter)+1));
    			lemma_mapper.put(key,h_stem_m1);
    			lemma_tf_mapper.put(key,lemma_tf_mapper.get(key)+tf );
    		 	}
    		 	else{
    		 		h_stem_m1.put(doc_id_cnter, 1);
    		 		lemma_mapper.put(key, h_stem_m1);
    		 		lemma_tf_mapper.put(key,lemma_tf_mapper.get(key)+tf );
    		 	}
			 
    	 } 
    	 else{
    		 TreeMap<Integer,Integer> h_stem_m1=new TreeMap<Integer,Integer>();
    		 h_stem_m1.put(doc_id_cnter, 1);
    		 lemma_mapper.put(key, h_stem_m1);
    		 lemma_tf_mapper.put(key,tf );
    	 }
     }
     }
     
    }
    public static void get_stems_of_docs(StringBuffer sb_1,int doc_id_cnter,Stemmer stem){
    	StringTokenizer itr = new StringTokenizer(sb_1.toString()); 
    	String token;
		int stem_no_tokens=0;
    	doc_id_cnter++;
    	 Map<String,Integer> tfms_1=new TreeMap<String,Integer>();
			
			
			while (itr.hasMoreTokens()) {
				stem_no_tokens++;
				token = itr.nextToken();
				if(!stop_lst.contains(token)){
				char[] ch_arr = token.toCharArray();
				stem.add(ch_arr,ch_arr.length);
			    stem.stem();
			    String st=stem.toString();
			    Integer tf=1;
		    	 if(stem_mapper.containsKey(st)){
		    		 TreeMap<Integer,Integer> h_stem_m1=stem_mapper.get(st); 
		    		 	if(h_stem_m1.containsKey(doc_id_cnter)){
		    		 			h_stem_m1.put(doc_id_cnter, (h_stem_m1.get(doc_id_cnter)+1));
		    		 			tfms_1.put(st,tfms_1.get(st)+tf );
		    		 	}
		    		 	else{
			    		 h_stem_m1.put(doc_id_cnter, 1);
			    		 tfms_1.put(st,tf );
		    		 	}
		    			stem_mapper.put(st,h_stem_m1);
		    			
		    	 } 
		    	 else{
		    		 TreeMap<Integer,Integer> h_stem_m1=new TreeMap<Integer,Integer>();
		    		 h_stem_m1.put(doc_id_cnter, 1);
		    		 stem_mapper.put(st, h_stem_m1);
		    		 tfms_1.put(st,tf );
		    	 }
			    }
			}
			ValueComp cmp1 = new ValueComp(tfms_1);
			TreeMap<String, Integer> tm_sorted = new TreeMap<String, Integer>(cmp1);
			tm_sorted.putAll(tfms_1);
			 Entry<String,Integer> entry_tm =tm_sorted.firstEntry();
			Document_info_len_max doc_details=new Document_info_len_max();
			 doc_details.setMax_tf(entry_tm.getValue());
			 doc_details.setMostFrequentStem(entry_tm.getKey());
			 doc_details.setDocumentId(doc_id_cnter);
			 doc_details.setDoclen(stem_no_tokens);
			 stem_tf_mapper.put(doc_id_cnter,doc_details);
    }

    public static void main(String[] args) throws FileNotFoundException,IOException {
        
        String str_txt ;
        Driver d = new Driver();
    	String s_line;	
    	int cnt1=0;
    	int cnt2=0;
    	int ct1_version1_compr=0;
    	Integer compr_string =null;
	
    	File fld_files = new File("resources/cranfield");
    	helper1.stopWords(stop_lst);
        Stemmer stem = new Stemmer();		
		File[] file_dir = fld_files.listFiles();
		cnt1++;
		BufferedReader br;
		int n_files=file_dir.length;
		  
	    for (int i = 0; i < file_dir.length; i++) {
		br = new BufferedReader(new FileReader(file_dir[i]));
		StringBuffer sb_1 = new StringBuffer();
		while ((s_line = br.readLine()) != null) {
			sb_1.append(helper1.replaceCharacters(s_line));
			sb_1.append(System.getProperty("line.separator"));
		}
		 str_txt=sb_1.toString();
		 get_stems_of_docs(sb_1,i,stem);
		 get_lemmas_of_docs(str_txt,i,d);
		}
	  
	  	uncmpr_files_gen_meth();
	  	long time_1=System.currentTimeMillis();
	  	lemma_compressed_gen_files();
	  	long time_2=System.currentTimeMillis();
		stem_compr_meth();
		long time_3=System.currentTimeMillis();
		  

		   System.out.println("The time taken to build index version 1 of uncompressed form is "+ timer1+" millisecs");
		   System.out.println("The time taken to build index version 2 uncompressed form is "+ timer2+" millisecs");
		   System.out.println("The time taken to build index version 1 compressed using block and gamma codings is "+ (time_2-time_1)+" millisecs");
		   System.out.println("The time taken to build index version 2 compressed using front and delta codingsis "+ (time_3-time_2)+" millisecs");
		   System.out.println("The size in bytes of the index Version 1 uncompressed "+index1_size+" bytes");
		   System.out.println("The size in bytes of the index Version 2 uncompressed  "+index2_size+" bytes");
	       System.out.println("The size in bytes of the index Version 1 compressed  "+lemma_compr_size+" bytes");
	       System.out.println("The size in bytes of the index Version 2 compressed  "+stem_compr_size+" bytes");
	       System.out.println("compression has saved :"+(index1_size-lemma_compr_size)+" bytes on version 1 index (using lemmas)");
	       System.out.println("compression has saved :"+(index2_size-stem_compr_size)+" bytes on version 2 index(using stems)");   
	       System.out.println("number of Inverted lists in index Version 1 is "+lemma_mapper.size());
		   System.out.println("number of Inverted lists in index Version 2 is "+stem_mapper.size());
		   String tokens[]={"reynold", "nasa", "prandtl", "flow", "pressur", "boundari", "shock" };
		   System.out.println("--------------------------------------------------------------------------------------------");
		   System.out.println("Statistics for Reynolds, NASA, Prandtl, flow, pressure, boundary, shock (using STEMS)");
		   for(int i=0;i<tokens.length;i++){
		   TreeMap<Integer,Integer> tm_1=  stem_mapper.get(tokens[i]);
			  String str = Integer.toBinaryString(tm_1.size());
			  byte[] tf = str.getBytes();
	          Iterator it = tm_1.entrySet().iterator();
	          int tot_s=0;
	          while (it.hasNext()) {
	              Map.Entry<Integer,Integer> map_12 = (Map.Entry<Integer,Integer>)it.next();
	              tot_s=tot_s+map_12.getValue();
	          }
		      System.out.println("  Token:"+tokens[i]);
	    	  System.out.println("  Document Freqency:"+tm_1.size());
	    	  System.out.println("  Term Frequency:"+tot_s );
	    	  System.out.println("  inverted list length  :"+tf.length+"bytes");
	       }
		   System.out.println("_______________________________________________________________________________________________________");
		   System.out.println("Stats for Reynolds, NASA, Prandtl, flow, pressure, boundary, shock using LEMMA");
		   String tokens1[]={"reynold", "nasa", "prandtl", "flow", "pressure", "boundary", "shock" };
		   for(int i=0;i<tokens1.length;i++){
		   TreeMap<Integer,Integer> tm_1=  lemma_mapper.get(tokens1[i]);
			  String str = Integer.toBinaryString(tm_1.size());
	          byte[] tf = str.getBytes();
	          Iterator it = tm_1.entrySet().iterator();
	          int tot_s=0;
	          while (it.hasNext()) {
	              Map.Entry<Integer,Integer> map_12 = (Map.Entry<Integer,Integer>)it.next();tot_s=tot_s+map_12.getValue();
	              
	          }
		      System.out.println("  Token:"+tokens1[i]);
	    	  System.out.println("  Document Frequency:"+tm_1.size());
	    	  System.out.println("  Term Frequency:"+tot_s );
	    	  System.out.println("  inverted list length :"+tf.length+"bytes");
	    	  }
		   System.out.println("--------------------------------------------------------------------------------------------------------------");
		   System.out.println("Statistics for NASA and its first three entries in posting list-------------------------------");
		   TreeMap<Integer,Integer> tm_2=  lemma_mapper.get("nasa");
		   int val_1=1,val_2=1;
		   int dr[]=new int[4];
		   Iterator it1 = tm_2.entrySet().iterator();
		   while(it1.hasNext()&&val_2<4){
			   Map.Entry<Integer,Integer> map_23 = (Map.Entry<Integer,Integer>)it1.next();
			   dr[val_2]=map_23.getValue();
			   val_2++;
		   }
		   System.out.println("document frequency for nasa is "+ tm_2.size());
		   
		   Iterator it2 = tm_2.entrySet().iterator();
		   while(val_1<4 && it2.hasNext()){
			   Map.Entry<Integer,Integer> map_34 = (Map.Entry<Integer,Integer>)it2.next();
			   Document_info_len_max a=stem_tf_mapper.get(map_34.getKey());
			   System.out.print("The term frequency in the document for entry  "+ val_1+"  is "+dr[val_1] );
			   System.out.print(" document length for entry "+ val_1+ "  is "+a.getDoclen());
			   System.out.print(" maximum term frequency for entry "+ val_1+ " is "+a.getMax_tf());
			   val_1++;
			   System.out.println();
		   }
		   TreeMap<String,Integer> freq_doc=new TreeMap<String,Integer>();
		   Iterator it3=lemma_mapper.entrySet().iterator();
		   while(it3.hasNext()){
			  Map.Entry<String,TreeMap<Integer,Integer>> out_res= (Map.Entry<String,TreeMap<Integer,Integer>>)it3.next();
			  freq_doc.put(out_res.getKey(),out_res.getValue().size());
		   }		   
		   Map.Entry<String, Integer> max_mapper = null;
		   TreeMap<String,Integer> min_mapper=new TreeMap<String,Integer>();
		   for (Map.Entry<String, Integer> entry : freq_doc.entrySet())
		   {
		       if (max_mapper == null || entry.getValue().compareTo(max_mapper.getValue()) > 0)
		       {
		           max_mapper = entry;
		       }
		   }
		   Iterator itr_12=freq_doc.entrySet().iterator();
		   while(itr_12.hasNext()){
			   Map.Entry<String, Integer> out_res=(Map.Entry<String, Integer>)itr_12.next();
			   if(out_res.getValue()==1){
				   min_mapper.put(out_res.getKey(), out_res.getValue());
			   }
		   }
		   System.out.println("-------------------------------------------------------------------------------------------------------");
		   System.out.println("maximum df in the index1 collection is "+ max_mapper);
		   System.out.println("minimum df in the index1 collection is "+ min_mapper );
		   TreeMap<String,Integer> freq_doc2=new TreeMap<String,Integer>();   
		   Iterator it4=stem_mapper.entrySet().iterator();
		   while(it4.hasNext()){
			  Map.Entry<String,TreeMap<Integer,Integer>> out_res0= (Map.Entry<String,TreeMap<Integer,Integer>>)it4.next();
			  freq_doc2.put(out_res0.getKey(),out_res0.getValue().size());
		   }
		   Map.Entry<String, Integer> max_mapper2 = null;
		   TreeMap<String,Integer> min_mapper2=new TreeMap<String,Integer>();
		   for (Map.Entry<String, Integer> entry : freq_doc2.entrySet())
		   {
		       if (max_mapper2 == null || entry.getValue().compareTo(max_mapper2.getValue()) > 0)
		       {
		           max_mapper2 = entry;
		       }
		   }
		   Iterator itr_a=freq_doc2.entrySet().iterator();
		   while(itr_a.hasNext()){
			   Map.Entry<String, Integer> out_res=(Map.Entry<String, Integer>)itr_a.next();
			   if(out_res.getValue()==1){
				   min_mapper2.put(out_res.getKey(), out_res.getValue());
			   }
		   }
		   System.out.println("-----------------------------------------------------------------------------------------");
		   System.out.println("maximum df in the index2 collection is "+ max_mapper2);
		   System.out.println("minimum df in the index2 collection is "+ min_mapper2 );
		  
		  Iterator s_max_tf=stem_tf_mapper.entrySet().iterator();
		  Integer max_val_s=0;
		  String val_str_12=null;
		  Integer get_id=0;
		  Integer doclen_max=0;
		  Integer document_id=0;
		  while(s_max_tf.hasNext()){
			  Map.Entry<Integer, Document_info_len_max> map_12=(Map.Entry<Integer, Document_info_len_max>)s_max_tf.next();
			  if(map_12.getValue().getMax_tf()>max_val_s){
				  max_val_s=map_12.getValue().getMax_tf();
				  val_str_12=map_12.getValue().getMostFrequentStem();
				  get_id=map_12.getKey();
			  }
			  if(map_12.getValue().getDoclen()>doclen_max){
				  doclen_max=map_12.getValue().getDoclen();
				  document_id=map_12.getKey();
			  }
		  }
		  System.out.println("--------------------------------------------------------------------");
		  System.out.println("The document containing max term freq is "+ get_id+"   max freq term is "+ val_str_12+"  and max tf value is  "+ max_val_s);
		  System.out.println("--------------------------------------------------------------------");
		  System.out.println("The document having max doc length is "+ document_id+"  and its doc length is "+doclen_max);
		  
    
    	
    }   
}