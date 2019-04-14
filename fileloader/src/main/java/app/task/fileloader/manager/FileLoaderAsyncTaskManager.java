package app.task.fileloader.manager;

import app.task.fileloader.listener.FileLoaderStatusListener;
import app.task.fileloader.model.FileLoaderDataModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

class FileLoaderAsyncTaskManager {
    AsyncHttpClient get(final FileLoaderDataModel fileLoaderDataModel, final FileLoaderStatusListener fileLoaderStatusListener) {
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(fileLoaderDataModel.getUrl(), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                fileLoaderDataModel.getFileLoaderListener().onStart(fileLoaderDataModel);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                fileLoaderDataModel.setData(response);
                fileLoaderDataModel.getFileLoaderListener().onSuccess(fileLoaderDataModel);
                // Successfull response from server
                fileLoaderStatusListener.setDone(fileLoaderDataModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                fileLoaderDataModel.getFileLoaderListener().onFailure(fileLoaderDataModel, statusCode, errorResponse, e);
                // Failure Response from server
                fileLoaderStatusListener.onFailure(fileLoaderDataModel);
            }

            @Override
            public void onRetry(int retryNo) {
                // Retry the failed request
                fileLoaderDataModel.getFileLoaderListener().onRetry(fileLoaderDataModel, retryNo);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                // Cancel the request
                fileLoaderStatusListener.setCancelled(fileLoaderDataModel);
            }
        });
        return client;
    }
}


