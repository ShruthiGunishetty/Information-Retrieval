import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
public class Tokenization {
public static void main (String args[]) throws IOException{
	//directory where the cranfield connection exists
	//File dir=new File("C:\\Users\\Shru\\Desktop\\UTD\\sem3\\IR\\assignment1\\Cranfield");
	long startTime = System.currentTimeMillis();
	 File dir=new File(args[0]);
	//file array where each element in the array points to a file in the cranfield collection
	File[] files=dir.listFiles();
	int nooffiles=files.length;
	//using a hashmap to store the tokens from the files
	Map<String,Integer> tokens=new HashMap<>();
	
	for(File f:files)
		findTokens(tokens,f);
	
	int total=totalTokens(tokens);
	int tokensize=tokens.size();
	System.out.println("Number of Tokens in the Cranfield Text Collection : "+total);
	System.out.println("Number of unique tokens in the Cranfield Text Collection : "+tokensize);
	System.out.println("Number of Tokens that occur only once in the Cranfield Collection : "+onceTokens(tokens));
	topFrequent(tokens,30);
	System.out.println("The average number of word tokens per document : "+(total/nooffiles));
	Map<String,Integer> stemmedTokens=stemming(tokens);
	int total_stemmed=totalTokens(stemmedTokens);
	int tokensize_stemmed=stemmedTokens.size();
	System.out.println("Number of Stems in the Cranfield Text Collection : "+total_stemmed);
	System.out.println("Number of unique stems in the Cranfield Text Collection : "+tokensize_stemmed);
	System.out.println("Number of stems that occur only once in the Cranfield Collection : "+onceTokens(stemmedTokens));
	topFrequent(stemmedTokens,30);
	System.out.println("The average number of stems per document : "+(total_stemmed/nooffiles));
	long endTime   = System.currentTimeMillis();
	long totalTime = endTime - startTime;
	System.out.println("total time taken:"+totalTime+" millisec");
	
	
}
//findTokens takes a file and preprocesses the tokens and adds them to the hashmap
public static void findTokens(Map<String,Integer> words,File f) throws IOException{
	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
	String line;
	//preprocessing of the tokens is done here. It replaces all the tags, special characters, digits with empty spaces using replaceAll method
	while((line=br.readLine())!=null){
		line=line.toLowerCase();
    	line=line.replaceAll("\\<.*>","");
    	line=line.replaceAll("[0-9].*[0-9]*", "");
    	line=line.replaceAll("[^a-zA-Z0-9\\s]","");
    	if(line.trim().isEmpty())
    	   continue;
    	String str[]=line.split(" ");
    	for(String s:str){
    		if(s.isEmpty())
    			continue;
    		if(words.containsKey(s)){
    			words.put(s,words.get(s)+1);
    		}
    		else{
    			words.put(s,1);
    		}
    	}
    	
		
	}
	
}
//totalTokens returns the total number of tokens in the cranfield collection
public static int totalTokens(Map<String,Integer> words){
	int count=0;
	for(String s:words.keySet()){
		count=count+words.get(s);
	}
	return count;
	
}
//This method returns the count of tokens that has only 1 as the value in the hashmap
public static int onceTokens(Map<String,Integer> words){
	int count=0;
	for(String s:words.keySet()){
		if(words.get(s)==1)
			count++;
	}
	return count;
}
//This uses the comparator method to sort the hashmap based on the values of the hashmap rather than keys and displays the top 30 of them
public static void topFrequent(Map<String,Integer> words,int top){
	int count=0;
	List<Map.Entry<String, Integer>> ls=new LinkedList<Map.Entry<String, Integer>>(words.entrySet());
	Collections.sort(ls,new Comparator<Map.Entry<String, Integer>> (){
		public int compare(Map.Entry<String, Integer> m1,Map.Entry<String, Integer> m2){
			return(m2.getValue()).compareTo(m1.getValue());
		}
	});
	System.out.println("The "+top+" most frequent word tokens in the Cranfield Collection: ");
	for(Map.Entry<String, Integer> mapresult:ls){
		
		System.out.println(mapresult.getKey()+" "+ mapresult.getValue());
		count++;
		if(count==top)
			break;
	}
	
}
//Stemming method returns the stems on the tokens and uses the Stemmer class which implements the porter stemming algorithm
public static HashMap<String, Integer> stemming(Map<String, Integer> words) {
	HashMap<String, Integer> Stemming = new HashMap<>();
	Stemmer s = new Stemmer();
	for (String str : words.keySet()) {
		for (int i = 0; i < str.length(); i++) {
			s.add(str.charAt(i));
		}
		s.stem();
		String u = s.toString();
		if (!u.equals(str)) {
			if (Stemming.containsKey(u)) {
				Stemming.put(u, 1 + Stemming.get(u));
			} else {
				Stemming.put(u, 1);
			}
		}

	}
	return Stemming;
}

}

/*Sample output:
 * Number of Tokens in the Cranfield Text Collection : 216672
Number of unique tokens in the Cranfield Text Collection : 10040
Number of Tokens that occur only once in the Cranfield Collection : 4530
The 30 most frequent word tokens in the Cranfield Collection: 
the 18813
of 12344
and 6174
a 5730
in 4472
to 4142
is 4008
for 3341
are 2369
with 2187
on 1887
flow 1713
at 1708
by 1692
that 1516
an 1355
be 1249
pressure 1100
as 1075
from 1074
this 1051
which 946
number 922
boundary 889
results 860
it 818
mach 768
theory 762
layer 723
method 673
The average number of word tokens per document : 154
Number of Stems in the Cranfield Text Collection : 6070
Number of unique stems in the Cranfield Text Collection : 4112
Number of stems that occur only once in the Cranfield Collection : 3028
The 30 most frequent word tokens in the Cranfield Collection: 
gener 15
observ 11
oper 10
deriv 9
determin 9
integr 9
investig 8
simul 8
separ 8
continu 8
indic 8
comput 8
predict 7
approxim 7
acceler 7
illustr 7
diffus 7
stabil 7
compar 7
correl 7
measur 6
express 6
develop 6
examin 6
revers 6
design 6
contribut 6
engin 6
depend 6
estim 6
The average number of stems per document : 4
total time taken:751 millisec
   */
