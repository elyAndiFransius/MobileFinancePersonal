package com.example.personalfinancemobile.activity.target

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.NumberFormat
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import com.example.personalfinancemobile.app.ui.utils.setupBackButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InputTargetActivity : AppCompatActivity() {

    private val PICK_FILE_REQUEST_CODE = 1001

    private lateinit var fileEditText: EditText
    private lateinit var btn_save: Button
    private lateinit var gol: EditText
    private lateinit var targetAmount: EditText
    private lateinit var currentAmount: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input_target)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_save = findViewById(R.id.id_btn_save)
        gol = findViewById(R.id.id_gol)
        targetAmount = findViewById(R.id.id_amount)
        currentAmount = findViewById(R.id.id_belance)
        startDate = findViewById(R.id.id_start)
        endDate = findViewById(R.id.id_end)
        fileEditText = findViewById(R.id.id_file)
        val backButton = findViewById<ImageView>(R.id.id_back)
        setupBackButton(this, backButton)
        val belanceEditText = findViewById<EditText>(R.id.id_belance)
        belanceEditText.addTextChangedListener(NumberFormat(belanceEditText))

        val AmountEditText = findViewById<EditText>(R.id.id_amount)
        AmountEditText.addTextChangedListener(NumberFormat(AmountEditText))

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        startDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                startDate.setText(dateFormat.format(selectedDate.time))
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        endDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                endDate.setText(dateFormat.format(selectedDate.time))
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        fileEditText.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Pilih File"), PICK_FILE_REQUEST_CODE)
        }

        btn_save.setOnClickListener {
            val golString = gol.text.toString()
            val targetAmountString = targetAmount.text.toString()
            val currentAmountString = currentAmount.text.toString()
            val startDateString = startDate.text.toString()
            val endDateString = endDate.text.toString()
            val targetAmountInt = targetAmountString.toIntOrNull()
            val currentAmountInt = currentAmountString.toIntOrNull()

            val startDateParsed = try {
                dateFormat.parse(startDateString)
            } catch (e: Exception) {
                null
            }

            val endDateParsed = try {
                dateFormat.parse(endDateString)
            } catch (e: Exception) {
                null
            }

            fun createPartFromString(value: String): RequestBody {
                return value.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            if (targetAmountInt != null && currentAmountInt != null && startDateParsed != null && endDateParsed != null) {
                val golPart = createPartFromString(golString)
                val targetAmountPart = createPartFromString(targetAmountInt.toString())
                val currentAmountPart = createPartFromString(currentAmountInt.toString())
                val startDatePart = createPartFromString(startDateString)
                val endDatePart = createPartFromString(endDateString)


                var filePart: MultipartBody.Part? = null

                selectedFileUri?.let { uri ->

                    // penggecekan untuk melihat format yang kirimkan
                    val mimeType = contentResolver.getType(uri)
                    if (mimeType != null && mimeType.startsWith("image/")) {
                        // Ini gambar, resezi dulu
                    } else {
                        Toast.makeText(this@InputTargetActivity, "Silahkan pilih file berupa gambar", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                    // Resize bitmap, misal max width = 1024
                    val resizedBitmap = resizeBitmap(bitmap, 379, 263)

                    // Kompres ke JPEG dengan kualitas 80%
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val fileBytes = byteArrayOutputStream.toByteArray()

                    val fileName = getFileNameFromUri(this, uri)

                    val requestFile = fileBytes?.toRequestBody(
                        contentResolver.getType(uri)?.toMediaTypeOrNull()
                    ) ?: run {
                        Toast.makeText(this, "Gagal membaca file", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    filePart = MultipartBody.Part.createFormData("file", fileName, requestFile)

                    Log.d("InputCheck", "imageUri: $filePart")
                    Log.d("InputCheck", "targetAmount: ${targetAmount.text}")
                    Log.d("InputCheck", "currentAmount: ${currentAmount.text}")
                    Log.d("InputCheck", "startDate: ${startDate.text}")
                    Log.d("InputCheck", "endDate: ${endDate.text}")
                }
                val sessionManager = SessionManager(this)
                val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
                val token = sessionManager.fetchAuthToken()
                apiService.createTarget(
                    golPart, targetAmountPart, currentAmountPart, startDatePart, endDatePart, filePart, "Bearer $token"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@InputTargetActivity, "Target anda berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("TargetError", "Target gagal dibuat: $errorBody")
                            Toast.makeText(this@InputTargetActivity, "Coba lagi", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@InputTargetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("TargetError", "Throwable: ${t.message}")
                    }
                })
            } else {

                Toast.makeText(this@InputTargetActivity, "Mohon isi semua inputan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                selectedFileUri = uri
                val fileName = getFileNameFromUri(this, uri)
                fileEditText.setText(fileName)
            }
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown_file"
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this@InputTargetActivity, BerhasilMencatatProgresActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val widthRatio = maxWidth.toFloat() / width
        val heightRatio = maxHeight.toFloat() / height
        val ratio = minOf(widthRatio, heightRatio)

        // Hitung ukuran baru dengan menjaga aspek rasio
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


}
