package com.example.androidmodel.tools;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author kfflso
 * @data 2025-02-12 14:26
 * @plus:
 */
public class SilenceInstallUtils {
    private final Context context;

    public SilenceInstallUtils(Context context) {
        this.context = context;
    }

    private interface PackageListener {
        void onInstall(String msg, boolean success);

        void onUninstall(String msg, boolean success);
    }
    public abstract static class PackageInstallListener implements PackageListener {
        @Override
        public void onInstall(String msg, boolean success) {
        }

        @Override
        public void onUninstall(String msg, boolean success) {
        }
    }
    private static class InstallFlags {
        public static final int INSTALL_REPLACE_EXISTING = 0x00000002;

        /**
         * Flag parameter for {@link #installPackage} to indicate that you want to
         * allow test packages (those that have set android:testOnly in their
         * manifest) to be installed.
         *
         * @hide
         */
        public static final int INSTALL_ALLOW_TEST = 0x00000004;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package
         * must be installed to internal storage.
         *
         * @hide
         */
        public static final int INSTALL_INTERNAL = 0x00000010;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this install
         * was initiated via ADB.
         *
         * @hide
         */
        public static final int INSTALL_FROM_ADB = 0x00000020;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this install
         * should immediately be visible to all users.
         *
         * @hide
         */
        public static final int INSTALL_ALL_USERS = 0x00000040;

        /**
         * Flag parameter for {@link #installPackage} to indicate that an upgrade to a lower version
         * of a package than currently installed has been requested.
         *
         * <p>Note that this flag doesn't guarantee that downgrade will be performed. That decision
         * depends
         * on whenever:
         * <ul>
         * <li>An app is debuggable.
         * <li>Or a build is debuggable.
         * <li>Or {@link #INSTALL_ALLOW_DOWNGRADE} is set.
         * </ul>
         *
         * @hide
         */
        public static final int INSTALL_REQUEST_DOWNGRADE = 0x00000080;

        /**
         * Flag parameter for {@link #installPackage} to indicate that all runtime
         * permissions should be granted to the package. If {@link #INSTALL_ALL_USERS}
         * is set the runtime permissions will be granted to all users, otherwise
         * only to the owner.
         *
         * @hide
         */
        public static final int INSTALL_GRANT_RUNTIME_PERMISSIONS = 0x00000100;

        /**
         * Flag parameter for {@link #installPackage} to indicate that all restricted
         * permissions should be whitelisted. If {@link #INSTALL_ALL_USERS}
         * is set the restricted permissions will be whitelisted for all users, otherwise
         * only to the owner.
         *
         * <p>
         * <strong>Note: </strong>In retrospect it would have been preferred to use
         * more inclusive terminology when naming this API. Similar APIs added will
         * refrain from using the term "whitelist".
         * </p>
         *
         * @hide
         */
        public static final int INSTALL_ALL_WHITELIST_RESTRICTED_PERMISSIONS = 0x00400000;

        /**
         * {@hide}
         */
        public static final int INSTALL_FORCE_VOLUME_UUID = 0x00000200;

        /**
         * Flag parameter for {@link #installPackage} to indicate that we always want to force
         * the prompt for permission approval. This overrides any special behaviour for internal
         * components.
         *
         * @hide
         */
        public static final int INSTALL_FORCE_PERMISSION_PROMPT = 0x00000400;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package is
         * to be installed as a lightweight "ephemeral" app.
         *
         * @hide
         */
        public static final int INSTALL_INSTANT_APP = 0x00000800;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package contains
         * a feature split to an existing application and the existing application should not
         * be killed during the installation process.
         *
         * @hide
         */
        public static final int INSTALL_DONT_KILL_APP = 0x00001000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package is
         * to be installed as a heavy weight app. This is fundamentally the opposite of
         * {@link #INSTALL_INSTANT_APP}.
         *
         * @hide
         */
        public static final int INSTALL_FULL_APP = 0x00004000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package
         * is critical to system health or security, meaning the system should use
         * internally.
         *
         * @hide
         */
        public static final int INSTALL_ALLOCATE_AGGRESSIVE = 0x00008000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package
         * is a virtual preload.
         *
         * @hide
         */
        public static final int INSTALL_VIRTUAL_PRELOAD = 0x00010000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package
         * is an APEX package
         *
         * @hide
         */
        public static final int INSTALL_APEX = 0x00020000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that rollback
         * should be enabled for this install.
         *
         * @hide
         */
        public static final int INSTALL_ENABLE_ROLLBACK = 0x00040000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that package verification should be
         * disabled for this package.
         *
         * @hide
         */
        public static final int INSTALL_DISABLE_VERIFICATION = 0x00080000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that
         * {@link #INSTALL_REQUEST_DOWNGRADE} should be allowed.
         *
         * @hide
         */
        public static final int INSTALL_ALLOW_DOWNGRADE = 0x00100000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that this package
         * is being installed as part of a staged install.
         *
         * @hide
         */
        public static final int INSTALL_STAGED = 0x00200000;

        /**
         * Flag parameter for {@link #installPackage} to indicate that check whether given APEX can be
         * updated should be disabled for this install.
         *
         * @hide
         */
        public static final int INSTALL_DISABLE_ALLOWED_APEX_UPDATE_CHECK = 0x00400000;
    }

    private static class LocalIntentReceiver {
        private final SynchronousQueue<Intent> mResult = new SynchronousQueue<>();

        private IIntentSender.Stub mLocalSender = new IIntentSender.Stub() {
            @Override
            public void send(int code, Intent intent, String resolvedType, IBinder whitelistToken,
                             IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) {
                try {
                    mResult.offer(intent, 5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        public IntentSender getIntentSender() {
            final Constructor<IntentSender> constructor;
            try {
                constructor = IntentSender.class.getConstructor(IBinder.class);
                constructor.setAccessible(true);
                final IntentSender intentSender = constructor.newInstance((IIntentSender) mLocalSender);
                return intentSender;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public Intent getResult() {
            try {
                return mResult.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * android9及以上，使用,这是一个同步的方法
     * <!-- 应用卸载权限 -->s
     * <uses-permission android:name="android.permission.DELETE_PACKAGES" />
     * <!-- 应用安装权限 -->
     * <uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     * <p>
     * <uses-permission android:name="android.permission.REPLACE_EXISTING_PACKAGE" />
     * <p>
     * 需要系统权限
     * <p>
     * 主要通过系统类{@link android.content.pm.PackageInstaller}来进行静默安装
     */
    public boolean installPackage(String apkFilePath, PackageInstallListener mListener) {
        PackageInstaller.Session session;
        try {
            PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            // 覆盖安装,关闭安装验证,允许测试应用安装
            final int flags = InstallFlags.INSTALL_REPLACE_EXISTING | InstallFlags.INSTALL_DISABLE_VERIFICATION | InstallFlags.INSTALL_ALLOW_TEST | PackageManager.INSTALL_GRANT_RUNTIME_PERMISSIONS;
            // 自动授予运行时权限 PackageManager.INSTALL_GRANT_RUNTIME_PERMISSIONS
            setInstallFlags(params, flags);
            // 伪造安装来源为包管理器
            params.setInstallerPackageName("com.android.packageinstaller");
            params.setPackageSource(PackageInstaller.PACKAGE_SOURCE_STORE);
            // 创建安装会话
            int sessionId = packageInstaller.createSession(params);
            session = packageInstaller.openSession(sessionId);
            // 打开 APK 文件以供读取
            File apkFile = new File(apkFilePath);
            if (!apkFile.exists()) {
//                Global.log_d(TAG, "APK文件不存在:" + apkFilePath);
                return false;
            }
            InputStream in = new FileInputStream(apkFile);
            //获取输出流，用于将apk写入session
            OutputStream out = session.openWrite("app", 0, apkFile.length());
            //读取apk文件写入session
            byte[] buffer = new byte[65536];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            session.fsync(out);
            //写完需要关闭流，否则会抛异常“files still open”
            out.close();
            in.close();
            // 提交安装会话
            LocalIntentReceiver receiver = new LocalIntentReceiver();
            session.commit(receiver.getIntentSender());
            final Intent result = receiver.getResult();
            final int status = result.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE);
            String statusMessage = result.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
            boolean success = status == PackageInstaller.STATUS_SUCCESS;
//            if (Global.current_pkgName == null || Global.current_pkgName.isEmpty()) {
//                Global.current_pkgName = result.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
//            }
            // 如果提示已经安装了，尝试卸载后再安装
//            if (statusMessage.toLowerCase().contains("already") || statusMessage.toLowerCase().contains("signatures do not match")) {
//                if (uninstallPackage(Global.current_pkgName)) {
//                    return installPackage(apkFilePath, mListener);
//                }
//            }
            if (mListener != null) mListener.onInstall(statusMessage, success);
            // 保持原来的上报状态代码逻辑
//            if (mListener != null && !mListener.equals(listener))
//                listener.onInstall(statusMessage, success);
//            // 设置全局包名
//            Global.log_d(TAG, "install status=" + status + " message=" + statusMessage);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
//            Global.log_d(TAG, "安装应用时出错");
//            if (mListener != null) mListener.onInstall(e.getMessage(), false);
//            // 保持原来的上报状态代码逻辑
//            if (mListener != null && !mListener.equals(listener))
//                listener.onInstall(e.getMessage(), false);
            return false;
        }
    }
    public void setInstallFlags(PackageInstaller.SessionParams params, int flags) {
        try {
            Field field_installFlags = params.getClass().getDeclaredField("installFlags");
            int installFlags = field_installFlags.getInt(params);
            installFlags |= flags;
            field_installFlags.set(params, installFlags);
        } catch (Exception e) {
            e.printStackTrace();
//            Global.log_e(TAG, "setInstallFlags failed!", e);
        }
    }

}
