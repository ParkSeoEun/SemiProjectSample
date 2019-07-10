package com.example.semiprojectsample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;

public class FragmentModifyWrite extends Fragment {

    private EditText mEdtWriteModify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        Intent intent = getActivity().getIntent();
        long memoId = intent.getIntExtra("memoId",-1);

        Toast.makeText(getActivity(), "memoId"+memoId, Toast.LENGTH_SHORT).show();

        mEdtWriteModify = view.findViewById(R.id.edtWriteModify);

        MemoBean memoBean = FileDB.findMemo(getActivity(), memoId);
        mEdtWriteModify.setText(memoBean.memo);

        return view;
    }
}
