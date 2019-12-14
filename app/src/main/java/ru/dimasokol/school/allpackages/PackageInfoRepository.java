package ru.dimasokol.school.allpackages;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageInfoRepository {

    private static PackageInfoRepository sInstance;

    public static PackageInfoRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PackageInfoRepository(context);
        }

        return sInstance;
    }

    private final Context mContext;
    private Map<String, InfoEntity> mEntities = new HashMap<>();

    private List<OnInfoReady> mListeners = new ArrayList<>();

    public PackageInfoRepository(Context context) {
        mContext = context.getApplicationContext();
    }

    public void addListener(OnInfoReady listener) {
        removeListener(listener);
        mListeners.add(listener);
    }

    public void removeListener(OnInfoReady listener) {
        mListeners.remove(listener);
    }

    public void loadPackageInfo(String packageName) {
        InfoEntity entity = mEntities.get(packageName);

        if (entity != null) {
            notifyAllListeners(entity);
        } else {
            new LoadingTask().execute(packageName);
        }
    }

    class LoadingTask extends AsyncTask<String, Void, InfoEntity> {

        @Override
        protected InfoEntity doInBackground(String... strings) {
            String packageName = strings[0];

            try {
                final PackageManager packageManager = mContext.getPackageManager();
                PackageInfo info = packageManager.getPackageInfo(packageName, 0);

                String label = info.applicationInfo.loadLabel(packageManager).toString();
                Drawable icon = info.applicationInfo.loadIcon(packageManager);

                return new InfoEntity(packageName, label, icon);

            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InfoEntity infoEntity) {
            if (infoEntity != null) {
                mEntities.put(infoEntity.getPackageName(), infoEntity);
                notifyAllListeners(infoEntity);
            }
        }
    }

    private void notifyAllListeners(InfoEntity infoEntity) {
        for (OnInfoReady listener : mListeners) {
            listener.onReady(infoEntity);
        }
    }

    public interface OnInfoReady {
        void onReady(InfoEntity entity);
    }

    public static class InfoEntity {
        private final String mPackageName;
        private final String mApplicationName;
        private final Drawable mIcon;

        public InfoEntity(String packageName, String applicationName, Drawable icon) {
            mPackageName = packageName;
            mApplicationName = applicationName;
            mIcon = icon;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public String getApplicationName() {
            return mApplicationName;
        }

        public Drawable getIcon() {
            return mIcon;
        }
    }

}
