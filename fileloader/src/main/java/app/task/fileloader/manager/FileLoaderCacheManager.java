package app.task.fileloader.manager;

import android.util.LruCache;
import app.task.fileloader.model.FileLoaderDataModel;

class FileLoaderCacheManager {
    private static FileLoaderCacheManager instance;
    private LruCache<String, FileLoaderDataModel> mDownloadDataTypeCache;

    private FileLoaderCacheManager() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        int maxCacheSize = maxMemory / 8;
        mDownloadDataTypeCache = new LruCache<>(maxCacheSize);
    }

    static FileLoaderCacheManager getInstance() {
        if (instance == null) {
            instance = new FileLoaderCacheManager();
        }
        return instance;
    }

    FileLoaderDataModel getMDownloadDataType(String key) {
        return mDownloadDataTypeCache.get(key);
    }

    void putMDownloadDataType(String key, FileLoaderDataModel fileLoaderDataModel) {
        mDownloadDataTypeCache.put(key, fileLoaderDataModel);
    }

    void clearCache() {
        mDownloadDataTypeCache.evictAll();
    }
}
