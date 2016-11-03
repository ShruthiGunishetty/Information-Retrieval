import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;

public class helper1{
	public  static void stopWords(ArrayList<String> sl) throws FileNotFoundException,IOException{
    BufferedReader br = new BufferedReader(new FileReader("resources/stopwords"));
    String str_line;
	while ((str_line = br.readLine()) != null) {
			sl.add(str_line);
	}
    }
	public static String deltaCode(int nval)
	{
	    if(nval!=1)
	    {
	    String binary = Integer.toBinaryString(nval);
	    int binarylength = binary.length();
	    String leadingoff= binary.substring(1);
	    String gamma =gammaCode(binarylength);
	    String deltaCode = gamma+ leadingoff;
	    return deltaCode;                
	    }
	    else 
	    	return "0";
	}
    public static String replaceCharacters(String sl){
    	
    	return(sl.toString().replaceAll("\\<.*?>","").replaceAll("'s", "").replaceAll("[+^:,?';=%#&~`$!@*_)/(}{]","").replaceAll("-","\t").replaceAll("\\.", "").replaceAll("[0-9]+", "")); 
    }
    public static String frontCoding_meth(String arr[]){
	
	Integer b1_front=0;
	Boolean bool_match;
	int min=0;
	String t_1=arr[0];
	int tm_1_len=t_1.length();
	String tm_2=arr[1];
	int tm_2_len=tm_2.length();
	String tm_3=arr[2];
	int tm_3_len=tm_3.length();
	String tm_4=arr[3];
	int tm_4_len=tm_4.length();
	bool_match=false;
	min=Math.min( Math.min(tm_1_len, tm_2_len) , Math.min(tm_3_len, tm_4_len) );
	for(int i=0;i<min;i++){
		
		if((t_1.charAt(i)==tm_2.charAt(i) && tm_3.charAt(i)==tm_4.charAt(i) && tm_2.charAt(i)==tm_3.charAt(i))){
			b1_front=i;
			bool_match=true;
		}
		else
			 break;	
		
	}
	Integer start_length_1=b1_front+1;
	if(bool_match&&start_length_1>1){
		String terms_1;
		String terms_2;
		String terms_3;
		String terms_4;
		if(start_length_1<tm_1_len)
		  terms_1=t_1.substring(start_length_1,tm_1_len );
		else
			terms_1=""; 
		if(start_length_1<tm_2_len)
			  terms_2=tm_2.substring(start_length_1,tm_2_len );
		else
			terms_2=""; 
		if(start_length_1<tm_3_len)
			  terms_3=tm_3.substring(start_length_1,tm_3_len );
		else
			terms_3=""; 
		if(start_length_1<tm_4_len)
			  terms_4=tm_4.substring(start_length_1,tm_4_len );
		else
			terms_4=""; 
	String tk=t_1.substring(0, start_length_1);
	String res;
	res=start_length_1.toString();
	res=res+tk+"*";
	res=res+terms_1+terms_2.length()+"<>"+terms_2+terms_3.length()+"<>"+terms_3+terms_4.length()+"<>"+terms_4;
	return res;
	}
	else{
		String str_tk=tm_1_len+t_1+tm_2_len+tm_2+tm_3_len+tm_3+tm_4_len+tm_4;
		return str_tk;
	}
	
}


public static String gammaCode(int nval)
{
    if(nval!=1)
    {
        String binary = Integer.toBinaryString(nval);
        String leadingoff = binary.substring(1); 
        int binarylength = leadingoff.length();
        String t = "";
        for(int i = 0; i<binarylength ; i++) 
        {
            t=t+"1";
        }
        t+="0";
        String gammaCode = t+leadingoff;
        return gammaCode;
    }
    else
    	return "0";
}

}