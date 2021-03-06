package com.qh.pay.dao;

import com.qh.pay.domain.PayConfigCompanyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 支付公司配置
 * @date 2017-11-06 16:00:33
 */
@Mapper
public interface PayConfigCompanyDao {

	List<PayConfigCompanyDO> getByChannel(@Param("outChannel") String outChannel);

	PayConfigCompanyDO getByCompany(@Param("company") String company,@Param("outChannel") String outChannel);

	PayConfigCompanyDO get(@Param("company") String company,@Param("payMerch") String payMerch,@Param("outChannel") String outChannel);
	
	List<PayConfigCompanyDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PayConfigCompanyDO payConfigCompany);
	
	int update(PayConfigCompanyDO payConfigCompany);
	
	int remove(@Param("company") String company,@Param("payMerch") String payMerch,@Param("outChannel") String outChannel);
	
}
