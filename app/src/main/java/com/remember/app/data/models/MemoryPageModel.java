package com.remember.app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemoryPageModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("secondname")
    @Expose
    private String secondname;
    @SerializedName("thirtname")
    @Expose
    private String thirtname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("coords")
    @Expose
    private String coords;
    @SerializedName("oblast")
    @Expose
    private String oblast;
    @SerializedName("rajon")
    @Expose
    private String rajon;
    @SerializedName("gorod")
    @Expose
    private String gorod;
    @SerializedName("nazvaklad")
    @Expose
    private String nazvaklad;
    @SerializedName("uchastok")
    @Expose
    private String uchastok;
    @SerializedName("nummogil")
    @Expose
    private String nummogil;
    @SerializedName("datarod")
    @Expose
    private String datarod;
    @SerializedName("datasmert")
    @Expose
    private String datasmert;
    @SerializedName("optradio")
    @Expose
    private String optradio;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("picture_cut")
    @Expose
    private String pictureCut;
    @SerializedName("picture_data")
    @Expose
    private String pictureData;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("star")
    @Expose
    private String star;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("religiya")
    @Expose
    private String religiya;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getThirtname() {
        return thirtname;
    }

    public void setThirtname(String thirtname) {
        this.thirtname = thirtname;
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

    public String getOblast() {
        return oblast;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }

    public String getRajon() {
        return rajon;
    }

    public void setRajon(String rajon) {
        this.rajon = rajon;
    }

    public String getGorod() {
        return gorod;
    }

    public void setGorod(String gorod) {
        this.gorod = gorod;
    }

    public String getNazvaklad() {
        return nazvaklad;
    }

    public void setNazvaklad(String nazvaklad) {
        this.nazvaklad = nazvaklad;
    }

    public String getUchastok() {
        return uchastok;
    }

    public void setUchastok(String uchastok) {
        this.uchastok = uchastok;
    }

    public String getNummogil() {
        return nummogil;
    }

    public void setNummogil(String nummogil) {
        this.nummogil = nummogil;
    }

    public String getDatarod() {
        return datarod;
    }

    public void setDatarod(String datarod) {
        this.datarod = datarod;
    }

    public String getDatasmert() {
        return datasmert;
    }

    public void setDatasmert(String datasmert) {
        this.datasmert = datasmert;
    }

    public String getOptradio() {
        return optradio;
    }

    public void setOptradio(String optradio) {
        this.optradio = optradio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictureCut() {
        return pictureCut;
    }

    public void setPictureCut(String pictureCut) {
        this.pictureCut = pictureCut;
    }

    public String getPictureData() {
        return pictureData;
    }

    public void setPictureData(String pictureData) {
        this.pictureData = pictureData;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
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

    public String getReligiya() {
        return religiya;
    }

    public void setReligiya(String religiya) {
        this.religiya = religiya;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    protected MemoryPageModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        secondname = in.readString();
        thirtname = in.readString();
        name = in.readString();
        comment = in.readString();
        coords = in.readString();
        oblast = in.readString();
        rajon = in.readString();
        gorod = in.readString();
        nazvaklad = in.readString();
        uchastok = in.readString();
        nummogil = in.readString();
        datarod = in.readString();
        datasmert = in.readString();
        optradio = in.readString();
        picture = in.readString();
        pictureCut = in.readString();
        pictureData = in.readString();
        status = (Object) in.readValue(Object.class.getClassLoader());
        star = in.readString();
        flag = in.readString();
        religiya = in.readString();
        userId = in.readString();
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
        dest.writeString(secondname);
        dest.writeString(thirtname);
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeString(coords);
        dest.writeString(oblast);
        dest.writeString(rajon);
        dest.writeString(gorod);
        dest.writeString(nazvaklad);
        dest.writeString(uchastok);
        dest.writeString(nummogil);
        dest.writeString(datarod);
        dest.writeString(datasmert);
        dest.writeString(optradio);
        dest.writeString(picture);
        dest.writeString(pictureCut);
        dest.writeString(pictureData);
        dest.writeValue(status);
        dest.writeString(star);
        dest.writeString(flag);
        dest.writeString(religiya);
        dest.writeString(userId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MemoryPageModel> CREATOR = new Parcelable.Creator<MemoryPageModel>() {
        @Override
        public MemoryPageModel createFromParcel(Parcel in) {
            return new MemoryPageModel(in);
        }

        @Override
        public MemoryPageModel[] newArray(int size) {
            return new MemoryPageModel[size];
        }
    };
}
