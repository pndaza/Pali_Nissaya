package mm.pndaza.palitawnissaya.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaliBook implements Parcelable {
    private String id;
    private String name;
    private int firstPage;
    private int lastPage;


    public PaliBook(String id, String name, int firstPage, int lastPage) {
        this.id = id;
        this.name = name;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
    }

    protected PaliBook(Parcel in) {
        id = in.readString();
        name = in.readString();
        firstPage = in.readInt();
        lastPage = in.readInt();
    }

    public static final Creator<PaliBook> CREATOR = new Creator<PaliBook>() {
        @Override
        public PaliBook createFromParcel(Parcel in) {
            return new PaliBook(in);
        }

        @Override
        public PaliBook[] newArray(int size) {
            return new PaliBook[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(firstPage);
        dest.writeInt(lastPage);
    }
}
