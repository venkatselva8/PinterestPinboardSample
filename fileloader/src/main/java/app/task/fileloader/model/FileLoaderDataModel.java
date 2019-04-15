package app.task.fileloader.model;

import app.task.fileloader.listener.FileLoaderListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class FileLoaderDataModel {
    private String url;
    private byte[] data;
    private FileDataType fileDataType;
    private FileLoaderListener fileLoaderListener;
    private String keyMD5;
    public String comeFrom = "Internet";

    protected FileLoaderDataModel(String url, FileDataType fileDataType, FileLoaderListener fileLoaderListener) {
        this.url = url;
        this.fileDataType = fileDataType;
        this.fileLoaderListener = fileLoaderListener;
        this.keyMD5 = md5(this.url);
    }

    public String getKeyMD5() {
        return keyMD5;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getData() {
        return data;
    }

    public FileDataType getFileDataType() {
        return fileDataType;
    }

    public FileLoaderListener getFileLoaderListener() {
        return fileLoaderListener;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public abstract FileLoaderDataModel getCopyOfMe(FileLoaderListener fileLoaderListener);

    private static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2) {
                    h.insert(0, "0");
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
