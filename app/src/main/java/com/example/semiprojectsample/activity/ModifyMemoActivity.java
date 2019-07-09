package com.example.semiprojectsample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;
import com.example.semiprojectsample.fragment.FragmentCamera;
import com.example.semiprojectsample.fragment.FragmentMemoWrite;
import com.example.semiprojectsample.fragment.FragmentModifyCamera;
import com.example.semiprojectsample.fragment.FragmentModifyWrite;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModifyMemoActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private Button mbtnCancelModify, mbtnSaveModify;
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_memo);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mbtnCancelModify = findViewById(R.id.btnCancelModify);
        mbtnSaveModify = findViewById(R.id.btnSaveModify);

        //  탭 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("메모"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // 버튼 이벤트
        mbtnCancelModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mbtnSaveModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProc();
            }
        });

        //ViewPager 생성
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                mTabLayout.getTabCount());
        //tab 이랑 viewpager 랑 연결
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }
    //저장버튼 저장처리
    private void saveProc() {

        //1.첫번째 프래그먼트의 EditText 값을 받아온다.
        FragmentModifyWrite f0 = (FragmentModifyWrite)mViewPagerAdapter.instantiateItem(mViewPager,0);
        //2.두번째 프래그먼트의 mPhotoPath 값을 가져온다.
        FragmentModifyCamera f1 = (FragmentModifyCamera)mViewPagerAdapter.instantiateItem(mViewPager,1);

        EditText edtWriteMemo = f0.getView().findViewById(R.id.edtWriteModify);
        String memoStr = edtWriteMemo.getText().toString();
        String photoPath = f1.mPhotoPath;

        MemoBean bean = new MemoBean();
        MemoBean memoBean = FileDB.findMemo(this, bean.memoId);

        // 사진수정이 안 되었을 시 photoPath 를 기존의 사진 경로로 지정
        if(photoPath == null) {
            photoPath = memoBean.memoPicPath;
        }

        Log.e("SEMI", "memoStr: " + memoStr + ", photoPath: " + photoPath);
        Toast.makeText(this, "memoStr: " + memoStr + ", photoPath: " + photoPath, Toast.LENGTH_LONG).show();

        //TODO 파일DB 에 저장처리
        FileDB fileDB = new FileDB();
        memoBean.memoPicPath = photoPath;
        memoBean.memo = memoStr;
        memoBean.memoDate = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date());
        fileDB.setMemo(this, memoBean);

        mViewPagerAdapter.notifyDataSetChanged();
        finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private int tabCount;

        public ViewPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.tabCount = count;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentModifyWrite();
                case 1:
                    return new FragmentModifyCamera();
            }
            return null;
        }
        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
