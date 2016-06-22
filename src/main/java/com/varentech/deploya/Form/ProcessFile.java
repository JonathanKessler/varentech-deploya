package com.varentech.deploya.Form;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import com.varentech.deploya.entities.EntriesDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;


/**
 * This class manages the given Linux shell commands and prints the stdOutput and stdErr of both.
 *
 * @author Varen Technologies
 * @see java.lang.Process
 */

public class ProcessFile {

    private Logger logger = LoggerFactory.getLogger(ProcessFile.class);

    /**
     * Execute a some sort of program with the given execute command and arguments.
     * Also prints the standard output and standard error if such are produced from this given
     * program.
     *
     * @throws Exception if an invalid execute command is given.
     */

    public void executeArguments() {


        ExecutorService service = Executors.newSingleThreadExecutor();

        try {
            Runnable r = new Runnable() {
                public void run() {

                    try {

                        Resource res = new Resource();
                        String output = "";

                        Process p = Runtime.getRuntime().exec(res.entry.getExecuteArguments());
                        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = null;

                        while ((line = in.readLine()) != null) {
                            output = output + line;
                        }

                        String stdErr = "";
                        BufferedReader stdErrReader = new BufferedReader(
                                new InputStreamReader(p.getErrorStream()));

                        while ((line = stdErrReader.readLine()) != null) {
                            stdErr = stdErr + line;
                        }

                        //save output and error
                        res.entriesDetail.setOutput(output);
                        res.entriesDetail.setError(stdErr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };

            Future<?> f = service.submit(r);

            f.get(1, TimeUnit.MINUTES);     // attempt the task for one minute
        } catch (final InterruptedException e) {
            // The thread was interrupted during sleep, wait or join
        } catch (final TimeoutException e) {
            // Took too long!
            logger.error("Execution timed out after 1 minute");
            Resource res = new Resource();
            res.entriesDetail.setError("Execute command timed out after 1 minute. " + res.entriesDetail.getError());
        } catch (final ExecutionException e) {
            // An exception from within the Runnable task
        }
    }

    /**
     * Unpack some given archive file with the given unpacking arguments.
     * Also prints the standard error if such error occurred.
     *
     * @throws Exception if an invalid unpacking argument is given.
     */
    public File unpack(File current) {

        Resource res = new Resource();
        String file_name = current.getName();
        int dot = file_name.lastIndexOf('.');
        String fileExtension = file_name.substring(dot);
        Process p = null;


        try {

            logger.info("Unpack to the temporary directory");


            if (fileExtension.equals(".tar")) {
                p = Runtime.getRuntime().exec("tar -xf " + current + " -C " + current.getParent());

            } else if (fileExtension.equals(".gz") || fileExtension.equals(".tgz")) {
                p = Runtime.getRuntime().exec("tar -xzf " + current + " -C " + current.getParent());

            } else if (fileExtension.equals(".zip") || fileExtension.equals(".jar")) {
                p = Runtime.getRuntime().exec("unzip " + current + " -d " + current.getParent());

            } else {
                logger.error("Incorrect file extension");
            }

            String stdErr = "";
            String line;
            BufferedReader stdErrReader = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            while ((line = stdErrReader.readLine()) != null) {
                stdErr = stdErr + line;
            }

            res.entriesDetail.setError(stdErr);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return current.getParentFile();
    }


    /**
     * This method finds the hash value of each individual file in an array, and it is then inserted into our database.
     *
     * @param fileList
     */
    public void hashFiles(File[] fileList, File current) {
        Resource res = new Resource();
        FormServlet form = new FormServlet();

        for (int i = 0; i < fileList.length; i++) {

            File file = fileList[i];
            try {
                String fileName = file.getName();
                HashCode hashCode = null;

                if (!file.isDirectory()) {
                    hashCode = Files.hash(file, Hashing.md5());
                }

                if (file.equals(current)) {
                    res.entriesDetail.setHashValue(hashCode.toString());
                    logger.info("Hash code was found for {}.", fileName);
                } else {

                    EntriesDetail subentry = new EntriesDetail();
                    EntriesDetailsDoaImpl imp = new EntriesDetailsDoaImpl();

                    subentry.setFileName(form.renaming(fileName));
                    subentry.setOutput(res.entriesDetail.getOutput());
                    subentry.setError(res.entriesDetail.getError());
                    if (!file.isDirectory()) {
                        subentry.setHashValue(hashCode.toString());
                    }

                    imp.insertIntoEntriesDetail(subentry);
                    logger.info("{} was added to the database.", fileName);

                }

            } catch (IOException e) {
                e.printStackTrace();
                logger.debug("Unable to find the hash value of {}", file.getName());
                continue;
            }
        }
    }
}
