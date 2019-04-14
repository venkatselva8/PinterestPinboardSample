package app.task.fileloader.listener;


import app.task.fileloader.model.FileLoaderDataModel;

public interface FileLoaderListener
{
	void onStart(FileLoaderDataModel fileLoaderDataModel);
	
	void onSuccess(FileLoaderDataModel fileLoaderDataModel);
	
	void onFailure(FileLoaderDataModel fileLoaderDataModel, int statusCode, byte[] errorResponse, Throwable e);
	
	void onRetry(FileLoaderDataModel fileLoaderDataModel, int retryNo);
}
