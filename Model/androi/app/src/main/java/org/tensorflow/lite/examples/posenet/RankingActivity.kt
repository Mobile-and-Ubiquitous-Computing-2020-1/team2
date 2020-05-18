package org.tensorflow.lite.examples.posenet

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ScrollView

class RankingActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var scrollView : ScrollView
    private var actionBarHeight = 0
    private var memberDTOs = mutableListOf<MemberDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        updateData()

        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(memberDTOs)
        scrollView = findViewById(R.id.ranking_scrollbar) as ScrollView


        recyclerView = findViewById<RecyclerView>(R.id.main_recyclerview).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        layoutTheView()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        layoutTheView()
    }

    private fun layoutTheView() {
        val params = scrollView.layoutParams as MarginLayoutParams
        params.setMargins(20, actionBarHeight + 30, 20, 0)
        scrollView.layoutParams = params
    }

    private fun updateData()
    {
        memberDTOs.add(MemberDTO(R.drawable.testimage, 1, "김지수", "100"))
        memberDTOs.add(MemberDTO(R.drawable.testimage2, 2, "AA", "50"))
        memberDTOs.add(MemberDTO(R.drawable.testimage3, 3, "BB", "40"))
        memberDTOs.add(MemberDTO(R.drawable.testimage4, 4, "CC", "30"))
    }

    public fun onStartButtonClick(view : View){
        startActivity(Intent(this, CameraActivity::class.java))
    }

    public fun mOnPopupClick(v: View) { //데이터 담아서 팝업(액티비티) 호출
        val intent = Intent(this, AddFriendPopupActivity::class.java)
        //intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1)
    }
}