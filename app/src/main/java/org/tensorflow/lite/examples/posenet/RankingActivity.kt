package org.tensorflow.lite.examples.posenet

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ScrollView
import androidx.annotation.RequiresApi

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

        val users: ArrayList<User>? = intent.extras?.get("users") as ArrayList<User>?
        updateData(users)

        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(memberDTOs)
        scrollView = findViewById(R.id.ranking_scrollbar)


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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        layoutTheView()
    }

    private fun layoutTheView() {
        val params = scrollView.layoutParams as MarginLayoutParams
        params.setMargins(20, actionBarHeight + 30, 20, 0)
        scrollView.layoutParams = params
    }

    private fun updateData(users: ArrayList<User>?)
    {
        if (users != null) {
            users.sortByDescending { user -> user.getTotalScore() }
            memberDTOs.clear()
            for (i in 0 until users.size) {
                var user = users[i]
                memberDTOs.add(
                    MemberDTO(
                        R.drawable.testimage,
                        i,
                        user.id,
                        user.getTotalScore().toString()
                    )
                )
            }
        }
    }

    fun onStartButtonClick(view : View){
        startActivity(Intent(this, CameraActivity::class.java))
    }

    fun mOnPopupClick(view: View) { //데이터 담아서 팝업(액티비티) 호출
        val intent = Intent(this, AddFriendPopupActivity::class.java)
        //intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val friend_id = data.getStringExtra("result")
                Log.d("TAG", friend_id)
                //memberDTOs.add(MemberDTO(R.drawable.testimage4, 5, friend_id, "10"))
            }
        }
    }
}