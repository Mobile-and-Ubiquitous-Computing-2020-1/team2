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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class CameraActivity : AppCompatActivity() {
  private val TAG = CameraActivity::class.java.simpleName
  private lateinit var act : PosenetActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.tfe_pn_activity_camera)
    act = PosenetActivity()
    savedInstanceState ?: supportFragmentManager.beginTransaction()
      .replace(R.id.container, act)
      .commit()
  }

  fun onClickExit(view: View) {
    Log.d(TAG, "onClickExit")
    val intent = Intent()
    Log.d("pushups", act.pushups.toString())
    intent.putExtra("pushups", act.pushups)
    setResult(Activity.RESULT_OK, intent)
    finish()
  }
}
