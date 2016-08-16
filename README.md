# VARENTECH-DEPLOYA
### SIMPLE OVERVIEW OF THE APP

  The user will upload a file along with execute instructions, a desired location, and options to unpack and archive as well. The app will first store the file in the desired location and archive if necessary. Then it will unpack, if selected, and execute the file, which will produce standard output and/or standard error. A hash of the file, and all files contained in an archive, will be computed. All data will be stored in a database.

###LIST OF DEPENDENCIES USERS MUST HAVE INSTALLED TO USE THE APP

  - *Java 1.8*

###STARTING THE APP

####As Uber Jar

  Execute the jar using the commanad: java -jar deploya.jar. You can export the default configuration file using the command java -jar path/to/deploya.jar --export-config {path/to/export}/application.conf.
  
   *The user can override log level using logback and slf4j by creating a logback-test.xml and running: java -Dlogback.configurationFile=path/to/logback-test.xml -jar /path/to/deploya.jar.*
   
####As RPM

  First install the rpm using the command: rpm -i path/to/deploya-1.0-1.el7.centos.noarch.rpm. Next, go to the bin and run the command: ./script.
  
###USING THE APP
  
  After starting the application, go to the login page (currently: localhost:8080/Deploya/login.jsp) in the web browser, where the user will input a user name. Next, they will be redirected to a page where they can upload their file and necessary information. Once submitted, the standard output and/or standard error will be displayed on the screen. They will have the option to view the history page (accessible from the form upload page, the output page, and/or by putting in the url). The history page includes the information stored in the database. The database will be created if one does not already exist at the path given in the configuration file.
  
  They can compare two runs, either by files or output. When comparing by files, added files are shown in green, removed in red, changed in purple, and unchanged in grey. When comparing by output, yellow shows a change in output and red shows a change in error.
  
  The default configuration file can be overriden. To update this file, create a file named application.conf and add updated parameters. The application.conf file must be in your working directory when you run the app. 
  Below is the default configuration file.It is not necessary to include all variables, only ones that are being updated. 

	
			port_number=8080 
			default_directory=~/Deploya/archive
			tab_name_form=Form
			tab_name_history=History
			tab_name_login=Login
			page_title= Deploya
			context_path=/Deploya
			#execute timeout in minutes
			#if no timeout put -1
			execute_timeout=10
			path_to_database=~/Deploya/Deploya.db

    

