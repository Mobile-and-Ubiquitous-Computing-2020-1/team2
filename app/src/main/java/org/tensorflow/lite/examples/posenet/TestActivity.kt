/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.posenet

// import sun.text.normalizer.UTF16.append

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Posenet
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs


class TestActivity : AppCompatActivity() {
  private val minConfidence = 0.5
  var pushups = 0
  var pushupAngleL = 0.0
  var pushupAngleR = 0.0

  fun calAngle(starty: Double, startx: Double, stopy1: Double, stopy2: Double, stopx1: Double, stopx2: Double):Double{
    val angle1: Double = Math.atan2((starty - stopy1), (startx - stopx1))
    val angle2: Double = Math.atan2((starty - stopy2), (startx - stopx2))
    return abs((angle2-angle1) *180/3.14)
  }
  /** Returns a resized bitmap of the drawable image.    */
  private fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(257, 257, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, canvas.width, canvas.height)

    drawable.draw(canvas)
    return bitmap
  }

  /** Calls the Posenet library functions.    */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.tfe_pn_activity_test)

    val resourceList = intArrayOf(
//      R.raw.backward_dips,
//      R.raw.degree_45_narrow,
//      R.raw.degree_45_wide,
//      R.raw.false_form,
//      R.raw.false_knee_on_bottom,
//      R.raw.false_laying_on_floor,
//      R.raw.false_medium,
//      R.raw.false_small,
//      R.raw.false_wall,
//      R.raw.frontal_view,
//      R.raw.left_arm_mirror,
//      R.raw.left_side_regular,
//      R.raw.narrow,
      R.raw.rear_view,
      R.raw.regular,
      R.raw.regular_other_person,
      R.raw.right_arm_mirror,
      R.raw.wide)

    val dirnameList = arrayOf(
//      "backward_dips",
//      "degree_45_narrow",
//      "degree_45_wide",
//      "false_form",
//      "false_knee_on_bottom",
//      "false_laying_on_floor",
//      "false_medium",
//      "false_small",
//      "false_wall",
//      "frontal_view",
//      "left_arm_mirror",
//      "left_side_regular",
//      "narrow",
      "rear_view",
      "regular",
      "regular_other_person",
      "right_arm_mirror",
      "wide")

    for (i in 0..resourceList.size) {
      val resource = resourceList[i]
      val dirname = dirnameList[i]

      saveVideo(resource, "$filesDir/data/$dirname")
    }

  }

  private fun saveVideo(resource: Int, dirpath: String) {
    val videofile = Uri.parse("android.resource://" + packageName + "/" + resource)
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, videofile)

    val mediaPlayer = MediaPlayer.create(baseContext, videofile)
    val millisec = mediaPlayer.duration
    val frameInterval = 30
    var count = 0L

    Log.d("AAAA", millisec.toString())
    Log.d("AAAA", dirpath)

    val dir = File(dirpath)
    try {
      dir.mkdir()
    } catch (e: IOException) {}

    val posenet = Posenet(this.applicationContext)

    while (count < millisec) {
      var bitmap = retriever.getFrameAtTime(count * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
      val cropedBitmap = cropBitmap(bitmap)
      val scaledBitmap = Bitmap.createScaledBitmap(cropedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true)
      val person = posenet.estimateSinglePose(scaledBitmap)

      val out = File("$dirpath/$count.out")
      val builder = StringBuilder(1000)
      builder.append("score: $person.score\n")
      for (keypoint in person.keyPoints.toList()) {
        builder.append("${keypoint.bodyPart.toString()} ${keypoint.position.x} ${keypoint.position.y} ${keypoint.score}\n")
      }
      out.writeText(builder.toString())

      try {
        val out = FileOutputStream("$dirpath/$count.png")
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
      } catch (e: IOException) {
        e.printStackTrace()
      }
      count += frameInterval
    }
  }

  /**
   * from Posenet.kt
   * Crop bitmap file
   */
  private fun cropBitmap(bitmap: Bitmap): Bitmap {
    val bitmapRatio = bitmap.height.toFloat() / bitmap.width
    val modelInputRatio = MODEL_HEIGHT.toFloat() / MODEL_WIDTH
    var croppedBitmap = bitmap

    // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
    val maxDifference = 1e-5

    // Checks if the bitmap has similar aspect ratio as the required model input.
    when {
      abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
      modelInputRatio < bitmapRatio -> {
        // New image is taller so we are height constrained.
        val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
          bitmap,
          0,
          (cropHeight / 2).toInt(),
          bitmap.width,
          (bitmap.height - cropHeight).toInt()
        )
      }
      else -> {
        val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
          bitmap,
          (cropWidth / 2).toInt(),
          0,
          (bitmap.width - cropWidth).toInt(),
          bitmap.height
        )
      }
    }
    return croppedBitmap
  }
}
