# Proguard rules for NoteVibe
-keepattributes *Annotation*
-dontwarn java.lang.invoke.**
-keepclassmembers class * {
    @androidx.room.Dao *;
    @androidx.room.Database *;
    @androidx.room.Entity *;
}
