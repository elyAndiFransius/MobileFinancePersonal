package com.example.personalfinancemobile.activity.Auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.otpResetVerificationActivity
import com.example.personalfinancemobile.app.data.model.OTPResponse
import com.example.personalfinancemobile.app.data.model.SentOTPResponse
import com.example.personalfinancemobile.app.data.model.Validasi
import com.example.personalfinancemobile.app.data.model.sentOTP
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.databinding.ActivityOtpVerificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otpRegisVerificationActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private lateinit var timerText: TextView
    private lateinit var binding: ActivityOtpVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOtpInputs()
        startCountdownTimer()

        val email = intent.getStringExtra("email") ?: ""

        binding.btnSave.setOnClickListener {
            val kodeOtp = getOtpInput()
            if (kodeOtp.length < 6) {
                Toast.makeText(this, "Lengkapi kode OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = Validasi(email, kodeOtp)
            RetrofitInstance.getInstance(this).create(APIServices::class.java)
                .validasiOTP(data).enqueue(object : Callback<OTPResponse> {
                    override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@otpRegisVerificationActivity, "Verifikasi berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@otpRegisVerificationActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@otpRegisVerificationActivity, "OTP Salah", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                        Toast.makeText(this@otpRegisVerificationActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.reset.setOnClickListener {
            val data = sentOTP(email)
            RetrofitInstance.getInstance(this).create(APIServices::class.java)
                .sendOTP(data).enqueue(object : Callback<SentOTPResponse> {
                    override fun onResponse(call: Call<SentOTPResponse>, response: Response<SentOTPResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@otpRegisVerificationActivity, "Kode baru dikirim", Toast.LENGTH_SHORT).show()
                            clearOtpInputs()
                            startCountdownTimer()
                        } else {
                            Toast.makeText(this@otpRegisVerificationActivity, response.body()?.message ?: "Tunggu 30 detik!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SentOTPResponse>, t: Throwable) {
                        Toast.makeText(this@otpRegisVerificationActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun getOtpInput(): String {
        return listOf(
            binding.otp1.text.toString(),
            binding.otp2.text.toString(),
            binding.otp3.text.toString(),
            binding.otp4.text.toString(),
            binding.otp5.text.toString(),
            binding.otp6.text.toString()
        ).joinToString("")
    }

    private fun clearOtpInputs() {
        binding.otp1.setText("")
        binding.otp2.setText("")
        binding.otp3.setText("")
        binding.otp4.setText("")
        binding.otp5.setText("")
        binding.otp6.setText("")
        binding.otp1.requestFocus()
    }

    private fun setupOtpInputs() {
        val fields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4, binding.otp5, binding.otp6)
        for (i in fields.indices) {
            fields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < fields.size - 1) {
                        fields[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        fields[i - 1].requestFocus()
                    }
                }
            })
        }
    }

    private fun startCountdownTimer() {
        timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.timerText.text = "00:00"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}