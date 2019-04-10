package app.task.fileloader.listener

import app.task.fileloader.model.FileLoaderDataModel

/*
 * Created by venkatesh on 10-Apr-19.
 */

interface FileLoaderListener {
    fun onStart(fileLoaderDataModel: FileLoaderDataModel)

    fun onSuccess(fileLoaderDataModel: FileLoaderDataModel)

    fun onFailure(
        fileLoaderDataModel: FileLoaderDataModel,
        statusCode: Int,
        errorResponse: ByteArray,
        e: Throwable
    )

    fun onRetry(fileLoaderDataModel: FileLoaderDataModel, retryNo: Int)
}
