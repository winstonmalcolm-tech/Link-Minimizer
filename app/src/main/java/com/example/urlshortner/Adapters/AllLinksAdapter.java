package com.example.urlshortner.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urlshortner.Entities.Url;
import com.example.urlshortner.R;
import com.example.urlshortner.Util.LinksDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class AllLinksAdapter extends RecyclerView.Adapter<AllLinksAdapter.LinksViewHolder> {

    List<Url> urls = new ArrayList<>();
    Context context;

    public AllLinksAdapter(List<Url> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }


    //This function will update new data to recyclerview
    public void updateInfo(List<Url> newUrls) {

        LinksDiffUtilCallback diffUtilCallback = new LinksDiffUtilCallback(urls, newUrls);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        urls.clear();
        urls.addAll(newUrls);
        diffResult.dispatchUpdatesTo(this);
    }


    @NonNull
    @Override
    public LinksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinksViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_link_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinksViewHolder holder, int position) {
        holder.title.setText(urls.get(position).getTitle());
        holder.url.setText(urls.get(position).getUrl());

        holder.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("Url", Uri.parse(urls.get(holder.getAdapterPosition()).getUrl()));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Copied Url", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class LinksViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url;
        ImageButton copyBtn;

        public LinksViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.single_title_output);
            url = itemView.findViewById(R.id.single_short_link_output);
            copyBtn = itemView.findViewById(R.id.single_copy_btn);
        }
    }
}
