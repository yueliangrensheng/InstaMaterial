package com.yazao.instamaterial.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by shaopingzhai on 15/7/27.
 */
public class Beans implements Parcelable {
    private List<String> games;
    public String[] names;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.games);
        dest.writeStringArray(this.names);
    }

    public Beans() {
    }

    protected Beans(Parcel in) {
        this.games = in.createStringArrayList();
        this.names = in.createStringArray();
    }

    public static final Parcelable.Creator<Beans> CREATOR = new Parcelable.Creator<Beans>() {
        public Beans createFromParcel(Parcel source) {
            return new Beans(source);
        }

        public Beans[] newArray(int size) {
            return new Beans[size];
        }
    };
}
