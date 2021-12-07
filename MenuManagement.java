package models;

import java.util.ArrayList;

import beans.GoodsBean;

public class MenuManagement {
	private DataAccessObject dao;
	private OracleDAO oracleDao;
	public MenuManagement() {

	}

	public String backController(String clientData) {
		GoodsBean goods = null;
		String[] data = null;
		String jobCode = null;
		
		/*2R?data& &data*/
		if(clientData.indexOf('?') != -1) {
			jobCode = clientData.substring(0, clientData.indexOf('?'));
			data = clientData.substring(
					clientData.indexOf('?') + 1).split("&");
			if(data.length>0) { 
				goods = new GoodsBean();
				goods.setMenuCode(data[0]);
				if(jobCode.equals("2R")) {
					// +++> 구매가, 재고
					goods.setMenuName(data[1]);
					goods.setMenuPrice(Integer.parseInt(data[2]));
					goods.setMenuState(Integer.parseInt(data[3]));
					goods.setMenuCategory(data[4]);
					goods.setDiscountRate(Integer.parseInt(data[5]));
					goods.setMenuCost(Integer.parseInt(data[6]));
					goods.setMenuStocks(Integer.parseInt(data[7]));
				}else if(jobCode.equals("2M")) {
					goods.setMenuPrice(Integer.parseInt(data[1]));
					goods.setDiscountRate(Integer.parseInt(data[2]));
				}
			}
		}else{
			jobCode = clientData;
		}
		
		
		/* 배열데이터를 빈즈에 담기 */
		
		String message = null;
		switch(jobCode) {
		case "21": case "22": case "23":
			message = this.ctlReadMenu();
			break;
		case "2R":
			message = this.ctlRegMenu(goods);
			break;
		case "2M":
			message = this.ctlModMenu(goods);
			break;
		case "2D":
			message = this.ctlDelMenu(goods);
			break;
		case "24":
			this.ctlRegGoods();
			break;
		case "25":
			this.ctlModGoods();
			break;
		case "26":
			this.ctlDelGoods();
			break;
		}
		return message;
	}
	
	/* 메뉴등록 */
	private String ctlRegMenu(GoodsBean goods) {
		String menuList = null;
		boolean tran = false;
		
		oracleDao = new OracleDAO();
		oracleDao.startTransaction(false);
		
		// 1. 상품코드, 상품명 중복 확인
		if(oracleDao.isGoodsInfo(goods)) {
			// 2. 상품상태코드 유효성 확인
			if(!oracleDao.isGoodsStates(goods)) {
				// 3. 상품분류코드 유효성 확인
				if(!oracleDao.isGoodsCategories(goods)) {
					// 4. 상품등록
					if(!oracleDao.regGoods(goods)) {
						tran = true;
						// 5. 등록된 상품정보 가져오기
						menuList = this.toStringFromArray(oracleDao.getGoodsInfo(goods));
						
					}else {
						menuList = "잠시 후 다시 시도해 주세요.";
					}
				}else {
					menuList = "상분분류코드가 존재하지 않습니다.";
				}
			}else {
				menuList = "상품상태코드가 존재하지 않습니다.";
			}
		}else {
			menuList = "상품코드나 상품명이 중복되었습니다.";
		}
		
		oracleDao.endTransaction(tran);

		// 등록메뉴를 리턴
		return menuList;
	}
	
	/* 메뉴 내용 수정 */
	private String ctlModMenu(GoodsBean data) {
		dao = new DataAccessObject();
		ArrayList<GoodsBean> menuList = dao.getMenu();
		
		for(int recordIndex=0; recordIndex<menuList.size(); recordIndex++) {
			if(menuList.get(recordIndex).getMenuCode().equals(data.getMenuCode())) {
				menuList.get(recordIndex).setMenuPrice(data.getMenuPrice());
				menuList.get(recordIndex).setDiscountRate(data.getDiscountRate());
				break;
			}
		}
		
		return (dao.setMenu(menuList))? this.toStringFromArray(dao.getMenu()):"메뉴수정에 실패하였습니다. 다시 시도해 주세요";
	}
	/* 메뉴 삭제 */
	private String ctlDelMenu(GoodsBean data) {
		ArrayList<GoodsBean> menuList;
				
		dao = new DataAccessObject();
		/* DB 파일에 저장되어있는 모든 메뉴를 2차원 배열로 가져오기 */
		menuList = dao.getMenu();
		for(int recordIndex=0; recordIndex<menuList.size(); recordIndex++) {
			if(menuList.get(recordIndex).getMenuCode().equals(data.getMenuCode())) {
				menuList.remove(recordIndex);
				break;
			}
		}
		
		return (dao.setMenu(menuList))?this.toStringFromArray(dao.getMenu()):"메뉴삭제가 실패했습니다. 다시 시도해 주세요";
	}
	
	/* 메뉴읽기 */
	private String ctlReadMenu() {
				
		return "NEW 메뉴 정보 등록";
	}
	
	/* 2차원 배열 --> String */
	private String toStringFromArray(ArrayList<GoodsBean> menuList) {
		StringBuffer sb = new StringBuffer();
		
		for(int recordIndex=0; recordIndex<menuList.size(); recordIndex++) {
			sb.append(" ");
			sb.append(menuList.get(recordIndex).getMenuCode());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuName());
			sb.append(menuList.get(recordIndex).getMenuName().length() < 6? "\t\t": "\t");
			sb.append(menuList.get(recordIndex).getMenuPrice());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getStateName());
			sb.append(menuList.get(recordIndex).getStateName().length() < 6? "\t\t": "\t");
			sb.append(menuList.get(recordIndex).getCategoryName());
			sb.append(menuList.get(recordIndex).getCategoryName().length() < 6? "\t\t": "\t");
			sb.append(menuList.get(recordIndex).getDiscountRate());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuCost());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuStocks());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuState());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuCategory());
			
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
	/* 굿즈등록 */
	private void ctlRegGoods() {

	}

	/* 굿즈정보수정 */
	private void ctlModGoods() {

	}

	/* 굿즈 삭제 */
	private void ctlDelGoods() {

	}
}
