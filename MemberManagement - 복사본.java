package models;

import java.util.ArrayList;

import beans.GoodsBean;
import beans.MembersBean;

public class MemberManagement {
	private DataAccessObject dao;
	private OracleDAO oracleDao;
	public MemberManagement() {

	}

	public String backController(String clientData) {
		MembersBean member = null;
		String[] data = null;
		String message = null;
		String jobCode = null;
		/*2R?data& &data*/
		if(clientData.indexOf('?') != -1) {
			jobCode = clientData.substring(0, clientData.indexOf('?'));
			data = clientData.substring(
					clientData.indexOf('?') + 1).split("&");
			if(data.length>0) { 
				member = new MembersBean();
				member.setMemberCode(data[0]);
				if(jobCode.equals("3R")) {
					// +++> 구매가, 재고
					member.setMemberName(data[1]);
					member.setMemberPhone(Integer.parseInt(data[2]));
					member.setMemberRank(data[3]);
			
					}else if(jobCode.equals("3M")) {
						
				}
			}
		}else{
			jobCode = clientData;
		}
		
		switch(jobCode) {
		case "31": case "32": case "33":
			message = this.ctlReadMember();
			break;
		case "3R":
			message = this.ctlRegMember(member);
			break;
		
		}
		return message;
	}
	
	
	
	private String ctlModMember(String[] data) {
		dao = new DataAccessObject();
		String[][] list = dao.getMemberList();
		
		for(int recordIndex=0; recordIndex<list.length; recordIndex++ ) {
			if(data[0].equals(list[recordIndex][0])){
				list[recordIndex][2] = data[1];
				break;
			}
		}
		
		return (dao.setMember(list))? this.toStringFromArray(dao.getMemberList()):"회원정보 수정이 실패하였습니다. 다시 시도해 주세요~"; 
	}
	
	private String ctlDelMember(String[] data) {
		dao = new DataAccessObject();
		
		return this.toStringFromArray(dao.getMemberList()); 
	}

	private String ctlReadMember() {
		dao = new DataAccessObject();
		return this.toStringFromArray(dao.getMemberList());
	}
	
	private String toStringFromArray(ArrayList<MembersBean> memberList) {
		StringBuffer sb = new StringBuffer();
		
		for(int recordIndex=0; recordIndex<memberList.size(); recordIndex++) {
			sb.append(" ");
			sb.append(memberList.get(recordIndex).getMemberCode());
			sb.append("\t");
			sb.append(memberList.get(recordIndex).getMemberName());
			sb.append("\t");
			sb.append(memberList.get(recordIndex).getMemberPhone());
			sb.append("\t");
			sb.append(memberList.get(recordIndex).getMemberRank());
			sb.append("\n");
		}
		return sb.toString();
	}
	

	private String toStringFromArray(String[][] data) {
		StringBuffer sb = new StringBuffer();

		for(int recordIndex=0; recordIndex<data.length; recordIndex++) {
			sb.append(" ");
			for(int colIndex=0; colIndex<data[recordIndex].length; colIndex++) {
				sb.append(data[recordIndex][colIndex]);
				
				if(colIndex != data[recordIndex].length - 1) {
					sb.append("\t");
					if(colIndex == 1 && data[recordIndex][colIndex].length()<6) {
						sb.append("\t");
					}
				}
			}
			sb.append("\n");
		}


		return sb.toString();
	}

	/* 회원정보등록 */
	private String ctlRegMember(MembersBean member) {
		String memberList = null;
		boolean tran = false;

		oracleDao = new OracleDAO();
		oracleDao.startTransaction(false);

		// 1. 회원코드, 전화번호 중복 확인
		if(oracleDao.isMembersInfo(member)) {
			System.out.println(1);
			// 2. 회원등급코드 유효성 확인
			if(oracleDao.isMembersRank(member)) {
				System.out.println(2);
				// 3. 회원등록
				if(!oracleDao.regMembers(member)) {
					System.out.println(3);
					tran = true;
					// 4. 등록된 회원정보 가져오기
					memberList = this.toStringFromArray(oracleDao.getMembersInfo(member));					
				}else {
					memberList = "잠시 후 다시 시도해 주세요~";
				}
			} else {
				memberList = "회원등급코드가 존재하지 않습니다.";
			}
		}else {
			memberList = "회원코드나 전화번호가 중복되었습니다.";
		}

		oracleDao.endTransaction(tran);
		// 등록메뉴를 리턴
		return memberList;
	}
}
