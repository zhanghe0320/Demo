package com.demo.fragment.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.fragment.dao.FragmentBean;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.FragmentViewholder> {
    private Activity mActivity;
    private String mType;
    private List<FragmentBean> mData = new ArrayList<FragmentBean>();
    private static final String TAG = "FragmentAdapter";


    private FragmentAdapter.AdapterItemOnclick mOnclick = null;


    /**
     * 需要监听界面点击事件
     *
     * @param activity
     * @param type
     * @param data
     * @param onclick
     */
    public FragmentAdapter(Activity activity, String type, List data, FragmentAdapter.AdapterItemOnclick onclick) {
        this.mActivity = activity;
        this.mType = type;
        this.mData = data;
        mOnclick = onclick;


    }

    /**
     * 不需要监听界面点击事件
     *
     * @param activity
     * @param type
     * @param data
     */
    public FragmentAdapter(Activity activity, String type, List data) {
        this.mActivity = activity;
        this.mType = type;
        this.mData = data;
    }


    @Override
    public void onBindViewHolder(@NonNull final FragmentAdapter.FragmentViewholder holder, int position) {

        FragmentBean info = mData.get(position);
        holder.mTitle.setText(info.getmTitle() + "");
        holder.mTextBtn.setText(info.getmTextBtn() + "");
        holder.mTextText.setText(info.getmTextText() + "");
        holder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnclick == null) {
                    return;
                }
                mOnclick.TitleOnclick();
            }
        });

        holder.mTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnclick == null) {
                    return;
                }
                mOnclick.BtnOnclick();
            }
        });

        holder.mTextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnclick == null) {
                    return;
                }
                mOnclick.TextOncklick();
            }
        });


        holder.mTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnclick == null) {
                    return;
                }
                mOnclick.ItemOnclick(info);
            }
        });

    }

    @Override
    public FragmentAdapter.FragmentViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentAdapter.FragmentViewholder viewHolder = new FragmentAdapter.FragmentViewholder(LayoutInflater.from(mActivity).inflate(R.layout.fragment_adapter_new, parent, false));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List data) {
        // mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentAdapter.FragmentViewholder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 内部类做处理
     */
    class FragmentViewholder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mImageView;
        ImageView mTextImg;
        TextView mTextBtn;
        TextView mTextText;
        RelativeLayout mLinearyout;

        public FragmentViewholder(@NonNull View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.fragment_title);
            mImageView = (ImageView) itemView.findViewById(R.id.fragment_filter_img);
            mTextBtn = (TextView) itemView.findViewById(R.id.fragment_btn);
            mTextImg = (ImageView) itemView.findViewById(R.id.fragment_text_img);
            mTextText = (TextView) itemView.findViewById(R.id.fragment_text_text);
            mLinearyout = (RelativeLayout) itemView.findViewById(R.id.fragment_linearyout);
        }


    }


    /**
     * 界面点击事件
     */
    public interface AdapterItemOnclick {
        void TitleOnclick();

        void TextOncklick();

        void BtnOnclick();

        void ItemOnclick(FragmentBean fragmentBean);
    }


}
