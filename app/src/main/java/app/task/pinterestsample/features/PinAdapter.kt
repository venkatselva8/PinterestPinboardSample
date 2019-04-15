package app.task.pinterestsample.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.task.fileloader.listener.FileLoaderListener
import app.task.fileloader.manager.FileLoaderManager
import app.task.fileloader.model.FileLoaderDataModel
import app.task.fileloader.model.ImageFileLoader
import app.task.pinterestsample.R
import app.task.pinterestsample.features.model.Pin
import app.task.pinterestsample.util.DateUtil

/*
 * Created by venkatesh on 14-Apr-19.
 */

class PinAdapter(val listOfPins: ArrayList<Pin>) :
    RecyclerView.Adapter<PinAdapter.PinViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        return PinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pin,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfPins.size
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        val pin = listOfPins[position]
        val pinCategory = pin.categories

        if (!pinCategory.isNullOrEmpty() && pinCategory.size > 0) {
            pinCategory[0]?.title?.let {
                holder.tvPinTitle.text = it
            }
        }
        var pinDescription = ""
        pin.user?.name?.let { pinDescription = "Clicked by $it" }
        pin.createdAt?.let { dateAsString ->
            DateUtil.getDateFromString(
                "yyyy-MM-dd'T'HH:mm:ssXXX"
                , dateAsString
            )?.let { date ->
                pinDescription = pinDescription + " on " +
                        DateUtil.formattedDateFromDate(date, "dd-MM-yyyy")
                holder.tvPindescription.visibility = View.VISIBLE
                holder.tvPindescription.text = pinDescription
            }
        }
        pin.likes?.let { holder.tvLike.text = it.toString() }
        val pinImage = pin.urls?.small
        if (pinImage != null) {
            val fileLoaderManager: FileLoaderManager = FileLoaderManager.getInstance()
            val fileLoaderDataModel = ImageFileLoader(pinImage, object : FileLoaderListener {
                override fun onStart(fileLoaderDataModel: FileLoaderDataModel?) {
                    holder.imvPin.setImageResource(R.drawable.ic_pin_placeholder)
                }

                override fun onSuccess(fileLoaderDataModel: FileLoaderDataModel?) {
                    holder.imvPin.setImageBitmap((fileLoaderDataModel as ImageFileLoader).imageBitmap)
                }

                override fun onFailure(
                    fileLoaderDataModel: FileLoaderDataModel?,
                    statusCode: Int,
                    errorResponse: ByteArray?,
                    e: Throwable?
                ) {
                    holder.imvPin.setImageResource(R.drawable.ic_pin_image_error)
                }

                override fun onRetry(fileLoaderDataModel: FileLoaderDataModel?, retryNo: Int) {
                }

            })
            fileLoaderManager.getRequest(fileLoaderDataModel)
        } else {
            holder.imvPin.setImageResource(R.drawable.ic_pin_image_error)
        }
    }

    class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imvPin = itemView.findViewById(R.id.imv_pin_image) as ImageView
        val tvPinTitle = itemView.findViewById(R.id.tv_title) as TextView
        val tvPindescription = itemView.findViewById(R.id.tv_desc) as TextView
        val tvLike = itemView.findViewById(R.id.tv_like) as TextView
    }

}