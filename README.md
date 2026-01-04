# 📝 CloudNotes

A real-time note-taking application built with **Kotlin**, **Jetpack Compose**, and **Firebase**.

This project demonstrates a production-grade implementation of **CRUD operations** (Create, Read, Update, Delete) in a Cloud-First architecture. It features secure user authentication and instant data synchronization across devices using Firestore listeners.

## 📱 Features
* **User Authentication:** Secure Sign Up & Login (Firebase Auth).
* **Full CRUD:** Users can Create, Read, Update, and Delete notes.
* **Real-Time Sync:** Changes made on one device appear instantly on others.
* **Reactive UI:** Built with Jetpack Compose and MVVM architecture.

## 🛠️ Tech Stack
* **Language:** Kotlin
* **UI:** Jetpack Compose (Material3)
* **Backend:** Firebase Firestore (NoSQL) & Authentication
* **Architecture:** MVVM with Repository Pattern
* **Concurrency:** Coroutines & Flow (`callbackFlow`, `StateFlow`)
