package com.example.semiprojectsample.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.activity.MainActivity;
import com.example.semiprojectsample.activity.ModifyMemoActivity;
import com.example.semiprojectsample.activity.NewMemoActivity;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;

import java.util.ArrayList;
import java.util.List;

public class FragmentMemo extends Fragment {

    public static final int SAVE = 1001;
    ListView mLstMemo;
    List<MemoBean> memoList = new ArrayList<>();
    ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        // 새 메모 작성 버튼 이벤트
        mLstMemo = view.findViewById(R.id.lstMemo);
        view.findViewById(R.id.btnNewMemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새메모 화면으로 이동
                Intent i = new Intent(getActivity(), NewMemoActivity.class);
                startActivity(i);
            }
        });
        // 메모 리스트 획득
        memoList = FileDB.getMemoList(getActivity());
        // Adapter 생성 및 적용
        adapter = new ListAdapter(memoList, getActivity());
        // 리스트뷰에 Adapter 설정
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAVE) { // 리스트 갱신
            // DB 데이터 획득
            memoList = FileDB.getMemoList(getActivity());
            // Adapter에 원본데이터 저장
            adapter.setItems(memoList);
            adapter.notifyDataSetChanged();// 리스트 UI 갱신
        }
    }

    class ListAdapter extends BaseAdapter {
        List<MemoBean> memoList; // 원본 데이터
        Context mContext;
        LayoutInflater inflater;

        public ListAdapter(List<MemoBean> memoList, Context context) {
            this.memoList = memoList;
            this.mContext = context;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<MemoBean> memoList) {
            this.memoList = memoList;
        }

        @Override
        public int getCount() {
            return memoList.size();
        }

        @Override
        public Object getItem(int i) {
            return memoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            // view_item.xml 획득
            view = inflater.inflate(R.layout.view_memo, null);

            // 객체 획득
            ImageView imgViewPic = view.findViewById(R.id.imgVIewPic);
            TextView txtMemo = view.findViewById(R.id.txtMemo);
            Button btnEdit = view.findViewById(R.id.btnEdit);
            Button btnDelete = view.findViewById(R.id.btnDelete);
            Button btnDetail = view.findViewById(R.id.btnDetail);

            // 원본에서 i번째 Item 획득
            final MemoBean memo = memoList.get(i);
            // 원본 데이터를 UI에 적용
            Uri uri = Uri.parse(memo.memoPicPath);
            imgViewPic.setImageURI(uri);
            txtMemo.setText(memo.memo);
            // 수정 버튼 이벤트
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ModifyMemoActivity.class);
                    intent.putExtra("memoId", memo.memoId);

                    startActivity(intent);
                }
            });
            // 삭제 버튼 이벤트
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/*
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("메모 삭제");
                    alertDialogBuilder.setMessage("정말로 삭제하시겠습니까?").setCancelable(false)
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MemberBean memberBean = FileDB.getLoginMember(mContext);
                                    FileDB.delMemo(getActivity(), memberBean.memId,i);
                                    // 어댑터 갱신
                                    adapter.notifyDataSetChanged();
                                    mLstMemo.setAdapter(adapter);
                                }
                            });*/

                    FileDB.delMemo(getActivity(), memo.memoId);
                    // 어댑터 갱신
                    adapter.notifyDataSetChanged();
                    mLstMemo.setAdapter(adapter);

                }
            });
            // 상세보기 버튼 이벤트
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ModifyMemoActivity.class);
                    intent.putExtra("memoId", memo.memoId);

                    startActivity(intent);
                }
            });

            return view; // 완성된 UI 리턴
        }
    }
}
