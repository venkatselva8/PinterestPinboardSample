package app.task.fileloader.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import app.task.fileloader.listener.FileLoaderListener;

public class ImageFileLoader extends FileLoaderDataModel {
    public ImageFileLoader(String url, FileLoaderListener fileLoaderListener) {
        super(url, FileDataType.IMAGE, fileLoaderListener);
    }

    @Override
    public FileLoaderDataModel getCopyOfMe(FileLoaderListener fileLoaderListener) {
        return new ImageFileLoader(this.getUrl(), fileLoaderListener);
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeByteArray(getData(), 0, getData().length);
    }
}
