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

public class DataAccessObject {

	public DataAccessObject() {

	}

	String[][] getSalesStat(String[] month){
		String[][] salesMonthStat = new String[this.countRecord("C:\\posbyhong\\src\\datafile\\orders.txt", month, 0)][];
		
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\orders.txt")));
			String line;
			int index = -1;
			while((line=buffer.readLine()) != null) {
				if(Integer.parseInt(month[0])<=Integer.parseInt(line.substring(0,8))&&Integer.parseInt(line.substring(0,8))<=Integer.parseInt(month[1])) {
					index++;
					salesMonthStat[index] = line.split(",");
					//System.out.println(salesMonthStat[0][0]);

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
	
String[][] getSalesMonthStat(String month){
	String[][] salesMonthStat = new String[this.countRecord("C:\\posbyhong\\src\\datafile\\orders.txt", month, 0)][];
	
	BufferedReader buffer = null;
	try {
		buffer = new BufferedReader(new FileReader(new File("C:\\posbyhong\\src\\datafile\\orders.txt")));
		String line;
		int index = -1;
		while((line=buffer.readLine()) != null) {
			if(line.contains(month)) {
				index++;
				salesMonthStat[index] = line.split(",");
				//System.out.println(salesMonthStat[0][0]);

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
			sb.append(list.get(i).getOrderQuantity());
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

private int countRecord(String path) {
	int count = 0;
	File file = new File(path);
	try {
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		//1001|아메리카노|2000|1|HOT|10
		while((buffer.readLine()) != null) {
			count++;
		}
		buffer.close();
	}catch(IOException e) {

	}
	return count;
}
/*조건에 만족하는 레코드 개수 구하기*/
private int countRecord(String path, String condition, int colIndex) {
	int count = 0;
	String line=null;
	File file = new File(path);
	try {
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		//202109060918,1005,카푸치노(HOT),2700,8,0,1002  contains 202111
		while((line=buffer.readLine()) != null) {
			String[] record = line.split(",");
			if(record[colIndex].contains(condition)) {
				count++;
			}
		}
		buffer.close();
	}catch(IOException e) {

	}
	return count;
}
/*조건에 만족하는 레코드 개수 구하기*/
private int countRecord(String path, String[] condition, int colIndex) {
	int count = 0;
	String line=null;
	File file = new File(path);
	try {
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		//202109060918,1005,카푸치노(HOT),2700,8,0,1002  contains 202111
		while((line=buffer.readLine()) != null) {
			String[] record = line.split(",");
			if(Integer.parseInt(condition[0])<=Integer.parseInt(line.substring(0,8))&&Integer.parseInt(line.substring(0,8))<=Integer.parseInt(condition[1])) {
				count++;
			}
		}
		buffer.close();
	}catch(IOException e) {

	}System.out.println(count);
	return count;
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

public String[][] getMemberList(){
	/* Service Class 요청에 따른 회원 리스트를 이차원 배열로 작성 후 리턴 */
	String[][] memberList = new String[this.countRecord("C:\\posbyhong\\src\\datafile\\members.txt")][];
	File file = new File("C:\\posbyhong\\src\\datafile\\members.txt");
	FileReader reader = null;
	BufferedReader buffer = null;
	String line;
	int recordIndex = -1;
	try {
		reader = new FileReader(file);
		buffer = new BufferedReader(reader);
		while((line = buffer.readLine()) != null) {
			recordIndex++;
			String[] member = line.split("\\|");
			memberList[recordIndex] = member;
		}
	} catch (Exception e) {

	} finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return memberList;
}

public boolean setMember(String[][] memberInfo){
	boolean check = false;
	File file = new File("C:\\posbyhong\\src\\datafile\\members.txt");
	FileWriter writer = null;
	BufferedWriter buffer = null;
	StringBuffer line = new StringBuffer();

	try {
		writer = new FileWriter(file);
		buffer = new BufferedWriter(writer);

		for(int recordIndex=0; recordIndex<memberInfo.length; recordIndex++) {
			for(int colIndex=0; colIndex<memberInfo[0].length; colIndex++) {
				line.append(memberInfo[recordIndex][colIndex]);
				line.append((colIndex != memberInfo[recordIndex].length-1)? "|":"\n");
			}	
		}		
		buffer.write(line.toString());
		check = true;
	}catch(IOException e) {

	} 
	finally {
		try {buffer.close();} catch (IOException e) {}
	}

	return check;
}

public boolean setMember(String[] memberInfo){
	boolean check = false;
	File file = new File("C:\\posbyhong\\src\\datafile\\members.txt");
	FileWriter writer = null;
	BufferedWriter buffer = null;

	try {
		writer = new FileWriter(file, true);
		buffer = new BufferedWriter(writer);

		for(int colIndex=0; colIndex<memberInfo.length; colIndex++) {
			buffer.write(memberInfo[colIndex]);
			if(colIndex != memberInfo.length-1) {
				buffer.write("|");
			}
		}
		buffer.newLine();
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
