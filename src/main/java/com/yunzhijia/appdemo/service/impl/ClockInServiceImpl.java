package com.yunzhijia.appdemo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunzhijia.appdemo.mapper.ClockInMapper;
import com.yunzhijia.appdemo.pojo.ClockIn;
import com.yunzhijia.appdemo.service.ClockInService;
import org.springframework.transaction.annotation.Transactional;

@Service("clockInService")
public class ClockInServiceImpl implements ClockInService {
	
	@Autowired
	private ClockInMapper clockInMapper;

	@Transactional
	@Override
	public void addClockIn(ClockIn clockIn) {
		clockInMapper.insertSelective(clockIn);
	}

	@Override
	public List<ClockIn> selectByOutRecordId(String recordid) {
		return clockInMapper.selectByOutRecordId(recordid);
	}
}