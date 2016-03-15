/**
 * 
 */
package com.mingseal.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.mingseal.data.db.DBInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

/**
 * @author 商炎炳
 *
 */
public class FileDatabase {
	private static String storagePath = "/mnt/sdcard/rectPhoto/";
	private static final String TAG = "FileUtil";

	/**
	 * 如果文件目录不存在的话，就创建文件目录
	 * 
	 * @param file
	 */
	public static void fileDirectory(File file) {
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param fCopyBefore
	 *            源文件
	 * @param fCopyAfter
	 *            复制到的新文件
	 */
	public static void fileChannelCopy(File fCopyBefore, File fCopyAfter) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(fCopyBefore);
			fo = new FileOutputStream(fCopyAfter);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存Bitmap到sdcard
	 * 
	 * @param context
	 * @param bitmap
	 */
	public static void saveBitmap(Context context, Bitmap bitmap) {
		File file = new File(storagePath);
		if (!file.exists()) {
			file.mkdir();
		}

		long dataTake = System.currentTimeMillis();
		String jpegName = storagePath + dataTake + ".jpg";
		// Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			ToastUtil.displayPromptInfo(context, "图片保存在" + jpegName);
		} catch (IOException e) {
			ToastUtil.displayPromptInfo(context, "图片保存失败");
			e.printStackTrace();
		}

	}

	/**
	 * 导出数据库文件
	 * 
	 * @param context
	 */
	public static void exportToFile(Context context) {
		File sourceFile = new File(DBInfo.DB.DB_LOCATION);
		File savedFilePath = new File(DBInfo.DB.DB_SAVED_LOCATION_DIRECTORY);
		fileDirectory(savedFilePath);
		File savedFile = new File(DBInfo.DB.DB_SAVED_LOCATION);
		fileChannelCopy(sourceFile, savedFile);
		ToastUtil.displayPromptInfo(context, "数据库文件成功拷贝");
	}
}
