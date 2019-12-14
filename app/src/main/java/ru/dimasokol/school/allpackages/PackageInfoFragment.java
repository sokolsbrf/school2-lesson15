package ru.dimasokol.school.allpackages;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageInfoFragment extends Fragment implements PackageInfoRepository.OnInfoReady {

    private static final String ARG_PACKAGE_NAME = "package";

    private ImageView mIconView;
    private TextView mAppNameView;
    private TextView mPackageNameView;
    private String mPackageName;

    public static PackageInfoFragment newInstance(String packageName) {
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE_NAME, packageName);
        PackageInfoFragment fragment = new PackageInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_package_info, container, false);

        mIconView = root.findViewById(R.id.image_icon);
        mAppNameView = root.findViewById(R.id.text_app_name);
        mPackageNameView = root.findViewById(R.id.text_package_name);

        mPackageName = getArguments().getString(ARG_PACKAGE_NAME);
        mPackageNameView.setText(mPackageName);

        final PackageInfoRepository infoRepository = PackageInfoRepository.getInstance(requireContext());
        infoRepository.addListener(this);
        infoRepository.loadPackageInfo(mPackageName);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PackageInfoRepository.getInstance(requireContext()).removeListener(this);
    }

    @Override
    public void onReady(PackageInfoRepository.InfoEntity entity) {
        if (mPackageName.equals(entity.getPackageName())) {
            mIconView.setImageDrawable(entity.getIcon());
            mAppNameView.setText(entity.getApplicationName());
        }
    }
}
