package com.yunzhijia.appdemo.redis;

import com.yunzhijia.appdemo.redis.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Repository
public class RedisDao  extends AbstractBaseRedisDao<String, HashMap<String, Object>>{
	private static Logger logger = LoggerFactory.getLogger(RedisDao.class);
	/**
	 * 新增键值对
	 *
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addString(final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey = serializer.serialize(key);
				byte[] jvalue = serializer.serialize(value);
				return connection.setNX(jkey, jvalue);
			}
		});
		return result;
	}

	/**
	 * 新增(拼接字符串)
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean appendString(final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey = serializer.serialize(key);
				byte[] jvalue = serializer.serialize(value);
				if (connection.exists(jkey)) {
					connection.append(jkey, jvalue);
					return true;
				} else {
					return false;
				}
			}
		});
		return result;
	}

	/**
	 * 新增(存储Map)
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public String addMap(String key, Map<String, String> map) {
		Jedis jedis = getJedis();
		String result = jedis.hmset(key, map);
		jedis.close();
		return result;
	}

	/**
	 * 获取map
	 *
	 * @param key
	 * @return
	 */
	public Map<String, String> getMap(String key) {
		Jedis jedis = getJedis();
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> iter = jedis.hkeys(key).iterator();
		while (iter.hasNext()) {
			String ikey = iter.next();
			map.put(ikey, jedis.hmget(key, ikey).get(0));
		}
		jedis.close();
		return map;
	}

	/**
	 * 新增(存储List)
	 *
	 * @param key
	 * @param pd
	 * @return
	 */
	public void addList(String key, List<String> list) {
		Jedis jedis = getJedis();
		jedis.del(key); // 开始前，先移除所有的内容
		for (String value : list) {
			jedis.rpush(key, value);
		}
		jedis.close();
	}

	/**
	 * 获取List
	 *
	 * @param key
	 * @return
	 */
	public List<String> getList(String key) {
		Jedis jedis = getJedis();
		List<String> list = jedis.lrange(key, 0, -1);
		jedis.close();
		return list;
	}

	/**
	 * 新增(存储set)
	 *
	 * @param key
	 * @param set
	 */
	public void addSet(String key, Set<String> set) {
		Jedis jedis = getJedis();
		jedis.del(key);
		for (String value : set) {
			jedis.sadd(key, value);
		}
		jedis.close();
	}

	/**
	 * 获取Set
	 *
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key) {
		Jedis jedis = getJedis();
		Set<String> set = jedis.smembers(key);
		jedis.close();
		return set;
	}

	/**
	 * 删除 (non-Javadoc)
	 *
	 * @see com.fh.dao.redis.RedisDao#delete(java.lang.String)
	 */
	public boolean delete(final String key) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey = serializer.serialize(key);
				if (connection.exists(jkey)) {
					connection.del(jkey);
					return true;
				} else {
					return false;
				}
			}
		});
		return result;
	}

	/**
	 * 删除多个 (non-Javadoc)
	 *
	 * @see com.fh.dao.redis.RedisDao#delete(java.util.List)
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 修改 (non-Javadoc)
	 *
	 * @see com.fh.dao.redis.RedisDao#eidt(java.lang.String, java.lang.String)
	 */
	public boolean eidt(String key, String value) {
		if (delete(key)) {
			addString(key, value);
			return true;
		}
		return false;
	}
	/**
	 * 先删除后添加
	 * @param key
	 * @param value
	 */
	public void del_add(String key, String value){
		delete(key);
		addString(key, value);
	}

	/**
	 * 通过key获取值 (non-Javadoc)
	 *
	 *
	 */
	public String get(final String keyId) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey = serializer.serialize(keyId);
				byte[] jvalue = connection.get(jkey);
				if (jvalue == null) {
					return null;
				}
				return serializer.deserialize(jvalue);
			}
		});
		return result;
	}

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.timeout}")
	private int timeout;


	/**
	 * 获取Jedis
	 *
	 * @return
	 */
	public Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = new Jedis(host, port);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("获取jedis出错****");
		}
		return jedis;
	}

//	/**
//	 * 读取redis.properties 配置文件
//	 *
//	 * @return
//	 * @throws IOException
//	 */
//	public Properties getPprVue() {
//		InputStream inputStream = SystemConstant.class.getClassLoader()
//				.getResourceAsStream("redis.properties");
//		Properties p = new Properties();
//		try {
//			p.load(inputStream);
//			inputStream.close();
//		} catch (IOException e) {
//			// 读取配置文件出错
//			e.printStackTrace();
//		}
//		return p;
//	}
}