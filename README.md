# VARENTECH-DEPLOYA
### SIMPLE OVERVIEW OF THE APP

  The user will upload a file along with execute instructions, a desired location, and options to unpack and archive as well. The app will first store the file in the desired location and archive if necessary. Then it will unpack, if selected, and execute the file, which will produce standard output and/or standard error. A hash of the file, and all files contained in an archive, will be computed. All data will be stored in a database.

###LIST OF DEPENDENCIES USERS MUST HAVE INSTALLED TO USE THE APP

  - *Java 1.8*

###STEPS TO ACTUALLY RUN THE APP

  Execute the jar using the commanad: java -jar deploya.jar. Then go to the login page (currently: localhost:8080/ProjectThunder/pages/login.jsp) in the web browser, where the user will input a user name. Next, they will be redirected to a page where they can upload their file and necessary information. Once submitted, the standard output and/or standard error will be displayed on the screen. They will have the option to view the history page (accessible from the form upload page, the output page, and/or by putting in the url). The history page includes the information stored in the database. They can compare two runs, either by files or output. 
  
  
  *The user can override log level using logback and slf4j.*

