/**
 * 
 */
package com.yunzhijia.appdemo.redis;

import java.net.URI;

import com.alibaba.druid.util.StringUtils;

import redis.clients.jedis.JedisShardInfo;

/**
 * @title
 * @Description 密码为空字符串时设置为null
 * @author
 * @date
 * @version 1.0
 */
public class JedisShardInfoNull extends JedisShardInfo {

	/**
	 * @param host
	 */
	public JedisShardInfoNull(String host) {
		super(host);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param uri
	 */
	public JedisShardInfoNull(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param name
	 */
	public JedisShardInfoNull(String host, String name) {
		super(host, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param port
	 */
	public JedisShardInfoNull(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param port
	 * @param name
	 */
	public JedisShardInfoNull(String host, int port, String name) {
		super(host, port, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public JedisShardInfoNull(String host, int port, int timeout) {
		super(host, port, timeout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param port
	 * @param timeout
	 * @param name
	 */
	public JedisShardInfoNull(String host, int port, int timeout, String name) {
		super(host, port, timeout, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param host
	 * @param port
	 * @param timeout
	 * @param weight
	 */
	public JedisShardInfoNull(String host, int port, int timeout, int weight) {
		super(host, port, timeout, weight);
		// TODO Auto-generated constructor stub
	}

	public void setPassword(String auth) {
		if (StringUtils.isEmpty(auth))
			auth = null;
		super.setPassword(auth);
	}
}
