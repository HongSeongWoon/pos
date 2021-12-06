package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.StoreBean;

public class OracleDAO {
	Connection connection;
	Statement statement;
	ResultSet rs;
	OracleDAO(){
		connection = null;
		statement = null;
		rs = null;
		driverLoading();
	}

	void driverLoading() {
		System.out.println("OracleDataBase 접속시도중");
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.181:1521:xe", "DBA1JO", "1234");
			System.out.println("OracleDataBase 연결 성공 ");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("OracleDataBase 연결 실패 ");
		} 
	}

	/*Transaction 수동 시작 자동 commit안되도록*/
	void startTransaction(boolean tran) {
		try {
			connection.setAutoCommit(tran);
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

	/*Transaction 종료*/
	void endTransaction(boolean endType) {
		try {
			if(endType) {connection.commit();}
			else {connection.rollback();}
			connection.setAutoCommit(true);
			connection.close();//이거안하면 오라클이 멈춤
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}
	/*매장코드 유효성 체크*/
	boolean isStore(StoreBean store) {

		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM SE WHERE SE_CODE = '"
				+ store.getSeCode() + "'";
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);//executeQuery 는 쿼리 날릴때 
			//executeStatement 는 스테이트먼트 날릴때
			//select날릴때는 rs.next로 
			while(rs.next()) {
				result = this.convertToBool(rs.getInt("CNT"));//오라클은 String으로 보내는데 이걸int 로 바꿈
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}//rs.close();이거안하면 오라클이 멈춤
			try {if(!statement.isClosed()) {statement.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
		}


		return result;
	}
	/*직원 코드 유효성 체크*/
	boolean isEmployee(StoreBean store) {
		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM EM WHERE EM_CODE = '"
				+ store.getSeCode() + "'";
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);//executeQuery 는 쿼리 날릴때 
			//executeStatement 는 스테이트먼트 날릴때
			//select날릴때는 rs.next로 
			while(rs.next()) {
				result = this.convertToBool(rs.getInt("CNT"));//오라클은 String으로 보내는데 이걸int 로 바꿈
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}//rs.close();이거안하면 오라클이 멈춤
			try {if(!statement.isClosed()) {statement.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
		}


		return result;
	}
	/*매장 오픈 및 클로징 기록*/
	boolean storeManagement(StoreBean store) {
		System.out.println(store.getSeState());
		boolean result = false;
		String dml = "INSERT INTO SH(SH_CODE, SH_SECODE, SH_EMCODE, SH_STATES)"
				+ "VALUES(DEFAULT, '" + store.getSeCode() + "', '" + store.getEmCode()
				+"', "	+ store.getSeState() + ")";
		try {
			statement = connection.createStatement();
			result = this.convertToBool(statement.executeUpdate(dml));//executeQuery 는 쿼리 날릴때 
			//executeStatement 는 스테이트먼트 날릴때
			//select날릴때는 rs.next로 
			
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!statement.isClosed()) {statement.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
		}


		return result;
	}
	/*매장 오픈 정보 검색*/
	StoreBean getStoreInfo(StoreBean store) {
		StoreBean storeBean = null;
		boolean result = false;
		String query = "SELECT SH_CODE AS SHCODE, "
				+ "SH_SECODE AS SECODE, SH_EMCODE AS EMCODE FROM SH "
				+ "WHERE SUBSTR(SH_CODE, 1, 8) = TO_CHAR(SYSDATE, 'YYYYMMDD') "
				+ "AND SH_SECODE = " + store.getSeCode()
				+" AND SH_STATES = "+store.getSeState();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			//select날릴때는 rs.next로 
			while(rs.next()) {
				storeBean = new StoreBean();
				storeBean.setSeDate(rs.getNString("SHCODE"));
				storeBean.setSeCode(rs.getNString("SECODE"));
				storeBean.setEmCode(rs.getNString("EMCODE"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!statement.isClosed()) {statement.close();}} catch(SQLException e) {e.printStackTrace();}
		}


		return storeBean;
	}
	/*1 OR 0의 리턴값을 TRUE OR FALSE로 CONVERT*/
	boolean convertToBool(int value) {
		return (value != 1)? false: true;
	}
}


/* JDBC - OJDBC
 * 1. 해당 데이터베이스의 Driver를 LOADING
 * 2. CONNECTION 개체 생성 == 데이터베이스와의 연결(TRANSACTION 시작)
 * 3. STATEMENT            >> 매장오픈
 * 	  PREPAREDSTATEMENT    >> 매장클로징
 * 4. DML >> INT
 *    QUERY  >> RESULTSET
 *    
 * JOB : 매장오픈
 *   CLIENT-DATA: 매장코드, 직원코드
 *   PROCESS 
 *     0. CONNECTION 생성
 *     1. STORES :: SELECT  << 매장코드 >> R:1  >> P2
 *     	  DAO :: ISSTORE
 *     2. EMPLOYEES :: SELECT << 직원코드  >> R:1 >> P3
 *        DAO : ISEMPLOYEE
 *     3. STOREHISTORY :: SELECT << 오픈일, 매장코드 >> R : 0  
 *     4. STOREHISTORY :: INSERT << 오픈일시, 매장코드, 직원코드, 9 >> R:1
 *        DAO : STOREMANAGEMENT
 *     5. STOREHISTORY :: SELECT << 매장코드, 오픈일 >> R:오픈일시
 *        DAO : GETSTOREINFO
 *     6. TRANSACTION END
 * 
 * */
