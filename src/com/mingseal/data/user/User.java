/**
 * 
 */
package com.mingseal.data.user;

/**
 * @author 商炎炳
 * @description 用户表
 */
public class User {
	private int id;// 主键
	private String username;// 用户名
	private String password;// 密码
	private String type;// 类型(管理员,技术支持,操作员)

	/**
	 * 无参构造函数
	 */
	public User() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param type
	 *            类型(管理员,技术支持,操作员)
	 */
	public User(String username, String password, String type) {
		super();
		this.username = username;
		this.password = password;
		this.type = type;
	}

	/**
	 * @param id
	 *            主键
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param type
	 *            类型(管理员,技术支持,操作员)
	 */
	public User(int id, String username, String password, String type) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.type = type;
	}
	
	

	/**
	 * @return 主键
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return 类型(管理员,技术支持,操作员)
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型(管理员,技术支持,操作员)
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", type=" + type + "]";
	}

}
