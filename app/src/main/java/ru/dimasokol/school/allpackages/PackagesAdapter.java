package ru.dimasokol.school.allpackages;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {

    private final List<PackageInfo> mPackages;
    private final OnPackageClickListener mListener;

    private LayoutInflater mInflater;

    private List<WeakReference<PackagesAdapter.ViewHolder>> mHolders = new ArrayList<>();

    private PackageInfoRepository.OnInfoReady mReadyListener = new PackageInfoRepository.OnInfoReady() {
        @Override
        public void onReady(PackageInfoRepository.InfoEntity entity) {
            for (WeakReference<ViewHolder> holderRef : mHolders) {
                PackagesAdapter.ViewHolder holder = holderRef.get();
                if (holder != null) {
                    holder.bindExtended(entity);
                }
            }
        }
    };

    public PackagesAdapter(List<PackageInfo> packages, OnPackageClickListener listener) {
        mPackages = packages;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mInflater = mInflater == null?
                LayoutInflater.from(parent.getContext()) : mInflater;

        final ViewHolder viewHolder = new ViewHolder(mInflater.inflate(R.layout.item_package, parent, false), mListener);
        mHolders.add(new WeakReference<>(viewHolder));

        PackageInfoRepository.getInstance(parent.getContext()).addListener(mReadyListener);
        return viewHolder;
    }

    public void reset(Context context) {
        PackageInfoRepository.getInstance(context).removeListener(mReadyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mPackages.get(position));
    }

    @Override
    public int getItemCount() {
        return mPackages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mPackageNameView;
        private TextView mApplicationNameView;
        private ImageView mAppIconView;

        private String mPackageName;

        private OnPackageClickListener mListener;
        private View.OnClickListener mItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPackageClicked((PackageInfo) v.getTag());
            }
        };

        public ViewHolder(@NonNull View itemView, OnPackageClickListener listener) {
            super(itemView);
            mPackageNameView = itemView.findViewById(R.id.text_package_name);
            mApplicationNameView = itemView.findViewById(R.id.text_app_name);
            mAppIconView = itemView.findViewById(R.id.image_icon);

            mListener = listener;
            itemView.setOnClickListener(mItemClickListener);
        }

        public void bind(final PackageInfo info) {
            mPackageName = info.packageName;

            mPackageNameView.setText(mPackageName);
            mApplicationNameView.setText(mPackageName);
            mAppIconView.setTag(mPackageName);
            mAppIconView.setImageDrawable(null);

            PackageInfoRepository.getInstance(itemView.getContext()).loadPackageInfo(info.packageName);
            itemView.setTag(info);
        }

        public void bindExtended(PackageInfoRepository.InfoEntity info) {
            if (mPackageName.equals(info.getPackageName())) {
                mApplicationNameView.setText(info.getApplicationName());
                mAppIconView.setImageDrawable(info.getIcon());
            }
        }
    }


    public interface OnPackageClickListener {
        void onPackageClicked(PackageInfo packageInfo);
    }
}
