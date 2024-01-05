package com.example.urlshortner.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urlshortner.Adapters.AllLinksAdapter;
import com.example.urlshortner.Database.AppDatabase;
import com.example.urlshortner.Entities.Url;
import com.example.urlshortner.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AllLinks extends Fragment {

    AppDatabase db;
    RecyclerView allLinksRv;
    Handler handler = new Handler();
    List<Url> urls = new ArrayList<Url>();
    AllLinksAdapter allLinksAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    ItemTouchHelper itemTouchHelper;
    Url deletedUrl = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(getResources().getColor(R.color.red))
                    .addActionIcon(R.drawable.trash_icon)
                    .addSwipeRightLabel("Delete")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            deletedUrl = urls.get(position);
            urls.remove(position);
            allLinksAdapter.updateInfo(urls);

            Snackbar.make(allLinksRv, deletedUrl.getTitle(), Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urls.add(position, deletedUrl);
                    allLinksAdapter.updateInfo(urls);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db.urlDao().delete(deletedUrl);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }).show();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_links, container, false);

        allLinksRv = view.findViewById(R.id.all_links_rv);
        progressBar = view.findViewById(R.id.load_all_links_progress);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);

        allLinksAdapter = new AllLinksAdapter(urls, getActivity().getApplicationContext());
        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"url_db").build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        allLinksRv.setLayoutManager(linearLayoutManager);
        allLinksRv.setAdapter(allLinksAdapter);

        itemTouchHelper.attachToRecyclerView(allLinksRv);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        urls = db.urlDao().getAll();

                        //Update the UI
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                allLinksAdapter.updateInfo(urls);
                            }
                        });
                    }
                }).start();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                urls = db.urlDao().getAll();

                //Update the UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        allLinksRv.setVisibility(View.VISIBLE);
                        allLinksAdapter.updateInfo(urls);
                    }
                });
            }
        }).start();

        return view;
    }
}