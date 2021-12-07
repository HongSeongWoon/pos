package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.GoodsBean;
import beans.MembersBean;
import beans.StoreBean;

public class OracleDAO {
	Connection connection;
	Statement statement;
	PreparedStatement psmt;
	ResultSet rs;
	OracleDAO(){
		connection = null;
		statement = null;
		psmt = null;
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
		String query = "SELECT COUNT(*) AS CNT FROM SE WHERE SE_CODE = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, store.getSeCode());
			rs = psmt.executeQuery();
			while(rs.next()) {
				result = this.convertToBool(rs.getInt("CNT"));//오라클은 String으로 보내는데 이걸int 로 바꿈
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}//rs.close();이거안하면 오라클이 멈춤
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
		}


		return result;
	}
	/*직원 코드 유효성 체크*/
	boolean isEmployee(StoreBean store) {
		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM EM WHERE EM_CODE = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, store.getEmCode());
			rs = psmt.executeQuery();//executeQuery 는 쿼리 날릴때 
			//executeStatement 는 스테이트먼트 날릴때
			//select날릴때는 rs.next로 
			while(rs.next()) {
				result = this.convertToBool(rs.getInt("CNT"));//오라클은 String으로 보내는데 이걸int 로 바꿈
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}//rs.close();이거안하면 오라클이 멈춤
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
		}


		return result;
	}
	/*매장 오픈 및 클로징 기록*/
	boolean storeManagement(StoreBean store) {
		//System.out.println(store.getSeState());
		boolean result = false;
		String dml = "INSERT INTO SH(SH_CODE, SH_SECODE, SH_EMCODE, SH_STATES)"
				+ "VALUES(DEFAULT, ?, ?, ?)";
		try {
			psmt = connection.prepareStatement(dml);
			psmt.setNString(1, store.getSeCode());
			psmt.setNString(2, store.getEmCode());
			psmt.setInt(3, store.getSeState());;
			result = this.convertToBool(psmt.executeUpdate());//executeQuery 는 쿼리 날릴때 
			//executeStatement 는 스테이트먼트 날릴때
			//select날릴때는 rs.next로 
			
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}//statement.close();이거안하면 오라클이 멈춤
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
				+ "AND SH_SECODE = ? AND SH_STATES = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, store.getSeCode());
			psmt.setInt(2, store.getSeState());			
			rs = psmt.executeQuery();
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
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}


		return storeBean;
	}
	/*기존 상품코드와의 중복 확인 */
	boolean isGoodsInfo(GoodsBean goods) {
		int cnt = 0;
		String query = "SELECT COUNT(*) FROM PR WHERE PR_CODE = ? OR PR_NAME = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, goods.getMenuCode());
			psmt.setNString(2, goods.getMenuName());
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {//자료가 넘어오면 진행하라
				 cnt = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		
		return this.convertToBool(cnt);
	}
	/*기존 상품명과의 중복 확인*/
	boolean isGoodsStates(GoodsBean goods) {
		int cnt = 0;
		String query = "SELECT COUNT(*) AS CNT FROM ST WHERE STAT_CODE = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setInt(1, goods.getMenuState());
						
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				 cnt = rs.getInt("CNT");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		
		return this.convertToBool(cnt);
	}
	/*상품분류코드 유효성 확인*/
	boolean isGoodsCategories(GoodsBean goods) {
		int cnt = 0;
		String query = "SELECT COUNT(*) AS CNT FROM CA WHERE CAT_CODE = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, goods.getMenuCategory());
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				 cnt = rs.getInt("CNT");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		
		return this.convertToBool(cnt);
	}
	/*상품등록 */
	boolean regGoods(GoodsBean goods) {
		int record = 0;
		String query = "INSERT INTO PR(PR_CODE, PR_NAME, PR_COST, "
				+ "PR_PRICE, PR_STOCKS, PR_DISCOUNT, PR_CATCODE, PR_STATCODE) "
				+ "     VALUES(?,?,?,?,?,?,?,?)";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, goods.getMenuCode() );
			psmt.setNString(2, goods.getMenuName());
			psmt.setInt(3, goods.getMenuCost());
			psmt.setInt(4, goods.getMenuPrice());
			psmt.setInt(5, goods.getMenuStocks());
			psmt.setInt(6, goods.getDiscountRate());
			psmt.setNString(7, goods.getMenuCategory());
			psmt.setInt(8, goods.getMenuState());
			
			
			record = psmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		
		return this.convertToBool(record);
	}
	/*등록된 상품 정보 확인*/
	ArrayList<GoodsBean> getGoodsInfo(GoodsBean goods) {
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
 		String query = "SELECT PR_CODE AS GCODE, PR_NAME AS GNAME, "
 				+ "PR_COST AS GCOST, PR_PRICE AS GPRICE, PR_STOCKS AS GSTOCKS, "
 				+ "PR_DISCOUNT AS GDISCOUNT, PR_CATCODE AS GCACODE, CA.CAT_NAME AS CANAME, "
 				+ "PR_STATCODE AS GSTCODE, ST.STAT_NAME AS SNAME "
 				+ "FROM PR INNER JOIN ST ON PR.PR_STATCODE = ST.STAT_CODE "
 				+ "		   INNER JOIN CA ON PR.PR_CATCODE = CA.CAT_CODE "
 				+ " WHERE PR_CODE = ?";
 		
 		
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, goods.getMenuCode());
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				GoodsBean gb = new GoodsBean();
				 gb.setMenuCode(rs.getNString("GCODE"));
				 gb.setMenuName(rs.getNString("GNAME"));
				 gb.setMenuCost(rs.getInt("GCOST"));
				 gb.setMenuPrice(rs.getInt("GPRICE"));
				 gb.setMenuStocks(rs.getInt("GSTOCKS"));
				 gb.setDiscountRate(rs.getInt("GDISCOUNT"));
				 gb.setMenuCategory(rs.getNString("GCACODE"));
				 gb.setCategoryName(rs.getNString("CANAME"));
				 gb.setMenuState(rs.getInt("GSTCODE"));
				 gb.setStateName(rs.getNString("SNAME"));
				 goodsList.add(gb);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		return goodsList;
	}
	
	/* 회원코드 유효성 검사 */
	boolean isMembersInfo(MembersBean member) {
		int cnt = 0;
		String query = "SELECT COUNT(*) FROM CU WHERE CU_CODE = ? AND CU_PHONE = ? ";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, member.getMemberCode());
			psmt.setInt(2, member.getMemberPhone());

			rs = psmt.executeQuery();

			while(rs.next()) {
				cnt = rs.getInt(1);
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}

		return this.convertToBool(cnt);
	}

	/* 등급코드 유효성 검사 */
	boolean isMembersRank(MembersBean member) {
		int cnt = 0;
		String query = "SELECT COUNT(*) FROM RA WHERE RA_CODE = ?";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, member.getMemberRank());

			rs = psmt.executeQuery();

			while(rs.next()) {
				cnt = rs.getInt(1);
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}

		return this.convertToBool(cnt);
	}

	

	/* 회원 등록 */
	boolean regMembers(MembersBean member) {
		int record = 0;
		String query = "INSERT INTO CU(CU_CODE, CU_NAME, CU_PHONE, CU_RACODE)"
				+ " VALUES(?,?,?,?)";
		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, member.getMemberCode());
			psmt.setNString(2, member.getMemberName());
			psmt.setInt(3, member.getMemberPhone());
			psmt.setNString(4, member.getMemberRank());



			record = psmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}

		return this.convertToBool(record);
	}

	/*멤버 리스트 확인*/
	ArrayList<MembersBean> getMembersInfo(MembersBean member) {
		ArrayList<MembersBean> MembersList = new ArrayList<MembersBean>();
		String query = "SELECT CU_CODE AS CUCODE, CU_NAME AS CUNAME, CU_PHONE AS CUPHONE, CU_RACODE AS CURACODE"
				+ " FROM CU WHERE CU_CODE = ?";

		try {
			psmt = connection.prepareStatement(query);
			psmt.setNString(1, member.getMemberCode());

			rs = psmt.executeQuery();

			while(rs.next()) {
				MembersBean mm = new MembersBean();
				mm.setMemberCode(rs.getNString("CUCODE"));
				mm.setMemberName(rs.getNString("CUNAME"));
				mm.setMemberPhone(rs.getInt("CUPHONE"));
				mm.setMemberRank(rs.getNString("CURACODE"));
				MembersList.add(mm);
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {if(!rs.isClosed()) {rs.close();}} catch (SQLException e) {e.printStackTrace();}
			try {if(!psmt.isClosed()) {psmt.close();}} catch(SQLException e) {e.printStackTrace();}
		}
		return MembersList;
	}
	
	
	
	/*1 OR 0의 리턴값을 TRUE OR FALSE로 CONVERT*/
	boolean convertToBool(int value) {
		return (value >= 1)? false: true;
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
/* 메뉴등록
 * VIEW______
 *  상품코드(pk), 상품명(uk), 상품가격, 상품상태(fk), 상품분류(fk), 할인율 
 *  +++++> 구매가, 재고
 * Bean______
 *  +++++> 구매가, 재고
 * MenuManagement
 *   goodsBean +++> set구매가, set재고
 *   
 * Process____
 *  0. CONNECTION 시작
 *  0. TRANSACTION AUTO COMMIT > FALSE
 *  
 *  1. 기존 상품코드와의 중복 확인  :: R:0 >>> P2
 *     TABLE : GOODS : SELECT
 *  2. 기존 상품명과의 중복 확인  :: R:0  >>> P3
 *     TABLE : GOODS : SELECT
 *  3. 상품상태코드 유효성 확인  :: R:1 >> P4
 *     TABLE : STATES  : SELECT
 *  4. 상품분류코드 유효성 확인  :: R:1 >> P5
 *     TABLE : CATEGORIES : SELECT
 *  5. 상품등록   :: R:1  >> P6
 *     TABLE : GOODS : INSERT
 *  6. 등록된 상품 정보 확인 :: GoodsBean :: SET    
 *  
 *  0. TRANSACTION AUTO COMMIT > TRUE
 *  0. CONNECTION 종료
 * */
/* 회원 등록
 * VIEW______
 *  회원코드(CU_CODE)(pk), 회원명(CU_NAME), 전화번호(CU_PHONE)(uk), 회원등급(CU_RACODE)(fk)
 *  Bean______
 *  * MemberManagement
 *   MembersBean    
 * Process____
 *  0. CONNECTION 시작
 *  0. TRANSACTION AUTO COMMIT > FALSE
 *  
 *  1. 기존 회원코드와의 중복 확인  :: R:0 >>> P2
 *     TABLE : CUSTOMER : SELECT
 *  2. 기존 전화번호와의 중복 확인  :: R:0  >>> P3
 *     TABLE : CUSTOMER : SELECT
 *  3. 회원등급코드 유효성 확인  :: R:1 >> P4
 *     TABLE : RANKS  : SELECT
 *  4. 회원등록   :: R:1  >> P6
 *     TABLE : CUSTOMER : INSERT
 *  5. 등록된 회원 정보 확인 :: MembersBean :: SET    
 *  
 *  0. TRANSACTION AUTO COMMIT > TRUE
 *  0. CONNECTION 종료
 * */