package io.dwak.freight.sample;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableModel implements Parcelable{
  private String myString = "lolString";


  protected ParcelableModel(Parcel in) {
    myString = in.readString();
  }

  public static final Creator<ParcelableModel> CREATOR = new Creator<ParcelableModel>() {
    @Override
    public ParcelableModel createFromParcel(Parcel in) {
      return new ParcelableModel(in);
    }

    @Override
    public ParcelableModel[] newArray(int size) {
      return new ParcelableModel[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(myString);
  }
}
