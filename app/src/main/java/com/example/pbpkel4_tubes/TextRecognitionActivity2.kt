package com.example.pbpkel4_tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pbpkel4_tubes.databinding.ActivityTextRecognition2Binding
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.pbpkel4_tubes.databinding.ActivityAddEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.Text
import com.google.android.gms.vision.text.TextRecognizer

class TextRecognitionActivity2 : AppCompatActivity() {
    var FOTO_URI : Uri? = null
    var bitmap : Bitmap? = null

    private lateinit var binding: ActivityTextRecognition2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognition2Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_text_recognition2)
        setContentView(binding.root)

        binding.btnPicture.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .start()
        }
        binding.btnCopy.setOnClickListener {
            copyToClipBoard(binding.resultText.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE && data != null) {
            FOTO_URI = data.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, FOTO_URI)
            binding.imageView.visibility = View.GONE
            binding.textView.visibility = View.GONE
            getTextFromImage(bitmap!!)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Take Picture Cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTextFromImage(bitmap: Bitmap) {
        val recognizer = TextRecognizer.Builder(this@TextRecognitionActivity2).build()
        if(!recognizer.isOperational) {
            Toast.makeText(this, "Failed Load the text", Toast.LENGTH_SHORT).show()
        } else {
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlockSparseArray = recognizer.detect(frame)
            val stringBuilder = StringBuilder()
            for (i in 0 until textBlockSparseArray.size()){
                val textBlock = textBlockSparseArray.valueAt(i)
                stringBuilder.append(textBlock.value)
                stringBuilder.append("\n")
            }
            binding.resultText.setText(stringBuilder.toString())
        }
    }

    private fun copyToClipBoard(resultText: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Data", resultText)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do You Want to exit?")
        builder.setTitle("Alert!")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener{dialog, which -> super.onBackPressed()})
        builder.setNegativeButton("No", DialogInterface.OnClickListener{dialog, which -> })
        builder.show()
    }
}