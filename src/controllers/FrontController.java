package controllers;

import models.*;

/* Role : 클라이언트로부터의 서비스 요청에 따라 해당 서비스 클래스 호출
 *         :: 클라이언트로부터 전달되어지는 JobCode로 분기
 *     
 * */    
public class FrontController {
	private String message = null;
	private StoreManagement sm = null;
	private MenuManagement mm = null;
	private MemberManagement member = null;
	private SalesManagement sales = null;
	
	public FrontController() {}
	
	public String getRequest(String jobCode) {
		
		switch(jobCode.charAt(0)) {
		case '1':
			sm = new StoreManagement();
			message = sm.backController(jobCode);
			break;
		case '2':
			mm = new MenuManagement();
			message = mm.backController(jobCode);
			break;
		case '3':
			member = new MemberManagement();
			message = member.backController(jobCode);
			break;
		
		}
		
		return message;
	}
	
	
	public String getRequest(String jobCode, String[] data) {
			
		switch(jobCode.charAt(0)) {
		case '1':
			sm = new StoreManagement();
			message = sm.backController(jobCode, data);
		
			break;
		case '2':
			mm = new MenuManagement();
			message = mm.backController(jobCode, data);
			break;
		case '3':
			member = new MemberManagement();
			message = member.backController(jobCode, data);
			break;
		case '4':
			sales = new SalesManagement();
			message =sales.backController(jobCode,data);
			
			break;	
		}
		
		return message;
	}
}

/* switch(데이터){
 * case 비교값:
 *   실행구문;
 *   break;
 * case 비교값:
 *   실행구문;
 *   break;
 * case 비교값:
 *   실행구문;
 *   break;
 *     :  
 * }
 * 
 * 매장관리 : 11, 12, 13, 14  >> StoreManagement.class
 * 상품관리 : 21, 22, 23, 24, 25, 26, >> MenuManagement.class
 * 회원관리 : 31, 32, 33, >> MemberManagement.class
 * 판매관리 : 4 >> SalesManagement.class
 * 
 * 
 * View :: jobCode >> FrontController :: jobCode로 분기  >> 해당 클래스
 * View >> FrontController << 해당 클래스
 */
