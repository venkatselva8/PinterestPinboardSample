package app.task.pinterestsample.features

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.task.pinterestsample.R
import app.task.pinterestsample.features.model.Pin
import app.task.pinterestsample.features.pinboard.PinAdapter
import app.task.pinterestsample.features.pinboard.PinPresenterImpl
import app.task.pinterestsample.util.AppUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PinPresenterImpl.PinView {

    lateinit var tvMessage: TextView
    lateinit var rvPins: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshToPin: SwipeRefreshLayout
    var isPinLoadedSuccess = false
    lateinit var pinPresenterImpl: PinPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvPins = findViewById(R.id.rv_pins)
        tvMessage = findViewById(R.id.tv_message)
        progressBar = findViewById(R.id.progress_bar)
        swipeRefreshToPin = findViewById(R.id.swipe_refresh_pin)
        isPinLoadedSuccess = false

        pinPresenterImpl = PinPresenterImpl(this, this)
        pinPresenterImpl.getPins()

        swipeRefreshToPin.setOnRefreshListener {
            pinPresenterImpl.getPins()
        }
    }

    override fun loadPins(listOfPin: ArrayList<Pin>) {
        isPinLoadedSuccess = true
        rvPins.visibility = View.VISIBLE
        tvMessage.visibility = View.GONE
        rvPins.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPins.adapter = PinAdapter(listOfPin)
    }

    override fun showMessage(message: String) {
        rvPins.visibility = View.GONE
        tvMessage.visibility = View.VISIBLE
        if (swipeRefreshToPin.isRefreshing) {
            swipeRefreshToPin.isRefreshing = false
        }
        isPinLoadedSuccess = false

        tvMessage.text = message
    }

    override fun showProgress() {
        swipeRefreshToPin.isEnabled = false
        if (!isPinLoadedSuccess) {                        // On first time
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        swipeRefreshToPin.isEnabled = true
        if (!isPinLoadedSuccess) {
            progressBar.visibility = View.GONE
        }
        if (swipeRefreshToPin.isRefreshing) {
            swipeRefreshToPin.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_clear_cache -> {
                cacheClear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cacheClear() {
        pinPresenterImpl.clearCache()
        AppUtil.showToast(this, "Cache cleared")
    }
}
