package app.task.fileloader.manager;

import android.util.LruCache;
import app.task.fileloader.model.FileLoaderDataModel;

public class FileLoaderCacheManager {
    private int maxCacheSize;
    private static FileLoaderCacheManager instance;
    private LruCache<String, FileLoaderDataModel> mDownloadDataTypeCache;

    private FileLoaderCacheManager() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        maxCacheSize = maxMemory / 8; // 4 * 1024 * 1024; // 4MiB
        mDownloadDataTypeCache = new LruCache<>(maxCacheSize);
    }

    public static FileLoaderCacheManager getInstance() {
        if (instance == null) {
            instance = new FileLoaderCacheManager();
        }
        return instance;
    }

    public FileLoaderDataModel getMDownloadDataType(String key) {
        return mDownloadDataTypeCache.get(key);
    }

    public boolean putMDownloadDataType(String key, FileLoaderDataModel fileLoaderDataModel) {
        return mDownloadDataTypeCache.put(key, fileLoaderDataModel) != null;
    }

    public void clearCache() {
        mDownloadDataTypeCache.evictAll();
    }
}
