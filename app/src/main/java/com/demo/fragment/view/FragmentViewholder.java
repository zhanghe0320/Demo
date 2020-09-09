package com.demo.fragment.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;

/**
 *
 * 如果viewholder 可以通用，可以拿出来
 */
public class FragmentViewholder extends RecyclerView.ViewHolder {


    TextView mTitle;
    ImageView mImageView;
    ImageView mTextImg;
    TextView mTextBtn;
    TextView mTextText;
    LinearLayout mLinearyout;

    public FragmentViewholder(@NonNull View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.fragment_title);
        mImageView = (ImageView) itemView.findViewById(R.id.fragment_filter_img);
        mTextBtn = (TextView) itemView.findViewById(R.id.fragment_btn);
        mTextImg = (ImageView) itemView.findViewById(R.id.fragment_text_img);
        mTextText = (TextView) itemView.findViewById(R.id.fragment_text_text);
        mLinearyout = (LinearLayout) itemView.findViewById(R.id.fragment_linearyout);
    }
}
