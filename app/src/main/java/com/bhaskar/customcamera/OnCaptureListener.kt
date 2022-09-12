package com.bhaskar.customcamera

interface OnCaptureListener {
    fun onCaptureSuccess(photoPath: String)
    fun onCaptureFailure(e: Exception)
}