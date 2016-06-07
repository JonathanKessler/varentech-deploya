package com.varentech.deploya.Form;

import java.io.*;

import com.varentech.deploya.entities.EntriesDetail;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;



public class ExecuteJar {

    public void exJar() {

        try {

            // print a message
            System.out.println("Executing HelloWorldJar.jar");


            //execute first jar
            Resource res = new Resource();
            String output = null;
            Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                output= output + line;
            }

            //save output
            res.entriesDetail.setOutput(output);

            //print hash of tar
            //Hash hash = new Hash();
           //System.out.println(hash.checkSum("C:\\Users\\kesslerk\\Documents\\test.tar"));

            File file = new File(res.entry.getFileName());
            System.out.println("tar file hash code");
            System.out.print(file.hashCode());



            // print another message
            System.out.println("It should now open.");





        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void findAllFileNames(){
        //print all file names in tar
        TarArchiveInputStream tarInput = null;
        try {
            EntriesDetail subentry = new EntriesDetail();
            tarInput = new TarArchiveInputStream(new FileInputStream("C:\\Users\\kesslerk\\Documents\\test.tar"));
            TarArchiveEntry tarentry;
            while (null!=(tarentry=tarInput.getNextTarEntry())) {
                System.out.println(tarentry.getName());
                subentry.setFileName(tarentry.getName());
                System.out.println("jar files hash");
                System.out.println(tarentry.hashCode());

                //
                //find all hash codes
                //

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
