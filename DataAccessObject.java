package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import beans.GoodsBean;

import beans.OrderBean;
import beans.StoreBean;
import views.MemberBean;

public class DataAccessObject {

	public DataAccessObject() {

	}

	ArrayList<StoreBean> getSalesStat(String[] month){
		ArrayList<StoreBean> salesMonthStat = new ArrayList();
		StoreBean sBean;
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\orders.txt")));
			String line;
			
			while((line=buffer.readLine()) != null) {
				if(Integer.parseInt(month[0])<=Integer.parseInt(line.substring(0,8))&&Integer.parseInt(line.substring(0,8))<=Integer.parseInt(month[1])) {
					
					sBean=new StoreBean();
					String[] menuInfo = line.split(",");
					sBean.setStoreDate(menuInfo[0]);
					sBean.setStoreMenuCode(menuInfo[1]);
					sBean.setStoreMenuName(menuInfo[2]);
					sBean.setStoreMenuPrice(menuInfo[3]);
					sBean.setStoreCountNumber(menuInfo[4]);
					sBean.setStoreDiscountRate(menuInfo[5]);
					//sBean.setStoreMemberCode(!menuInfo[6].equals(null)? menuInfo[6]:" ");
					salesMonthStat.add(sBean);

				}
			}
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(IOException e2){
			e2.printStackTrace();
		}
		finally {
			try {buffer.close();} catch (IOException e) {}
		}
				
		return salesMonthStat;
	}
	
ArrayList<StoreBean> getSalesMonthStat(String month){
	ArrayList<StoreBean> salesMonthStat=new ArrayList<StoreBean>();
	StoreBean sBean;
	BufferedReader buffer = null;
	try {
				
		buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\orders.txt")));
		String line;
		
		while((line=buffer.readLine()) != null) {
			if(line.contains(month)) {
			sBean=new StoreBean();
			String[] menuInfo = line.split(",");
			sBean.setStoreDate(menuInfo[0]);
			sBean.setStoreMenuCode(menuInfo[1]);
			sBean.setStoreMenuName(menuInfo[2]);
			sBean.setStoreMenuPrice(menuInfo[3]);
			sBean.setStoreCountNumber(menuInfo[4]);
			sBean.setStoreDiscountRate(menuInfo[5]);
			//sBean.setStoreMemberCode(!menuInfo[6].equals(null)? menuInfo[6]:" ");
			salesMonthStat.add(sBean);
			}
		}
		
	}catch(FileNotFoundException e1) {
		e1.printStackTrace();
	}catch(IOException e2){
		e2.printStackTrace();
	}
	finally {
		try {buffer.close();} catch (IOException e) {}
	}
			
	return salesMonthStat;
}

boolean setOrders(ArrayList<OrderBean> list) {
	boolean response = false;
	BufferedWriter buffer=null;
	StringBuffer sb = new StringBuffer();
	File file = 
			new File("C:\\posbyhong\\src\\datafile\\orders.txt");
	try {
		FileWriter writer = new FileWriter(file, true);
		buffer = new BufferedWriter(writer);
		for(int i=0; i<list.size();i++) {
			sb.append(list.get(i).getOrderCode()+",");
			sb.append(list.get(i).getGoodsCode()+",");
			sb.append(list.get(i).getGoodsName()+",");
			sb.append(list.get(i).getGoodsPrice()+",");
			sb.append(list.get(i).getOrderQuantity()+",");
			sb.append(list.get(i).getDiscountRate());
			if(list.get(i).getMemberCode() != null) {
				sb.append(","+list.get(i).getMemberCode());
			}
			sb.append("\n");

		}
		buffer.write(sb.toString());
		

		response = true;
	} catch (IOException e) {

	}
	finally {try {
		buffer.close();
	} catch (IOException e) {}
	}
	return response;
}
/*1001 아메리카노 2500   0%*/
OrderBean getGoodsInfo(OrderBean ob) {
	
	OrderBean goodsInfo = new OrderBean();
	BufferedReader buffer = null;
	try {
		buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\goodsInfo.txt")));
		String line;
		while((line=buffer.readLine())!=null) {
			
			String[] record = line.split("\\|");
			if(ob.getGoodsCode().equals(record[0])){
				goodsInfo.setGoodsCode(record[0]);
				goodsInfo.setGoodsName(record[1]);
				goodsInfo.setGoodsPrice(Integer.parseInt(record[2]));
				goodsInfo.setOrderQuantity(0);
				goodsInfo.setDiscountRate(Integer.parseInt(record[5]));

				break;
			}
		}
	}catch(FileNotFoundException e1) {
		e1.printStackTrace();
	}catch(IOException e2) {
		e2.printStackTrace();
	}finally {try {buffer.close();} catch (IOException e) {}
	}		
	return goodsInfo;
}

boolean setStoreState(String date, String time, int state) {
	boolean response = false;
	File file = 
			new File("C:\\posbyhong\\src\\datafile\\storestate.txt");
	try {
		FileWriter writer = new FileWriter(file, true);
		BufferedWriter buffer = new BufferedWriter(writer);

		buffer.write(date + "|" + time + "|" + state);
		buffer.newLine();
		buffer.close();
		response = true;
	} catch (IOException e) {

	}
	return response;
}




boolean setMenu(GoodsBean goods) {
	boolean check = false;
	BufferedWriter buffer = null;
	StringBuffer sb = new StringBuffer();
	sb.append(goods.getMenuCode() + "|");
	sb.append(goods.getMenuName()+"|");
	sb.append(goods.getMenuPrice()+"|");
	sb.append(goods.getMenuState()+"|");
	sb.append(goods.getMenuCategory()+"|");
	sb.append(goods.getDiscountRate());

	try {
		
		buffer = new BufferedWriter(new FileWriter(new File("C:\\posbyhong\\src\\datafile\\goodsinfo.txt"),true));
		buffer.write(sb.toString());
		buffer.newLine();
		check = true;
		
	}catch(IOException e) {

	} 
	finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return check;
}

boolean setMenu(ArrayList<GoodsBean> list) {
	boolean check = false;
	StringBuffer line = new StringBuffer();
	BufferedWriter buffer = null;
	
	for(int recordIndex=0; recordIndex<list.size(); recordIndex++) {
		line.append(list.get(recordIndex).getMenuCode()+"|");
		line.append(list.get(recordIndex).getMenuName()+"|");
		line.append(list.get(recordIndex).getMenuPrice()+"|");
		line.append(list.get(recordIndex).getMenuState()+"|");
		line.append(list.get(recordIndex).getMenuCategory()+"|");
		line.append(list.get(recordIndex).getDiscountRate()+"\n");
	}
	try {
		
		buffer = new BufferedWriter(new FileWriter(new File("C:\\posbyhong\\src\\datafile\\goodsinfo.txt")));
		buffer.write(line.toString());
		check = true;
	}catch(IOException e) {//어떠한 입출력 예외의 발생을 통지하는 시그널을 발생시킵니다. 이 클래스는 입출력 처리의 실패, 또는 인터럽트의 발생에 의해 작성되는 예외의 일반적인 클래스입니다.

	} 
	finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return check;
}
ArrayList<GoodsBean> getMenu(){
	ArrayList<GoodsBean> menuList=new ArrayList<GoodsBean>();
	GoodsBean goods;
	String line;
	String[] menuInfo;
	BufferedReader buffer=null;
	
	
	try {
		
		buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\goodsinfo.txt")));
		
		//1001|아메리카노|2000|1|HOT|10
		while((line = buffer.readLine()) != null) {
			goods=new GoodsBean();
			menuInfo = line.split("\\|");
			//System.out.println(menuInfo[0]);
			goods.setMenuCode(menuInfo[0]);
			goods.setMenuName(menuInfo[1]);
			goods.setMenuPrice(Integer.parseInt(menuInfo[2]));
			goods.setMenuState(menuInfo[3].charAt(0));
			goods.setMenuCategory(menuInfo[4]);
			goods.setDiscountRate(Integer.parseInt(menuInfo[5]));
			menuList.add(goods);
		}
		buffer.close();
	}catch(IOException e) {
		menuList=null;
	}finally {try {buffer.close();} catch (IOException e) {}
	}	
	return menuList;
}

ArrayList<MemberBean> getMemberList(){
	/* Service Class 요청에 따른 회원 리스트를 이차원 배열로 작성 후 리턴 */
	ArrayList<MemberBean> memberList = new ArrayList<MemberBean>();
	MemberBean members;
	String[] memberInfo;

	BufferedReader buffer = null;
	String line;
	
	try {
		buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\members.txt")));
		while((line = buffer.readLine()) != null) {
			members = new MemberBean();
			memberInfo = line.split("\\|");
			members.setMemberCode(memberInfo[0]);
			members.setMemberName(memberInfo[1]);
			members.setCallNumber(memberInfo[2]);
			memberList.add(members);
		}
	} catch (Exception e) {

	} finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return memberList;
}

public boolean setMember(ArrayList<MemberBean> memberInfo){
	boolean check = false;
	StringBuffer line = new StringBuffer();
	BufferedWriter buffer = null;
	
	for(int recordIndex=0; recordIndex<memberInfo.size(); recordIndex++) {
		line.append(memberInfo.get(recordIndex).getMemberCode()+ "|");
		line.append(memberInfo.get(recordIndex).getMemberName()+ "|");
		line.append(memberInfo.get(recordIndex).getCallNumber()+ "\n");

	}
	
	try {
		buffer = new BufferedWriter(new FileWriter(new File("C:\\posbyhong\\src\\datafile\\members.txt")));
		buffer.write(line.toString());
		check = true;
	}catch(IOException e) {

	} 
	finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return check;
}

public boolean setMember(MemberBean member){
	boolean check = false;
	
	FileWriter writer = null;
	BufferedWriter buffer = null;
	StringBuffer sb = new StringBuffer();
	sb.append(member.getMemberCode()+"|");
	sb.append(member.getMemberName()+"|");
	sb.append(member.getCallNumber()+"\n");
	try {

		buffer = new BufferedWriter(new FileWriter(new File("C:\\posbyhong\\src\\datafile\\members.txt"),true));
		
		buffer.write(sb.toString());
		
		check = true;
		
			
	}catch(IOException e) {

	} 
	finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return check;
}
}

/* File >> 사용할 파일의 경로와 이름 지정
 * FileReader, FileWriter
 * BufferedReader, BufferedWriter
 * */
