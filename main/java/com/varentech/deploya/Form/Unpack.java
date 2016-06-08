package com.varentech.deploya.Form;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Unpack {

  public void unpack(File archive) throws IOException{
    Resource res = new Resource();

    //if we are going to unpack
    if(res.entry.getUnpackArguments()!=null){
      //unpack file here

      //Read TAR file into TarArchiveInputStream
      TarArchiveInputStream myTarFile = new TarArchiveInputStream(new FileInputStream(archive));

      //To read each individual TAR file.
      TarArchiveEntry entry = null;
      String individualFiles;
      int offset;
      FileOutputStream outputFile = null;
      //Create a loop to read every single entry in TAR file.
      while((entry = myTarFile.getNextTarEntry()) != null){
        //Get the name of the file.
        individualFiles = entry.getName();
        //Get the size of the file and create a byte array for the size.
        byte[] content = new byte[(int) entry.getSize()];
        offset = 0;
        //Some SOP statements to check progress.
        System.out.println("File Name in TAR File is: " + individualFiles);
        System.out.println("Sive of the file is: " + entry.getSize());
        System.out.println("Byte Array length: " + content.length);

        //Read file from the archive into a byte array
        myTarFile.read(content,offset,content.length - offset);
        //Define OutputStream for writing the file.
        outputFile = new FileOutputStream(new File(individualFiles));
        //Use IOUtils to write content of byte array to physical file.
        IOUtils.copy(myTarFile, outputFile);

        //Close Output Stream
        outputFile.close();
      }
      //close TarArchiveInputStream
      myTarFile.close();
    }
  }
}
