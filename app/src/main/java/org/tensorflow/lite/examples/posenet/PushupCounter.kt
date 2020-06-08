package org.tensorflow.lite.examples.posenet

import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Position
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
        val shoulderDistance = (leftShoulderPos - rightShoulderPos).size()

        return sideBodyParts.size - num_high_confidence_parts > 1 // The confidence of half body parts should be high
                && shoulderDistance > shoulderDistanceThreshold * smoothedUnitLength
    }

    private fun isPoseError(): Boolean {
        val wrist = if (direction.equals(Direction.LEFT)) BodyPart.LEFT_WRIST else BodyPart.RIGHT_WRIST
        val ankle = if (direction.equals(Direction.LEFT)) BodyPart.LEFT_ANKLE else BodyPart.RIGHT_ANKLE
        val shoulder = if (direction.equals(Direction.LEFT)) BodyPart.LEFT_SHOULDER else BodyPart.RIGHT_SHOULDER
        val knee = if (direction.equals(Direction.LEFT)) BodyPart.LEFT_KNEE else BodyPart.RIGHT_KNEE
        val hip = if (direction.equals(Direction.LEFT)) BodyPart.LEFT_HIP else BodyPart.RIGHT_HIP
        val bodyAngle = triangulateBodyParts(wrist, ankle, shoulder)
        val hipAngle = triangulateBodyParts(knee, hip, shoulder)
        val kneeAngle = triangulateBodyParts(ankle, knee, hip)
        if (bodyAngle == null || hipAngle == null || kneeAngle == null) {
            return true
        }
        if (bodyAngle <= 5 || bodyAngle >= 30) {
            return true
        }
        if (hipAngle <= 135 || hipAngle >= 180) {
            return true
        }
        if (kneeAngle <= 150 || kneeAngle >= 210) {
            return true
        }
        return false
    }

    private fun triangulateBodyParts(bodyPart1: BodyPart, bodyPart2: BodyPart, bodyPart3: BodyPart): Double? {
        val pos1 = person.getPosition(bodyPart1)
        val pos2 = person.getPosition(bodyPart1)
        val pos3 = person.getPosition(bodyPart1)
        if (pos1 == null || pos2 == null || pos3 == null) {
            return null
        }
        return triangulate(pos1, pos2, pos3)
    }

    private fun triangulate(pos1: Position, pos2: Position, pos3: Position): Double {
        val vec21 = pos1 - pos2
        val vec23 = pos3 - pos2
        val cos = vec21 * vec23 / (vec21.size() * vec23.size())
        val theta = Math.acos(cos) * 180 / Math.PI
        val sign = (pos2.x - pos1.x) * (pos3.y - pos1.y) - (pos2.y - pos1.y) * (pos3.x - pos1.x) > 0
        return if (sign && direction == Direction.RIGHT || !sign && direction == Direction.LEFT) theta else 360 - theta
    }

    private fun updateState() {

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

    private fun getSideBodyPartPosition(oneSideBodyPart: OneSideBodyPart): Position? {
        val bodyPart = hashMapOf(
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE)
        )[Pair(direction, oneSideBodyPart)]
        if (bodyPart == null) {
            return null
        } else {
            return person.getPosition(bodyPart)
        }
    }
}

private enum class OneSideBodyPart {
    WRIST, ANKLE, SHOULDER, KNEE, HIP
}

class PushupResult(
    val count: Int,
    val isCameraAngleError: Boolean = false,
    val isPoseError: Boolean = false
)

private enum class Direction {
    LEFT, RIGHT
}
