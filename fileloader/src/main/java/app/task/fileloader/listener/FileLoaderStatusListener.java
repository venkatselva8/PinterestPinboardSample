package app.task.fileloader.listener;


import app.task.fileloader.model.FileLoaderDataModel;

public interface FileLoaderStatusListener {
    void setDone(FileLoaderDataModel fileLoaderDataModel);

    void setCancelled(FileLoaderDataModel fileLoaderDataModel);

    void onFailure(FileLoaderDataModel fileLoaderDataModel);
}
