package com.bhaskar.customcamera

import android.app.Activity
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File

/**
 * This library binds custom camera and takes photo using it.
 *                                                  -- Bhaskar
 */
class CustomCamera(
    private val activity: Activity,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView
) {
    companion object {
        const val TAG = "CustomCamera"
    }

    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var preview: Preview? = null
    private var onCaptureListener: OnCaptureListener? = null

    /**
     * Photo capture callback listener
     */
    fun addOnCaptureListener(onCaptureListener: OnCaptureListener) {
        this.onCaptureListener = onCaptureListener
    }

    /**
     * Function to start and render camera on the view
     */
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(previewView.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        }, ContextCompat.getMainExecutor(activity))
    }

    /**
     * Function to take pictures, fire it according to your CTA
     */
    fun takePhoto(directoryName: String) {
        val fileName = "$directoryName-${System.currentTimeMillis()}.png"
        val outputDirectory = setOutputDirectory(directoryName)
        val photoFile = File(outputDirectory, fileName)
        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            output,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d(TAG, "onImageSaved: ${outputFileResults.savedUri}")
                    onCaptureListener?.onCaptureSuccess("$outputDirectory/$fileName")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d(TAG, "onError: ${exception.message}")
                    onCaptureListener?.onCaptureFailure(exception)
                }
            })
    }

    /**
     * To determine the picture file output directory or saving directory
     */
    private fun setOutputDirectory(directoryName: String): File {
        val directory = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, directoryName).apply { mkdirs() }
        }
        return if (directory != null && directory.exists())
            directory else activity.filesDir
    }
}