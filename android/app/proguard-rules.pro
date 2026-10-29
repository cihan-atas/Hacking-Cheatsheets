# Hacking Cheatsheets Proguard rules
-keep class com.hackingcheatsheets.app.data.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
