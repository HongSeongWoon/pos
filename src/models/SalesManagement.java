package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesManagement {
	private DataAccessObject dao;
	public SalesManagement() {}
	
	public String backController(String jobCode,String[] data) {
		String message=null;
		switch(jobCode) {
		case "4S":
			message=this.ctlSales(data);
			break;
		case "4D":
			message=this.ctlOrders(data);
			break;
			
		}
		
		return message;
	}
	/*주문처리*/
	private String ctlOrders(String[] data) {
		String message = null;
		dao=new DataAccessObject();
		String date = now();
		for(int recordIndex=0; recordIndex<data.length;recordIndex++) {
			data[recordIndex]= date +","+ data[recordIndex];
		}
		dao=new DataAccessObject();
		//boolean result = dao.setOrders(data);
		message = dao.setOrders(data)? "주문이 완료되었습니다.":"주문 완료에 실패하였습니다.";
		return message;
	}
	private String now() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		Date d=new Date();
		return sdf.format(d);
	}

	/* 판매개시 */
	private String ctlSales(String[] data) {
		// 전달 받은 상품코드로 상품 검색 후 상품정보 전달
		dao=new DataAccessObject();
		String[] rowData=dao.getGoodsInfo(data[0]);
		String goodsInfo = rowData[0]+","+rowData[1]+","+rowData[2]+","+"0"+","+rowData[5];
		
		
		return goodsInfo;
	}
}
