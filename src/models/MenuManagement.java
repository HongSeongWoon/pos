package models;

import java.util.ArrayList;

import beans.GoodsBean;

public class MenuManagement {
	private DataAccessObject dao;

	public MenuManagement() {

	}

	public String backController(String clientData) {
		GoodsBean goods = null;
		String[] data = null;
		String jobCode = null;
		/*2R?data&data&data*/
		if(clientData.indexOf('?') != -1) {//21 일때 -1로 리턴된값을 조건으로 써준것.
			jobCode=clientData.substring(0, clientData.indexOf('?'));
			data = clientData.substring(clientData.indexOf('?')+1).split("&");
			if(data.length>0) {//데이터가 있을때에만 진행한다. 21일때는 진행이 안된다.
				goods=new GoodsBean();
				goods.setMenuCode(data[0]);
				if(jobCode.equals("2R")){
					goods.setMenuName(!data[1].equals(jobCode)? data[1]:null);
					goods.setMenuPrice(data[2] != null?  Integer.parseInt(data[2]):null);
					goods.setMenuState(data[3] != null? data[3].charAt(0):null);
					goods.setMenuCategory(data[4] != null? data[4]:null);
					goods.setDiscountRate(data[5] != null? Integer.parseInt(data[5]):null);
				}else if(jobCode.equals("2M")){
					goods.setMenuPrice(Integer.parseInt(data[1]));
					goods.setDiscountRate(Integer.parseInt(data[2]));
				}
			}
		}
		else {
			jobCode = clientData;
		}
		/*배열데이터를 빈즈에 담기*/

		String message = null;
		switch(jobCode) {
		case "21":case "22":case "23":
			message = this.ctlReadMenu();
			break;
		case "2R":
			message = this.ctlRegMenu(goods);
			break;
		case "2M":
			message = this.ctlModMenu(goods);
			break;
		case "2D":
			message = this.ctldelMenu(goods);
			break;
		}
		return message;
	}



	/*메뉴 내용 수정*/
	private String ctlModMenu(GoodsBean data) {
		dao=new DataAccessObject();
		ArrayList<GoodsBean> menuList=dao.getMenu();

		for(int recordIndex=0; recordIndex<menuList.size(); recordIndex++) {
			if(menuList.get(recordIndex).getMenuCode().equals(data.getMenuCode())) {
				menuList.get(recordIndex).setMenuPrice(data.getMenuPrice());
				menuList.get(recordIndex).setDiscountRate(data.getDiscountRate());
				break;
			}
		}


		return (dao.setMenu(menuList))? this.toStringFromArray(dao.getMenu()):"메뉴수정에 실패하였습니다. 다시 시도해 주세요";
	}
	/*메뉴삭제*/
	private String ctldelMenu(GoodsBean data) {
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
		dao = new DataAccessObject();		
		return this.toStringFromArray(dao.getMenu());
	}
	/* 2차원 배열 --> String */
	private String toStringFromArray(ArrayList<GoodsBean> menuList) {
		StringBuffer sb = new StringBuffer();

		for(int recordIndex=0; recordIndex<menuList.size(); recordIndex++) {
			sb.append(" ");
			sb.append(menuList.get(recordIndex).getMenuCode());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuName());
			sb.append(menuList.get(recordIndex).getMenuName().length()<6?"\t":"");
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuPrice());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuState()=='1'? "가능":"불능");
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getMenuCategory());
			sb.append("\t");
			sb.append(menuList.get(recordIndex).getDiscountRate());
			sb.append("\n");
		}
		return sb.toString();
	}

	/* 메뉴등록 */
	private String ctlRegMenu(GoodsBean data) {
		String menuList = null;
		dao = new DataAccessObject();
		// DAO에 메뉴등록 요청
		if(dao.setMenu(data)) {
			// DAO에 등록된 메뉴 읽기 요청
			menuList = this.toStringFromArray(dao.getMenu());
		}else {
			menuList = "메뉴등록작업이 실패하였습니다. 다시 한번 입력해주세요";
		}

		// 등록메뉴를 리턴
		return menuList;
	}

	/* 메뉴수정 */
	private String ctlModMenu() {

		dao = new DataAccessObject();

		return this.toStringFromArray(dao.getMenu());
	}

	/* 메뉴삭제 */
	private String ctlDelMenu() {

		dao = new DataAccessObject();

		return this.toStringFromArray(dao.getMenu());
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
