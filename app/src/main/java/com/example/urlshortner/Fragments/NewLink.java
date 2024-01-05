package com.example.urlshortner.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urlshortner.API.UrlShortenerEndPoints;
import com.example.urlshortner.Database.AppDatabase;
import com.example.urlshortner.Entities.Url;
import com.example.urlshortner.MainActivity;
import com.example.urlshortner.Models.UrlData;
import com.example.urlshortner.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewLink extends Fragment {

    Button shortenUrlBtn;
    TextInputLayout urlInput, titleInput;
    TextView outputTitletxt, outputUrltxt;
    ProgressBar progressBar;
    AppDatabase db;
    String generatedUrl = "";
    Handler myhandler;
    RelativeLayout outputView;
    ImageButton copyBtn;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_link, container, false);
        shortenUrlBtn = view.findViewById(R.id.shorten_url_btn);
        urlInput = view.findViewById(R.id.url_input);
        titleInput = view.findViewById(R.id.title_input);
        outputUrltxt = view.findViewById(R.id.short_link_output);
        outputTitletxt = view.findViewById(R.id.title_output);
        progressBar = view.findViewById(R.id.progressBar);
        outputView = view.findViewById(R.id.output_view);
        copyBtn = view.findViewById(R.id.copy_btn);
        myhandler = new Handler();

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"url_db").build();


        shortenUrlBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = Objects.requireNonNull(titleInput.getEditText()).getText().toString();
                String url = Objects.requireNonNull(urlInput.getEditText()).getText().toString();

                if (title.equals("") || url.equals("")) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                shortenUrlBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                getShortenLink(title, url);
            }
        });

        outputUrltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(generatedUrl));
                startActivity(browserIntent);
            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("Url", Uri.parse(generatedUrl));
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        return view;
    }

    private void getShortenLink(String title, String url) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://url-shortener-service.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UrlShortenerEndPoints service = retrofit.create(UrlShortenerEndPoints.class);


        service.getShortenUrl(url).enqueue(new Callback<UrlData>() {
            @Override
            public void onResponse(Call<UrlData> call, Response<UrlData> response) {

                progressBar.setVisibility(View.GONE);
                shortenUrlBtn.setVisibility(View.VISIBLE);
                outputView.setVisibility(View.VISIBLE);

                titleInput.getEditText().setText("");
                urlInput.getEditText().setText("");

                generatedUrl = response.body().getResult_url();
                outputUrltxt.setText(response.body().getResult_url());
                outputTitletxt.setText(title);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.urlDao().insertUrl(new Url(title, response.body().getResult_url()));

                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<UrlData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                shortenUrlBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}