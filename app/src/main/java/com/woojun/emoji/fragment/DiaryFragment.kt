package com.woojun.emoji.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.woojun.emoji.R
import com.woojun.emoji.databinding.FragmentDiaryBinding
import com.woojun.emoji.util.AppDatabase
import com.woojun.emoji.util.Chat
import com.woojun.emoji.util.Diary
import com.woojun.emoji.util.DiaryAdapter
import com.woojun.emoji.util.EmotionViewModel
import com.woojun.emoji.util.MainViewModel
import com.woojun.emoji.util.MyApp
import com.woojun.emoji.util.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!
    private lateinit var viewModel2: EmotionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            viewModel2 = ViewModelProvider(requireActivity())[EmotionViewModel::class.java]
            red.setOnClickListener {
                drawingView.setColor("#FF0000")
            }
            yellow.setOnClickListener {
                drawingView.setColor("#FFD600")
            }
            green.setOnClickListener {
                drawingView.setColor("#58CF44")
            }
            black.setOnClickListener {
                drawingView.setColor("#000000")
            }
            plus.setOnClickListener {
                drawingView.setColor("#0000ff")
            }
            white.setOnClickListener {
                drawingView.setColor("#ffffff")
            }


            slider.addOnChangeListener { _, value, _ ->
                drawingView.setBrushSize(value)
            }

            button.setOnClickListener {
                val bitmap = drawingView.getCanvasBitmap()
                val data = viewModel2.getData()!!
                data.image = encodeBitmapToBase64(bitmap!!)

                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(requireContext())

                    val isFirst = MyApp.prefs.getString("isFirst", "o")

                    if (isFirst == "x") {
                        val user = db?.userDao()!!.getUser()
                        user.list.add(data)
                        db.userDao().updateUser(user)
                    } else {
                        db?.userDao()!!.insertUser(User(0, mutableListOf(data)))
                        MyApp.prefs.setString("isFirst", "x")
                    }
                }
                viewModel.resetData()
                viewModel2.resetData()

                val navController = findNavController()
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(navController.graph.startDestinationId, true)
                    .build()
                navController.navigate(R.id.chat, null, navOptions)

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun saveBitmapToDownloads(bitmap: Bitmap) {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val imageFile = File(downloadsPath, "a")

        if (!downloadsPath.exists()) {
            downloadsPath.mkdirs()
        }

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // Bitmap을 JPG 형식으로 압축하여 저장
        } catch (e: IOException) {
            e.printStackTrace()
            // 예외 처리 로직
        } finally {
            try {
                out?.flush()
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                // 예외 처리 로직
            }
        }
    }

}