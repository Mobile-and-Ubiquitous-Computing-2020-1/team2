package org.tensorflow.lite.examples.posenet

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var totalScore: Int = 0,
    var friends_id: ArrayList<String> = ArrayList(),
    var scores: ArrayList<Int> = ArrayList()
)
