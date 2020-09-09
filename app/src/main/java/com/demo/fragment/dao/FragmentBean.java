package com.demo.fragment.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class FragmentBean implements Parcelable {
    private String mTitle;//文字描述1
    private String mImageViewUrl;
    private String mTextImgUrl;
    private String mTextBtn;//按钮
    private String mTextText;//文字描述

    public FragmentBean(String title, String imageViewUrl, String textImgUrl, String textBtn, String textText) {
        this.mTitle = title;//文字描述1
        this.mImageViewUrl = imageViewUrl;
        this.mTextImgUrl = textImgUrl;
        this.mTextBtn = textBtn;//按钮
        this.mTextText = textText;//文字描述
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImageViewUrl() {
        return mImageViewUrl;
    }

    public void setmImageViewUrl(String mImageViewUrl) {
        this.mImageViewUrl = mImageViewUrl;
    }

    public String getmTextImgUrl() {
        return mTextImgUrl;
    }

    public void setmTextImgUrl(String mTextImgUrl) {
        this.mTextImgUrl = mTextImgUrl;
    }

    public String getmTextBtn() {
        return mTextBtn;
    }

    public void setmTextBtn(String mTextBtn) {
        this.mTextBtn = mTextBtn;
    }

    public String getmTextText() {
        return mTextText;
    }

    public void setmTextText(String mTextText) {
        this.mTextText = mTextText;
    }

    public static Creator<FragmentBean> getCREATOR() {
        return CREATOR;
    }

    protected FragmentBean(Parcel in) {
        mTitle = in.readString();
        mImageViewUrl = in.readString();
        mTextImgUrl = in.readString();
        mTextBtn = in.readString();
        mTextText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mImageViewUrl);
        dest.writeString(mTextImgUrl);
        dest.writeString(mTextBtn);
        dest.writeString(mTextText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FragmentBean> CREATOR = new Creator<FragmentBean>() {
        @Override
        public FragmentBean createFromParcel(Parcel in) {
            return new FragmentBean(in);
        }

        @Override
        public FragmentBean[] newArray(int size) {
            return new FragmentBean[size];
        }
    };

    @Override
    public String toString() {
        return "FragmentBean{" +
                "mTitle='" + mTitle + '\'' +
                ", mImageViewUrl='" + mImageViewUrl + '\'' +
                ", mTextImgUrl='" + mTextImgUrl + '\'' +
                ", mTextBtn='" + mTextBtn + '\'' +
                ", mTextText='" + mTextText + '\'' +
                '}';
    }


}
