package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.CancerEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.history.HistoryFactory
import com.dicoding.asclepius.view.history.HistoryViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyViewModel: HistoryViewModel
    private var isSaved = false
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = HistoryFactory.getInstance(this)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        // Mendapatkan data hasil prediksi dan URI gambar dari Intent
        val resultText: String? = intent.getStringExtra("RESULT_TEXT")
        imageUri = intent.getParcelableExtra("IMAGE_URI")

        // Set gambar dan teks hasil prediksi
        if (imageUri != null) {
            binding.resultImage.setImageURI(imageUri)
        } else {
            binding.resultImage.setImageResource(R.drawable.ic_place_holder) // Atur gambar placeholder jika imageUri null
        }

        resultText?.let {
            binding.resultText.text = it
        }

        // Aksi tombol Simpan
        binding.btnSave.setOnClickListener {
            if (!isSaved) {
                imageUri?.let { uri ->
                    resultText?.let { result ->
                        val cancerEntity = CancerEntity(
                            image = uri.toString(),
                            result = result
                        )

                        historyViewModel.insertCancers(listOf(cancerEntity))
                        isSaved = true

                        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

                        binding.btnSave.isEnabled = false
                        finish()
                    }
                } ?: run {
                    Toast.makeText(this, "Tidak ada gambar untuk disimpan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
