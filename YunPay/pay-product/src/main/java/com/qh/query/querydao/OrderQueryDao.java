package com.qh.query.querydao;

import com.qh.pay.api.Order;
import com.qh.pay.domain.FooterDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderQueryMapper
 * @Description 订单查询
 * @Date 2017年12月1日 上午10:36:47
 * @version 1.0.0
 */
@Mapper
public interface OrderQueryDao {

	Order get(@Param("orderNo") String orderNo, @Param("merchNo") String merchNo);

	List<Order> list(Map<String, Object> map);

	FooterDO listFooter(Map<String, Object> map);
	
	int count(Map<String,Object> map);

	List<Order> detection();
}
