package app.task.fileloader.manager;

import android.util.Log;
import app.task.fileloader.listener.FileLoaderListener;
import app.task.fileloader.listener.FileLoaderStatusListener;
import app.task.fileloader.model.FileLoaderDataModel;
import com.loopj.android.http.AsyncHttpClient;

import java.util.HashMap;
import java.util.LinkedList;

public class FileLoaderDataType {
    private static FileLoaderDataType instance;
    private HashMap<String, LinkedList<FileLoaderDataModel>> allRequestsByKey = new HashMap<>();
    private HashMap<String, AsyncHttpClient> allRequestsClient = new HashMap<>();
    private FileLoaderCacheManager fileLoaderCacheManager;

    private FileLoaderDataType() {
        fileLoaderCacheManager = FileLoaderCacheManager.getInstance();
    }

    public static FileLoaderDataType getInstance() {
        if (instance == null) {
            instance = new FileLoaderDataType();
        }
        return instance;
    }

    public void getRequest(final FileLoaderDataModel fileLoaderDataModel) {
        final String mKey = fileLoaderDataModel.getKeyMD5();
        // Check if exist in the cache
        FileLoaderDataModel fileLoaderDataModelFromCache = fileLoaderCacheManager.getMDownloadDataType(mKey);
        if (fileLoaderDataModelFromCache != null) {
            fileLoaderDataModelFromCache.comeFrom = "Cache";
            // call interface
            FileLoaderListener fileLoaderListener = fileLoaderDataModel.getFileLoaderListener();
            fileLoaderListener.onStart(fileLoaderDataModelFromCache);
            fileLoaderListener.onSuccess(fileLoaderDataModelFromCache);
            return;
        }
        // This will run if two request come for same url
        if (allRequestsByKey.containsKey(mKey)) {
            fileLoaderDataModel.comeFrom = "Cache";
            allRequestsByKey.get(mKey).add(fileLoaderDataModel);
            return;
        }
        // Put it in the allRequestsByKey to manage it after done or cancel
        if (allRequestsByKey.containsKey(mKey)) {
            allRequestsByKey.get(mKey).add(fileLoaderDataModel);
        } else {
            LinkedList<FileLoaderDataModel> lstMDDataType = new LinkedList<>();
            lstMDDataType.add(fileLoaderDataModel);
            allRequestsByKey.put(mKey, lstMDDataType);
        }
        // Create new WebCallDataTypeDownload by clone it from the parameter
        FileLoaderDataModel newWebCallDataTypeDownload = fileLoaderDataModel.getCopyOfMe(new FileLoaderListener() {
            @Override
            public void onStart(FileLoaderDataModel mDownloadDataType) {
                for (FileLoaderDataModel m : allRequestsByKey.get(mDownloadDataType.getKeyMD5())) {
                    m.setData(mDownloadDataType.getData());
                    m.getFileLoaderListener().onStart(m);
                }
            }

            @Override
            public void onSuccess(FileLoaderDataModel mDownloadDataType) {
                for (FileLoaderDataModel m : allRequestsByKey.get(mDownloadDataType.getKeyMD5())) {
                    m.setData(mDownloadDataType.getData());
                    Log.e("HERRRR", m.comeFrom);
                    m.getFileLoaderListener().onSuccess(m);
                }
                allRequestsByKey.remove(mDownloadDataType.getKeyMD5());
            }

            @Override
            public void onFailure(FileLoaderDataModel mDownloadDataType, int statusCode, byte[] errorResponse, Throwable e) {
                for (FileLoaderDataModel m : allRequestsByKey.get(mDownloadDataType.getKeyMD5())) {
                    m.setData(mDownloadDataType.getData());
                    m.getFileLoaderListener().onFailure(m, statusCode, errorResponse, e);
                }
                allRequestsByKey.remove(mDownloadDataType.getKeyMD5());
            }

            @Override
            public void onRetry(FileLoaderDataModel mDownloadDataType, int retryNo) {
                for (FileLoaderDataModel m : allRequestsByKey.get(mDownloadDataType.getKeyMD5())) {
                    m.setData(mDownloadDataType.getData());
                    m.getFileLoaderListener().onRetry(m, retryNo);
                }
            }
        });
        // Get from download manager
        final FileLoaderAsyncTaskManager fileLoaderAsyncTaskManager = new FileLoaderAsyncTaskManager();
        AsyncHttpClient client = fileLoaderAsyncTaskManager.get(newWebCallDataTypeDownload, new FileLoaderStatusListener() {
            @Override
            public void setDone(FileLoaderDataModel mDownloadDataType) {
                // put in the cache when mark as done
                fileLoaderCacheManager.putMDownloadDataType(mDownloadDataType.getKeyMD5(), mDownloadDataType);
                allRequestsClient.remove(mDownloadDataType.getKeyMD5());
            }

            @Override
            public void onFailure(FileLoaderDataModel mDownloadDataType) {
                allRequestsClient.remove(mDownloadDataType.getKeyMD5());
            }

            @Override
            public void setCancelled(FileLoaderDataModel mDownloadDataType) {
                allRequestsClient.remove(mDownloadDataType.getUrl());
            }
        });
        allRequestsClient.put(mKey, client);
    }

    public void cancelRequest(FileLoaderDataModel fileLoaderDataModel) {
        if (allRequestsByKey.containsKey(fileLoaderDataModel.getKeyMD5())) {
            allRequestsByKey.get(fileLoaderDataModel.getKeyMD5()).remove(fileLoaderDataModel);
            fileLoaderDataModel.comeFrom = "cancelRequest";
            fileLoaderDataModel.getFileLoaderListener().onFailure(fileLoaderDataModel, 0, null, null);
        }
    }

    public boolean isQueueEmpty() {
        return allRequestsByKey.size() == 0;
    }

    public void clearTheCash() {
        fileLoaderCacheManager.clearTheCash();
    }
}
