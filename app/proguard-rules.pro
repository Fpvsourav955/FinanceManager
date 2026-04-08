# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
##############################################
# 🔥 GENERAL RULES
##############################################

# Keep model classes (Room entities, Firebase models)
-keepclassmembers class * {
    @androidx.room.* <fields>;
}

-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep class * extends androidx.room.RoomDatabase

##############################################
# 🔥 FIREBASE AUTH / DATABASE
##############################################

-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

##############################################
# 🔥 GOOGLE SIGN-IN / PLAY SERVICES
##############################################

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

##############################################
# 🔥 GLIDE (VERY IMPORTANT)
##############################################

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule


##############################################
# 🔥 LOTTIE
##############################################

-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

##############################################
# 🔥 CIRCLE IMAGE VIEW
##############################################

-keep class de.hdodenhof.circleimageview.** { *; }

##############################################
# 🔥 ROOM DATABASE
##############################################

-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

##############################################
# 🔥 VIEW BINDING / DATA BINDING
##############################################

-keep class androidx.databinding.** { *; }
-dontwarn androidx.databinding.**

##############################################
# 🔥 RECYCLERVIEW / ADAPTERS
##############################################

-keep class * extends androidx.recyclerview.widget.RecyclerView$Adapter { *; }

##############################################
# 🔥 MATERIAL COMPONENTS
##############################################

-keep class com.google.android.material.** { *; }

##############################################
# 🔥 KEEP YOUR ACTIVITIES & FRAGMENTS
##############################################

-keep public class * extends android.app.Activity
-keep public class * extends androidx.fragment.app.Fragment

##############################################
# 🔥 KEEP MODELS (IMPORTANT)
##############################################

-keepclassmembers class com.sourav.financemanager.** {
    <fields>;
}

##############################################
# 🔥 PREVENT WARNINGS
##############################################

-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**