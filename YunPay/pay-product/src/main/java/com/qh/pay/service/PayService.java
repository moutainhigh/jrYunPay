package com.qh.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.qh.common.config.CfgKeyConst;
import com.qh.common.config.Constant;
import com.qh.common.utils.R;
import com.qh.pay.api.Order;
import com.qh.pay.api.PayConstants;
import com.qh.pay.api.constenum.AuditResult;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.constenum.PaymentMethod;
import com.qh.pay.api.utils.AesUtil;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.domain.*;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PayService
 * @Description 支付服务类
 * @Date 2017年11月6日 下午2:44:55
 * @version 1.0.0
 */
public interface PayService {

	/**
	 * @Description 发起支付
	 * @param merchant
	 * @return
	 */
	Object order(Merchant merchant, JSONObject jo);

	/**
	 * 查询商户余额
	 */
	R queryAmount(Merchant merchant);
	/**
	 * @Description 支付后台回调
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 */
	R notify(String merchNo, String orderNo, HttpServletRequest request, String requestBody);

	
	/**
	 * @Description 订单回调后通知
	 * @param merchNo
	 * @param orderNo
	 */
	String orderNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * 
	 * @Description 事件过期订单回调后通知
	 * @param merchNo
	 * @param orderNo
	 * @return
	 */
	String eventOrderNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 订单通知保存
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderDataMsg(String merchNo, String orderNo);

	/**
	 * @Description 支付订单查询
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R query(Merchant merchant, JSONObject jo);
	
	/**
	 * @Description 代付受理
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R orderAcp(Merchant merchant, JSONObject jo);
	
	
	/**
	 * @Description 审核未通过
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderAcpNopassDataMsg(String merchNo, String orderNo);
	/**
	 * @Description 代付下单发起
	 * @param merchNo
	 * @param orderNo 
	 */
	R orderAcp(String merchNo, String orderNo);
	
	/**
	 * @Description 代付订单回调通知
	 * @param merchNo
	 * @param orderNo 
	 * @return
	 */
	String orderAcpNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * @Description  事件过期订单回调后通知
	 * @param merchNo
	 * @param orderNo 
	 * @return
	 */
	String eventOrderAcpNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 代付最终保存结果
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderAcpDataMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 代付回调通知
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 * @return
	 */
	R notifyAcp(String merchNo, String orderNo, HttpServletRequest request, String requestBody);
	
	/**
	 * @Description 代付查询
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R acpQuery(Merchant merchant, JSONObject jo);
	
	
	/**
	 * 
	 * @Description 前台返回地址
	 * @param order
	 * @return
	 */
	 static String commonReturnUrl(Order order){
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_return_url) + 
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * 
	 * @Description 后台返回地址
	 * @param order
	 * @return
	 */
	static String commonNotifyUrl(Order order){
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_notify_url) +  
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}

	/**
	 * 补单回调
	 */
	R datetion(String orderNO);

	/**
	 * 
	 * @Description 前台返回地址
	 * @param order
	 * @return
	 */
	static String commonAcpReturnUrl(Order order){
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain  + RedisUtil.getSysConfigValue(CfgKeyConst.pay_acp_return_url) + 
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * 
	 * @Description 后台返回地址
	 * @param order
	 * @return
	 */
	public static String commonAcpNotifyUrl(Order order){
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_acp_notify_url) +  
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * @Description 跳转地址
	 * @return
	 * @throws Exception 
	 */
	static String commonJumpUrl(Order order) throws Exception {
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_jump_url) + 
				"?" + PayConstants.web_context +"="  +	paramEncrypt(order.getMerchNo(), order.getOrderNo());
	}
	
	/**
	 * @Description 快捷绑卡跳转地址
	 * @return
	 * @throws Exception 
	 */
	static String commonCardUrl(Order order) throws Exception {
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_card_url) + 
				"?" + PayConstants.web_context +"="  +	paramEncrypt(order.getMerchNo(), order.getOrderNo());
	}
	
	/**
	 * @Description 支付扫码跳转地址
	 * @param order
	 * @return
	 */
	static String commonQrUrl(Order order) throws Exception{
		String domain = order.getCallbackDomain();
		if(StringUtils.isBlank(order.getCallbackDomain())) {
			domain = RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain);
		}
		return domain + RedisUtil.getSysConfigValue(CfgKeyConst.pay_qr_url) +
				"?" + PayConstants.web_context +"="  +	paramEncrypt(order.getMerchNo(), order.getOrderNo());
	}
	
	static String paramEncrypt(String merchNo,String orderNo) throws Exception{
		return URLEncoder.encode(AesUtil.encrypt(merchNo + RedisConstants.link_symbol + orderNo,AesUtil.getPay_key()),Constant.ec_utf_8);
	}
	
	/**
	 * 
	 * @Description 初始化返回数据
	 * @param order
	 * @return
	 */
	static Map<String,String> initRspData(Order order){
		Map<String, String> data = new HashMap<>();
		data.put(OrderParamKey.merchNo.name(), order.getMerchNo());
		data.put(OrderParamKey.orderNo.name(), order.getOrderNo());
		data.put(OrderParamKey.outChannel.name(), order.getOutChannel());
		return data;
	}

	/**
	 * @Description 初始化商户资金流水
	 * @param order
	 * @return
	 */
	static RecordMerchBalDO initRdMerchBal(Order order,int feeType,int orderType,int profitLoss) {
		RecordMerchBalDO rdMerchBal = new RecordMerchBalDO();
		rdMerchBal.setOrderNo(order.getOrderNo());
		rdMerchBal.setMerchNo(order.getMerchNo());
		rdMerchBal.setFeeType(feeType);
		rdMerchBal.setOrderType(orderType);
		rdMerchBal.setCrtDate(order.getCrtDate());
		rdMerchBal.setProfitLoss(profitLoss);
		return rdMerchBal;
	}

	/**
	 * @Description 初始化商户资金流水
	 * @param merchCharge
	 * @return
	 */
	static RecordMerchBalDO initRdMerchChargeBal(MerchCharge merchCharge,int feeType,int orderType,int profitLoss) {
		RecordMerchBalDO rdMerchBal = new RecordMerchBalDO();
		rdMerchBal.setOrderNo(merchCharge.getBusinessNo());
		rdMerchBal.setMerchNo(merchCharge.getMerchNo());
		rdMerchBal.setFeeType(feeType);
		rdMerchBal.setOrderType(orderType);
		rdMerchBal.setCrtDate(merchCharge.getCrtDate());
		rdMerchBal.setProfitLoss(profitLoss);
		return rdMerchBal;
	}
	
	/**
	 * @Description 初始化平台资金流水
	 * @param order
	 * @return
	 */
	static RecordFoundAcctDO initRdFoundAcct(Order order,int feeType,int orderType,int profitLoss,String username) {
		RecordFoundAcctDO rdFoundAcct = new RecordFoundAcctDO();
		rdFoundAcct.setOrderNo(order.getOrderNo());
		rdFoundAcct.setMerchNo(order.getMerchNo());
		rdFoundAcct.setFeeType(feeType);
		rdFoundAcct.setOrderType(orderType);
		rdFoundAcct.setCrtDate(order.getCrtDate());
		rdFoundAcct.setProfitLoss(profitLoss);
		rdFoundAcct.setUsername(username);
		return rdFoundAcct;
	}
	/**
	 * @Description 初始化平台资金流水
	 * @param order
	 * @return
	 */
	static RecordFoundAcctDO initRdFoundAcct(Order order,int feeType,int orderType,int profitLoss) {
		return initRdFoundAcct(order, feeType, orderType, profitLoss, null);
	}
	
	/**
	 * @Description 初始化第三方支付公司资金流水
	 * @param order
	 * @return
	 */
	static RecordPayMerchBalDO initRdPayMerchAcct(Order order,int feeType,int orderType,int profitLoss) {
		RecordPayMerchBalDO rdPayMerchAcct = new RecordPayMerchBalDO();
		rdPayMerchAcct.setOrderNo(order.getOrderNo());
		rdPayMerchAcct.setMerchNo(order.getMerchNo());
		rdPayMerchAcct.setFeeType(feeType);
		rdPayMerchAcct.setOrderType(orderType);
		rdPayMerchAcct.setCrtDate(order.getCrtDate());
		rdPayMerchAcct.setProfitLoss(profitLoss);
		rdPayMerchAcct.setPayCompany(order.getPayCompany());
		rdPayMerchAcct.setOutChannel(order.getOutChannel());
		rdPayMerchAcct.setPayMerch(order.getPayMerch());
		return rdPayMerchAcct;
	}

	/**
	 * @Description 初始化审核记录
	 * @param order
	 * @return
	 */
	static PayAuditDO initPayAudit(Order order,int auditType) {
		PayAuditDO payAudit = new PayAuditDO();
		payAudit.setOrderNo(order.getOrderNo());
		payAudit.setMerchNo(order.getMerchNo());
		payAudit.setAuditType(auditType);
		payAudit.setAuditResult(AuditResult.init.id());
		payAudit.setCrtTime(DateUtil.getCurrentTimeInt());
		return payAudit;
	}

	/**
	 * 支付商户号分身
	 * @param payMerch
	 * @return
	 */
	static  String PayMerchAvatar(String payMerch) {
		if(payMerch.lastIndexOf("_"+PaymentMethod.D0) > 0) {
			payMerch = payMerch.substring(0, payMerch.lastIndexOf("_"+PaymentMethod.D0));
		}else if(payMerch.lastIndexOf("_"+PaymentMethod.D1) > 0) {
			payMerch = payMerch.substring(0, payMerch.lastIndexOf("_"+PaymentMethod.D1));
		}else if(payMerch.lastIndexOf("_"+PaymentMethod.T1) > 0) {
			payMerch = payMerch.substring(0, payMerch.lastIndexOf("_"+PaymentMethod.T1));
		}else if(payMerch.lastIndexOf("_"+PaymentMethod.T0) > 0) {
			payMerch = payMerch.substring(0, payMerch.lastIndexOf("_"+PaymentMethod.T0));
		}
		return payMerch;
	}
	
	/**
	 * @Description 商户充值保存数据
	 * @param merchNo
	 * @param orderNo
	 */
	void chargeDataMsg(String merchNo, String orderNo);

	/**
	 * @Description 手动同步
	 * @param merchNo
	 * @param orderNo
	 * @param businessNo 
	 * @return
	 */
	R syncOrder(String merchNo, String orderNo, String businessNo);

	/**
	 * @Description 同步代付订单信息
	 * @param merchNo
	 * @param orderNo
	 * @param businessNo
	 * @return
	 */
	R syncOrderAcp(String merchNo, String orderNo, String businessNo);

	/**
	 * @Description 用户提现
	 * @param order
	 * @return
	 */
	R withdraw(Order order);

	/**
	 * @Description 线下转账
	 * @param orderNo
	 * @param merchNo
	 * @param auditType
	 * @return
	 */
	R offlineTransfer(String orderNo, String merchNo, Integer auditType);



}
