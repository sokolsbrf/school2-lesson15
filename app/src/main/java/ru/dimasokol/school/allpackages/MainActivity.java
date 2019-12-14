package ru.dimasokol.school.allpackages;

import android.content.pm.PackageInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements PackagesListFragment.PackageInfoHolder {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, new PackagesListFragment())
                    .commit();
        }
    }

    @Override
    public void showPackageInfo(PackageInfo info) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_host, PackageInfoFragment.newInstance(info.packageName))
                .commit();
    }
}
