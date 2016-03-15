/**
 * 
 */
package com.mingseal.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * @author 商炎炳
 *
 */
public class CameraParaUtil {
	private static final String TAG = "CameraParaUtil";
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();
	private static CameraParaUtil myCamPara = null;

	private CameraParaUtil() {

	}

	public static CameraParaUtil getInstance() {
		if (myCamPara == null) {
			myCamPara = new CameraParaUtil();
			return myCamPara;
		} else {
			return myCamPara;
		}
	}

	/**
	 * 得到预览的Size
	 * 
	 * @param list
	 * @param th
	 * @param minWidth
	 * @return
	 */
	public Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth) {
		Collections.sort(list, sizeComparator);

		int i = 0;
		for (Size s : list) {
			if ((s.width >= minWidth) && equalRate(s, th)) {
				// Log.i(TAG, "PreviewSize:w = " + s.width + "h = " + s.height);
				break;
			}
			i++;
		}
		if (0 == list.size()) {
			return list.get(0);
		} else {
			return list.get(i - 1);
		}
	}

	/**
	 * 得到保存的图片的size
	 * 
	 * @param list
	 * @param th
	 * @param minWidth
	 * @return
	 */
	public Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth) {
		Collections.sort(list, sizeComparator);

		int i = 0;
		for (Size s : list) {
			if ((s.width >= minWidth) && equalRate(s, th)) {
				// Log.i(TAG, "PictureSize : w = " + s.width + "h = " +
				// s.height);
				break;
			}
			i++;
		}
		if (0 == list.size()) {
			// i = 0;// 如果没找到，就选最小的size
			return list.get(0);
		} else {
			return list.get(i - 6);// (1920*1080)
		}
	}

	public boolean equalRate(Size s, float rate) {
		float r = (float) (s.width) / (float) (s.height);
		if (Math.abs(r - rate) <= 0.03) {
			return true;
		} else {
			return false;
		}
	}

	public class CameraSizeComparator implements Comparator<Camera.Size> {
		public int compare(Size lhs, Size rhs) {
			// TODO Auto-generated method stub
			if (lhs.width == rhs.width) {
				return 0;
			} else if (lhs.width > rhs.width) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 打印支持的previewSizes
	 * 
	 * @param params
	 */
	public void printSupportPreviewSize(Camera.Parameters params) {
		List<Size> previewSizes = params.getSupportedPreviewSizes();
		for (int i = 0; i < previewSizes.size(); i++) {
			Size size = previewSizes.get(i);
			Log.i(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
		}

	}

	/**
	 * 打印支持的pictureSizes
	 * 
	 * @param params
	 */
	public void printSupportPictureSize(Camera.Parameters params) {
		List<Size> pictureSizes = params.getSupportedPictureSizes();
		for (int i = 0; i < pictureSizes.size(); i++) {
			Size size = pictureSizes.get(i);
			Log.i(TAG, "pictureSizes:width = " + size.width + " height = " + size.height);
		}
	}

	/**
	 * 打印支持的聚焦模式
	 * 
	 * @param params
	 */
	public void printSupportFocusMode(Camera.Parameters params) {
		List<String> focusModes = params.getSupportedFocusModes();
		for (String mode : focusModes) {
			Log.i(TAG, "focusModes--" + mode);
		}
	}

}
