package com.remember.app.data.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MemoryPageModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("secondname")
    @Expose
    private String secondName;
    @SerializedName("thirtname")
    @Expose
    private String thirdName;
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
    @SerializedName("sector")
    @Expose
    private String sector;
    @SerializedName("datarod")
    @Expose
    private String dateBirth;
    @SerializedName("datasmert")
    @Expose
    private String dateDeath;
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

    @Expose
    private boolean isShowMore = false;

    public String getFullName() {
        return name + " " + secondName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecondName() {
        return secondName == null ? "" : secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName == null ? "" : thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getName() {
        return name == null ? "" : name;
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
        return coords == null ? "" : coords;
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
        return gorod == null ? "" : gorod;
    }

    public void setGorod(String gorod) {
        this.gorod = gorod;
    }

    public String getNazvaklad() {
        return nazvaklad == null ? "" : nazvaklad;
    }

    public void setNazvaklad(String nazvaklad) {
        this.nazvaklad = nazvaklad;
    }

    public String getUchastok() {
        return uchastok == null ? "" : uchastok;
    }

    public void setUchastok(String uchastok) {
        this.uchastok = uchastok;
    }

    public String getNummogil() {
        return nummogil == null ? "" : nummogil;
    }

    public void setNummogil(String nummogil) {
        this.nummogil = nummogil;
    }

    public String getSector() {
        return sector == null ? "" : sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getDateDeath() {
        return dateDeath;
    }

    public void setDateDeath(String dateDeath) {
        this.dateDeath = dateDeath;
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

    public boolean isShowMore() {
        return isShowMore;
    }

    public void setShowMore(boolean showMore) {
        isShowMore = showMore;
    }

    public MemoryPageModel() {
    }

    public MemoryPageModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        secondName = in.readString();
        thirdName = in.readString();
        name = in.readString();
        comment = in.readString();
        coords = in.readString();
        oblast = in.readString();
        rajon = in.readString();
        gorod = in.readString();
        nazvaklad = in.readString();
        uchastok = in.readString();
        nummogil = in.readString();
        dateBirth = in.readString();
        dateDeath = in.readString();
        optradio = in.readString();
        picture = in.readString();
        pictureCut = in.readString();
        pictureData = in.readString();
        status = (Object) in.readValue(Object.class.getClassLoader());
        star = in.readString();
        flag = in.readString();
        religiya = in.readString();
        userId = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isShowMore = in.readBoolean();
        } else {
            isShowMore = in.readInt() == 0;
        }
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
        dest.writeString(oblast);
        dest.writeString(rajon);
        dest.writeString(gorod);
        dest.writeString(nazvaklad);
        dest.writeString(uchastok);
        dest.writeString(nummogil);
        dest.writeString(dateBirth);
        dest.writeString(dateDeath);
        dest.writeString(optradio);
        dest.writeString(picture);
        dest.writeString(pictureCut);
        dest.writeString(pictureData);
        dest.writeValue(status);
        dest.writeString(star);
        dest.writeString(flag);
        dest.writeString(religiya);
        dest.writeString(userId);
        dest.writeInt(isShowMore ? 1 : 0);
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

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                secondName,
                thirdName,
                name,
                comment,
                coords,
                oblast,
                rajon,
                gorod,
                nazvaklad,
                uchastok,
                nummogil,
                dateBirth,
                dateDeath,
                optradio,
                picture,
                pictureCut,
                pictureData,
                status,
                star,
                flag,
                religiya,
                userId,
                isShowMore);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MemoryPageModel)) {
            return false;
        }
        MemoryPageModel memoryPageModel = (MemoryPageModel) obj;
        return memoryPageModel.id.equals(id) &&
                memoryPageModel.secondName.equals(secondName) &&
                memoryPageModel.thirdName.equals(thirdName) &&
                memoryPageModel.name.equals(name) &&
                memoryPageModel.comment.equals(comment) &&
                memoryPageModel.coords.equals(coords) &&
                memoryPageModel.oblast.equals(oblast) &&
                memoryPageModel.rajon.equals(rajon) &&
                memoryPageModel.gorod.equals(gorod) &&
                memoryPageModel.nazvaklad.equals(nazvaklad) &&
                memoryPageModel.uchastok.equals(uchastok) &&
                memoryPageModel.nummogil.equals(nummogil) &&
                memoryPageModel.dateBirth.equals(dateBirth) &&
                memoryPageModel.dateDeath.equals(dateDeath) &&
                memoryPageModel.optradio.equals(optradio) &&
                memoryPageModel.picture.equals(picture) &&
                memoryPageModel.pictureCut.equals(pictureCut) &&
                memoryPageModel.pictureData.equals(pictureData) &&
                memoryPageModel.status.equals(status) &&
                memoryPageModel.star.equals(star) &&
                memoryPageModel.flag.equals(flag) &&
                memoryPageModel.religiya.equals(religiya) &&
                memoryPageModel.userId.equals(userId) &&
                memoryPageModel.isShowMore == isShowMore;
    }

    public static final DiffUtil.ItemCallback<MemoryPageModel> DIFF_MEMORY_PAGE_CALLBACK = new DiffUtil.ItemCallback<MemoryPageModel>() {
        @Override
        public boolean areItemsTheSame(MemoryPageModel oldItem, MemoryPageModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(MemoryPageModel oldItem, MemoryPageModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
