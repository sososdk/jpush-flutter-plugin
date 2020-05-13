-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.push.**{*;}

-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-dontwarn com.meizu.cloud.**
-keep class com.meizu.cloud.** { *; }

-dontwarn com.coloros.mcsdk.**
-keep class com.coloros.mcsdk.** { *; }
-dontwarn com.heytap.**
-keep class com.heytap.** { *; }
-dontwarn com.mcs.**
-keep class com.mcs.** { *; }

-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }