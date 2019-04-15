package app.task.pinterestsample.features

import android.content.Context
import app.task.fileloader.listener.FileLoaderListener
import app.task.fileloader.manager.FileLoaderManager
import app.task.fileloader.model.FileLoaderDataModel
import app.task.fileloader.model.JsonFileLoader
import app.task.pinterestsample.features.model.Pin
import app.task.pinterestsample.util.AppConstant
import app.task.pinterestsample.util.NetworkUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.charset.StandardCharsets

/*
 * Created by venkatesh on 13-Apr-19.
 */

class PinPresenterImpl(val mContext: Context, val pinView: PinView) :
    PinPresenter {

    var fileLoaderManager: FileLoaderManager = FileLoaderManager.getInstance()

    override fun getPins() {

        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            pinView.showMessage("No internet connection, try again")
            return
        }
        val fileLoaderDataModel = JsonFileLoader(AppConstant.GET_PINS_API_URL, object : FileLoaderListener {
            override fun onStart(fileLoaderDataModel: FileLoaderDataModel) {
                pinView.showProgress()
            }

            override fun onSuccess(fileLoaderDataModel: FileLoaderDataModel) {
                pinView.hideProgress()
                var errorMessage = AppConstant.ERROR_DEFAULT_MESSAGE
                val response = String(fileLoaderDataModel.data, StandardCharsets.UTF_8)
                val listOfPin = Gson().fromJson<ArrayList<Pin>>(response, object : TypeToken<ArrayList<Pin>>() {}.type)
                if (listOfPin != null) {
                    if (listOfPin.isNotEmpty()) {
                        pinView.loadPins(listOfPin)
                        return
                    }
                    errorMessage = "No Pins available"
                }
                pinView.showMessage(errorMessage)
            }

            override fun onFailure(
                fileLoaderDataModel: FileLoaderDataModel,
                statusCode: Int,
                errorResponse: ByteArray,
                e: Throwable
            ) {
                pinView.hideProgress()
                pinView.showMessage(AppConstant.ERROR_DEFAULT_MESSAGE)
            }

            override fun onRetry(fileLoaderDataModel: FileLoaderDataModel, retryNo: Int) {
            }
        })

        fileLoaderManager.getRequest(fileLoaderDataModel)
    }

    override fun clearCache() {
        fileLoaderManager.clearCache()
    }


    interface PinView {

        fun loadPins(listOfPin: ArrayList<Pin>)

        fun showMessage(message: String)

        fun showProgress()

        fun hideProgress()

    }

}