package com.example.arsan_irianto.katalogfilm;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arsan_irianto.katalogfilm.databases.MovieHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.CONTENT_URI;
import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.FavouriteColumn.BACKDROP;
import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.FavouriteColumn.OVERVIEW;
import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.FavouriteColumn.POSTER;
import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.FavouriteColumn.RELEASEDATE;
import static com.example.arsan_irianto.katalogfilm.databases.DatabaseContract.FavouriteColumn.TITLE;

public class DetailFilmActivity extends AppCompatActivity {

    public static String EXTRA_ID_MOVIE = "EXTRA_ID_MOVIE";
    public static String EXTRA_TITLE = "EXTRA_TITLE";
    public static String EXTRA_OVERVIEW = "EXTRA_OVERVIEW";
    public static String EXTRA_RELEASEDATE = "EXTRA_RELEASEDATE";
    public static String EXTRA_POSTERIMAGE = "EXTRA_POSTERIMAGE";
    public static String EXTRA_BACKDROP = "EXTRA_BACKDROP";

    public static int RESULT_ADD = 101;
    public static int RESULT_DELETE = 301;

    @BindView(R.id.tv_detail_judul)
    TextView tvDetailJudul;
    @BindView(R.id.tv_detail_jadwal)
    TextView tvDetailJadwal;
    @BindView(R.id.tv_detail_overview)
    TextView tvDetailOverview;

    @BindView(R.id.image_detail_film)
    ImageView imageDetailFilm;

    @BindView(R.id.fav_button)
    MaterialFavoriteButton favoriteButton;

    private MovieHelper movieHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);

        ButterKnife.bind(this);

        final String judul = getIntent().getStringExtra(EXTRA_TITLE);
        final String jadwal = getIntent().getStringExtra(EXTRA_RELEASEDATE);
        final String deskripsi = getIntent().getStringExtra(EXTRA_OVERVIEW);
        final String posterFIlm = getIntent().getStringExtra(EXTRA_POSTERIMAGE);
        final String backDrop = getIntent().getStringExtra(EXTRA_BACKDROP);

        tvDetailJudul.setText(judul);
        tvDetailJadwal.setText(jadwal);
        tvDetailOverview.setText(deskripsi);

        Glide.with(DetailFilmActivity.this)
                .load(posterFIlm)
                //.override(600, 800)
                .crossFade()
                .into(imageDetailFilm);

        MovieHelper movieHelper = new MovieHelper(this);
        movieHelper.open();

        favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                String favoriteStatus="";

                ContentValues values = new ContentValues();
                values.put(TITLE,judul);
                values.put(OVERVIEW, deskripsi);
                values.put(RELEASEDATE, jadwal);
                values.put(POSTER, posterFIlm);
                values.put(BACKDROP, backDrop);

                if(favorite){
                    //favoriteStatus = "Favourite";
                    getContentResolver().insert(CONTENT_URI,values);

                    setResult(RESULT_ADD);
                    finish();
                }else{
                    favoriteStatus = "Unfavourite";
                }
                Toast.makeText(getApplicationContext(), favoriteStatus, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) {
            movieHelper.close();
        }
    }
}
