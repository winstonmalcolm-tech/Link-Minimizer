package com.example.urlshortner.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.urlshortner.Fragments.AllLinks;
import com.example.urlshortner.Fragments.NewLink;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position) {
            case 0: return new NewLink();
            case 1: return new AllLinks();
            default: return new NewLink();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
