package in.co.rubberduck.notapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.co.rubberduck.notapp.R;

public class HelpActivity extends AppCompatActivity {

    static ViewPager viewPager_Help;
    FragmentPagerAdapter pagerAdapter;
    Bundle args;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        args=new Bundle();
        viewPager_Help=(ViewPager)findViewById(R.id.viewPager_Help);
        pagerAdapter= new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager_Help.setAdapter(pagerAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment spf=null;
            switch (position)
            {
                case 0:
                    spf=new FirstSlidePageFragment();
                    break;
                case 1:
                    spf=new SecondSlidePageFragment();
                    break;
                case 2:
                    spf=new ThirdSlidePageFragment();
                    break;
                case 3:
                    spf=new ForthSlidePageFragment();
                    break;
            }
            return spf;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class FirstSlidePageFragment extends Fragment implements View.OnClickListener
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.help_fragment, container, false);
            Log.e("HelpActivity","First");
            Button button_take_a_tour=(Button)rootView.findViewById(R.id.button_take_a_tour);
            TextView textView_fragment_1=(TextView)rootView.findViewById(R.id.textView_fragment_1);
            TextView textView_fragment_2=(TextView)rootView.findViewById(R.id.textView_fragment_2);
            TextView textView_fragment_3=(TextView)rootView.findViewById(R.id.textView_fragment_3);
            TextView textView_fragment_5=(TextView)rootView.findViewById(R.id.textView_fragment_5);
            textView_fragment_1.setOnClickListener(this);
            textView_fragment_2.setOnClickListener(this);
            textView_fragment_3.setOnClickListener(this);
            textView_fragment_5.setMovementMethod(LinkMovementMethod.getInstance());
            button_take_a_tour.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
           switch (v.getId())
           {
               case R.id.textView_fragment_1:
               case R.id.button_take_a_tour:
                   viewPager_Help.setCurrentItem(1);
                   break;
               case R.id.textView_fragment_2:
                   viewPager_Help.setCurrentItem(2);
                   break;
               case R.id.textView_fragment_3:
                   viewPager_Help.setCurrentItem(3);
                   break;
           }
        }
    }
    public static class SecondSlidePageFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.help_fragment_1, container, false);
            TextView textView_VersionNumber=(TextView)rootView.findViewById(R.id.textView_VersionNumber);
            textView_VersionNumber.setText("Version "+BuildConfig.VERSION_NAME);
            Log.e("HelpActivity", "Second");
            return rootView;
        }
    }
    public static class ThirdSlidePageFragment extends Fragment implements View.OnClickListener
    {
        static Button button_manage;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.help_fragment_2, container, false);
            Log.e("HelpActivity","Third");
            button_manage=(Button)rootView.findViewById(R.id.button_manage);
            button_manage.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_manage:
                Toast toast=new Toast(getActivity());
                ImageView imageView=new ImageView(getActivity());
                imageView.setImageResource(R.drawable.help_easter_egg);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(imageView);
                toast.show();
                break;
        }
    }
    }
    public static class ForthSlidePageFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.help_fragment_3, container, false);
            Log.e("HelpActivity","Forth");
            return rootView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        if(sharedPreferences.getBoolean("isFirstTime",false)){
            editor.putBoolean("isFirstTime",false);
            editor.commit();
            startActivity(new Intent(HelpActivity.this,MainActivity.class));
            finish();
            Toast.makeText(getBaseContext(),"------WELCOME------",Toast.LENGTH_SHORT);
        }
    }
}