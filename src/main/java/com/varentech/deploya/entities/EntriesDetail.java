package com.varentech.deploya.entities;

/**
 * Class that holds the values of all the Entries_Details values.
 */
public class EntriesDetail {

    private String fileName;
    private String hashValue;
    private String output;
    private String error;

    //Default Constructor
    public EntriesDetail() { }

    //Constructor
    public EntriesDetail(
            final String fileName, final String hashValue, final String output, final String error) {
        this.fileName = fileName;
        this.hashValue = hashValue;
        this.output = output;
        this.error = error;
    }

    //Setters
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }
    public void setHashValue(final String hashValue) {
        this.hashValue = hashValue;
    }
    public void setOutput(final String output) {
        this.output = output;
    }
    public void setError(final String error) {
        this.error = error;
    }

    //Getters
    public String getFileName() {
        return fileName;
    }
    public String getHashValue() {
        return hashValue;
    }
    public String getOutput() {
        return output;
    }
    public String getError() {
        return error;
    }

}

