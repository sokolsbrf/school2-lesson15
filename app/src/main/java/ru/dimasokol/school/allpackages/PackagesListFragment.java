package ru.dimasokol.school.allpackages;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PackagesListFragment extends Fragment {

    private RecyclerView mPackagesListView;
    private PackagesAdapter mAdapter;

    public PackagesListFragment() {
        super(R.layout.fragment_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        mPackagesListView = root.findViewById(R.id.recycler);
        mPackagesListView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new PackagesAdapter(requireContext().getPackageManager().getInstalledPackages(0),
                new PackagesAdapter.OnPackageClickListener() {
            @Override
            public void onPackageClicked(PackageInfo packageInfo) {
                FragmentActivity activity = getActivity();
                if (activity instanceof PackageInfoHolder) {
                    ((PackageInfoHolder) activity).showPackageInfo(packageInfo);
                }
            }
        });
        mPackagesListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.reset(requireContext());
    }

    public interface PackageInfoHolder {
        void showPackageInfo(PackageInfo info);
    }
}
