package com.hyperether.getgoing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyperether.getgoing.util.Constants;

public class CBDataFrame implements Parcelable {

    private static CBDataFrame instance = null;

    private int profileId;
    private String profileName;
    private int measurementSystemId;
    private int age;
    private int weight;
    private int height;

    private Constants.gender gender;

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getMeasurementSystemId() {
        return measurementSystemId;
    }

    public void setMeasurementSystemId(int measurementSystemId) {
        this.measurementSystemId = measurementSystemId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Constants.gender getGender() {
        return gender;
    }

    public void setGender(Constants.gender gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.profileId);
        out.writeString(this.profileName);
        out.writeInt(this.measurementSystemId);
        out.writeInt(this.weight);
        out.writeInt(this.age);
    }

    public static final Parcelable.Creator<CBDataFrame> CREATOR =
            new Parcelable.Creator<CBDataFrame>() {
                @Override
                public CBDataFrame createFromParcel(Parcel in) {
                    return new CBDataFrame(in);
                }

                @Override
                public CBDataFrame[] newArray(int size) {
                    return new CBDataFrame[size];
                }
            };

    public CBDataFrame(Parcel in) {
        this.profileId = in.readInt();
        this.profileName = in.readString();
        this.measurementSystemId = in.readInt();
        this.weight = in.readInt();
        this.age = in.readInt();
    }

    public CBDataFrame() {

    }

    public CBDataFrame(boolean initZeros) {
        this.age = 0;
        this.gender = Constants.gender.Male;
        this.height = 0;
        this.weight = 0;
        measurementSystemId = Constants.METRIC;
        profileName = "";
        profileId = 0;
    }

    public static CBDataFrame getInstance() {
        if (instance == null) {
            instance = new CBDataFrame(true);
        }
        return instance;
    }
}
