package com.mingseal.data.manager;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.Point;


/** 
* @ClassName: PointMgr 
* @Description: 点管理
* @author lyq
* @date 2015年6月15日 下午4:10:09 
*  
*/
public class PointMgr {
	
	private List<Point> pointSet;
	
	/**
	 * <p>Title: getPointSet
	 * <p>Description: 获取点列表
	 * @return
	 */
	public List<Point> getPointSet() {
		return pointSet;
	}

	public PointMgr(){
		pointSet = new ArrayList<Point>();
	}

    /**
     * 方法描述:[添加新点]
     * @param point
     * @return
     */
    public boolean addPoint(Point point) {
        return pointSet.add(point);
    }
    
    /**
     * 方法描述:[删除点]
     * @param position
     */
    public void deletePoint(int position){
        pointSet.remove(position);
    }
    
    /**
     * 方法描述:[替换点]
     * @param position
     * @param point
     */
    public void replacePoint(int position, Point point){
        pointSet.set(position, point);
    }
    
    /**
     * 方法描述:[交换点]
     * @param position
     * @param positionNext
     */
    public void swapPoint(int position, int positionNext){
        Object temp = pointSet.get(position);
        pointSet.set(positionNext, pointSet.get(position));
        pointSet.set(position, (Point) temp);
    }
    
    /**
     * 方法描述:[获取当前任务列表中所有点]
     * @return number
     */
    public int getPointNum(){
        return pointSet.size();
    }
    
    /**
     * 方法描述:[获取指定位置点]
     * @param position
     * @return
     */
    public Point getPoint(int position){
        return pointSet.get(position);
    }
    
    /**
     * 方法描述:[清空列表中所有点]
     */
    public void clearPoitnSet(){
        pointSet.clear();
    }
}
