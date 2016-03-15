/**
 * 
 */
package com.mingseal.utils;

import java.io.IOException;
import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author 商炎炳
 *
 */
public class CameraInterface {

	private static final String TAG = "CameraInterface";
	private Camera mCamera;
	private Camera.Parameters mParams;
	private boolean isPreviewing = false;
	private float mPreviwRate = -1f;
	private static CameraInterface mCameraInterface;
	private Context context;

	public interface CamOpenOverCallback {
		public void cameraHasOpened();
	}

	private CameraInterface(Context context) {
		this.context = context;
	}

	public static synchronized CameraInterface getInstance(Context context) {
		if (mCameraInterface == null) {
			mCameraInterface = new CameraInterface(context);
		}
		return mCameraInterface;
	}

	/**
	 * 打开Camera
	 * 
	 * @param callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback) {
		Log.i(TAG, "Camera open....");
		try {
			mCamera = Camera.open();
			Log.i(TAG, "Camera open over....");
			callback.cameraHasOpened();
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.displayPromptInfo(context, "请打开摄像头");
		}
	}

	/**
	 * 开启预览
	 * 
	 * @param holder
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceHolder holder, float previewRate) {
		Log.i(TAG, "doStartPreview...");
		if (isPreviewing) {
			mCamera.stopPreview();
			return;
		}
		if (mCamera != null) {

			mParams = mCamera.getParameters();
			mParams.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式
			CameraParaUtil.getInstance().printSupportPictureSize(mParams);
			CameraParaUtil.getInstance().printSupportPreviewSize(mParams);
			// 设置PreviewSize和PictureSize
			Size pictureSize = CameraParaUtil.getInstance().getPropPictureSize(mParams.getSupportedPictureSizes(),
					previewRate, 800);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			Size previewSize = CameraParaUtil.getInstance().getPropPreviewSize(mParams.getSupportedPreviewSizes(),
					previewRate, 800);
			mParams.setPreviewSize(previewSize.width, previewSize.height);

			mCamera.setDisplayOrientation(90);

			CameraParaUtil.getInstance().printSupportFocusMode(mParams);
			List<String> focusModes = mParams.getSupportedFocusModes();
			if (focusModes.contains("continuous-video")) {
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setParameters(mParams);

			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();// 开启预览
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			isPreviewing = true;
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); // 重新get一次
			Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width + "Height = "
					+ mParams.getPreviewSize().height);
			Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width + "Height = "
					+ mParams.getPictureSize().height);
		}
	}

	/**
	 * 停止预览，释放Camera
	 */
	public void doStopCamera() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mPreviwRate = -1f;
			mCamera.release();
			mCamera = null;
		}
	}
	
	/**
	 * 设置焦距
	 */
	public void setZoom(int zoomValue) {
		try {
			Parameters params = mCamera.getParameters();
			final int MAX = params.getMaxZoom();// (0-99)
			if (MAX == 0) {
				return;
			}
			if(zoomValue>MAX){
				zoomValue=MAX;
			}
//			Log.d(TAG, "-----------------MAX:" + MAX + "   params : " + zoomValue);
			params.setZoom(zoomValue);
			mCamera.setParameters(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拍照
	 */
	public void doTakePicture() {
		if (isPreviewing && (mCamera != null)) {
			mCamera.takePicture(null, null, mJpegPictureCallback);
		}
	}

	/**
	 * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
	 */
	ShutterCallback mShutterCallback = new ShutterCallback() {
		// 快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(TAG, "myShutterCallback:onShutter...");
		}
	};
	/**
	 * 拍摄的未压缩原数据的回调,可以为null
	 */
	PictureCallback mRawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myRawCallback:onPictureTaken...");

		}
	};
	/**
	 * 对jpeg图像数据的回调,最重要的一个回调
	 */
	PictureCallback mJpegPictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if (null != data) {
				b = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			// 保存图片到sdcard
			if (null != b) {
				// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
				// 90)失效。
				// 图片竟然不能旋转了，故这里要旋转下
				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				FileDatabase.saveBitmap(context,rotaBitmap);
			}
			// 再次进入预览
			mCamera.stopPreview();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mCamera.startPreview();
			isPreviewing = true;
		}
	};

}
