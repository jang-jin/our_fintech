package com.example.david0926.templete;



import android.graphics.Typeface;
import android.os.Handler;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText expense;
    private Handler mHandler;
    private String ip = "192.168.98.16";
    private int port = 8080;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String name_salary;
    private String results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Animation> in_list = new ArrayList<Animation>();
        int t = 0;
        int step_t = 0;
        for(int i = 0; i < 11; i++){
            Animation in = new AlphaAnimation(0.0f, 1.0f);
            if(i == 1 || i == 4 || i == 8){
                step_t = 1000;

            }else{
                step_t = 3000;
            }
            in.setDuration(step_t);
            in.setStartOffset(t);
            in_list.add(in);
            t += step_t;
        }

        // 데이터 받아오기
        Intent MainIntent = getIntent();
        String results = MainIntent.getStringExtra("결과");
        String[] results_list = results.split("-");
        results_list[3] = Integer.toString(Integer.parseInt(results_list[3]) / 10000);
        results_list[4] = Integer.toString(Integer.parseInt(results_list[4]) / 10000);
        name_salary = results_list[0] + "-" + results_list[3];

        for(int i = 3; i < results_list.length; i++){
            int step = results_list[i].length() / 3;
            int start_index = results_list[i].length() % 3;
            for(int j = step-1; j >= 0; j--){
                if(start_index == 0 && j == 0){
                    break;
                }
                results_list[i] = results_list[i].substring(0, start_index+3*j) + "," + results_list[i].substring(start_index+3*j);
            }
        }

        // 총급여
        TextView main_salary = (TextView)findViewById(R.id.main_salary);
        String main_salary_text = results_list[1] + "님의 연봉은 " + results_list[3] + " 만원입니다.";
        int main_salary_text_start = main_salary_text.indexOf("님의 연봉은 ") + 7;
        int main_salary_text_end = main_salary_text.indexOf(" 만원입니다.");
        SpannableString main_salary_spannable = new SpannableString(main_salary_text);
        main_salary_spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#4641D9")), main_salary_text_start, main_salary_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary_spannable.setSpan(new StyleSpan(Typeface.BOLD), main_salary_text_start, main_salary_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary_spannable.setSpan(new RelativeSizeSpan(1.3f), main_salary_text_start, main_salary_text_end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_salary.setText(main_salary_spannable);
        main_salary.startAnimation(in_list.get(0));

        // 올해 사용할 예측금액
        ImageView card_char1 = (ImageView) findViewById((R.id.card_char1));
        ImageView speech_bubble1 = (ImageView) findViewById((R.id.speech_bubble1));
        card_char1.startAnimation(in_list.get(1));
        speech_bubble1.startAnimation(in_list.get(1));

        TextView main_total_expense = (TextView)findViewById(R.id.main_total_expense);
        String main_total_expense_text = "";
        if(results_list[2].equals("1")){
            main_total_expense_text = "내가 예측한 너의\n올해 예상 지출 금액은 약 " + results_list[4] + "만원이야!";
        }else{
            main_total_expense_text = "네가 생각한 예상 금액을 포함한\n올해 예상 지출 금액은 약 " + results_list[4] + "만원이야!";
        }
        main_total_expense.setText(main_total_expense_text);
        main_total_expense.startAnimation(in_list.get(2));

        // 신용카드 기준 세제혜택
        TextView main_tax_benefit1 = (TextView)findViewById(R.id.main_tax_benefit1);
        TextView main_tax_benefit2 = (TextView)findViewById(R.id.main_tax_benefit2);
        String main_tax_benefit1_text = "신용카드만 사용했을 때 공제혜택";
        String main_tax_benefit2_text = "\n" + results_list[5] + "원";

        int main_tax_benefit2_text_start = 1;
        int main_tax_benefit2_text_end = main_tax_benefit2_text.indexOf("원");
        SpannableString main_tax_benefit2_spannable = new SpannableString(main_tax_benefit2_text);
        main_tax_benefit2_spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF2424")), main_tax_benefit2_text_start, main_tax_benefit2_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_tax_benefit2_spannable.setSpan(new StyleSpan(Typeface.BOLD), main_tax_benefit2_text_start, main_tax_benefit2_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        main_tax_benefit1.setText(main_tax_benefit1_text);
        main_tax_benefit2.setText(main_tax_benefit2_spannable);
        main_tax_benefit1.startAnimation(in_list.get(3));
        main_tax_benefit2.startAnimation(in_list.get(3));

        // 알려줄게
        ImageView card_char2 = (ImageView) findViewById((R.id.card_char2));
        ImageView speech_bubble2 = (ImageView) findViewById((R.id.speech_bubble2));
        card_char2.startAnimation(in_list.get(4));
        speech_bubble2.startAnimation(in_list.get(4));

        TextView main_golden_rate = (TextView)findViewById(R.id.main_golden_rate1);
        String main_golden_rate_text = "내가 지금부터 혜택을 최대로 받을 수 있는\n카드 사용 황금 비율을 알려줄게~";
        main_golden_rate.setText(main_golden_rate_text);
        main_golden_rate.startAnimation(in_list.get(5));

        // 연말 사용 예측 금액
        TextView main_predict_expense1 = (TextView)findViewById(R.id.main_predict_expense1);
        TextView main_predict_expense2 = (TextView)findViewById(R.id.main_predict_expense2);
        String main_predict_expense2_text = results_list[6] + "원";
        main_predict_expense2.setText(main_predict_expense2_text);
        main_predict_expense1.startAnimation(in_list.get(6));
        main_predict_expense2.startAnimation(in_list.get(6));

        // 황금비율
        TextView main_golden_rate2 = (TextView)findViewById(R.id.main_golden_rate2);

        TextView main_golden_rate_month_cc1 = (TextView)findViewById(R.id.main_golden_rate_month_cc1);
        TextView main_golden_rate_month_cc2 = (TextView)findViewById(R.id.main_golden_rate_month_cc2);
        String main_golden_rate_month_cc2_text = results_list[7] + "원";
        main_golden_rate_month_cc2.setText(main_golden_rate_month_cc2_text);

        TextView main_golden_rate_month_dc1 = (TextView)findViewById(R.id.main_golden_rate_month_dc1);
        TextView main_golden_rate_month_dc2 = (TextView)findViewById(R.id.main_golden_rate_month_dc2);
        String main_golden_rate_month_dc2_text = results_list[8] + "원";
        main_golden_rate_month_dc2.setText(main_golden_rate_month_dc2_text);

        TextView main_golden_rate_year_cc1 = (TextView)findViewById(R.id.main_golden_rate_year_cc1);
        TextView main_golden_rate_year_cc2 = (TextView)findViewById(R.id.main_golden_rate_year_cc2);
        String main_golden_rate_year_cc2_text = results_list[9] + "원";
        main_golden_rate_year_cc2.setText(main_golden_rate_year_cc2_text);

        TextView main_golden_rate_year_dc1 = (TextView)findViewById(R.id.main_golden_rate_year_dc1);
        TextView main_golden_rate_year_dc2 = (TextView)findViewById(R.id.main_golden_rate_year_dc2);
        String main_golden_rate_year_dc2_text = results_list[10] + "원";
        main_golden_rate_year_dc2.setText(main_golden_rate_year_dc2_text);

        main_golden_rate2.startAnimation(in_list.get(7));
        main_golden_rate_month_cc1.startAnimation(in_list.get(7));
        main_golden_rate_month_cc2.startAnimation(in_list.get(7));
        main_golden_rate_month_dc1.startAnimation(in_list.get(7));
        main_golden_rate_month_dc2.startAnimation(in_list.get(7));
        main_golden_rate_year_cc1.startAnimation(in_list.get(7));
        main_golden_rate_year_cc2.startAnimation(in_list.get(7));
        main_golden_rate_year_dc1.startAnimation(in_list.get(7));
        main_golden_rate_year_dc2.startAnimation(in_list.get(7));

        // 신용카드 기준 세제혜택
        TextView main_tax_benefit_real1 = (TextView)findViewById(R.id.main_tax_benefit_real1);
        TextView main_tax_benefit_real2 = (TextView)findViewById(R.id.main_tax_benefit_real2);
        String main_tax_benefit_real1_text = "황금비율로 사용했을 때 공제혜택";
        String main_tax_benefit_real2_text = "\n" + results_list[11] + "원";

        SpannableString main_tax_benefit_real1_spannable = new SpannableString(main_tax_benefit_real1_text);
        main_tax_benefit_real1_spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBB00")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_tax_benefit_real1_spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int main_tax_benefit_real2_text_start = 1;
        int main_tax_benefit_real2_text_end = main_tax_benefit_real2_text.indexOf("원");
        SpannableString main_tax_benefit_real2_spannable = new SpannableString(main_tax_benefit_real2_text);
        main_tax_benefit_real2_spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF2424")), main_tax_benefit_real2_text_start, main_tax_benefit_real2_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_tax_benefit_real2_spannable.setSpan(new StyleSpan(Typeface.BOLD), main_tax_benefit_real2_text_start, main_tax_benefit_real2_text_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_tax_benefit_real2_spannable.setSpan(new RelativeSizeSpan(1.3f), main_tax_benefit_real2_text_start, main_tax_benefit_real2_text_end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_tax_benefit_real1.setText(main_tax_benefit_real1_spannable);
        main_tax_benefit_real2.setText(main_tax_benefit_real2_spannable);
        main_tax_benefit_real1.startAnimation(in_list.get(7));
        main_tax_benefit_real2.startAnimation(in_list.get(7));

        // 새로운 세제혜택
        ImageView card_char3 = (ImageView) findViewById((R.id.card_char3));
        ImageView speech_bubble3 = (ImageView) findViewById((R.id.speech_bubble3));
        card_char3.startAnimation(in_list.get(8));
        speech_bubble3.startAnimation(in_list.get(8));

        TextView main_tax_benefit_difference = (TextView)findViewById(R.id.main_tax_benefit_difference);
        String main_tax_benefit_difference_text = "";
        if(results_list[12].equals("0") && !results_list[11].equals("0")){
            main_tax_benefit_difference_text = "공제 한도를 초과하여\n더 이상 공제 받을 금액이 없어..😢";
        }else{
            main_tax_benefit_difference_text = "알려준 대로 사용한다면\n" + results_list[12] + "원 개이득이지롱~👍";
        }
        main_tax_benefit_difference.setText(main_tax_benefit_difference_text);
        main_tax_benefit_difference.startAnimation(in_list.get(9));

        // 예상 지출 금액 다를시
        TextView main_if_different = (TextView)findViewById(R.id.main_if_different);
        CardView card_login_expense = (CardView)findViewById(R.id.card_login_expense);
        LinearLayout layout_btn_login_re = (LinearLayout)findViewById((R.id.layout_btn_login_re));
        main_if_different.startAnimation(in_list.get(10));
        card_login_expense.startAnimation(in_list.get(10));
        layout_btn_login_re.startAnimation(in_list.get(10));

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

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login_re:
                expense = findViewById(R.id.expense);
                Log.w("서버", "1");
                connect(name_salary + "-2-" + expense.getText().toString());
//                password = findViewById(R.id.login_password);
//                Toast.makeText(this, "Hello, "+salary.getText().toString()+"!", Toast.LENGTH_SHORT).show();
//                Log.w("서버에서 받아온 값2", ""+ results);
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
//            case R.id.btn_newaccount:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                break;

        }
    }

    void connect(String values){
        mHandler = new Handler();
        Thread checkUpdate = new Thread(){
            public void run(){
                try{
                    socket = new Socket(ip, port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1){
                    Log.w("서버 접속못함", "서버 접속못함");
                    e1.printStackTrace();
                }

                try{
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF(values);
                } catch (IOException e){
                    e.printStackTrace();
                    Log.w("서버", "버퍼 생성 잘못됨");
                }
                Log.w("서버", "버퍼 생성 잘됨");

                try{
//                    String line = (String)dis.readUTF();
                    byte[] buf = new byte[100];
                    int read_byte = dis.read(buf);
                    Log.w("서버", ""+read_byte);
                    results = new String(buf, 0, read_byte);
//                    Log.w("서버에서 받아온 값", ""+line);
                    Log.w("서버에서 받아온 값1", ""+ results);

                    Intent MainSelfIntent = new Intent(MainActivity.this, MainActivity.class);
                    MainSelfIntent.putExtra("결과",results);
                    startActivity(MainSelfIntent);
                    finish();
                } catch (Exception e){
                }
            }
        };
        checkUpdate.start();
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
