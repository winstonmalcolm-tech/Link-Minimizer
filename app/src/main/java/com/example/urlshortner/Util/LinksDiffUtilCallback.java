package com.example.urlshortner.Util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.urlshortner.Entities.Url;

import java.util.List;

public class LinksDiffUtilCallback extends DiffUtil.Callback {

    private List<Url> oldList;
    private List<Url> newList;

    public LinksDiffUtilCallback(List<Url> oldList, List<Url> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
