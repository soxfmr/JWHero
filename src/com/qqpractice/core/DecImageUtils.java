package com.qqpractice.core;

import java.io.ByteArrayInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecImageUtils {
	public Bitmap getImage(byte[] bytes) {
		Bitmap bitmap = null;
		if(bytes != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			bitmap = BitmapFactory.decodeStream(bis);
		}
		return bitmap;
	}
}
