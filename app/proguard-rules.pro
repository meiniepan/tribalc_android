# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\admin\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class companyName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn butterknife.**
-dontwarn com.alibaba.**
-dontwarn com.facebook.**
-dontwarn org.linphone.**
-dontwarn retrofit2.**
-dontwarn rx.**
-dontwarn okio.**
-keep class cn.finalteam.galleryfinal.widget.*{*;}
-keep class cn.finalteam.galleryfinal.widget.crop.*{*;}
-keep class cn.finalteam.galleryfinal.widget.zoonview.*{*;}
-keep class com.gs.buluo.app.view.widget.**{*;}
-keep class com.tencent.mm.**{*;}
-dontwarn com.sina.**
-keep class com.sina.**{*;}
-keep class com.baidu.**{*;}