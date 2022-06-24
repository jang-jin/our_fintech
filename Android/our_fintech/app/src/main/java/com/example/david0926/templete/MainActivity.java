package com.example.david0926.templete;



import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation in = new AlphaAnimation(0.0f, 1.0f);

        // 총급여
        TextView main_salary = (TextView)findViewById(R.id.main_salary);
        String main_salary_text = "장진" + "님의 연봉은 " + "2000" + " 만원입니다.";
        int main_salary_text_start = main_salary_text.indexOf("님의 연봉은 ") + 7;
        int main_salary_text_end = main_salary_text.indexOf(" 만원입니다.");
        SpannableString main_salary_spannable = new SpannableString(main_salary_text);
        main_salary_spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#4641D9")), main_salary_text_start, main_salary_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary_spannable.setSpan(new StyleSpan(Typeface.BOLD), main_salary_text_start, main_salary_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary_spannable.setSpan(new RelativeSizeSpan(1.3f), main_salary_text_start, main_salary_text_end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary.setText(main_salary_spannable);
        in.setDuration(3000);
        main_salary.startAnimation(in);

        // 올해 사용할 예측금액
        TextView main_total_expense = (TextView)findViewById(R.id.main_total_expense);
        String main_total_expense_text = "제가 " + "장진" + "님의 카드 내역을 통해 분석한\n올해 지출 예상금액은 약 " + "123" + "만원입니다.";
        main_total_expense.setText(main_total_expense_text);

//        //Toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);
//        setSupportActionBar(toolbar);


//        //Tab & ViewPager
//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//
//        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
//        View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_people);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
//
//        View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_chat);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
//
//        View view3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_setting);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        final ViewPager pager = findViewById(R.id.pager);
//        final TabAdapter adapter  = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        pager.setAdapter(adapter);
//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        pager.getRootView().setBackgroundColor(Color.WHITE);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of Navigation Drawer




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_devinfo) {
            DialogFragment dfm = new Dialog_DevInfo();
            dfm.show(getSupportFragmentManager(), "dialog_devinfo");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
