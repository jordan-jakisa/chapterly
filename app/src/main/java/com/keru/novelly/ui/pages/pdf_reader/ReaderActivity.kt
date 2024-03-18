package com.keru.novelly.ui.pages.pdf_reader

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.keru.novelly.databinding.ActivityReaderBinding
import com.keru.novelly.di.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.io.File
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ReaderActivity : ComponentActivity() {

    private lateinit var binding: ActivityReaderBinding
    private lateinit var bookUriString: String

    private lateinit var prefs: DataStore<Preferences>
    private lateinit var currentPageKey: Preferences.Key<Int>
    private lateinit var file: File
    private val vm: ReaderViewModel by viewModels()

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookUriString = intent.getStringExtra("bookUri") ?: ""
        val fileUri = Uri.parse(bookUriString)
        file = File(fileUri.path!!)

        binding = ActivityReaderBinding.inflate(layoutInflater)
        prefs = this.dataStore
        currentPageKey = intPreferencesKey(file.name.toString())
        getPagePref()
        initViews()
        setContentView(binding.root)
    }

    private fun initViews() {
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
            }
            bookTitleText.text = Uri.decode(file.name)
            openPdf(currentPage)
        }
    }

    private fun openPdf(currentPage: Int) {
        try {
            if (!binding.pdfView.isRecycled) binding.pdfView.recycle()

            binding.pdfView

            binding.pdfView.fromFile(file).defaultPage(currentPage).enableDoubletap(true)
                .enableAnnotationRendering(true).spacing(16).enableAntialiasing(true)
                .onPageChange { page, pageCount ->
                    val cPage = page + 1
                    val progress = cPage / pageCount.toFloat()

                    Log.d("Prefs", "Progress: $progress")

                    this.currentPage = page
                    binding.pageNumberView.text = "${page + 1}/${pageCount}"

                    vm.updateBookProgress(file.name, progress.toString())
                    vm.updateBookProgress(file.name + "_page", page.toString())

                    file.setLastModified(System.currentTimeMillis())

                    /**
                     *When the user reaches the last page
                     *1. Show confetti
                     *2. Save book to users completed books
                     */

                    if (page + 1 == pageCount) {

                        val party = Party(
                            angle = 45,
                            speed = 0f,
                            maxSpeed = 30f,
                            damping = 0.9f,
                            spread = 360,
                            timeToLive = 5000,
                            rotation = Rotation.enabled(),
                            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                            position = Position.Relative(0.5, 0.3)
                        )
                        binding.konfettiView.start(party)
                        Toast.makeText(
                            this,
                            "Congratulations upon completing ${file.name}! 🎆",
                            Toast.LENGTH_LONG
                        ).show()

                        vm.addBookToCompleted(file.name)
                    }
                }.load()
        } catch (e: Exception) {
            Log.d("Prefs", "Error ==> ${e.message}")
        }
    }

    private fun getPagePref() {
        runBlocking {
            currentPage = vm.getPrefValue(file.name + "_page").toInt()
        }
    }
}