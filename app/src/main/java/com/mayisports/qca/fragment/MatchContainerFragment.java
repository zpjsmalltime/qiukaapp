package com.mayisports.qca.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;

/**
 *
 * 赛事模块   嵌入具体内容容器 暂时无用
 * Created by zhangpengju on 2018/5/2.
 */

public class MatchContainerFragment extends Fragment {



    View viewRoot;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(QCaApplication.getContext(), R.layout.match_contaner_ly,null);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
