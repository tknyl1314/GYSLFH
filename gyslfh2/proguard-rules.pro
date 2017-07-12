# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/whs/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

# 个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

# arcgis

-dontwarn com.esri.arcgisruntime.**
-keep class com.esri.arcgisruntime.** { *; }

-dontwarn org.simpleframework.xml.stream.**


# 百度
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }

#  不混淆okhttp sdk
-dontwarn okio.**
-dontwarn okhttp3.**

#  不混淆retrofit2 sdk
-keep class com.squareup.retrofit2.** {*;}
-dontwarn rcom.squareup.retrofit2.**


-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

#  不混淆个推推送sdk
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#  不混淆bugly sdk
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#  不混淆jedis sdk
-dontwarn redis.clients.**
-keep class redis.clients.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *;}
-keep class org.greenrobot.** { *; }

-dontwarn com.facebook.**
-keep class com.facebook.** { *;}

-dontwarn org.simpleframework.xml.stream.**
-keep class org.simpleframework.** { *;}

-dontwarn com.simpleframework.xml.stream.**
-keep class org.simpleframework.** { *;}

-dontwarn cn.finalteam.rxgalleryfinal.**
-keep class cn.finalteam.rxgalleryfinal.** { *;}
#1.support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#2.rx
#-dontwarn io.reactivex.**
#-keep io.reactivex.**
#-keepclassmembers class io.reactivex.** { *; }

#3.retrolambda
-dontwarn java.lang.invoke.*

#4.support-v4
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

#5.ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#6.photoview
-keep class uk.co.senab.photoview** { *; }
-keep interface uk.co.senab.photoview** { *; }

#7.rxgalleryfinal
-keep class cn.finalteam.rxgalleryfinal.ui.widget** { *; }

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes *Annotation*
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

