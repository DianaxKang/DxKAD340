package com.dxk.dxkapplication;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    private final ArrayList<Camera> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cameraDescriptionView;
        private final ImageView cameraImageView;

        public ViewHolder(View view) {
            super(view);
            cameraDescriptionView = view.findViewById(R.id.cameraListItemDescription);
            cameraImageView = view.findViewById(R.id.cameraListItemImage);
        }

        public TextView getCameraDescriptionView() {
            return cameraDescriptionView;
        }

        public ImageView getCameraImageView() {
            return cameraImageView;
        }
    }

    public CameraAdapter(ArrayList<Camera> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.camera_activity_layout,
                viewGroup, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = viewGroup.getHeight() / 2;
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Camera camera = localDataSet.get(position);
        viewHolder.getCameraDescriptionView().setText(String.valueOf(camera.getDescription()));
        Picasso .get().load(camera.getImageURL()).into(viewHolder.getCameraImageView());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}