package com.shabit.tourthepast.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shabit.tourthepast.MainConfig;
import com.shabit.tourthepast.R;
import com.shabit.tourthepast.MainApplication;
import com.shabit.tourthepast.utility.DatabaseHandler;
import com.shabit.tourthepast.utility.ImageLoader;
import com.shabit.tourthepast.utility.Pojo;
import com.shabit.tourthepast.utility.Preferences;
import com.shabit.tourthepast.utility.ResourcesHelper;
import com.shabit.tourthepast.view.ObservableStickyScrollView;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.List;


public class DetailActivity extends AppCompatActivity {

    public DatabaseHandler db;
    public ImageLoader imageLoader;
    ViewPager viewpager;
    int position;
    String[] str_cid, str_cat_id, str_cat_image, str_cat_name, str_title, str_image, str_desc, str_cont, str_ingre, str_calories, str_proteins, str_fat, str_carbs, str_time;
    String ttpcid, ttpcat_id, ttpcatname, ttpheading, ttpimage, ttpdes, ttpcont, ttpingre, ttpcalories, ttpproteins, ttpfat, ttpcarbs, ttptime;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Preferences.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        db = new DatabaseHandler(this);

        Intent i = getIntent();

        position = i.getIntExtra("POSITION", 0);
        str_cid = i.getStringArrayExtra("CATEGORY_ITEM_CID");
        str_cat_name = i.getStringArrayExtra("CATEGORY_ITEM_NAME");
        str_cat_image = i.getStringArrayExtra("CATEGORY_ITEM_IMAGE");
        str_cat_id = i.getStringArrayExtra("CATEGORY_ITEM_CAT_ID");
        str_image = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEIMAGE");
        str_title = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEHEADING");
        str_desc = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEDESCRI");
        str_cont = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPECONTENT");
        str_ingre = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEINGRE");
        str_calories = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPECALORIES");
        str_proteins = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEPROTEINS");
        str_fat = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPEFAT");
        str_carbs = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPECARBS");
        str_time = i.getStringArrayExtra("CATEGORY_ITEM_WORCIPETIME");

        viewpager = (ViewPager) findViewById(R.id.news_slider);
        imageLoader = new ImageLoader(getApplicationContext());

        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewpager.setAdapter(adapter);

        boolean found = false;
        int j1 = 0;
        for (int i1 = 0; i1 < str_cat_id.length; i1++) {
            if (str_cat_id[i1].contains(String.valueOf(position))) {
                found = true;
                j1 = i1;
                break;
            }
        }
        if (found) {
            viewpager.setCurrentItem(j1);
        }

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                position = viewpager.getCurrentItem();
                ttpcat_id = str_cat_id[position];

                List<Pojo> pojolist = db.getFavRow(ttpcat_id);
                if (pojolist.size() == 0) {
                    menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_outline_white));
                } else {
                    if (pojolist.get(0).getCatId().equals(ttpcat_id)) {
                        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white));
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int position) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });

        // init analytics tracker
        ((MainApplication) getApplication()).getTracker();

    }

    @Override
    public void onStart() {
        super.onStart();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        Favorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                position = viewpager.getCurrentItem();
                ttpheading = str_title[position];
                ttpdes = str_desc[position];
                String formattedString = android.text.Html.fromHtml(ttpdes).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ttpheading + "\n" + formattedString + "\n" + " I Would like to share this recipe with you"+ "\n"+ "Download app from this link");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.action_favorite:
                position = viewpager.getCurrentItem();
                ttpcat_id = str_cat_id[position];
                List<Pojo> pojolist = db.getFavRow(ttpcat_id);
                if (pojolist.size() == 0) {
                    AddFavorite(position);
                } else {
                    if (pojolist.get(0).getCatId().equals(ttpcat_id)) {
                        RemoveFavorite(position);
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddFavorite(int position) {
        ttpcat_id = str_cat_id[position];
        ttpcid = str_cid[position];
        ttpcatname = str_cat_name[position];
        ttpheading = str_title[position];
        ttpimage = str_image[position];
        ttpdes = str_desc[position];
        ttpcont = str_cont[position];
        ttpingre = str_ingre[position];
        ttpcalories = str_calories[position];
        ttpproteins = str_proteins[position];
        ttpfat = str_fat [position];
        ttpcarbs = str_carbs[position];
        ttptime = str_time[position];

        db.AddtoFavorite(new Pojo(ttpcat_id, ttpcid, ttpcatname, ttpheading, ttpimage, ttpdes, ttpcont, ttpingre, ttpcalories, ttpproteins, ttpfat, ttpcarbs, ttptime));
        Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white));
    }

    //remove from favorite
    public void RemoveFavorite(int position) {
        ttpcat_id = str_cat_id[position];
        db.RemoveFav(new Pojo(ttpcat_id));
        Toast.makeText(getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT).show();
        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_outline_white));
    }

    public void Favorite() {
        int first = viewpager.getCurrentItem();
        String Image_id = str_cat_id[first];

        List<Pojo> pojolist = db.getFavRow(Image_id);
        if (pojolist.size() == 0) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_outline_white));

        } else {
            if (pojolist.get(0).getCatId().equals(Image_id)) {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white));
            }

        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set toolbar background
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setVisibility(View.VISIBLE);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(null);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return str_cat_id.length;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.activity_detail_item, container, false);
            assert imageLayout != null;

            final ObservableStickyScrollView observableStickyScrollView = (ObservableStickyScrollView) imageLayout.findViewById(R.id.container_content);
            final View panelTopView = imageLayout.findViewById(R.id.toolbar_image_panel_top);
            final View panelBottomView = imageLayout.findViewById(R.id.toolbar_image_panel_bottom);
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.toolbar_image_imageview);
            final TextView titleTextView = (TextView) imageLayout.findViewById(R.id.toolbar_image_title);

            imageLoader.DisplayImage(MainConfig.SERVER_URL + "/upload/" + str_image[position], imageView);

            titleTextView.setText(str_title[position]);

            TextView recipeTextView = (TextView) imageLayout.findViewById(R.id.activity_detail_content_recipe_text);
            recipeTextView.setText(Html.fromHtml(str_cont[position]));

            TextView ingredientsTextView = (TextView) imageLayout.findViewById(R.id.activity_detail_content_ingredients_text);
            ingredientsTextView.setText(Html.fromHtml(str_ingre[position]));



            // scroll view
            observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
                private final int THRESHOLD = DetailActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
                private final int PADDING_LEFT = DetailActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
                private final int PADDING_BOTTOM = DetailActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
                private final float SHADOW_RADIUS = 16;

                private int mPreviousY = 0;
                private ColorDrawable mTopColorDrawable = new ColorDrawable();
                private ColorDrawable mBottomColorDrawable = new ColorDrawable();


                @Override
                public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {
                    // do not calculate if header is hidden
                    if (y > THRESHOLD && mPreviousY > THRESHOLD) return;

                    // calculate panel alpha
                    int alpha = (int) (y * (255f / (float) THRESHOLD));
                    if (alpha > 255) alpha = 255;

                    // set color drawables
                    mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(DetailActivity.this, R.attr.colorPrimary));
                    mTopColorDrawable.setAlpha(alpha);
                    mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(DetailActivity.this, R.attr.colorPrimary));
                    mBottomColorDrawable.setAlpha(alpha);

                    // set panel background
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        panelTopView.setBackground(mTopColorDrawable);
                        panelBottomView.setBackground(mBottomColorDrawable);
                    } else {
                        panelTopView.setBackgroundDrawable(mTopColorDrawable);
                        panelBottomView.setBackgroundDrawable(mBottomColorDrawable);
                    }

                    // calculate image translation
                    float translation = y / 2;

                    // set image translation
                    imageView.setTranslationY(translation);

                    // calculate title padding
                    int paddingLeft = (int) (y * (float) PADDING_LEFT / (float) THRESHOLD);
                    if (paddingLeft > PADDING_LEFT) paddingLeft = PADDING_LEFT;

                    int paddingRight = PADDING_LEFT - paddingLeft;

                    int paddingBottom = (int) ((THRESHOLD - y) * (float) PADDING_BOTTOM / (float) THRESHOLD);
                    if (paddingBottom < 0) paddingBottom = 0;

                    // set title padding
                    titleTextView.setPadding(paddingLeft, 0, paddingRight, paddingBottom);

                    // calculate title shadow
                    float radius = ((THRESHOLD - y) * SHADOW_RADIUS / (float) THRESHOLD);

                    // set title shadow
                    titleTextView.setShadowLayer(radius, 0f, 0f, getResources().getColor(android.R.color.black));

                    // previous y
                    mPreviousY = y;
                }
            });

            // invoke scroll event because of orientation change toolbar refresh
            observableStickyScrollView.post(new Runnable() {
                @Override
                public void run() {
                    observableStickyScrollView.scrollTo(0, observableStickyScrollView.getScrollY() - 1);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
}
