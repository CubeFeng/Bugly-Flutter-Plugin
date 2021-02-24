package com.tencent.bugly.bugly_crash;

import android.content.Context;

import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * Description:bugly oa futter plugin
 *
 * @author rockypzhang
 * @since 2019/5/28
 */
public class BuglyCrashPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    private static Context mContext;

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "bugly");
        mContext = registrar.activeContext();
        channel.setMethodCallHandler(new BuglyCrashPlugin());
    }

    /**
     * flutter层调用方法的native层实现
     *
     * @param call   flutter方法回调
     * @param result 返回结果
     */
    @Override
    public void onMethodCall(MethodCall call, Result result) {
        String methodName = call.method;
        switch (methodName) {
            case "getPlatformVersion":
                getPlatformVersion(call, result);
                break;
            case "initCrashReport":
                initCrashReport(call, result);
                break;
            case "postException":
                postException(call, result);
                break;
            case "setAppChannel":
                setAppChannel(call, result);
                break;
            case "setAppPackage":
                setAppPackage(call, result);
                break;
            case "setAppVersion":
                setAppVersion(call, result);
                break;
            case "setUserSceneTag":
                setUserSceneTag(call, result);
                break;
            case "setUserId":
                setUserId(call, result);
                break;
            case "putUserData":
                putUserData(call, result);
                break;
            case "setIsDevelopmentDevice":
                setIsDevelopmentDevice(call, result);
                break;
            default: {
                if (methodName.contains("log")) {
                    buglyLog(call);
                } else {
                    result.notImplemented();
                }
                break;
            }
        }
    }

    private void getPlatformVersion(MethodCall call, Result result) {
        result.success("Android " + android.os.Build.VERSION.RELEASE);
    }

    private void setIsDevelopmentDevice(MethodCall call, Result result) {
        boolean isDevelopmentDevice = false;
        if (call.hasArgument("isDevelopmentDevice")) {
            isDevelopmentDevice = call.argument("isDevelopmentDevice");
        }
        CrashReport.setIsDevelopmentDevice(mContext, isDevelopmentDevice);
        BuglyCrashPluginLog.d("isDevelopmentDevice:" + isDevelopmentDevice);
    }

    private void putUserData(MethodCall call, Result result) {
        String userKey = "";
        String userValue = "";
        if (call.hasArgument("userKey")) {
            userKey = call.argument("userKey");
        }
        if (call.hasArgument("userValue")) {
            userValue = call.argument("userValue");
        }
        CrashReport.putUserData(mContext, userKey, userValue);
        BuglyCrashPluginLog.d("userKey:" + userKey + " userValue:" + userValue);
    }

    private void setUserId(MethodCall call, Result result) {
        int userSceneTag = 0;
        String userId = "";
        if (call.hasArgument("userId")) {
            userId = call.argument("userId");
        }
        CrashReport.setUserId(mContext, userId);
        BuglyCrashPluginLog.d("mContext:" + mContext + " appVersion:" + userSceneTag);
    }

    private void setUserSceneTag(MethodCall call, Result result) {
        int userSceneTag = 0;
        if (call.hasArgument("userSceneTag")) {
            userSceneTag = call.argument("userSceneTag");
        }
        CrashReport.setUserSceneTag(mContext, userSceneTag);
        BuglyCrashPluginLog.d("mContext:" + mContext + " appVersion:" + userSceneTag);
    }

    private void setAppVersion(MethodCall call, Result result) {
        String appVersion = "";
        if (call.hasArgument("appVersion")) {
            appVersion = call.argument("appVersion");
        }
        CrashReport.setAppVersion(mContext, appVersion);
        BuglyCrashPluginLog.d("mContext:" + mContext + " appVersion:" + appVersion);
    }

    private void initCrashReport(MethodCall call, Result result) {
        String appId = "";
        boolean isDebug = false;
        if (call.hasArgument("appId")) {
            appId = call.argument("appId");
        }
        if (call.hasArgument("isDebug")) {
            isDebug = call.argument("isDebug");
            BuglyCrashPluginLog.isEnable = isDebug;
        }
        CrashReport.initCrashReport(mContext, appId, isDebug);
        BuglyCrashPluginLog.d("onMethodCall initCrashReport");
    }

    private void postException(MethodCall call, Result result) {
        String type = "";
        String error = "";
        String stackTrace = "";
        Map<String, String> extraInfo = null;
        if (call.hasArgument("type")) {
            type = call.argument("type");
        }
        if (call.hasArgument("error")) {
            error = call.argument("error");
        }
        if (call.hasArgument("stackTrace")) {
            stackTrace = call.argument("stackTrace");
        }
        if (call.hasArgument("extraInfo")) {
            extraInfo = call.argument("extraInfo");
        }
        BuglyCrashPluginLog.d("type:" + type + "error:" + error + " stackTrace:" + stackTrace
                + "extraInfo:" + extraInfo);
        int category = 8;
        CrashReport.postException(category, type, error, stackTrace, extraInfo);
    }

    private void setAppChannel(MethodCall call, Result result) {
        String appChannel = "";
        if (call.hasArgument("appChannel")) {
            appChannel = call.argument("appChannel");
        }
        CrashReport.setAppChannel(mContext, appChannel);
    }

    private void setAppPackage(MethodCall call, Result result) {
        String appPackage = "";
        if (call.hasArgument("appPackage")) {
            appPackage = call.argument("appPackage");
        }
        CrashReport.setAppPackage(mContext, appPackage);
    }

    /**
     * 打印上报用户自定义日志
     *
     * @param call    flutter方法回调
     */
    private void buglyLog(MethodCall call) {
        String tag = "";
        String content = "";
        if (call.hasArgument("tag")) {
            tag = call.argument("tag");
        }
        if (call.hasArgument("content")) {
            content = call.argument("content");
        }

        if (call.method.equals("logd")) {
            BuglyLog.d(tag, content);
        } else if (call.method.equals("logi")) {
            BuglyLog.i(tag, content);
        } else if (call.method.equals("logv")) {
            BuglyLog.v(tag, content);
        } else if (call.method.equals("logw")) {
            BuglyLog.w(tag, content);
        } else if (call.method.equals("loge")) {
            BuglyLog.e(tag, content);
        }
        BuglyCrashPluginLog.d("tag:" + tag + " content:" + content);
    }
}
