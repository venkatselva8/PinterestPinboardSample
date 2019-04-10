package app.task.fileloader.model;

import app.task.fileloader.listener.FileLoaderListener;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonFileLoader extends FileLoaderDataModel {
    public JsonFileLoader(String url, FileLoaderListener fileLoaderListener) {
        super(url, FileDataType.JSON, fileLoaderListener);
    }

    @Override
    public FileLoaderDataModel getCopyOfMe(FileLoaderListener fileLoaderListener) {
        return new JsonFileLoader(this.getUrl(), fileLoaderListener);
    }

    private String getJsonAsString() {
        try {
            return new String(getData(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Object getJson(Type type) {
        Gson gson = new Gson();
        return gson.fromJson(getJsonAsString(), type);
    }
}
