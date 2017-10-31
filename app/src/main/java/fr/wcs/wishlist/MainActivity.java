package fr.wcs.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import fr.wcs.wishlist.Helpers.UserHelper;
import fr.wcs.wishlist.Models.User;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.abs_layout);

        ImageView shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Viens voir ma WishList ! \n\n LIENLIENLIENLIENLIENLIEN");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("UserName");
        Log.d("HELPER", "onCreate: " + userName);
        mUser = UserHelper.init(userName);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    WishFragment wishFragment = new WishFragment();
                    return wishFragment;
                case 1:
                    OfferedFragment offeredFragment = new OfferedFragment();
                    return offeredFragment;
                case 2:
                    GiftFragment giftFragment = new GiftFragment();
                    return giftFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Souhaits";
                case 1:
                    return "Offerts";
                case 2:
                    return "Offrir";
            }
            return null;
        }
    }
}



