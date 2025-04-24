package com.marknote.android.viewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.marknote.android.R;
import com.marknote.android.viewPager.fragment.AiFragment;
import com.marknote.android.viewPager.fragment.BillFragment;
import com.marknote.android.viewPager.fragment.FragmentAdapter;
import com.marknote.android.viewPager.fragment.TaskFragment;
import com.marknote.android.viewPager.fragment.WeatherFragment;

public class HomeActivity extends AppCompatActivity {
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new WeatherFragment());
        fragmentAdapter.addFragment(new BillFragment());
        fragmentAdapter.addFragment(new TaskFragment());
        fragmentAdapter.addFragment(new AiFragment());
        viewPager.setAdapter(fragmentAdapter);


        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 当底部导航栏被选中时，根据选中的菜单项，设置ViewPager的当前项
                if (item.getItemId() == R.id.weather) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.bill) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if(item.getItemId() == R.id.task){
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (item.getItemId()== R.id.ai){
                    viewPager.setCurrentItem(3);
                }
                return false;


            }
        });

        // 设置 ViewPager 的页面改变监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 更新 BottomNavigationView 的选中项
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



}