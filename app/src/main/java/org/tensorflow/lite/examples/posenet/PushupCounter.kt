package org.tensorflow.lite.examples.posenet

import org.tensorflow.lite.examples.posenet.lib.Person

enum class CountType {
    SUCCESS,
    PROGRESS,
    CAMERA_ERROR,
    POSE_ERROR
}

class PushupCounter {
    var time: Int = 0

    fun count(person: Person, isRightCameraPosition: Boolean): CountType {
        time += 1
        if (time % 100 == 0)
            return CountType.SUCCESS
        return CountType.PROGRESS
    }
}