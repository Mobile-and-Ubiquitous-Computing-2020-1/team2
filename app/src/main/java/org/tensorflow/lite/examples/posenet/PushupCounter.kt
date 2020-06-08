package org.tensorflow.lite.examples.posenet

import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Position
import org.tensorflow.lite.examples.posenet.lib.distance
import java.util.ArrayDeque

class PushupCounter {

    // Constants
    private val confidenceThreshold: Float = 0.5f
    private val shoulderDistanceThreshold: Float = 1f/6
    private val errorRateThreshold: Float = 0.5f
    private val sampleInterval = 30 // TODO
    private val windowSize = 1000 / sampleInterval

    // Windows for smoothing
    private val unitLengthWindow = ArrayDeque<Float>()
    private val shoulderYWindow = ArrayDeque<Float>()

    // States changing in real-time
    private var person: Person = Person()
    private var direction: Direction = Direction.LEFT
    private var smoothedUnitLength = 0
    private var smoothedShoulderY = 0

    private var unitLength: Float = 0f

    // Count
    private var totalCount: Int = 0

    // Only important body parts
    private val LEFT_BODY_PARTS = setOf(
        BodyPart.LEFT_SHOULDER,
        BodyPart.LEFT_WRIST,
        BodyPart.LEFT_HIP,
        BodyPart.LEFT_KNEE,
        BodyPart.LEFT_ANKLE
    )
    private val RIGHT_BODY_PARTS = setOf(
        BodyPart.RIGHT_SHOULDER,
        BodyPart.RIGHT_WRIST,
        BodyPart.RIGHT_HIP,
        BodyPart.RIGHT_KNEE,
        BodyPart.RIGHT_ANKLE
    )

    fun count(person: Person /* TODO Add camera angle flag (accelerometer) */): PushupResult {
        this.person = person
        updateStates()

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

    private fun isCameraAngleError(): Boolean {
        var leftShoulderPos = Position()
        var rightShoulderPos = Position()
        var num_high_confidence_parts = 0

        val sideBodyParts = if (direction == Direction.LEFT) LEFT_BODY_PARTS else RIGHT_BODY_PARTS

        for (keyPoint in person.keyPoints) {
            if (sideBodyParts.contains(keyPoint.bodyPart) && keyPoint.score > confidenceThreshold) {
                num_high_confidence_parts++
            }

            when {
                keyPoint.bodyPart.equals(BodyPart.LEFT_SHOULDER) -> {
                    leftShoulderPos = keyPoint.position
                }
                keyPoint.bodyPart.equals(BodyPart.RIGHT_SHOULDER) -> {
                    rightShoulderPos = keyPoint.position
                }
            }
        }
        val shoulderDistance = distance(leftShoulderPos, rightShoulderPos)

        return sideBodyParts.size - num_high_confidence_parts > 1 // The confidence of half body parts should be high
                && shoulderDistance > shoulderDistanceThreshold * smoothedUnitLength
    }

    private fun isPoseError(): Boolean {
        return false
    }

    private fun updateState() {
        TODO("Not yet implemented")
    }

    private fun updateStates() {
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
