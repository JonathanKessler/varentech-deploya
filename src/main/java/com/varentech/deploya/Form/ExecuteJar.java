package com.varentech.deploya.Form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.FileInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;



public class ExecuteJar {

    public void exJar() {

        try {

            // print a message
            System.out.println("Executing HelloWorldJar.jar");


            //execute first jar
            Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            //execute second jar
            p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\AnotherJar\\out\\artifacts\\AnotherJar_jar\\AnotherJar.jar");
            in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            //print hash of tar
            Hash hash = new Hash();
            System.out.println(hash.checkSum("C:\\Users\\kesslerk\\Documents\\test.tar"));


            //print all file names in tar
            TarArchiveInputStream tarInput = new TarArchiveInputStream(new FileInputStream("C:\\Users\\kesslerk\\Documents\\test.tar"));

            TarArchiveEntry entry;
            while (null!=(entry=tarInput.getNextTarEntry())) {
                System.out.println(entry.getName());
                //System.out.println(entry.hashCode());

            }



            // print another message
            System.out.println("It should now open.");





        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
