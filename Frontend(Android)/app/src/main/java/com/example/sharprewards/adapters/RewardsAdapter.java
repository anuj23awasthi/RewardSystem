package com.example.sharprewards.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharprewards.R;
import com.example.sharprewards.models.Reward;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {

    List<Reward> list;

    public RewardsAdapter(List<Reward> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reward, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reward r = list.get(position);

        holder.name.setText(r.title);
        holder.cost.setText("Cost: " + r.cost + " tokens");
        holder.desc.setText(r.description);
        holder.brand.setText("Brand: " + r.brand);
        holder.expiry.setText("Expires: " + r.expiryDate);

        holder.redeemBtn.setOnClickListener(v -> {
            // redeem action handled in activity
            if (listener != null) listener.onRedeemClicked(r);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnRedeemClickListener {
        void onRedeemClicked(Reward reward);
    }

    private OnRedeemClickListener listener;

    public void setOnRedeemClickListener(OnRedeemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, cost, desc, brand, expiry;
        Button redeemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rewardName);
            cost = itemView.findViewById(R.id.rewardCost);
            desc = itemView.findViewById(R.id.rewardDesc);
            brand = itemView.findViewById(R.id.rewardBrand);
            expiry = itemView.findViewById(R.id.rewardExpiry);
            redeemBtn = itemView.findViewById(R.id.redeemBtn);
        }
    }
}
