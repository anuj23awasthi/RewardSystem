package com.example.sharprewards.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharprewards.R;
import com.example.sharprewards.models.LeaderboardEntry;
import com.example.sharprewards.models.User;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    List<LeaderboardEntry> list;

    public LeaderboardAdapter(List<LeaderboardEntry> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeaderboardEntry entry = list.get(position);

        holder.rank.setText("#" + (position + 1));
        holder.name.setText(entry.user.name);

        // 🔥 FIX: Show correct token value
        // If coming from GLOBAL leaderboard → entry.correct will be false because conversion created new object
        if (entry.date == null || entry.correct == false) {
            holder.tokens.setText("Tokens: " + entry.tokensEarned);  // totalTokens mapped to tokensEarned in conversion
            holder.streak.setText("");  // hide correct/incorrect
        } else {
            holder.tokens.setText("Tokens: " + entry.tokensEarned);  // today's tokens
            holder.streak.setText("Correct: " + (entry.correct ? "Yes" : "No"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView rank, name, tokens, streak;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            tokens = itemView.findViewById(R.id.tokens);
            streak = itemView.findViewById(R.id.streak);
        }
    }
}
