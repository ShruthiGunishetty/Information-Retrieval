The folder contains the following files:
  Driver.java
  helper1.java
  Stemmer.java
  Document_info_length_max.java
  Decsription_statistics.txt
  resources folder-- location for cranfield documents, stopwords list
  
  The following jar files are used:
                     Stanford-corenlp-3.4.1.jar
                     stanford-corenlp-3.4-models.jar 
Make sure these jar files are referenced by the program.
put the cranfield documents inside 'resources' folder inside 'cranfield' folder
put the stopwords file inside 'resources' folder and run the following
To compile the program
   set classpath=C:\Users\shru\Desktop\jars\stanford-corenlp-3.4.1.jar; C:\Users\shru\Desktop\jars\stanford-corenlp-3.4-models.jar;
   javac Driver.java helper1.java Document_info_length_max.java
To run the program
   java Driver

In the program, the stopwords location is given as: 
	  resources/stopwords
In the program,the Cranfield documents location is given as:
      resources/cranfield
   
	   

	   
