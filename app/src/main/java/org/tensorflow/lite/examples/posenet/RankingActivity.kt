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
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class RankingActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var scrollView : ScrollView
    private var actionBarHeight = 0
    private var memberDTOs = mutableListOf<MemberDTO>()

    private val TAG = RankingActivity::class.java.simpleName
    private var usersRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var valueEventListener: ValueEventListener
    private var users: ArrayList<User> = ArrayList()
    private lateinit var currentUser: User

    private lateinit var currentUserName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)
        setContentView(R.layout.activity_rank)

        //val users_origin: ArrayList<User>? = intent.extras?.get("users") as ArrayList<User>?
        //updateData(users)

        currentUserName = intent.extras?.get("currentUserName") as String
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersType: GenericTypeIndicator<ArrayList<User>> =
                    object : GenericTypeIndicator<ArrayList<User>>() {}
                users = dataSnapshot.getValue(usersType) as ArrayList<User>
                for (user in users) {
                    Log.d(TAG, user.id.toString())
                    Log.d(TAG, user.totalScore.toString())
                    if (user.id.equals(currentUserName)) {
                        currentUser = user
                    }
                }
                if (!::currentUser.isInitialized) {
                    currentUser = User(currentUserName)
                    users.add(currentUser)
                    usersRef.setValue(users)
                }
                updateData(users)
                viewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    databaseError.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        usersRef.addValueEventListener(valueEventListener)


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
            users.sortByDescending { user -> user.totalScore }
            memberDTOs.clear()
            memberDTOs.add(MemberDTO(R.drawable.testimage, "Ranking", "User ID", "Total Score"))
            for (i in 0 until users.size) {
                var user = users[i]
                memberDTOs.add(
                    MemberDTO(
                        R.drawable.testimage,
                        (i+1).toString(),
                        user.id,
                        user.totalScore.toString()
                    )
                )
            }
            viewAdapter.notifyDataSetChanged()
        }
    }

    fun onStartButtonClick(view : View){
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, 1)
    }

    fun mOnPopupClick(view: View) { //데이터 담아서 팝업(액티비티) 호출
        val intent = Intent(this, AddFriendPopupActivity::class.java)
        //intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 2)
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
                val pushups = data.extras?.get("pushups") as Int
                Log.d(TAG, "pushup : " + pushups.toString())
                for (i in 0 until users.size){
                    if(users[i].id.equals(currentUserName)) {
                        users[i].totalScore = users[i].totalScore + pushups
                        usersRef.setValue(users)
                        updateData(users)
                    }
                }
            }
        }

    }
    override fun onStop() {
        super.onStop()
        usersRef.removeEventListener(valueEventListener)
    }
}