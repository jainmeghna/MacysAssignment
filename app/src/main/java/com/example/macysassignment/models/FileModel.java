package com.example.macysassignment.models;

/**
 * Created by mjain on 8/9/2016.
 */
public class FileModel {
    String fileName;
    String fileAbsoluteName;
    Long fileLength;
    String fileExtention;

    public String getFileName() {
        return fileName;
    }

    public String getFileAbsoluteName() {
        return fileAbsoluteName;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public String getFileExtention() {
        return fileExtention;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileAbsoluteName(String fileAbsoluteName) {
        this.fileAbsoluteName = fileAbsoluteName;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public void setFileExtention(String fileExtention) {
        this.fileExtention = fileExtention;
    }
}
