//package com.mingseal.data.manager;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.StringTokenizer;
//
//import android.os.Environment;
//
//import com.mingseal.data.param.TaskParam;
//import com.mingseal.data.point.IOPort;
//import com.mingseal.data.point.Point;
//import com.mingseal.data.point.PointType;
//import com.mingseal.data.point.glueparam.PointGlueAloneParam;
//import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
//import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
//import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
//import com.mingseal.data.point.glueparam.PointGlueLineArcParam;
//import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
//import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
//import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
//import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
//import com.mingseal.data.point.weldparam.PointWeldAloneParam;
//import com.mingseal.data.point.weldparam.PointWeldBaseParam;
//
//class FileNameSelector implements FilenameFilter {
//	String extension = ".";
//
//	public FileNameSelector(String fileExtensionNoDot) {
//		extension += fileExtensionNoDot;
//	}
//
//	@Override
//	public boolean accept(File dir, String filename) {
//		return filename.endsWith(extension);
//	}
//}
//
///** 
//* @ClassName: TaskMgr 
//* @Description: 任务管理
//* @author lyq
//* @date 2015年6月15日 下午4:09:41 
//*  
//*/
//public class TaskMgr {
//
//	private static String SD_ROOT_PATH = null;
//	private static String LA_ROOT_PATH = null;
//	private static String ROBOT_TYPE_PATH = null;
//	private static String TYPE_NUM_PATH = null;
//
//	private File[] files;
//	private String mStrRobotName;
//	private String m_taskPath;
//
//	TaskMgr() {
//	}
//
//	/**
//	 * 判断SDCard是否存在[当没有外挂SD卡时,内置ROM也被识别为存在SD卡]
//	 * 
//	 * @return true:存在 false:不存在
//	 */
//	public static boolean isSdCardExist() {
//		return Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED);
//	}
//
//	/**
//	 * 
//	 * @param value
//	 *            布尔类型值
//	 * @return 1:true 2:false
//	 */
//	public int booleanToInt(boolean value) {
//		if (value) {
//			return 1;
//		} else {
//			return 0;
//		}
//	}
//
//	/**
//	 * 
//	 * @param value
//	 *            整型真值
//	 * @return 布尔真值
//	 */
//	public boolean intToBoolean(int value) {
//		if (0 == value) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	/**
//	 * @param strTaskPath
//	 *            任务工作路径
//	 * @param strRobotName
//	 *            机器型号名称字符串
//	 * @return 初始化 true:初始化成功 false:初始化失败
//	 */
//	public boolean InitTask(String strTaskPath, String strRobotName) {
//
//		String TaskRootFolderName = "DHP-400LA_Task";
//		// mStrRobotName = String.valueOf(OrderParam.getM_nRobotType());
//		// String SecondFolderName =
//		// String.valueOf(OrderParam.getM_RobotTypeNum());
//		mStrRobotName = "LA703B";
//		String SecondFolderName = "1";
//
//		SD_ROOT_PATH = Environment.getExternalStorageDirectory()
//				+ File.separator;
//
//		LA_ROOT_PATH = SD_ROOT_PATH + File.separator + TaskRootFolderName
//				+ File.separator;
//
//		ROBOT_TYPE_PATH = LA_ROOT_PATH + mStrRobotName + File.separator;
//
//		TYPE_NUM_PATH = ROBOT_TYPE_PATH + SecondFolderName + File.separator;
//
//		if (!isSdCardExist()) {
//			System.out.println("未发现SD存储卡插入!");
//			return false;
//		} else {
//
//			// File dirTaskFile = new File(LA_TASK_PATH);
//			// if (!dirTaskFile.exists()) {
//			// dirTaskFile.mkdir();
//			// }
//			//
//			// File dirFirstFile = new File(ROBOT_TYPE_PATH);
//			// if (!dirFirstFile.exists()) {
//			// dirFirstFile.mkdir();
//			// }
//			//
//			// File dirSecondFile = new File(TYPE_NUM_PATH);
//			// if (!dirSecondFile.exists()) {
//			// dirSecondFile.mkdir();
//			// }
//
//			File taskPathFile = new File(TYPE_NUM_PATH);
//			taskPathFile.mkdirs();// 创建多级目录,若目录已存在,则继续创建子目录
//
//			// 获取当前路径下所有文件,并存储在文件数组中
//			files = new File(TYPE_NUM_PATH).listFiles(new FileNameSelector(
//					"txt"));
//		}
//		return true;
//	}
//
//	/**
//	 * 检查工作任务是否与机器人匹配
//	 *
//	 * @param strTask
//	 *            任务文件路径
//	 * @return true:匹配成功 false:匹配失败
//	 */
//	public boolean CheckTask(String strTask) {
//		String strRobotType = null;
//		int start, end = 0;
//		try (FileInputStream file = new FileInputStream(strTask)) {
//			String tmpString = file.toString();
//			start = tmpString.indexOf("[Robot]");
//			end = tmpString.indexOf("*");
//			// 获取从[Robot]字符串到第一个*中间的子字符串,此即为机器型号
//			strRobotType = tmpString.substring(start, end);
//			// 匹配是否符合
//			if (strRobotType.equals(mStrRobotName)) {
//				return true;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//		return false;
//	}
//
//	/**
//	 * 解析任务
//	 *
//	 * @param index
//	 *            任务索引号
//	 * @param taskParam
//	 *            任务参数
//	 * @param pointMgr
//	 *            点集
//	 * @return true:解析成功 false:解析失败
//	 */
//	public boolean ReadTask(int index, TaskParam taskParam, PointMgr pointMgr) {
//		try (FileInputStream fileInputStream = new FileInputStream(files[index])) {
//			InputStreamReader inputStreamReader = new InputStreamReader(
//					fileInputStream, "UTF-8");
//			BufferedReader bufferedReader = new BufferedReader(
//					inputStreamReader);
//			String line = "";
//			while ((line = bufferedReader.readLine()) != null) {
//				// 判断并去掉标签
//				if (line.contains("[Robot]")) {
//					ReadTaskParam(line.substring(7), taskParam);
//				} else if (line.contains("[Point]")) {
//					ReadTaskPoint(line.substring(7), pointMgr);
//				}
//			}
//			bufferedReader.close();
//			inputStreamReader.close();
//			fileInputStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			System.out.println("Done");
//		}
//		return true;
//	}
//
//	/**
//	 * 新建任务
//	 *
//	 * @param strTaskName
//	 *            任务名
//	 * @param index
//	 *            索引值
//	 * @return true:新建成功 false:新建失败或文件已存在
//	 */
//	public boolean AddTask(String strTaskName, int index) {
//		File newFile = new File(TYPE_NUM_PATH, strTaskName);
//		if (newFile.exists()) {
//			return false;
//		} else {
//			try {
//				newFile.createNewFile();
//				return true;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//	}
//
//	/**
//	 * 根据索引值删除相应文件
//	 *
//	 * @param index
//	 *            索引值
//	 * @return 删除文件是否成功 true:删除成功 false:删除失败或文件不存在
//	 */
//	public boolean DeleteTask(int index) {
//		int taskCount = files.length;
//		if (files[index].exists()) {
//			if (files[index].delete()) {
//				for (int i = index; i < taskCount - 1; i++) {
//					files[i] = files[i + 1];
//				}
//				RefreshTaskList();
//				// 删除成功
//				return true;
//			} else {
//				// 删除失败
//				return false;
//			}
//		} else {
//			// 文件不存在
//			return false;
//		}
//	}
//	
//    /**
//     * 重新读取文件列表
//     */
//    public void RefreshTaskList() {
//        files = null;
//        files = new File(TYPE_NUM_PATH).listFiles(new FileNameSelector("txt"));
//    }
//    
//    public boolean CopyTask(int indexDest, String strTaskName, int indexSrc) {
//        return true;
//    }
//
//    public boolean RenameTask(int index, String strTaskName) {
//        return true;
//    }
//    
//    /**
//     * @param index 索引值
//     * @return 索引值对应任务名
//     */
//    public String GetTaskName(int index) {
//        String taskName = files[index].getName();
//        int lastIndex = taskName.lastIndexOf(".");
//        return taskName.substring(0, lastIndex);
//    }
//    
//    public int GetTaskNum() {
//        return files.length;
//    }
//    
//    /**
//     * 保存任务点
//     *
//     * @param fileName 文件名
//     * @param point    任务点
//     * @return true:保存成功   false:保存失败
//     */
//    public boolean SaveTaskPoint(String fileName, Point point) {
//        StringBuffer pointData = new StringBuffer(256);
//        boolean[] portData;
//        int x = point.getX();
//        int y = point.getY();
//        int z = point.getZ();
//        int u = point.getU();
//        try (FileOutputStream fout = new FileOutputStream(fileName)) {
//            switch (point.getPointParam().getPointType()) {
//            /*点胶*/
//                case POINT_GLUE_BASE:
//                    pointData.append("[Point]")
//                            .append(point.getPointParam().getPointType())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_GLUE_ALONE:{
//                    PointGlueAloneParam pointParam = (PointGlueAloneParam)point.getPointParam();
//                    pointData.append("[Point]")
//                            .append(pointParam.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u).append("*")
//                            .append(pointParam.getDotGlueTime()).append("*")
//                            .append(pointParam.getStopGlueTime()).append("*")
//                            .append(pointParam.getUpHeight()).append("*")
//                            .append(booleanToInt(pointParam.isOutGlue())).append("*");
//                    for (int i = 0; i < 19; i++) {
//                        pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("*").append(booleanToInt(pointParam.isPause()));
//                    pointData.append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_LINE_START:{
//                	PointGlueLineStartParam pointParam = (PointGlueLineStartParam)point.getPointParam();
//                	portData = pointParam.getGluePort();
//                    pointData.append("[Point]")
//                    .append(point.getPointParam().getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getOutGlueTime())
//                    .append("*").append(pointParam.isTimeMode())
//                    .append("*").append(pointParam.getMoveSpeed())
//                    .append("*").append(booleanToInt(pointParam.isOutGlue())).append("*");
//                    for (int i = 0; i < 19; i++) {
//                    	pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("*").append(pointParam.getStopGlueTime())
//                    .append("*").append(pointParam.getUpHeight())
//                    .append("*").append(pointParam.getBreakGlueLen())
//                    .append("*").append(pointParam.getDrawDistance())
//                    .append("*").append(pointParam.getDrawSpeed())
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_LINE_ARC:{
//                	PointGlueLineArcParam pointParam = (PointGlueLineArcParam)point.getPointParam();
//                	pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_LINE_MID:{
//                	PointGlueLineMidParam pointParam = (PointGlueLineMidParam)point.getPointParam();
//                	portData = pointParam.getGluePort();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getMoveSpeed())
//                    .append("*").append(booleanToInt(pointParam.isOutGlue())).append("*");
//                    for (int i = 0; i < 19; i++) {
//                    	pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("*").append(pointParam.getRadius())
//                    .append("*").append(pointParam.getStopGlueDisPrev())
//                    .append("*").append(pointParam.getStopGLueDisNext())
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_LINE_END:{
//                	PointGlueLineEndParam pointParam = (PointGlueLineEndParam)point.getPointParam();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getStopGlueTime())
//                    .append("*").append(pointParam.getUpHeight())
//                    .append("*").append(pointParam.getBreakGlueLen())
//                    .append("*").append(pointParam.getDrawDistance())
//                    .append("*").append(pointParam.getDrawSpeed())
//                    .append("*").append(booleanToInt(pointParam.isPause()))
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_FACE_START:{
//                	PointGlueFaceStartParam pointParam = (PointGlueFaceStartParam)point.getPointParam();
//                	portData = pointParam.getGluePort();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getOutGlueTime())
//                    .append("*").append(pointParam.getMoveSpeed())
//                    .append("*").append(booleanToInt(pointParam.isOutGlue())).append("*");
//                    for (int i = 0; i < 19; i++) {
//                    	pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("*").append(pointParam.getStopGlueTime())
//                    .append("*").append(pointParam.getUpHeight())
//                    .append("*").append(pointParam.getLineNum())
//                    .append("*").append(booleanToInt(pointParam.isStartDir()))
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_FACE_END:{
//                	PointGlueFaceEndParam pointParam = (PointGlueFaceEndParam)point.getPointParam();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getStopGlueTime())
//                    .append("*").append(pointParam.getUpHeight())
//                    .append("*").append(pointParam.getLineNum())
//                    .append("*").append(booleanToInt(pointParam.isStartDir()))
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_INPUT:{
//                	PointGlueInputIOParam pointParam = (PointGlueInputIOParam)point.getPointParam();
//                	portData = pointParam.getInputPort();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getGoTimePrev())
//                    .append("*").append(pointParam.getGoTimeNext()).append("*");
//                    for (int i = 0; i < 19; i++) {
//                    	pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("\r\n");
//                }
//                    break;
//                case POINT_GLUE_OUTPUT:{
//                	PointGlueOutputIOParam pointParam = (PointGlueOutputIOParam)point.getPointParam();
//                	portData = pointParam.getInputPort();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getGoTimePrev())
//                    .append("*").append(pointParam.getGoTimeNext()).append("*");
//                    for (int i = 0; i < 19; i++) {
//                    	pointData.append(booleanToInt(portData[i]));
//                    }
//                    pointData.append("\r\n");
//                }
//                    break;
//            /*焊锡*/
//                case POINT_WELD_BASE:{
//                	PointWeldBaseParam pointParam = (PointWeldBaseParam)point.getPointParam();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_WELD_ALONE:{
//                	PointWeldAloneParam pointParam = (PointWeldAloneParam)point.getPointParam();
//                    pointData.append("[Point]")
//                    .append(pointParam.getPointType().getValue())
//                    .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                    .append("*").append(pointParam.getPreHeatTime())
//                    .append("*").append(pointParam.getStopSnTime())
//                    .append("*").append(pointParam.getSendSnSpeedFir())
//                    .append("*").append(pointParam.getSendSnSumFir())
//                    .append("*").append(pointParam.getSendSnSpeedSec())
//                    .append("*").append(pointParam.getSendSnSumSec())
//                    .append("*").append(pointParam.getUpHeight())
//                    .append("*").append(booleanToInt(pointParam.isSn()))
//                    .append("*").append(booleanToInt(pointParam.isPause()))
//                    .append("*").append(pointParam.getDipDistance())
//                    .append("\r\n");
//                }
//                    break;
//                case POINT_WELD_LINE_START:{
//                	
//                }
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_LINE_ARC:
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_LINE_MID:
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_LINE_END:
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_FACE_START:
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_FACE_END:
//                    pointData.append("[Point]")
//                            .append(point.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("\r\n");
//                    break;
//                case POINT_WELD_BLOW:
//                    PointWeldBlow pointWeldBlow = (PointWeldBlow) point;
//                    pointData.append("[Point]")
//                            .append(pointWeldBlow.getPointType().getValue())
//                            .append("*").append(x).append("*").append(y).append("*").append(z).append("*").append(u)
//                            .append("*").append(pointWeldBlow.getBlowSnTime())
//                            .append("\r\n");
//                    break;
//            }
//            fout.write(pointData.toString().getBytes());
//            fout.flush();
//            fout.close();
//            System.out.println("Done");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    /**
//     * @param pointData 任务点信息
//     * @param pointMgr  任务点集
//     * @return true:成功 false:失败
//     */
//    public boolean ReadTaskPoint(String pointData, PointMgr pointMgr) {
//        String tmpValue, head = null;
//        Point point = null;
//        boolean[] gluePort = new boolean[IOPort.USER_O_NO_ALL.ordinal()];
//        boolean[] ioPort = new boolean[IOPort.USER_O_NO_ALL.ordinal() - 7];
//        //用*分割
//        StringTokenizer paramToKenizer = new StringTokenizer(pointData, "*");
//
//        int pointTypeNum = Integer.valueOf(paramToKenizer.nextToken());
//        int x = Integer.valueOf(paramToKenizer.nextToken());
//        int y = Integer.valueOf(paramToKenizer.nextToken());
//        int z = Integer.valueOf(paramToKenizer.nextToken());
//        int u = Integer.valueOf(paramToKenizer.nextToken());
//
//        switch (pointTypeNum) {
//            case 0x01:
//                point = new PointGlueBase();
//                PointGlueBase pointGlueBase = (PointGlueBase) point;
//                point.setX(x);
//                point.setY(y);
//                point.setZ(z);
//                point.setU(u);
//                pointMgr.addPoint(pointGlueBase);
//                break;
//            case 0x2:
//                point = new PointGlueAlone();
//                PointGlueAlone pointGlueAlone = (PointGlueAlone) point;
//                pointGlueAlone.setX(x);
//                pointGlueAlone.setY(y);
//                pointGlueAlone.setZ(z);
//                pointGlueAlone.setU(u);
//                pointGlueAlone.setDotGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueAlone.setStopGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueAlone.setUpHeight(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueAlone.setOutGlue(Boolean.valueOf(paramToKenizer.nextToken()));
//                tmpValue = paramToKenizer.nextToken();
//                //读取到的端口数字,一共19位,每一位代表一个端口
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal(); i++) {
//                    gluePort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueAlone.setGluePort(gluePort);
//
//                pointGlueAlone.setPause(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointMgr.addPoint(pointGlueAlone);
//                break;
//            case 0x03:
//                point = new PointGlueLineStart();
//                PointGlueLineStart pointGlueLineStart = (PointGlueLineStart) point;
//                pointGlueLineStart.setX(x);
//                pointGlueLineStart.setY(y);
//                pointGlueLineStart.setZ(z);
//                pointGlueLineStart.setU(u);
//                pointGlueLineStart.setOutGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineStart.setTimeMode(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointGlueLineStart.setMoveSpeed(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineStart.setOutGlue(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                tmpValue = paramToKenizer.nextToken();
//                //读取到的端口数字,一共19位,每一位代表一个端口
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal(); i++) {
//                    gluePort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueLineStart.setGluePort(gluePort);
//
//                pointGlueLineStart.setStopGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineStart.setUpHeight(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineStart.setBreakGlueLen(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineStart.setDrawSpeed(Integer.valueOf(paramToKenizer.nextToken()));
//                pointMgr.addPoint(pointGlueLineStart);
//                break;
//            case 0x04:
//                point = new PointGlueLineArc();
//                PointGlueLineArc pointGlueLineArc = (PointGlueLineArc) point;
//                pointGlueLineArc.setX(x);
//                pointGlueLineArc.setY(y);
//                pointGlueLineArc.setZ(z);
//                pointGlueLineArc.setU(u);
//                pointMgr.addPoint(pointGlueLineArc);
//                break;
//            case 0x05:
//                point = new PointGlueLineMid();
//                PointGlueLineMid pointGlueLineMid = (PointGlueLineMid) point;
//                pointGlueLineMid.setX(x);
//                pointGlueLineMid.setY(y);
//                pointGlueLineMid.setZ(z);
//                pointGlueLineMid.setU(u);
//                pointGlueLineMid.setMoveSpeed(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineMid.setOutGlue(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                tmpValue = paramToKenizer.nextToken();
//                //读取到的端口数字,一共19位,每一位代表一个端口
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal(); i++) {
//                    gluePort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueLineMid.setGluePort(gluePort);
//
//                pointGlueLineMid.setRadius((float) (Integer.valueOf(paramToKenizer.nextToken())) / 100);
//                pointGlueLineMid.setStopGlueDisPrev(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineMid.setStopGLueDisNext(Integer.valueOf(paramToKenizer.nextToken()));
//                pointMgr.addPoint(pointGlueLineMid);
//                break;
//            case 0x06:
//                point = new PointGlueLineEnd();
//                PointGlueLineEnd pointGlueLineEnd = (PointGlueLineEnd) point;
//                pointGlueLineEnd.setX(x);
//                pointGlueLineEnd.setY(y);
//                pointGlueLineEnd.setZ(z);
//                pointGlueLineEnd.setU(u);
//                pointGlueLineEnd.setStopGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineEnd.setUpHeight(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineEnd.setBreakGlueLen(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineEnd.setDrawDistance(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineEnd.setDrawSpeed(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueLineEnd.setPause(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointMgr.addPoint(pointGlueLineEnd);
//                break;
//            case 0x07:
//                point = new PointGlueFaceStart();
//                PointGlueFaceStart pointGlueFaceStart = (PointGlueFaceStart) point;
//                pointGlueFaceStart.setX(x);
//                pointGlueFaceStart.setY(y);
//                pointGlueFaceStart.setZ(z);
//                pointGlueFaceStart.setU(u);
//                pointGlueFaceStart.setOutGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceStart.setMoveSpeed(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceStart.setOutGlue(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                tmpValue = paramToKenizer.nextToken();
//                //读取到的端口数字,一共19位,每一位代表一个端口
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal(); i++) {
//                    gluePort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueFaceStart.setGluePort(gluePort);
//
//                pointGlueFaceStart.setStopGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceStart.setUpHeight(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceStart.setLineNum(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceStart.setStartDir(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointMgr.addPoint(pointGlueFaceStart);
//                break;
//            case 0x08:
//                point = new PointGlueFaceEnd();
//                PointGlueFaceEnd pointGlueFaceEnd = (PointGlueFaceEnd) point;
//                pointGlueFaceEnd.setX(x);
//                pointGlueFaceEnd.setY(y);
//                pointGlueFaceEnd.setZ(z);
//                pointGlueFaceEnd.setU(u);
//                pointGlueFaceEnd.setStopGlueTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceEnd.setUpHeight(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceEnd.setLineNum(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueFaceEnd.setStartDir(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointGlueFaceEnd.setPause(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointMgr.addPoint(pointGlueFaceEnd);
//                break;
//            case 0x09:
//                point = new PointGlueInputIO();
//                PointGlueInputIO pointGlueInputIO = (PointGlueInputIO) point;
//                pointGlueInputIO.setX(x);
//                pointGlueInputIO.setY(y);
//                pointGlueInputIO.setZ(z);
//                pointGlueInputIO.setU(u);
//                pointGlueInputIO.setGoTimePrev(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueInputIO.setGoTimeNext(Integer.valueOf(paramToKenizer.nextToken()));
//                tmpValue = paramToKenizer.nextToken();
//                //读取到的IO端口数字,一共12位,每一位代表一个端口
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal() - 7; i++) {
//                    ioPort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueInputIO.setInputPort(ioPort);
//                pointMgr.addPoint(pointGlueInputIO);
//                break;
//            case 0x0A:
//                point = new PointGlueOutputIO();
//                PointGlueOutputIO pointGlueOutputIO = (PointGlueOutputIO) point;
//                pointGlueOutputIO.setX(x);
//                pointGlueOutputIO.setY(y);
//                pointGlueOutputIO.setZ(z);
//                pointGlueOutputIO.setU(u);
//                pointGlueOutputIO.setGoTimePrev(Integer.valueOf(paramToKenizer.nextToken()));
//                pointGlueOutputIO.setGoTimeNext(Integer.valueOf(paramToKenizer.nextToken()));
//                tmpValue = paramToKenizer.nextToken();
//                for (int i = 0; i < IOPort.USER_O_NO_ALL.ordinal() - 7; i++) {
//                    ioPort[i] = intToBoolean(tmpValue.charAt(i));
//                }
//                pointGlueOutputIO.setOutputPort(ioPort);
//                pointMgr.addPoint(pointGlueOutputIO);
//                break;
//
//            case 0x100:
//                point = new PointWeldBase();
//                PointWeldBase pointWeldBase = (PointWeldBase) point;
//                pointWeldBase.setX(x);
//                pointWeldBase.setY(y);
//                pointWeldBase.setZ(z);
//                pointWeldBase.setU(u);
//                pointMgr.addPoint(pointWeldBase);
//                break;
//            case 0x101:
//                point = new PointWeldAlone();
//                PointWeldAlone pointWeldAlone = (PointWeldAlone) point;
//                pointWeldAlone.setX(x);
//                pointWeldAlone.setY(y);
//                pointWeldAlone.setZ(z);
//                pointWeldAlone.setU(u);
//                pointWeldAlone.setPreHeatTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setStopSnTime(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setSendSnSpeedFirst(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setSendSnSumFirst(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setSendSnSpeedSecond(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setSendSnSumSecond(Integer.valueOf(paramToKenizer.nextToken()));
//                pointWeldAlone.setUpHeight(Integer.valueOf(Integer.valueOf(paramToKenizer.nextToken())));
//                pointWeldAlone.setOutSn(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointWeldAlone.setPause(intToBoolean(Integer.valueOf(paramToKenizer.nextToken())));
//                pointWeldAlone.setDipDistance(Integer.valueOf(paramToKenizer.nextToken()));
//
//                pointMgr.addPoint(pointWeldAlone);
//                break;
//            case 0x109:
//                point = new PointWeldBlow();
//                PointWeldBlow pointWeldBlow = (PointWeldBlow) point;
//                pointWeldBlow.setX(x);
//                pointWeldBlow.setY(y);
//                pointWeldBlow.setZ(z);
//                pointWeldBlow.setU(u);
//                pointWeldBlow.setBlowSnTime(Integer.valueOf(paramToKenizer.nextToken()));
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
//    
//
//    /**
//     * 保存任务参数
//     *
//     * @param fileName  文件名
//     * @param taskParam 任务参数
//     * @return true:保存成功   false:保存失败
//     */
//    public boolean SaveTaskParam(String fileName, TaskParam taskParam) {
//        String strRobotName = taskParam.getStrRobotName();
//        StringBuffer taskParamData = new StringBuffer(256);
//        try (FileOutputStream fout = new FileOutputStream(fileName)) {
//            taskParamData.append("[Robot]")
//                    .append(strRobotName).append("*")
//                    .append(taskParam.getnAccelerateTime()).append("*")
//                    .append(taskParam.getnXYNullSpeed()).append("*")
//                    .append(taskParam.getnZNullSpeed()).append("*")
//                    .append(taskParam.getnUNullSpeed()).append("*")
//                    .append(taskParam.getnStartSpeed()).append("*")
//                    .append(taskParam.getnBackSnSpeedFir()).append("*")
//                    .append(taskParam.getnBackSnSumFir()).append("*")
//                    .append(taskParam.getnBackSnSpeedSec()).append("*")
//                    .append(taskParam.getnBackSnSumSec()).append("*")
//                    .append(taskParam.getnSnHeight()).append("*")
//                    .append(taskParam.getnWorkMode()).append("*")
//                    .append(taskParam.getnSpeedCurve()).append("*")
//                    .append(taskParam.getnDecelerateTime()).append("*")
//                    .append(taskParam.getnAccelerate()).append("*")
//                    .append(taskParam.getnDecelerate())
//                    .append("\r\n");
//            fout.write(taskParamData.toString().getBytes());
//            fout.flush();
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            System.out.println("Done");
//        }
//        return true;
//    }
//    
//    /**
//     * 解析任务参数
//     * 未处理头标签!
//     *
//     * @param taskParamData 任务参数字符串
//     * @param taskParam     任务参数对象
//     * @return true
//     */
//    public boolean ReadTaskParam(String taskParamData, TaskParam taskParam) {
//
//        //用*号分割字符串
//        StringTokenizer paramsToKenizer = new StringTokenizer(taskParamData, "*");
//
//        taskParam.setStrRobotName(paramsToKenizer.nextToken());
//        taskParam.setnAccelerate(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnXYNullSpeed(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnZNullSpeed(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnUNullSpeed(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnStartSpeed(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnBackSnSpeedFir(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnBackSnSumFir(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnBackSnSpeedSec(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnBackSnSumSec(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnSnHeight(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnWorkMode(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnSpeedCurve(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnDecelerateTime(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnAccelerate(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnDecelerate(Integer.valueOf(paramsToKenizer.nextToken()));
//        taskParam.setnTaskNum(1);
//        taskParam.setnStartX(0);
//        taskParam.setnStartY(0);
//        taskParam.setnStartZ(0);
//        taskParam.setnStartU(0);
//        taskParam.setnDemoSpeed(30);
//        return true;
//    }
//    
//    public void SortTask(int sortType) {
//
//    }
//
//    public boolean IsTaskExist(String strTaskName) {
//        return true;
//    }
//}
