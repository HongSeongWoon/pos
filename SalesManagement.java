package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import beans.OrderBean;

public class SalesManagement {
	DataAccessObject dao;
	
	public SalesManagement() {}
	
	public String backController(String request) {
		ArrayList orders = new ArrayList();
		String message = null;
		String jobCode = request.substring(0, request.indexOf("?"));
		/* Client Data 추출 
		 * jobCode 4S : String 상품코드
		 *         20211115160051,1001,아메리카노,2000,1,10,1001
		 * jobCode 4D : String 상품코드  int 수량   String 회원코드
		 *            + String 날짜  String 상품명    int 가격   int 할인율  
		 * */
		/*  4S?1001&1 */
		String[] data = request.substring(request.indexOf("?")+1).split("&");
		if(data.length == 1) {
			/*  4S?1001 */
			OrderBean ob = new OrderBean();
			ob.setGoodsCode(data[0]);
			orders.add(ob);
		}else if(data.length % 2 == 0) {
			/*  4S?1001&1 */
			for(int recordIndex=0; recordIndex<data.length; recordIndex+=2) {
			OrderBean ob = new OrderBean();
			ob.setGoodsCode(data[recordIndex]);
			ob.setOrderQuantity(Integer.parseInt(data[recordIndex+1]));
			orders.add(ob);
			}
		}else {
			/*  4S?1001&1&1001 */
			/*  4S?1001&1&1002&2&1005 */
			String memberCode = data[data.length-1];			
			for(int recordIndex=0; recordIndex<data.length-1; recordIndex+=2) {
				//recordIndex = recordIndex*2;
				
				OrderBean ob = new OrderBean();
			
				ob.setGoodsCode(data[recordIndex]);
				ob.setOrderQuantity(Integer.parseInt(data[recordIndex+1]));
				if(data.length %2==1) {
				ob.setMemberCode(memberCode);}
				orders.add(ob);
				
			}			
		}
		
		
		switch(jobCode) {
		case "4S":
			message = this.ctlSales(orders);
			break;
		case "4D":
			message = this.ctlOrders(orders);
			break;
		}
		
		return message;
	}
	
	/* 주문처리 */
	private String ctlOrders(ArrayList orders) {
		String message = null;
		dao = new DataAccessObject();
		String date = now();
		for(int recordIndex=0; recordIndex<orders.size(); recordIndex++) {
			 //orders.set(recordIndex, date);
			 ((OrderBean)orders.get(recordIndex)).setOrderCode(date);
		}
		
		String[] daoOrders = new String[orders.size()];
		for(int recordIndex=0; recordIndex<daoOrders.length; recordIndex++) {
			daoOrders[recordIndex] = ((OrderBean)orders.get(recordIndex)).getOrderCode() +
					"," + ((OrderBean)orders.get(recordIndex)).getGoodsCode() + 
					"," + ((OrderBean)orders.get(recordIndex)).getOrderQuantity() + 
					(((OrderBean)orders.get(recordIndex)).getMemberCode() != null? "," + ((OrderBean)orders.get(recordIndex)).getMemberCode() : "");
		}
		
		/* boolean result = dao.setOrders(String[] orders) */
		message = dao.setOrders(daoOrders)? "주문이 완료되었습니다.":"죄송합니다.~~~";
		return message;
	}
	
	private String now() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = new Date();
		return sdf.format(d);
	}
	
	/* 판매개시 */
	private String ctlSales(ArrayList orders) {
		String goodsInfo;
		// 전달 받은 상품코드로 상품 검색 후 상품정보 전달
		dao = new DataAccessObject();
		/* 10000001   (HOT)아메리카노   2500   qty     0% */
		String[] rowData = dao.getGoodsInfo(((OrderBean)orders.get(0)).getGoodsCode());
		System.out.println(rowData[0]);
		goodsInfo = rowData[0] + "," + rowData[1] + "," + rowData[2] + "," + "0" + "," + rowData[5];
		
		return goodsInfo;
	}
}
