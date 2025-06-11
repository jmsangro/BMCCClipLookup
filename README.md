# BMCCClipLookup
App to lookup up and play video clips by location, person, or theme

 The app is developed in Java, using the JavaFX UI framework for stand alone apps. 
 In order to run on Ubuntu Linux at the time of developement, it was developed to run on open jdk 21.
 
 "Database"
 No actual database is used. Data is read from spreadsheet files in .csv format. 
 1 file is used. It must be referred to by -DdataFile="location/name_of_db_file.csv" on the java command line. 
 See Test_data.csv for proper column content and order. The database file contains entries for each clip available with fields for the name of the interviewee, theme of the clip, its title, etc.


Video clips need to be on the file system and references to them correct in the database file (see above).
Thumbnail photos of the interviewees need to be on the file system and references to them need to be correct in the database file.

 
 Dependencies
 Java JDK jdk-21.0.2
 JavaFX SDK - javafx-sdk-21.0.7
 opencsv-5.8.jar
 commons-lang3-3.17.0.jar
 
 Basic Installation/Deployment
 Download and install the java jdk for your operating system. https://jdk.java.net/archive/
 Download and install the javafx sdk for your operating system. https://gluonhq.com/products/javafx/
 Download jar files of libraries in the dependencies list.
  https://mvnrepository.com/artifact/com.opencsv/opencsv/5.8
  https://commons.apache.org/proper/commons-lang/download_lang.cgi
 Compile source .java files into .class files
 Edit example command line script file (.bat or .sh) to correct locations for the dependencies.
 Run the script file.
