package com.example.pbpkel4_tubes

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.hardware.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat

class CameraHardware : AppCompatActivity(), SensorEventListener {
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_hardware)
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        } catch (e: Exception) {
            Log.d("Error", "Failed to get camera: " + e.message)
        }
        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissinfInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0) }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if(proximitySensor == null){
            Toast.makeText(this, "No proximity sensor found in device", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // method to check accuracy changed in sensor
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                mCamera?.stopPreview()
                mCamera?.release()

                if (event.values[0] == 0f) {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                }
                else {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
                }
                mCameraView = CameraView(this@CameraHardware, mCamera!!)
                val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                camera_view.addView(mCameraView)
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }
}