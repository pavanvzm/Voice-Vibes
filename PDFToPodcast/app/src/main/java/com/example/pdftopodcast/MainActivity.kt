package com.example.pdftopodcast

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.shockwave.pdfium.PdfiumCore
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var selectPdfButton: Button
    private lateinit var selectedPdfTextView: TextView
    private lateinit var playButton: Button
    private lateinit var stopButton: Button

    private var selectedPdfUri: Uri? = null
    private var extractedText: String? = null
    private lateinit var tts: TextToSpeech

    private val selectPdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                selectedPdfUri = uri
                selectedPdfTextView.text = "Selected PDF: ${uri.lastPathSegment}"
                extractTextFromPdf(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectPdfButton = findViewById(R.id.selectPdfButton)
        selectedPdfTextView = findViewById(R.id.selectedPdfTextView)
        playButton = findViewById(R.id.playButton)
        stopButton = findViewById(R.id.stopButton)

        tts = TextToSpeech(this, this)

        selectPdfButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            selectPdfLauncher.launch(intent)
        }

        playButton.setOnClickListener {
            extractedText?.let { text ->
                speak(text)
            }
        }

        stopButton.setOnClickListener {
            if (::tts.isInitialized) {
                tts.stop()
            }
        }
    }

    private fun extractTextFromPdf(uri: Uri) {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            val pdfiumCore = PdfiumCore(this)
            val pdfDocument = pdfiumCore.newDocument(parcelFileDescriptor)
            val pageCount = pdfiumCore.getPageCount(pdfDocument)
            val text = StringBuilder()
            for (i in 0 until pageCount) {
                pdfiumCore.loadPage(pdfDocument, i)
                text.append(pdfiumCore.getPageText(pdfDocument, i))
            }
            extractedText = text.toString()
            pdfiumCore.closeDocument(pdfDocument)
            selectedPdfTextView.text = "PDF text extracted. Length: ${extractedText?.length}"
        } catch (e: IOException) {
            e.printStackTrace()
            selectedPdfTextView.text = "Error extracting text from PDF."
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                selectedPdfTextView.text = "TTS language not supported."
            } else {
                playButton.isEnabled = true
                stopButton.isEnabled = true
            }
        } else {
            selectedPdfTextView.text = "TTS initialization failed."
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
