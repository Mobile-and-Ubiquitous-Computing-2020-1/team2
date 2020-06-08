package org.tensorflow.lite.examples.posenet

import org.tensorflow.lite.examples.posenet.lib.Person

class PushupCounter {
    var person: Person? = null
    private var direction: Direction = Direction.LEFT
    private var unitLength: Float = 0f

    private var totalCount: Int = 0

    fun count(person: Person): PushupResult {
        // TODO
        this.person = person
        updateValues()

        when {
            isCameraAngleError() -> {
                return PushupResult(0, true, false)
            }
            isPoseError() -> {
                return PushupResult(0, false, true)
            }
            else -> {
                totalCount += 1
                updateState()
                return PushupResult(totalCount)
            }
        }
    }

    private fun isPoseError(): Boolean {
        return false
    }

    private fun isCameraAngleError(): Boolean {
        return false
    }

    private fun updateState() {
        TODO("Not yet implemented")
    }

    private fun updateValues() {
        updateDirection()
        updateUnitLength()
        updateShoulderY()
    }

    private fun updateDirection() {
        // TODO
        direction = Direction.LEFT
    }

    private fun updateUnitLength() {
        // TODO
        unitLength = 0f
    }

    private fun updateShoulderY() {

    }
}


class PushupResult(
    val count: Int,
    val isCameraAngleError: Boolean = false,
    val isPoseError: Boolean = false
)


private enum class Direction {
    LEFT, RIGHT
}
