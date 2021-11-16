package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreManagement {
	private Date d;
	private DataAccessObject dao;
	
	public StoreManagement() {
		// 1. 현재 시스템 날짜와 시간을 취득  yyyyMMddHHmmss
		d = new Date();
		dao = new DataAccessObject();
	}

	public String backController(String jobCode) {
		String message = null;
		switch(jobCode) {
		case "11":
			message = this.ctlStoreOpen();
			break;
		case "12":
			this.ctlStoreClose();
			break;
		case "13":
			this.ctlTodaySales();
			break;
		case "14":
			this.ctlSalesAnalisis();
			break;
		}
		return message;
	}
	public String backController(String jobCode,String[] data) {
		String message = null;
		switch(jobCode) {
		case "13":
			message=this.getSalesMonthStat(data);
			
			break;
		case "14":
			message=this.getSelectedStat(data);
			
			break;
		}
		return message;
	}

	/*jobCode :: 14 >> 해당범위 매출현황*/
	private String getSelectedStat(String[] data) {
		String message=null;
		String[][] salesStat = null;
		dao = new DataAccessObject();
		
		salesStat = dao.getSalesStat(data);
		/* 해당월   건수    매출액    할인적용매출액 
		 * */
		StringBuffer buffer = new StringBuffer();
		buffer.append(" "+data[0]);
		buffer.append("\t");
		buffer.append(salesStat.length);
		buffer.append("\t");
		buffer.append(this.getSalesAmount(salesStat));
		buffer.append("\t");
		buffer.append(this.getDiscountSalesAmount(salesStat));
		return buffer.toString();
	}
	
	/*jobCode :: 13 >> 금월 매출현황*/
	private String getSalesMonthStat(String[] data) {
		String message=null;
		String[][] salesMonthStat = null;
		dao = new DataAccessObject();
		
		salesMonthStat = dao.getSalesMonthStat(data[0]);
		/* 해당월   건수    매출액    할인적용매출액 
		 * */
		StringBuffer buffer = new StringBuffer();
		buffer.append(" "+data[0]);
		buffer.append("\t");
		buffer.append(salesMonthStat.length);
		buffer.append("\t");
		buffer.append(this.getSalesAmount(salesMonthStat));
		buffer.append("\t");
		buffer.append(this.getDiscountSalesAmount(salesMonthStat));
		return buffer.toString();
	}

	private int getDiscountSalesAmount(String[][] stat) {
		int sum=0;
		for(int recordIndex=0; recordIndex<stat.length; recordIndex++) {
			// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
			sum += (Integer.parseInt(stat[recordIndex][3])*Integer.parseInt(stat[recordIndex][4]))*((100-Integer.parseInt(stat[recordIndex][5]))/100.0);//실수로 나누어서 값도 실수
		}
		return sum;
	}

	private int getSalesAmount(String[][] stat) {
		int sum=0;
		for(int recordIndex=0; recordIndex<stat.length; recordIndex++) {
			// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
			sum += Integer.parseInt(stat[recordIndex][3])*Integer.parseInt(stat[recordIndex][4]);
		}
		return sum;
	}

	/*jobCode :: 11  >> 매장오픈 처리 */
	private String ctlStoreOpen() {
		boolean response;
		//  1-1. yyyyMMdd & HHmmss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 2. 파일 저장
		//  2-1. 파일 접근 클래스 호출 :: DataAccessObject.class
		// 3. 매장오픈이 처리되었는지 안되었는지 응답 받기  true ::오픈성공 false:오픈실패
		response = dao.setStoreState(sdf.format(d).substring(0, 8), 
				sdf.format(d).substring(8), 1);
		 
		// 4. View에 전달에 메세지 리턴
		return (response)? "매장이 오픈되었습니다." : "매장 오픈이 실패하였습니다.";
	}

	/*jobCode :: 12  >> 매장클로즈 처리 */
	private void ctlStoreClose() {
		
	}

	/*jobCode :: 13  >> 금일 매출 현황 처리 */
	private void ctlTodaySales() {

	}

	/*jobCode :: 14  >> 매출 통계 처리 */
	private void ctlSalesAnalisis(){

	}
}
