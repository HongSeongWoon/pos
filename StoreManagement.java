package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import beans.StoreBean;

public class StoreManagement {
	private Date d;
	private OracleDAO dao =null;
	private StoreBean store;

	public StoreManagement() {
		// 1. 현재 시스템 날짜와 시간을 취득  yyyyMMddHHmmss
		d = new Date();

	}

	public String backController(String jobCodeData) {
		String message = null;
		String jobCode= jobCodeData.substring(0,2);
		switch(jobCode) {
		case "11":
			message = this.ctlStoreOpen(jobCodeData.substring(jobCodeData.indexOf('?')+1));
			break;
		case "12":
			message = this.ctlStoreClose(jobCodeData.substring(jobCodeData.indexOf('?')+1));
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

	public String backController(String jobCode, String[] data) {
		String message = null;
		switch(jobCode) {
		case "13":
			message = getSalesMonthStat(data);
			break;
		}

		return message;
	}

	/*jobCode :: 13  >> 금월 매출현황 */
	private String getSalesMonthStat(String[] data) {
		String message = null;
		String[][] salesMonthStat = null;
		dao = new  OracleDAO();
		salesMonthStat = null;//추후수정
		/* 해당월   건수    매출액    할인적용매출액 
		 * */
		StringBuffer buffer = new StringBuffer();
		buffer.append(" " + data[0]);
		buffer.append("\t");
		buffer.append(salesMonthStat.length);
		buffer.append("\t");
		buffer.append(this.getSalesAmount(salesMonthStat));
		buffer.append("\t");
		buffer.append(this.getDiscountSalesAmount(salesMonthStat));
		return buffer.toString();
	}

	/* 매출액 구하기 */
	private int getSalesAmount(String[][] stat) {
		int sum = 0;
		for(int recordIndex=0; recordIndex<stat.length; recordIndex++) {
			// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
			sum += Integer.parseInt(stat[recordIndex][3]) * Integer.parseInt(stat[recordIndex][4]);
		}
		return sum;
	}
	/* 할인매출액 구하기 */
	private int getDiscountSalesAmount(String[][] stat) {
		int sum = 0;
		for(int recordIndex=0; recordIndex<stat.length; recordIndex++) {
			// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
			sum += (Integer.parseInt(stat[recordIndex][3]) * Integer.parseInt(stat[recordIndex][4])) * ((100 - Integer.parseInt(stat[recordIndex][5]))/100.0);
		}
		return sum;
	}
	/*jobCode :: 11  >> 매장오픈 처리 */
	private String ctlStoreOpen(String clientData) {
		String message ="";
		boolean response;
		boolean tranState = false;
		store = new StoreBean();

		/*split을 이용한 데이터분리*/
		String[] data = clientData.split("&");
		store.setSeCode(data[0]);
		store.setEmCode(data[1]);
		store.setSeState(9);

		dao = new OracleDAO();
		
		/* PROCESS
		 * 0. CONNECTION 생성
		 * 1. STOFRES :: SELECT << 매장코드 >> R:1  >> P2
		 * 	  DAO :: ISSOTRE
		 * 2. EMPLOYEES :: SELECT << 직원코드  >> R:1 >> P3
		 *    DAO :: ISEMPLYEE
		 * 3. STOREHISTORY :: INSERT << 오픈일시, 매장코드, 직원코드, 9>> R:1
		 * DAO : STOREMANAGEMENT
		 * 4. STOREHISTORY :: SELECT << 매장코드, 오픈일 >> R:오픈일시
		 * 5. TRANSACTION END
		 * 
		 * */
		dao.startTransaction(false);
		if(dao.isStore(store)) {
			System.out.println(1);
			if(dao.isEmployee(store)) {
				System.out.println(2);
				if(dao.storeManagement(store)) {
					System.out.println(3);
					if((store=dao.getStoreInfo(store)) != null) {
						System.out.println(4);
						System.out.println(store.getSeDate());
						System.out.println(store.getSeCode());
						System.out.println(store.getEmCode());
						
							tranState = true;
						}else {
						message ="잠시후 다시 시도해주세요!";
					}

				}else {
					message ="잠시후 다시 시도해주세요!"; 
				}

			}else {
				message ="존재하지 않는 직원코드!"; 
			}

		}else {
			message ="존재하지 않는 매장코드!"; 
		}

		
		dao.endTransaction(tranState);
		
		return message;
		/*클라이언트 보낼 정보 가공*/

	}

	/*jobCode :: 12  >> 매장클로즈 처리 */
	private String ctlStoreClose(String clientData) {
		String message ="";
		boolean response;
		boolean tranState = false;
		store = new StoreBean();

		/*split을 이용한 데이터분리*/
		String[] data = clientData.split("&");
		store.setSeCode(data[0]);
		store.setEmCode(data[1]);
		store.setSeState(-9);

		dao = new OracleDAO();
		
		dao.startTransaction(false);
		if(dao.isStore(store)) {
			System.out.println(1);
			if(dao.isEmployee(store)) {
				System.out.println(2);
				if(dao.storeManagement(store)) {
					System.out.println(3);
					if((store=dao.getStoreInfo(store)) != null) {
						System.out.println(4);
						System.out.println(store.getSeDate());
						System.out.println(store.getSeCode());
						System.out.println(store.getEmCode());
						
							tranState = true;
						}else {
						message ="잠시후 다시 시도해주세요!";
					}

				}else {
					message ="잠시후 다시 시도해주세요!"; 
				}

			}else {
				message ="존재하지 않는 직원코드!"; 
			}

		}else {
			message ="존재하지 않는 매장코드!"; 
		}

		
		dao.endTransaction(tranState);
		
		return message;
	}

	/*jobCode :: 13  >> 금일 매출 현황 처리 */
	private void ctlTodaySales() {

	}

	/*jobCode :: 14  >> 매출 통계 처리 */
	private void ctlSalesAnalisis(){

	}
}
