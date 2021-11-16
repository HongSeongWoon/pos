package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataAccessObject {

	public DataAccessObject() {

	}

	String[][] getSalesStat(String[] month){
		String[][] salesMonthStat = new String[this.countRecord("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\orders.txt", month, 0)][];
		
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\orders.txt")));
			String line;
			int index = -1;
			while((line=buffer.readLine()) != null) {
				if(line.contains(month[0])||line.contains(month[1])) {
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
	String[][] salesMonthStat = new String[this.countRecord("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\orders.txt", month, 0)][];
	
	BufferedReader buffer = null;
	try {
		buffer = new BufferedReader(new FileReader(new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\orders.txt")));
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

boolean setOrders(String[] orders) {
	boolean response = false;
	BufferedWriter buffer=null;
	File file = 
			new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\orders.txt");
	try {
		FileWriter writer = new FileWriter(file, true);
		buffer = new BufferedWriter(writer);
		for(int i=0; i<orders.length;i++) {
			buffer.write(orders[i]);
			buffer.newLine();

		}


		response = true;
	} catch (IOException e) {

	}
	finally {try {
		buffer.close();
	} catch (IOException e) {}
	}
	return response;
}

public String[] getGoodsInfo(String goodsCode) {
	String[] goodsInfo = null;
	BufferedReader buffer=null;
	try {
		buffer = new BufferedReader(new FileReader(new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\goodsInfo.txt")));
		String line;
		while((line=buffer.readLine())!=null) {
			goodsInfo = line.split("\\|");
			if(goodsCode.equals(goodsInfo[0])){


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
			new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\storestate.txt");
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
			if(record[colIndex].contains(condition[0])||record[colIndex].contains(condition[1])) {
				count++;
			}
		}
		buffer.close();
	}catch(IOException e) {

	}System.out.println(count);
	return count;
}

boolean setMenu(String[] data) {
	boolean check = false;
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\goodsinfo.txt");
	FileWriter writer = null;
	BufferedWriter buffer = null;

	try {
		writer = new FileWriter(file, true);
		buffer = new BufferedWriter(writer);

		for(int colIndex=0; colIndex<data.length; colIndex++) {
			buffer.write(data[colIndex]);
			if(colIndex != data.length-1) {
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

boolean setMenu(String[][] data) {
	boolean check = false;
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\goodsinfo.txt");
	FileWriter writer = null;
	BufferedWriter buffer = null;
	StringBuffer line = new StringBuffer();

	try {
		writer = new FileWriter(file);
		buffer = new BufferedWriter(writer);

		for(int recordIndex=0; recordIndex<data.length; recordIndex++) {
			for(int colIndex=0; colIndex<data[0].length; colIndex++) {
				line.append(data[recordIndex][colIndex]);
				line.append((colIndex != data[recordIndex].length-1)? "|" : "\n");	
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
String[][] getMenu(){
	String[][] menuList = new String[this.countRecord("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\goodsinfo.txt")][];
	String menu = null;
	String[] menuInfo = null;

	int index = -1;
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\goodsinfo.txt");
	try {
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		//1001|아메리카노|2000|1|HOT|10
		while((menu = buffer.readLine()) != null) {
			index++;
			menuInfo = menu.split("\\|");
			menuList[index] = menuInfo;
		}
		buffer.close();
	}catch (IOException e) {
		menuList = null;	
	}

	return menuList;
}

public String[][] getMemberList(){
	/* Service Class 요청에 따른 회원 리스트를 이차원 배열로 작성 후 리턴 */
	String[][] memberList = new String[this.countRecord("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\members.txt")][];
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\members.txt");
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
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\members.txt");
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
	File file = new File("C:\\Users\\홍성운\\OneDrive\\바탕 화면\\Coding\\211110\\pos by prof\\src\\datafile\\members.txt");
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
