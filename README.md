# Moonlight Android

[![AppVeyor Build Status](https://ci.appveyor.com/api/projects/status/232a8tadrrn8jv0k/branch/master?svg=true)](https://ci.appveyor.com/project/cgutman/moonlight-android/branch/master)
[![Translation Status](https://hosted.weblate.org/widgets/moonlight/-/moonlight-android/svg-badge.svg)](https://hosted.weblate.org/projects/moonlight/moonlight-android/)

[Moonlight for Android](https://moonlight-stream.org) is an open source client for NVIDIA GameStream and [Sunshine](https://github.com/LizardByte/Sunshine).

## Moonlight WorkBuddy fork

此分支基于 Moonlight Android `b48494cb96bff23d8886c4775cc4f39a1075495d`（12.2），保留原项目的 GPL-3.0 许可。定制范围仅包括：

* Xiaomi Touch 双指滚动倍率与余数归一化
* WorkBuddy 三指拖动期间的实体触摸板输入 gate
* 可与官方 Moonlight 并存的独立应用包名

这些适配只针对已验证的 Xiaomi Touch，不宣称支持所有 Android 触摸板。

### WorkBuddy 构建与发布

GitHub Actions 会在普通提交和 Pull Request 上运行 NonRoot 单元测试并编译 Debug APK。Release 工作流也支持手动运行以验证正式签名构建，但只有推送与 `app/build.gradle` 中 `versionName` 完全一致的 `v*` 标签时才会创建 GitHub Release。

正式发布包含 `Moonlight-WorkBuddy-<version>.apk` 和 `SHA256SUMS.txt`。签名证书保存在 GitHub `release` Environment 中，并固定校验证书 SHA-256：`1427DD5A9FEA1B17A0D04EEBED6B510EF66F9DC8A830028D31BF5A446AB6044C`。`v12.2-workbuddy.1` 是最后一个本机构建版本，从下一个版本开始由 GitHub Actions 自动发布；已有标签和 Release 不允许覆盖。

维护新版本时，先提升 `versionName` 和 `versionCode`，等待分支 CI 通过，再创建并推送 `v${versionName}` 标签。CI 和 Pull Request 无权读取正式签名 Secrets。

Moonlight for Android will allow you to stream your full collection of games from your Windows PC to your Android device,
whether in your own home or over the internet.

Moonlight also has a [PC client](https://github.com/moonlight-stream/moonlight-qt) and [iOS/tvOS client](https://github.com/moonlight-stream/moonlight-ios).

You can follow development on our [Discord server](https://moonlight-stream.org/discord) and help translate Moonlight into your language on [Weblate](https://hosted.weblate.org/projects/moonlight/moonlight-android/).

## Downloads
* [Google Play Store](https://play.google.com/store/apps/details?id=com.limelight)
* [Amazon App Store](https://www.amazon.com/gp/product/B00JK4MFN2)
* [F-Droid](https://f-droid.org/packages/com.limelight)
* [APK](https://github.com/moonlight-stream/moonlight-android/releases)

## Building
* Install Android Studio and the Android NDK
* Run ‘git submodule update --init --recursive’ from within moonlight-android/
* In moonlight-android/, create a file called ‘local.properties’. Add an ‘ndk.dir=’ property to the local.properties file and set it equal to your NDK directory.
* Build the APK using Android Studio or gradle

## Authors

* [Cameron Gutman](https://github.com/cgutman)  
* [Diego Waxemberg](https://github.com/dwaxemberg)  
* [Aaron Neyer](https://github.com/Aaronneyer)  
* [Andrew Hennessy](https://github.com/yetanothername)

Moonlight is the work of students at [Case Western](http://case.edu) and was
started as a project at [MHacks](http://mhacks.org).
