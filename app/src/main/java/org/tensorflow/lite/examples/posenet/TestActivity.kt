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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Posenet
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs


class TestActivity : AppCompatActivity() {

  private val bodyJoints = listOf(
    Pair(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW),
    Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
    Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
    Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
    Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
    Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
    Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
    Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
    Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
    Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
    Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
    Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
  )

  private fun initInputArray(bitmap: Bitmap): ByteBuffer {
    val bytesPerChannel = 4
    val inputChannels = 3
    val batchSize = 1
    val inputBuffer = ByteBuffer.allocateDirect(
      batchSize * bytesPerChannel * bitmap.height * bitmap.width * inputChannels
    )
    inputBuffer.order(ByteOrder.nativeOrder())
    inputBuffer.rewind()

    val mean = 128.0f
    val std = 128.0f
    for (row in 0 until bitmap.height) {
      for (col in 0 until bitmap.width) {
        val pixelValue = bitmap.getPixel(col, row)
        inputBuffer.putFloat(((pixelValue shr 16 and 0xFF) - mean) / std)
        inputBuffer.putFloat(((pixelValue shr 8 and 0xFF) - mean) / std)
        inputBuffer.putFloat(((pixelValue and 0xFF) - mean) / std)
      }
    }
    return inputBuffer
  }

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

    val sampleImageView = findViewById<ImageView>(R.id.image)

    //val videoview = findViewById<VideoView>(R.id.videoview)
    val videofile = Uri.parse("android.resource://" + packageName + "/" + R.raw.false_wall)
    //videoview.setVideoURI(videofile)
    //videoview.start()

    val retriever = MediaMetadataRetriever()
    var bitmapArrayList = ArrayList<Bitmap>()
    retriever.setDataSource(this, videofile)
    val mediaPlayer = MediaPlayer.create(baseContext, videofile)
    val millisec = mediaPlayer.duration
    var count = 0L
    var k = 0

    val posenet = Posenet(this.applicationContext)

    while(count < millisec) {
      var bitmap = retriever.getFrameAtTime(count * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
      val scaledBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_WIDTH, MODEL_HEIGHT, true)
      val inputArray = (initInputArray(bitmap))
      val stream = ByteArrayOutputStream()
      scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
      val image = stream.toByteArray()

      val imageBytes = ByteArray(inputArray.remaining())

      try{
        val os = openFileOutput("false_wall" + count/1000 + ".png", Context.MODE_PRIVATE)
        os.write(imageBytes)
        Log.d("tag", "written")
        os.close()
      }catch(e : IOException){
        e.printStackTrace()
      }

      //Log.d("tag", filesDir.toString())
      count += 500



      /*
      sampleImageView.setImageBitmap(scaledBitmap)


      //val drawedImage = ResourcesCompat.getDrawable(resources, R.drawable.image, null)
      //val imageBitmap = drawableToBitmap(drawedImage!!)
      //sampleImageView.setImageBitmap(imageBitmap)
      val person = posenet.estimateSinglePose(scaledBitmap)

      // Draw the keypoints over the image.
      val paint = Paint()
      paint.color = Color.RED
      val size = 2.0f
      val maxy = 0
      val mutableBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true)
      val canvas = Canvas(mutableBitmap)

      for (keypoint in person.keyPoints) {

        if (keypoint.position.y.toFloat() > maxy) {

          paint.color = Color.BLUE

        } else {
          paint.color = Color.RED
        }
        canvas.drawCircle(
          keypoint.position.x.toFloat(),
          keypoint.position.y.toFloat(), size, paint
        )
      }
      sampleImageView.adjustViewBounds = true
      sampleImageView.setImageBitmap(mutableBitmap)

      pushupAngleL = (calAngle(
        person.keyPoints[7].position.y.toDouble(),
        person.keyPoints[7].position.x.toDouble(),
        person.keyPoints[5].position.y.toDouble(),
        person.keyPoints[9].position.y.toDouble(),
        person.keyPoints[5].position.x.toDouble(),
        person.keyPoints[9].position.x.toDouble()
      ))
      pushupAngleR = (calAngle(
        person.keyPoints[8].position.y.toDouble(),
        person.keyPoints[8].position.x.toDouble(),
        person.keyPoints[6].position.y.toDouble(),
        person.keyPoints[10].position.y.toDouble(),
        person.keyPoints[6].position.x.toDouble(),
        person.keyPoints[10].position.x.toDouble()
      ))

      var angle = 0.0
      //choose which angle gets detected more accuratly and use that to calculate
      if (person.keyPoints[7].score > person.keyPoints[8].score){
        angle = pushupAngleL
      }
      else {
        angle = pushupAngleR
      }

      if (k==1){
        if (angle < 90.0) {
          k = 0
        }
      }
      //only if 90 degree goal was hit check for 150 degree goal
      else {
        if (angle > 150.0){
          pushups += 1
          k=1
        }
      }


      Log.d("tag", pushups.toString())
      //Log.d("tag", pushupAngleL.toString())
      //Log.d("tag", pushupAngleR.toString())

      count += 1000 // plus 1 sec

       */
    }
  }
}
