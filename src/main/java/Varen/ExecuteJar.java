package Varen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ExecuteJar {

    void exJar() {

        try {

            // print a message
            System.out.println("Executing HelloWorldJar.jar");

            // create a process and execute notepad.exe
            //Process process = Runtime.getRuntime().exec("notepad.exe");
            Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            // print another message
            System.out.println("It should now open.");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
