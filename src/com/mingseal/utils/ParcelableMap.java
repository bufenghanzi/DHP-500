package com.mingseal.utils;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;

public class ParcelableMap implements Parcelable {

	public HashMap<Integer, PointGlueAloneParam> map;


	public HashMap<Integer, PointGlueAloneParam> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, PointGlueAloneParam> map) {
		this.map = map;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeMap(map);

	}

	public static final Parcelable.Creator<ParcelableMap> CREATOR = new Parcelable.Creator<ParcelableMap>() {

		@Override
		public ParcelableMap createFromParcel(Parcel source) {
			ParcelableMap p = new ParcelableMap();
			p.map = source.readHashMap(HashMap.class.getClassLoader());
			return p;
		}

		@Override
		public ParcelableMap[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};


	@Override
	public String toString() {
		return "ParcelableMap [map=" + map + "]";
	}
	
}
