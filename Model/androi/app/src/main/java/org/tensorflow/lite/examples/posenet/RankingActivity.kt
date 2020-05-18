package org.tensorflow.lite.examples.posenet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

class RankingActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var memberDTOs = mutableListOf<MemberDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        updateData()

        Log.d("Test", "Set recycler view")
        Log.d("Test", "data size = " + memberDTOs.size)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(memberDTOs)


        recyclerView = findViewById<RecyclerView>(R.id.main_recyclerview).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        Log.d("Test", "End onCreate")
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
}