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

# 百度
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }

#  不混淆okhttp sdk
-dontwarn okio.**
-dontwarn okhttp3.**

#  不混淆retrofit2 sdk
-keep class com.squareup.retrofit2.** {*;}
-dontwarn rcom.squareup.retrofit2.**


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
#2.rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
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

