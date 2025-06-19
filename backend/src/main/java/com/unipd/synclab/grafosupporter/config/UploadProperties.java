package com.unipd.synclab.grafosupporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {

    private String combinationImagesDirectory;
    private long maxFileSize;
    private List<String> allowedImageTypes;

    public String getCombinationImagesDirectory() {
        return combinationImagesDirectory;
    }

    public void setCombinationImagesDirectory(String combinationImagesDirectory) {
        this.combinationImagesDirectory = combinationImagesDirectory;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public List<String> getAllowedImageTypes() {
        return allowedImageTypes;
    }

    public void setAllowedImageTypes(List<String> allowedImageTypes) {
        this.allowedImageTypes = allowedImageTypes;
    }

    @Override
    public String toString() {
        return "UploadProperties{" +
                ", combinationImagesSubdirectory='" + combinationImagesDirectory + '\'' +
                ", maxFileSize=" + maxFileSize +
                ", allowedImageTypes=" + allowedImageTypes +
                '}';
    }
}
