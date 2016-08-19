package com.devilwwj.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

public class SkinApplication extends Application {
    protected final String APK_NAME = "skin.apk";
    protected final String DEX_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/skin.apk";

    @Override
    public void onCreate() {
        super.onCreate();
        String skinPath = SkinConfig.getInstance(this).getSkinResourcePath();
        Log.e("path", skinPath + "");
        if (!TextUtils.isEmpty(skinPath)) {
            // 如果已经换皮肤，那么第二次进来时，需要加载该皮肤
            SkinPackageManager.getInstance(this).loadSkinAsync(skinPath, null);
        } else {
            // 把apk文件复制到sd卡
            SkinPackageManager.getInstance(this).copyApkFromAssets(this, APK_NAME,
                    DEX_PATH);
            PackageManager mpm = getPackageManager();
            // 得到包信息
            PackageInfo mInfo = mpm.getPackageArchiveInfo(DEX_PATH, PackageManager.GET_ACTIVITIES);
            SkinPackageManager.getInstance(this).mPackageName = mInfo.packageName;

//			mPackageName = mInfo.packageName;

            Method addAssetPath = null;
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, DEX_PATH);
                Resources superRes = getResources();
                Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                Log.e("path", DEX_PATH);

//						SharedPreferences sp = mContext.getSharedPreferences("path", Context.MODE_PRIVATE);
//						sp.edit().putString("path",dexPath_tmp).apply();
                SkinConfig.getInstance(this).setSkinResourcePath(DEX_PATH); // 保存资源路径
                SkinPackageManager.getInstance(this).mResources = skinResource;

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
}
