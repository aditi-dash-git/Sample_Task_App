# Sample Task App 🎤📸📝  
A Kotlin Multiplatform + Jetpack Compose prototype for speech & media-based task recording.

This application guides the user through three sample task activities:

1️⃣ Text Reading & Audio Recording  
2️⃣ Image Description Task  
3️⃣ Photo Capture Task  

All completed tasks are saved locally and displayed in a Task History.

---

## ✨ Features

### 🔊 Noise Test
✔ Ambient sound check using microphone  
✔ dB meter animation  
✔ Allows continuation only if noise level < 40 dB  

### 🗣 Text Reading Task
✔ Shows a sample text from API *(static placeholder in prototype)*  
✔ Hold Mic button → Record Audio  
✔ Red mic during recording  
✔ Validation (10–20 seconds limit)  
✔ Playback controls + progress bar  
✔ User quality checks with checkboxes  
✔ Saves task locally on submit  

### 🖼 Image Description Task
✔ Displays a sample image  
✔ User records description audio (10–20s validation)  
✔ Stores audio + image thumbnail in history  

### 📷 Photo Capture Task
✔ Camera access  
✔ Image capture preview  
✔ Optional recorded voice description  
✔ Saves both image + audio  

### 📂 Task History
✔ Displays all completed tasks  
✔ Shows task type, duration & timestamp  
✔ Summary: total tasks + total recording duration  

---

## 🛠️ Tech Stack

| Component | Technology |
|----------|------------|
| UI | Jetpack Compose Material 3 |
| Architecture | Compose Navigation (Simple State) |
| Audio | MediaRecorder + MediaPlayer |
| Image Loading | Coil |
| Camera | System Camera Intent |
| Storage | Local In-App Repository (In-Memory) |

---

## 📲 Permissions Used
android.permission.RECORD_AUDIO

android.permission.CAMERA

android.permission.READ_EXTERNAL_STORAGE

android.permission.WRITE_EXTERNAL_STORAGE


---

## 👇 Screens Included
| Screen | Description |
|--------|-------------|
| Start Screen | Entry to Sample Task |
| Noise Test Screen | Microphone dB meter |
| Task Selection Screen | Choose task type |
| Text Reading Screen | Record & validate speech |
| Image Description Screen | Speak about image |
| Photo Capture Screen | Capture & describe image |
| Task History Screen | View completed tasks |

---

## 🚀 How to Run


Clone → Open in Android Studio → Build & Run on Android 8.0+


APK: *https://github.com/aditi-dash-git/Sample_Task_App/blob/master/SampleTaskApp.apk*  
GitHub Repo: *https://github.com/aditi-dash-git/Sample_Task_App*

---

## 🎓 Submission Requirement Status

| Requirement | Status |
|------------|--------|
| KMM + Compose prototype | ✅ Completed |
| Noise test | ✅ Completed |
| 3 task flows (Text/Image/Photo) | ✅ Completed |
| Local task history list | ✅ Completed |
| UI similar to reference | 🔵 90% matched |
| APK + GitHub Public Repo | ✅ Completed |

---

## 👤 Developer
**Aditi Dash**  
B.Tech CSE!

---

📌 *Feel free to fork and extend this app with real backend storage & KMM shared logic.*

---

✨ Thank you for viewing this project!
