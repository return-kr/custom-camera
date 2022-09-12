# The library contains a BroadcastReceiver to check the connectivity with Internet #
## Use of the library ##
### Add library dependency to the app level build.gradle file. ###

```gradle
dependencies {
    // For cameraX library
    def camerax_version = "1.0.0-rc01"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-view:1.0.0-alpha20"
    // For Custom Camera
    implementation 'com.github.return-kr:custom-camera:$latest_stable_version'
}
```
### Add the following to the settings.gradle file. ###
```gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
### Initialize the Custom camera. ###
```kotlin
private var cameraTask: CustomCamera ? = null

cameraTask = CustomCamera(activity = this, lifecycleOwner = this, previewView = cameraView)
```
### Call the function to take photo by passing Directory name to it's parameter. ###
```kotlin

cameraTask?.takePhoto(directoryName = "CameraModuleTest")

```
### Set the listener to get the photo shoot callbacks. ###
```kotlin
cameraTask?.addOnCaptureListener(object : OnCaptureListener {
    override fun onCaptureSuccess(photoPath: String) {
        Log.d("addOnCaptureListener", "onCaptureSuccess: $photoPath")
    }

    override fun onCaptureFailure(e: Exception) {
        Log.d("addOnCaptureListener", "onCaptureFailure: ${e.message}")
    }
})
```
***End of Doc***