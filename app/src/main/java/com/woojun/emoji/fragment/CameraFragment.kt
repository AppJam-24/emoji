package com.woojun.emoji.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.woojun.emoji.R
import com.woojun.emoji.databinding.FragmentCameraBinding
import com.woojun.emoji.util.EmotionResult
import com.woojun.emoji.util.EmotionViewModel
import com.woojun.emoji.util.ImageData
import com.woojun.emoji.util.MainViewModel
import com.woojun.emoji.util.RetrofitAPI
import com.woojun.emoji.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModel2: EmotionViewModel
    private var check = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            camera.setLifecycleOwner(this@CameraFragment) // cameraView 기본 설정
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            viewModel2 = ViewModelProvider(requireActivity())[EmotionViewModel::class.java]

            camera.toggleFacing()

            camera.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    result.toBitmap { bitmap ->
                        if (bitmap != null) {
                            val base64Image = encodeBitmapToBase64(bitmap)
                            val imageData = ImageData(base64Image)

                            val retrofit = RetrofitClient.getInstance()
                            val apiService = retrofit.create(RetrofitAPI::class.java)

                            apiService.uploadImage(imageData).enqueue(object :
                                Callback<EmotionResult> {
                                override fun onResponse(call: Call<EmotionResult>, response: Response<EmotionResult>) {
                                    if (response.isSuccessful) {
                                        viewModel.onPhotoTaken()
                                        viewModel2.setEmotion(response.body()!!.emotion)

                                        findNavController().navigate(R.id.chat)
                                    }
                                }

                                override fun onFailure(call: Call<EmotionResult>, t: Throwable) {
                                    Toast.makeText(requireContext(), "다시 해주세요", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }

                override fun onVideoTaken(result: VideoResult) {
                }

                override fun onVideoRecordingEnd() {
                    super.onVideoRecordingEnd()
                }

                override fun onPictureShutter() {
                }

                override fun onVideoRecordingStart() {
                }
            })

            captureBtn.setOnClickListener {
                if (check) {
                    camera.takePicture()
                    check = false
                    Toast.makeText(requireContext(), "처리 중이니 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}