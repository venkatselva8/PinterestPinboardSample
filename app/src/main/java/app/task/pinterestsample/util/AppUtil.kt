package app.task.pinterestsample.util

import android.content.Context
import android.widget.Toast

/*
 * Created by venkatesh on 14-Apr-19.
 */

class AppUtil {

    companion object {

        public fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

    }

}