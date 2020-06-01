package org.tensorflow.lite.examples.posenet

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var friends_id: ArrayList<String> = ArrayList(),
    var scores: ArrayList<Int> = ArrayList()
) : Serializable {
    fun getTotalScore(): Int {
        return scores.sum()
    }
}
