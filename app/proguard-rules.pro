# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\ThinkPad\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-dontpreverify
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Annotation
-keepattributes Signature
-keep class android.**{*;}
# -------------系统类不需要混淆 --------------------------
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.**
-keep public class com.android.vending.licensing.ILicensingService
#  不混淆个推推送sdk
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#  不混淆bugly sdk
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#  不混淆jedis sdk
-dontwarn redis.clients.**
-keep class redis.clients.**{*;}
#  不混淆baidu sdk
-keep class com.baidu.** {*;}
-dontwarn com.baidu.**
#  不混淆commons
-keep class org.apache.commons.** {*;}
-dontwarn org.apache.commons.**
#  不混淆sun
-keep class sun.misc.**{*;}
-dontwarn sun.misc.**
#  不混淆tablefix
-keep class tablefixheaders.TableFixHeaders
-dontwarn tablefixheaders.TableFixHeaders
#  不混淆
-dontwarn javax.annotation.**
-dontwarn jcifs.**
-keep class jcifs.** {*;}

-dontwarn jcifs.**
-keep class jcifs.** {*;}

-dontwarn jsqlite.**
-keep class jsqlite.** {*;}

-dontwarn com.esri.**
-keep class com.esri.** { *;}


-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *;}


-dontwarn org.codehaus.jackson.map.ext.**
-keep class org.codehaus.jackson.** {*;}

-dontwarn org.apache.commons.lang3.**
-keep class org.apache.commons.lang3.** { *;}

-dontwarn org.kobjects.**
-keep class org.kobjects.** { *;}

-keep class org.ksoap2.** { *;}
-dontwarn org.ksoap2.**


-dontwarn org.kxml2.**
-keep class org.kxml2.** { *;}

-dontwarn org.**
-keep class org.** { *;}

-keep class com.otitan.entity.** { *; } #实体类不参与混淆
-keep class com.otitan.customui.** { *; } #自定义控件不参与混淆


-dontwarn com.nostra13.**
-keep class com.nostra13.** { *;}

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

-keepclasseswithmembernames class * {
    #class_specification 不混淆类及其成员
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#  不混淆okhttp sdk
-dontwarn okio.**
-dontwarn okhttp3.**
#  不混淆retrofit2 sdk
-keep class com.squareup.retrofit2.** {*;}
-dontwarn rcom.squareup.retrofit2.**

#1.support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#2.rx
-dontwarn io.reactivex.**
-keep io.reactivex.**
-keepclassmembers class io.reactivex.** { *; }

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
#}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;

