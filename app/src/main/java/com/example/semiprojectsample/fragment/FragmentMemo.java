package com.example.semiprojectsample.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.activity.MainActivity;
import com.example.semiprojectsample.activity.NewMemoActivity;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;

import java.util.ArrayList;
import java.util.List;

public class FragmentMemo extends Fragment {

    public ListView mLstMemo;
    List<MemoBean> memoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.view_memo, memoList);

        mLstMemo = view.findViewById(R.id.lstMemo);
        view.findViewById(R.id.btnNewMemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새메모 화면으로 이동
                Intent i = new Intent(getActivity(), NewMemoActivity.class);
                startActivity(i);
            }
        });
        MemberBean memberBean = new MemberBean();
        // 메모 리스트 획득
        memoList = FileDB.getMemoList(getActivity(), memberBean.memId);
        // adapter 생성 및 적요
        mLstMemo.setAdapter(adapter);

        mLstMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
                if(memoList == null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }


}
