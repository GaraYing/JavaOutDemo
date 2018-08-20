package com.yunzhijia.appdemo.redis;

import com.yunzhijia.appdemo.redis.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class RedisDao2 {
	private static Logger logger = LoggerFactory.getLogger(RedisDao2.class);
//	@Autowired
	private JedisSentinelPool sentinelPool;

	public JedisSentinelPool getSentinelPool() {
		return sentinelPool;
	}

	public void setSentinelPool(JedisSentinelPool sentinelPool) {
		this.sentinelPool = sentinelPool;
	}

	public Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			// 释放redis对象
			sentinelPool.returnBrokenResource(jedis);
			logger.error(e.getMessage());
		}
		return jedis;
	}

	public Jedis getJedis(String dbCode) {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
			jedis.select(Integer.parseInt(dbCode));
		} catch (Exception e) {
			e.printStackTrace();
			// 释放redis对象
			sentinelPool.returnBrokenResource(jedis);
			logger.error(e.getMessage());
		}
		return jedis;
	}

	public void returnResource(Jedis redis) {
		if (redis != null && sentinelPool != null) {
			sentinelPool.returnResource(redis);
		}
	}

	public void set(String key, String value) {
		Jedis redis = getJedis();
		if (redis == null)
			return;
		try {
			redis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(redis);
		}
	}

	// 往reids插入String数据
	public String set(String key, String value, String dbCode) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = getJedis(dbCode);
			ret = jedis.set(key, value);
		} catch (Exception e) {
			logger.error("Redis操作异常！", e);
		} finally {
			returnResource(jedis);
		}
		return ret;
	}

	public long incr(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return -1;
		try {
			return redis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(redis);
		}
		return -1;
	}

	public void setex(String key, int seconds, String value) {
		Jedis redis = getJedis();
		if (redis == null)
			return;
		try {
			redis.setex(key, seconds, value);
		} finally {
			returnResource(redis);
		}
	}
	
	public void setex(String key, int seconds, String value, String dbCode) {
		Jedis jedis = getJedis(dbCode);
		if (jedis == null)
			return;
		try {
			jedis.setex(key, seconds, value);
		} finally {
			returnResource(jedis);
		}
	}

	public String get(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			String rv = redis.get(key);
			return rv;
		} finally {
			returnResource(redis);
		}
	}

	// 从reids获取String数据
	public String get(String key, String dbCode) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = getJedis(dbCode);
			ret = jedis.get(key);
		} catch (Exception e) {
			logger.error("Redis操作异常！", e);
		} finally {
			returnResource(jedis);
		}
		return ret;
	}

	public Long del(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			Long rv = redis.del(key);
			return rv;
		} finally {
			returnResource(redis);
		}
	}
	
	public Long delete(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			Long rv = redis.del(key);
			return rv;
		} finally {
			returnResource(redis);
		}
	}
	
	public Long delObject(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			Long rv = redis.del(key.getBytes());
			return rv;
		} finally {
			returnResource(redis);
		}
	}
	
	public void delObject(Set<byte[]> keyByteSet, String dbCode) {
		Jedis redis = getJedis(dbCode);
		try {
			redis.del(keyByteSet.toArray(new byte[keyByteSet.size()][]));
		} finally {
			returnResource(redis);
		}
	}
	
	public Set<byte[]> keyByteSet(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if(redis == null) return null;
		try {
			return redis.keys(key.getBytes());
		} finally {
			returnResource(redis);
		}
	}

	public Long del(String... keys) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			Long rv = redis.del(keys);
			return rv;
		} finally {
			returnResource(redis);
		}
	}

	public Boolean exists(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return false;
		try {
			Boolean rv = redis.exists(key);
			return rv;
		} finally {
			returnResource(redis);
		}
	}

	public Long tll(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return 0L;
		try {
			Long rv = redis.ttl(key);
			return rv;
		} finally {
			returnResource(redis);
		}
	}
	
	public Long tllObject(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return 0L;
		try {
			Long rv = redis.ttl(key.getBytes());
			return rv;
		} finally {
			returnResource(redis);
		}
	}

	public void zadd(String key, Map<String, Double> scoreMembers) {
		if (scoreMembers == null || scoreMembers.isEmpty())
			return;
		Jedis redis = getJedis();
		try {
			redis.zadd(key, scoreMembers);
		} finally {
			returnResource(redis);
		}
	}

	public Long zcard(String key) {
		Jedis redis = getJedis();
		try {
			return redis.zcard(key);
		} finally {
			returnResource(redis);
		}
	}

	/**
	 * 得到有序集合中排序最大的元素
	 * 
	 * @param key
	 * @return
	 */
	public Tuple zgetMaxEleWithScore(String key) {
		Jedis redis = getJedis();
		try {
			Set<Tuple> set = redis.zrevrangeWithScores(key, 0, 1);
			if (set == null || set.isEmpty())
				return null;
			Iterator<Tuple> it = set.iterator();
			if (it.hasNext())
				return it.next();
			return null;
		} finally {
			returnResource(redis);
		}
	}

	public void hset(String key, String field, String value) {
		Jedis redis = getJedis();
		try {
			redis.hset(key, field, value);
		} finally {
			returnResource(redis);
		}
	}

	public void hmset(String key, Map<String, String> hash) {
		Jedis redis = getJedis();
		try {
			redis.hmset(key, hash);
		} finally {
			returnResource(redis);
		}
	}

	public String hget(String key, String field) {
		Jedis redis = getJedis();
		try {
			return redis.hget(key, field);
		} finally {
			returnResource(redis);
		}
	}

	public void zadd(String key, Double score, String member) {
		Jedis redis = getJedis();
		if (redis == null)
			return;
		try {
			redis.zadd(key, score, member);
		} finally {
			returnResource(redis);
		}
	}

	public Set<String> zrange(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zrevrange(key, 0, -1);// 取所有的排序数据
		} finally {
			returnResource(redis);
		}
	}

	public Long zremrangebyscore(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zremrangeByScore(key, 0, 9999);// 删除zset中所有数据
		} finally {
			returnResource(redis);
		}
	}

	// 反向取
	public Set<Tuple> zrevrangeWithScores(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zrevrangeWithScores(key, 0, -1);
		} finally {
			returnResource(redis);
		}
	}
	
	public Set<Tuple> zrevrangeWithScores(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.zrevrangeWithScores(key, 0, -1);
		} finally {
			returnResource(redis);
		}
	}

	public Long zrem(String key, String member) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zrem(key, member);// 删除一个元素
		} finally {
			returnResource(redis);
		}
	}

	public Long zrem(String key, String... members) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zrem(key, members);// 删除多个元素
		} finally {
			returnResource(redis);
		}
	}

	public Set<Tuple> zrangeWithScores(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.zrangeWithScores(key, 0, -1);// 取所有的排序数据
		} finally {
			returnResource(redis);
		}
	}
	
	public Double zincrBy(String key, String member, double score, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return -1D;
		try {
			return redis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(redis);
		}
		return -1D;
	}

	public Long sadd(String key, String... members) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.sadd(key, members);// 以set格式保存数据
		} finally {
			returnResource(redis);
		}
	}
	
	public Long saddByDb(String key, String dbCode, String... members) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.sadd(key, members);// 以set格式保存数据
		} finally {
			returnResource(redis);
		}
	}

	public Set<String> smembers(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.smembers(key);// 取set所有的数据
		} finally {
			returnResource(redis);
		}
	}

	public Set<String> smembers(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.smembers(key);// 取set所有的数据
		} finally {
			returnResource(redis);
		}
	}
	
	public Set<String> sdiff(String... keys) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.sdiff(keys);
		} finally {
			returnResource(redis);
		}
	}

	public Long srem(String key, String... members) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.srem(key, members);// 删除set中的元素
		} finally {
			returnResource(redis);
		}
	}
	
	public Long sremove(String key, String dbCode, String... members) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.srem(key, members);// 删除set中的元素
		} finally {
			returnResource(redis);
		}
	}

	public List<String> mget(String... keys) {
		Jedis redis = getJedis();
		if(redis == null) return null;
		try {
			return redis.mget(keys);// 获取redis存储的指定keys的数据
		} finally {
			returnResource(redis);
		}
	}

	public Long setnx(String key, String value) {
		Jedis redis = getJedis();
		if(redis == null) return null;
		try {
			return redis.setnx(key, value);
		} finally {
			returnResource(redis);
		}
	}
	
	public Long setnx(String key, String value, String dBCode) {
		Jedis redis = getJedis(dBCode);
		if (redis == null)
			return null;
		try {
			return redis.setnx(key, value);
		} finally {
			returnResource(redis);
		}
	}

	public String getset(String key, String value) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			String rv = redis.getSet(key, value);
			return rv;
		} finally {
			returnResource(redis);
		}
	}

	public Set<String> keys(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.keys(key);// 获取redis存储的指定key的数据
		} finally {
			returnResource(redis);
		}
	}

	public Long expire(String key, int seconds) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.expire(key, seconds);
		} finally {
			returnResource(redis);
		}
	}

	public Long expire(String key, int seconds, String dBCode) {
		Jedis redis = getJedis(dBCode);
		if (redis == null)
			return null;
		try {
			return redis.expire(key, seconds);
		} finally {
			returnResource(redis);
		}
	}

	public Boolean sismember(String key, String member) {
		Jedis redis = getJedis();
		if (redis == null)
			return null;
		try {
			return redis.sismember(key, member);
		} finally {
			returnResource(redis);
		}
	}

	public Long scard(String key) {
		Jedis redis = getJedis();
		if (redis == null)
			return 0L;
		try {
			return redis.scard(key);
		} finally {
			returnResource(redis);
		}
	}
	
	public Long scard(String key, String dbCode) {
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return 0L;
		try {
			return redis.scard(key);
		} finally {
			returnResource(redis);
		}
	}
	
	/**
	 * 移除并返回集合中的一个随机元素
	 * @author: GaryXu
	 * @Date: 2018年1月5日 上午10:56:03
	 * @param key
	 * @param dbCode
	 * @return
	 */
	public String sPop(String key, String dbCode){
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.spop(key);
		} finally {
			returnResource(redis);
		}
	}
	
	public ScanResult<String> scan(String cursor, String dbCode){
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.scan(cursor);
		} finally {
			returnResource(redis);
		}
	}
	
	public ScanResult<String> scan(String key, String cursor, ScanParams param, String dbCode){
		Jedis redis = getJedis(dbCode);
		if (redis == null)
			return null;
		try {
			return redis.sscan(key, cursor, param);
		} finally {
			returnResource(redis);
		}
	}

	// redis插入对象
	public String setObject(String key, Object value, String dbCode) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = getJedis(dbCode);
			ret = jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis操作异常！", e);
		} finally {
			if (jedis != null) {
				returnResource(jedis);
			}
		}
		return ret;
	}
	
	// redis插入对象带数据超时设置
	public String setObjectEx(String key, Object value, String dbCode, int time) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = getJedis(dbCode);
			ret = jedis.setex(key.getBytes(), time, SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis操作异常！", e);
		} finally {
			if (jedis != null) {
				returnResource(jedis);
			}
		}
		return ret;
	}

	// redis读取对象
	public Object getObject(String key, String dbCode) {
		Jedis jedis = null;
		Object ret = null;
		try {
			jedis = getJedis(dbCode);
			ret = SerializeUtil.unserialize(jedis.get(key.getBytes()));
		} catch (Exception e) {
			logger.error("Redis操作异常！", e);
		} finally {
			if (jedis != null) {
				returnResource(jedis);
			}
		}
		return ret;
	}
}