package com.example.adopy.UI_utilities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adopy.Activities.PetPageActivity;
import com.example.adopy.R;
import com.example.adopy.Utilities.Interfaces_and_Emuns.ILoadMore;
import com.example.adopy.Utilities.Models.PetModel;
import com.example.adopy.Utilities.MyLocation;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class ItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView petDist;
    public TextView petName;
    public CircleImageView petImage;
    public TextView petAge;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        petName = itemView.findViewById(R.id.pet_name);
        petImage = itemView.findViewById(R.id.pet_image);
        petAge = itemView.findViewById(R.id.pet_age);
        petDist = itemView.findViewById(R.id.pet_dist);
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}

public class PetAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "my_PetAdapter2";

    private Context context;
    private ArrayList<PetModel> petModels;

    double userLat, userLng;

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;

    int visibleThreshold = 6;
    int lastVisibleItem, totalItemCount;


    public PetAdapter2(RecyclerView recyclerView, Context context, ArrayList<PetModel> petModels) {
        this.context = context;
        this.petModels = petModels;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

        MyLocation myLocation = MyLocation.getInstance();
        userLat = myLocation.getLatitude();
        userLng = myLocation.getLongitude();
        Log.d(TAG, "getLastKnownLocation: " + userLat + " , " + userLng);
    }

    @Override
    public int getItemViewType(int position) {
        return petModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int itemPosition = ((RecyclerView) parent).getChildLayoutPosition(v);
                    PetModel pet = petModels.get(itemPosition);

                    Gson gson = new Gson();
                    Intent intent = new Intent(context, PetPageActivity.class);
                    String gStr = gson.toJson(pet);
                    intent.putExtra("pet", gStr);
                    context.startActivity(intent);
                }
            });
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            PetModel petModel = petModels.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder)holder;
            float[] results = new float[1];
//            Log.d(TAG, "onBindViewHolder: " + petModel.getName() + ": " + petModel.getLatitude() + " , " + petModel.getLongitude());// + "\nuri: " + petModel.getImageUri()
            Location.distanceBetween(userLat, userLng, petModel.getLatitude(), petModel.getLongitude(), results);
            String dist = String.valueOf(Math.round(results[0]) / 1000);
//        Log.d(TAG, "onBindViewHolder: " + results[0]/1000);

            viewHolder.petName.setText(petModel.getName());
            Glide.with(context).load(petModel.getImageUri()).placeholder(R.drawable.foot).into(viewHolder.petImage);
            viewHolder.petAge.setText(String.format(petModel.getAge().toString()));
            viewHolder.petDist.setText(dist + context.getString(R.string.km));
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return petModels.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

}
