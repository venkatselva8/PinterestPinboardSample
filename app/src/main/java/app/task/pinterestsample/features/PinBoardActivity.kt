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
import app.task.pinterestsample.util.AppConstant
import app.task.pinterestsample.util.AppUtil
import app.task.pinterestsample.util.EndlessRecyclerViewScrollListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_pinboard.*

class PinBoardActivity : AppCompatActivity(), PinPresenterImpl.PinView {

    lateinit var tvMessage: TextView
    lateinit var rvPins: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshToPin: SwipeRefreshLayout
    var isPinLoadedSuccess = false
    lateinit var pinPresenterImpl: PinPresenterImpl
    lateinit var fabClearCache: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinboard)
        setSupportActionBar(toolbar)

        rvPins = findViewById(R.id.rv_pins)
        tvMessage = findViewById(R.id.tv_message)
        progressBar = findViewById(R.id.progress_bar)
        swipeRefreshToPin = findViewById(R.id.swipe_refresh_pin)
        fabClearCache = findViewById(R.id.fab_clear_cache)
        isPinLoadedSuccess = false

        pinPresenterImpl = PinPresenterImpl(this, this)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPins.layoutManager = linearLayoutManager
        rvPins.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onScrollStateChanged(scrollState: Int) {
                if (scrollState == AppConstant.RECYCLERVIEW_SCROLL_ON_STOP) {
                    fabClearCache.show()
                } else {
                    fabClearCache.hide()
                }
            }

            override fun onLoadMore() {
                //Pagination
            }
        })

        swipeRefreshToPin.setOnRefreshListener {
            pinPresenterImpl.getPins()
        }

        fabClearCache.setOnClickListener {
            cacheClear()
        }

        pinPresenterImpl.getPins()
    }

    override fun loadPins(listOfPin: ArrayList<Pin>) {
        isPinLoadedSuccess = true
        rvPins.visibility = View.VISIBLE
        tvMessage.visibility = View.GONE
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
        rvPins.visibility = View.GONE
        tvMessage.visibility = View.GONE
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

    private fun cacheClear() {
        pinPresenterImpl.clearCache()
        AppUtil.showToast(this, "Cache cleared")
    }
}
