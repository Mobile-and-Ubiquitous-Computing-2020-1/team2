package org.tensorflow.lite.examples.posenet

import android.util.Log
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Position
import java.util.*
import kotlin.math.min

class PushupCounter {
    private val TAG = "PushupCounter"

    // Constants
    private val confidenceThreshold: Float = 0.5f
    private val shoulderDistanceThreshold: Float = 0.25f
    private val errorRateThreshold: Float = 0.5f
    private val windowSize = 10
    private val shoulderYThreshold = 0.09f

    // Windows for smoothing
    private val unitLengthWindow = ArrayDeque<Double>(windowSize)
    private val yShoulderWindow = ArrayDeque<Int>(windowSize)

    // Real-time changing states
    private var person: Person = Person()
    private var direction: Direction = Direction.LEFT
    private var smoothedUnitLength = .0
    private var smoothedYShoulder = 0
    private var unitLength: Double = .0
    private var maxYShoulder = 0
    private var minYShoulder = 0
    private var pushupStarted = false
    private var numFrameFromPushup: Int = 0  // The number of the frames from the start of the current push-up
    private var numErrorFrame: Int = 0
    private var numPushup: Int = 0

    // Body parts for counting
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

    fun count(person: Person, isAccError: Boolean): PushupResult {
//        Log.d(TAG, "numFrameFromPushup: " + numFrameFromPushup + ", numErrorFrame: " + numErrorFrame + ", numPushup: " + numPushup)
//        Log.d(TAG, "pushupStarted: " + pushupStarted)

        this.person = person
        updateStates()
        Log.d(TAG, "direction : " + direction + " unitLength: " + unitLength + " min: " + minYShoulder + ", max: " + maxYShoulder + ", current: " + smoothedYShoulder + ", count: " + numPushup)

        numFrameFromPushup++
        return when {
            isAccError || isCameraAngleError() -> {
                if (isAccError)
                    Log.d(TAG, "Acc Error")
                numErrorFrame++
                PushupResult(0, true, false)
            }
            isPoseError() -> {
                numErrorFrame++
                PushupResult(0, false, true)
            }
            else -> {
                updateState()
                PushupResult(numPushup)
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
//        Log.d(TAG, "isCameraAngleError: num_high_confidence_parts: " + num_high_confidence_parts + "/" + sideBodyParts.size)
        if (sideBodyParts.size - num_high_confidence_parts > 1) {// The confidence of half body parts should be high
            Log.d(TAG, "confidence")
            return true
        }
        if (shoulderDistance > shoulderDistanceThreshold * smoothedUnitLength) {
            Log.d(TAG, "shoulder")
            return true
        }
        return false
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
        if (bodyAngle <= 0 || bodyAngle >= 60) {
            Log.d(TAG, "bodyAngle: " + bodyAngle)
            return true
        }
        if (hipAngle <= 120 || hipAngle >= 210) {
            Log.d(TAG, "hipAngle: " + hipAngle)
            return true
        }
        if (kneeAngle <= 150 || kneeAngle >= 220) {
            Log.d(TAG, "kneeAngle: " + kneeAngle)
            return true
        }
        return false
    }

    private fun triangulateBodyParts(bodyPart1: BodyPart, bodyPart2: BodyPart, bodyPart3: BodyPart): Double {
        val pos1 = person.getPosition(bodyPart1)
        val pos2 = person.getPosition(bodyPart2)
        val pos3 = person.getPosition(bodyPart3)
        return triangulate(pos1, pos2, pos3)
    }

    private fun triangulate(pos1: Position, pos2: Position, pos3: Position): Double {
        val vec21 = pos1 - pos2
        val vec23 = pos3 - pos2
        val cos = vec21 * vec23 / (vec21.size() * vec23.size())
        val theta = Math.acos(cos) * 180 / Math.PI
        val sign = (pos2.x - pos1.x) * (pos3.y - pos1.y) - (pos2.y - pos1.y) * (pos3.x - pos1.x) > 0
        return if ((sign && direction == Direction.RIGHT) || (!sign && direction == Direction.LEFT)) theta
            else 360 - theta
    }

    private fun updateState() {
        if (maxYShoulder < smoothedYShoulder) maxYShoulder = smoothedYShoulder
        if (minYShoulder > smoothedYShoulder) minYShoulder = smoothedYShoulder
        if (maxYShoulder - minYShoulder > shoulderYThreshold * smoothedUnitLength) {
            if (pushupStarted && minYShoulder != smoothedYShoulder) {
                if (numErrorFrame / numFrameFromPushup < errorRateThreshold) {
                    numPushup++
                }
                numFrameFromPushup = 0
                numErrorFrame = 0
            } else {
                pushupStarted = true
            }
            minYShoulder = smoothedYShoulder
            maxYShoulder = smoothedYShoulder
        }
    }

    private fun updateStates() {
        updateDirection()
        updateUnitLength()
        updateShoulderY()
    }

    private fun updateDirection() {
        val sumXShoulder = person.getPosition(BodyPart.LEFT_SHOULDER).x + person.getPosition(BodyPart.RIGHT_SHOULDER).x
        val sumXAnkle = person.getPosition(BodyPart.LEFT_ANKLE).x + person.getPosition(BodyPart.RIGHT_ANKLE).x
        direction = if (sumXAnkle < sumXShoulder) Direction.LEFT else Direction.RIGHT
    }

    private fun updateUnitLength() {
        val posShoulder = getSideBodyPartPosition(OneSideBodyPart.SHOULDER)
        val posHip = getSideBodyPartPosition(OneSideBodyPart.HIP)
        val posKnee = getSideBodyPartPosition(OneSideBodyPart.KNEE)
        val posAnkle = getSideBodyPartPosition(OneSideBodyPart.KNEE)  // TODO:KNEE => ANKLE
        unitLength = (posShoulder - posHip).size() + (posHip - posKnee).size() + (posKnee - posAnkle).size()
        unitLengthWindow.addLast(unitLength)
        if (unitLengthWindow.size > windowSize) {
            unitLengthWindow.removeFirst()
        }
        val windowMidIndex = min(unitLengthWindow.size, windowSize) / 2
        smoothedUnitLength = unitLengthWindow.sorted()[0]
    }

    private fun updateShoulderY() {
        val yShoulder = getSideBodyPartPosition(OneSideBodyPart.SHOULDER).y
        yShoulderWindow.addLast(yShoulder)
        if (yShoulderWindow.size > windowSize) {
            yShoulderWindow.removeFirst()
        }
        val windowMidIndex = min(yShoulderWindow.size, windowSize) / 2
        smoothedYShoulder = yShoulderWindow.sorted()[windowMidIndex]
    }

    private fun getSideBodyPartPosition(oneSideBodyPart: OneSideBodyPart): Position {
        val bodyPart = hashMapOf(
            Pair(Pair(Direction.LEFT, OneSideBodyPart.ANKLE), BodyPart.LEFT_ANKLE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.WRIST), BodyPart.LEFT_WRIST),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.SHOULDER), BodyPart.LEFT_SHOULDER),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.KNEE), BodyPart.LEFT_KNEE),
            Pair(Pair(Direction.LEFT, OneSideBodyPart.HIP), BodyPart.LEFT_HIP),
            Pair(Pair(Direction.RIGHT, OneSideBodyPart.ANKLE), BodyPart.RIGHT_ANKLE),
            Pair(Pair(Direction.RIGHT, OneSideBodyPart.WRIST), BodyPart.RIGHT_WRIST),
            Pair(Pair(Direction.RIGHT, OneSideBodyPart.SHOULDER), BodyPart.RIGHT_SHOULDER),
            Pair(Pair(Direction.RIGHT, OneSideBodyPart.KNEE), BodyPart.RIGHT_KNEE),
            Pair(Pair(Direction.RIGHT, OneSideBodyPart.HIP), BodyPart.RIGHT_HIP)
        )[Pair(direction, oneSideBodyPart)]
        return if (bodyPart == null) Position() else person.getPosition(bodyPart)
    }
}

private enum class OneSideBodyPart { WRIST, ANKLE, SHOULDER, KNEE, HIP }

class PushupResult(
    val count: Int,
    val isCameraAngleError: Boolean = false,
    val isPoseError: Boolean = false
)

private enum class Direction { LEFT, RIGHT }
