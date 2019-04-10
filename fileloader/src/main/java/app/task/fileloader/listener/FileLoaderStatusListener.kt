package app.task.fileloader.listener

import app.task.fileloader.model.FileLoaderDataModel

/*
 * Created by venkatesh on 10-Apr-19.
 */

interface FileLoaderStatusListener {
    fun setDone(fileLoaderDataModel: FileLoaderDataModel)

    fun setCancelled(fileLoaderDataModel: FileLoaderDataModel)

    fun onFailure(fileLoaderDataModel: FileLoaderDataModel)
}
