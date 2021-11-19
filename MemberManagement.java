package models;

import java.util.ArrayList;

import beans.GoodsBean;
import views.MemberBean;

public class MemberManagement {
	private DataAccessObject dao;
	public MemberManagement() {

	}

	public String backController(String data) {
		String message = null;
		String jobCode=null;
		String rowData=null;
		if(data.indexOf('?') != -1) {
			jobCode=data.substring(0,data.indexOf('?'));
			rowData=data.substring(data.indexOf('?')+1);
		}else {
			jobCode =data;
		}
		//System.out.println(rowData);
		switch(jobCode) {
		case "31":case "32":case "33":
			message = this.ctlReadMember();
			break;
		case "3R":
			message = this.ctlRegMember(rowData);
			break;
		case "3M":
			message = this.ctlModMember(rowData);
			break;
		case "3D":
			message = this.ctldelMember(rowData);
			break;
		}
		return message;
	}
	
	private String ctlModMember(String memberInfo) {
		dao=new DataAccessObject();
		ArrayList<MemberBean> list=dao.getMemberList();
		//System.out.println(memberInfo);
		String[] rowInfo= memberInfo.split("&");
		for(int recordIndex=0; recordIndex<list.size(); recordIndex++) {
			if(rowInfo[0].equals(list.get(recordIndex).getMemberCode())) {
				list.get(recordIndex).setMemberCode(rowInfo[0]);
				list.get(recordIndex).setCallNumber(rowInfo[1]);
				break;
			}

		}
		return (dao.setMember(list))? this.toStringFromArray(dao.getMemberList()):"메뉴수정에 실패하였습니다. 다시 시도해 주세요";

	}
	private String ctldelMember(String memberInfo) {
		dao=new DataAccessObject();
		ArrayList<MemberBean> list=null;

		boolean check=true;
		list=dao.getMemberList();

		for(int i=0;i<list.size(); i++) {
			if(list.get(i).getMemberCode().equals(memberInfo)) {
				list.remove(i);
			}else {check=false;}
		}
		return (dao.setMember(list))?this.toStringFromArray(dao.getMemberList()):"회원 삭제에 실패하였습니다.";
	}


	private String ctlReadMember() {
		dao = new DataAccessObject();
		return this.toStringFromArray(dao.getMemberList());
	}

	private String toStringFromArray(ArrayList<MemberBean> memberList) {
		StringBuffer sb = new StringBuffer();

		for(int recordIndex=0; recordIndex<memberList.size(); recordIndex++) {
			//sb.append(" ");
			sb.append(memberList.get(recordIndex).getMemberCode());
			sb.append("\t");
			sb.append(memberList.get(recordIndex).getMemberName());
			sb.append("\t");
			sb.append(memberList.get(recordIndex).getCallNumber());
			sb.append("\n");
		}
		return sb.toString();
	}



	/* 회원정보등록 */
	private String ctlRegMember(String memberInfo) {
		
		String[] rowInfo= memberInfo.split("&");
		System.out.println(rowInfo[2]);
		String message = null;
		dao = new DataAccessObject();
		MemberBean members=new MemberBean();
		members.setMemberCode(rowInfo[0]);
		members.setMemberName(rowInfo[1]);
		members.setCallNumber(rowInfo[2]);
		if(dao.setMember(members)) {
			message = this.toStringFromArray(dao.getMemberList());
		}else {
			message = "회원등록작업이 실패하였습니다.\n다시 등록해 주시기 바랍니다.";
		}

		return message;
	}

	/* 회원정보수정 */
	private String ctlModMember() {

		dao = new DataAccessObject();

		return this.toStringFromArray(dao.getMemberList());
	}

	/* 회원정보삭제 */
	private String ctlDegMember() {

		dao = new DataAccessObject();
		
		return this.toStringFromArray(dao.getMemberList());
	}
}
