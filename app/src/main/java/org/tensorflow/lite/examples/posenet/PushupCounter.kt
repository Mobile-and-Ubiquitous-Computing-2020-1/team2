package org.tensorflow.lite.examples.posenet

import org.tensorflow.lite.examples.posenet.lib.Person

enum class CountType {
    SUCCESS,
    PROGRESS,
    CAMERA_ERROR,
    POSE_ERROR
}

class PushupCounter {

    fun count(person: Person, isRightCameraPosition: Boolean): CountType {

        return CountType.SUCCESS
    }
}