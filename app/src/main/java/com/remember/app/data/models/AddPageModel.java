package com.remember.app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddPageModel implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("secondname")
    private String secondName;
    @SerializedName("thirtname")
    private String thirdName;
    @SerializedName("name")
    private String name;
    @SerializedName("comment")
    private String comment;
    @SerializedName("coords")
    private String coords;
    @SerializedName("oblast")
    private String area;
    @SerializedName("rajon")
    private String district;
    @SerializedName("gorod")
    private String city;
    @SerializedName("nazvaklad")
    private String cemeteryName;
    @SerializedName("uchastok")
    private String spotId;
    @SerializedName("nummogil")
    private String graveId;
    @SerializedName("datarod")
    private String birthDate;
    @SerializedName("datasmert")
    private String deathDate;
    @SerializedName("optradio")
    private String optradio;
    @SerializedName("star")
    private String star;
    @SerializedName("flag")
    private String flag;
    @SerializedName("religiya")
    private String religion;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("picture_data")
    private String pictureData;

    public String getPictureData() {
        return pictureData;
    }

    public void setPictureData(String pictureData) {
        this.pictureData = pictureData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCemeteryName() {
        return cemeteryName;
    }

    public void setCemeteryName(String cemeteryName) {
        this.cemeteryName = cemeteryName;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getGraveId() {
        return graveId;
    }

    public void setGraveId(String graveId) {
        this.graveId = graveId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getOptradio() {
        return optradio;
    }

    public void setOptradio(String optradio) {
        this.optradio = optradio;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AddPageModel() {
    }

    private AddPageModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        secondName = in.readString();
        thirdName = in.readString();
        name = in.readString();
        comment = in.readString();
        coords = in.readString();
        area = in.readString();
        district = in.readString();
        city = in.readString();
        cemeteryName = in.readString();
        spotId = in.readString();
        graveId = in.readString();
        birthDate = in.readString();
        deathDate = in.readString();
        optradio = in.readString();
        star = in.readString();
        flag = in.readString();
        religion = in.readString();
        userId = in.readString();
        pictureData = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(secondName);
        dest.writeString(thirdName);
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeString(coords);
        dest.writeString(area);
        dest.writeString(district);
        dest.writeString(city);
        dest.writeString(cemeteryName);
        dest.writeString(spotId);
        dest.writeString(graveId);
        dest.writeString(birthDate);
        dest.writeString(deathDate);
        dest.writeString(optradio);
        dest.writeString(star);
        dest.writeString(flag);
        dest.writeString(religion);
        dest.writeString(userId);
        dest.writeString(pictureData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddPageModel> CREATOR = new Parcelable.Creator<AddPageModel>() {
        @Override
        public AddPageModel createFromParcel(Parcel in) {
            return new AddPageModel(in);
        }

        @Override
        public AddPageModel[] newArray(int size) {
            return new AddPageModel[size];
        }
    };
}