/**
 * 
 */
package com.mingseal.data.point;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 任务列表
 * 
 * @author 商炎炳
 *
 */
public class PointTask implements Parcelable{
	private int id;// 主键
	private String taskName;// 任务名称
	private List<Integer> pointids;// 任务列表所指的point的主键列表

	/**
	 * 无参的构造函数
	 */
	public PointTask() {
		super();
	}

	/**
	 * @param id
	 *            主键
	 * @param taskName
	 *            任务名称
	 * @param pointids
	 *            任务列表所指的point的主键列表
	 */
	public PointTask(int id, String taskName, List<Integer> pointids) {
		super();
		this.id = id;
		this.taskName = taskName;
		this.pointids = pointids;
	}

	/**
	 * 取得任务主键
	 * 
	 * @return 任务主键
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置任务主键
	 * 
	 * @param id
	 *            任务主键
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 取得任务名
	 * 
	 * @return 任务名
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * 设置任务名
	 * 
	 * @param taskName
	 *            任务名
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 取得任务点的主键集合
	 * 
	 * @return 任务点的主键集合
	 */
	public List<Integer> getPointids() {
		return pointids;
	}

	/**
	 * 设置任务点的主键集合
	 * 
	 * @param pointids
	 */
	public void setPointids(List<Integer> pointids) {
		this.pointids = pointids;
	}

	@Override
	public String toString() {
		return "PointTask [id=" + id + ", taskName=" + taskName + ", pointids=" + pointids + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((pointids == null) ? 0 : pointids.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointTask other = (PointTask) obj;
		if (id != other.id)
			return false;
		if (pointids == null) {
			if (other.pointids != null)
				return false;
		} else if (!pointids.equals(other.pointids))
			return false;
		if (taskName == null) {
			if (other.taskName != null)
				return false;
		} else if (!taskName.equals(other.taskName))
			return false;
		return true;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(taskName);
		dest.writeList(pointids);
	}
	
	public static final Parcelable.Creator<PointTask> CREATOR = new Creator<PointTask>() {
		
		@Override
		public PointTask[] newArray(int size) {
			return new PointTask[size];
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public PointTask createFromParcel(Parcel source) {
			PointTask task = new PointTask();
			task.id = source.readInt();
			task.taskName = source.readString();
			task.pointids = source.readArrayList(Integer.class.getClassLoader());
			return task;
		}
	};

}
