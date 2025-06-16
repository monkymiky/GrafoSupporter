package com.unipd.synclab.grafosupporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // Rende la classe una Spring Bean, così può essere iniettata
@ConfigurationProperties(prefix = "app.upload") // Mappa le proprietà che iniziano con "app.upload"
public class UploadProperties {

    private String combinationImagesDirectory;
    private long maxFileSize; // Usiamo long per la dimensione in bytes
    private List<String> allowedImageTypes; // Spring convertirà la stringa separata da virgole in List<String>

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

    // Puoi aggiungere un metodo toString() per debug
    @Override
    public String toString() {
        return "UploadProperties{" +
                ", combinationImagesSubdirectory='" + combinationImagesDirectory + '\'' +
                ", maxFileSize=" + maxFileSize +
                ", allowedImageTypes=" + allowedImageTypes +
                '}';
    }
}
