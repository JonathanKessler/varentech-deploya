package com.varentech.deploya.Form;

import java.io.*;

import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import com.varentech.deploya.entities.EntriesDetail;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;



public class ExecuteJar {

    public void exJar() {

        try {

            //execute jar
            Resource res = new Resource();
            String output = "";
            //Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar");
            Process p = Runtime.getRuntime().exec(res.entry.getExecuteArguments());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                output= output + "\n" + line;
            }

            //save output
            res.entriesDetail.setOutput(output);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void findAllFileNames(){

        Resource res = new Resource();
        Hash hash = new Hash();

        //to see if file is a tar
        int dot = res.entry.getFileName().lastIndexOf('.');
        String extension = res.entry.getFileName().substring(dot);

        if(extension.equals(".tar")) {
            //print all file names in tar
            TarArchiveInputStream tarInput = null;
            try {
                EntriesDetail subentry = new EntriesDetail();
                //File file = new File(res.entry.getFileName());
                tarInput = new TarArchiveInputStream(new FileInputStream("C:\\Users\\kesslerk\\Documents\\test.tar"));

                //tarInput = new TarArchiveInputStream(new FileInputStream(file));

                TarArchiveEntry tarentry;
                while (null != (tarentry = tarInput.getNextTarEntry())) {
                    //set file name
                    System.out.println(tarentry.getName());
                    subentry.setFileName(tarentry.getName());

                    //set hash
                    System.out.println("jar files hash");
                    //hash.checkSum(file.getAbsolutePath()+"\\"+tarentry.getName())

                    System.out.println(hash.createSha1(tarentry.getFile()));


                    //set output
                    subentry.setOutput(res.entriesDetail.getOutput());

                    //add subentry to database
                    EntriesDetailsDoaImpl impl = new EntriesDetailsDoaImpl();
                    impl.insertIntoEntriesDetail(subentry);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
