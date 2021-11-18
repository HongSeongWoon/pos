package views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import controllers.FrontController;

public class View {
	FrontController fc;
	//	private String message2="";
	public View() {
		fc=new FrontController();
		this.ctlView();
	}
	/* 데이터의 흐름 제어 */
	private void ctlView() {

		String title, message = "", request = "",sum="";
		int menuCode=-1, subCode=0;
		String[][] menu = saveMenu();
		title = this.makeTitle();
		String[] userData= null;

		while(true) {

			if(request.equals("")) {
				if(menuCode!=4) {
					menuCode = this.ctlMain(title, message, menu);
					message = "";// 얘가없으면 2->0->4 입력시 요쳥이 취소되었습니다. 메세지가 살아있다.
				}
				else if(menuCode == 0) {
					break;
				}
				else {
					message = (menuCode==4)? message:"";//이 삼항연산자를 사용하여 "주문이 완료되었습니다."라는 메세지를 띄웠다.
				}


				if(menuCode !=4) {
					subCode = this.ctlSub(title, message, menu[menuCode-1]);
					if(subCode !=0) {
						request = menuCode + "" + subCode;
						message = "";
						/*joCode 13 인 경우*/
						if(request.equals("13")) {
							String[] userDate={this.getDate("yyyyMM")};
							request+="?"+userDate;
							/*서버 서비스 요청*/
							message=fc.getRequest(request);

						}else if(request.equals("14")){
							this.display("시작일입력(yyyyMMdd) : ");
							String m1 = this.menuInput();
							this.display("마감일입력(yyyyMMdd) : ");
							String m2 = this.menuInput();
							String[] userDate={m1,m2};
							request+="?"+m1+"&"+m2;
							message=fc.getRequest(request);

						}
						else {

							/* 서버 서비스 요청 */

							message = fc.getRequest(request);
						}
					}else {
						message = "요청이 취소되었습니다.";
					}

				}else {
					String[] finalOrder = null;
					String[][] orders = new String[100][];
					boolean posDispCheck =true;
					String pos;
					int recordIndex = -1;
					while(true) {
						recordIndex++;
						//판매화면이동

						pos = this.posDisplay(title,orders,posDispCheck,message/*=(message.equals("요청이 취소되었습니다."))? "": message*//*, message2*/);
						//update
						if(!pos.equals("0")) {
							if(pos.toUpperCase().equals("Y")||pos.toUpperCase().equals("N")) break;
							// 서버에 상품코드 전달 후 상품정보 받
							// 상품검색 - 4S    
							// jobCode = 4S   검색코드 - 1차원배열에 저장
							request+=(request.equals("")?"4S":"")+"?"+pos;

							String[] order=fc.getRequest(request).split(",");
							posDispCheck=false;						
							orders[recordIndex]=order;
							/* request에 jobCode만 담기 */
							request = request.substring(0, request.indexOf("?"));
						
						}else {
							request ="";
							menuCode=-1;
							break;
						}
					}
					if(pos.toUpperCase().equals("Y")) {
						String memberCode = null;
						request="4D";

						/*포이트 적립여부*/
						this.display(" ----------- 포인트를 적립하시겠습니까?(y/n) : ");
						if(this.menuInput().toUpperCase().equals("Y")) {
							this.display("-------------- 회원코드 입력 :");
							memberCode = this.menuInput();
						}
						//orders --> finalOrder
						for(int i=0; i<recordIndex; i++) {
							request+=((i==0)?"?":"&")+orders[i][0]+"&"+orders[i][3];
						}
						request+=((memberCode == null)?"":"&"+memberCode);
						// 주문데이터 전송
						/*	1	1001,아메리카노,2000,2,10
							2	1002	 헤이즐넛		  		2500	3	10%
							3	1003	 바닐라아메리카노	  	2500	2	0%
						 *  :     :          :                :     :    :
						 *
						 * jobCode?1002&3&1003&2
						 * */

						message=fc.getRequest(request);

						//System.out.println(message);
						request="";
						menuCode=4;
					}else {
						request="";
						if(!pos.equals("0")) {
							menuCode = 4;
						}
					}
				}
			}else {
				if(request.equals("21")) {
					userData = this.regMenu(title, message);

					if(userData != null) {
						request = "2R";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}
						message = fc.getRequest(request);
						request = "21";
					}else {
						request = "";
						message = "";
					}					
				}else if(request.equals("31")) {
					userData = this.ctlRegMember(title, message);
					if(userData != null) {
						request = "3R";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}						message = fc.getRequest(request);
						request = "31";
					}else {
						request = "";
						message = "";
					}
				}else if(request.equals("22")) {
					/*메뉴수정*/
					userData = this.ctlModMenu(title, message);
					if(userData != null) {
						request ="2M";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}
						message = fc.getRequest(request);
						request = "22";
					}
				}else if(request.equals("23")) {
					/*메뉴삭정제*/
					userData = this.ctlDelMenu(title, message);
					if(userData != null) {
						request ="2D";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}
						message = fc.getRequest(request);
						request = "23";
					}
				}
				else if(request.equals("32")) {
					/*회원수정*/
					userData = this.ctlModMember(title, message);
					if(userData != null) {
						request ="3M";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}
						message = fc.getRequest(request);
						request = "32";
					}
				}
				else if(request.equals("33")) {
					/*회원삭제*/
					userData = this.ctlDelMember(title, message);
					if(userData != null) {
						request ="3D";
						request+="?";
						for(int i=0;i<userData.length;i++) {
							request+= (i==0)?userData[i]:"&"+userData[i];
						}
						message = fc.getRequest(request);
						request = "33";
					}
				}else if(request.equals("13")) {
					this.stat(title, message);
					request = "";
					message ="";
				}else if(request.equals("14")) {
					this.stat2(title, message);
					request = "";
					message ="";
				}
			}
		}
	}
	/*범위 통계 출력*/
	private void stat2(String title, String message) {
		// 화면출력
		this.display(title );
		this.display(" --------------------------------------------------\n  해당범위 		 건수    매출액    할인적용매출액 \n --------------------------------------------------\n");
		this.display("\n  "+ message + "\n");
		this.display(" ------------------------------ 확인(press any key) : ");
		this.menuInput();
	}

	/*통계출력*/
	private void stat(String title, String message) {
		// 화면출력
		this.display(title );
		this.display(" --------------------------------------------------\n  해당월 		 건수    매출액    할인적용매출액 \n --------------------------------------------------\n");
		this.display("\n  "+ message + "\n");
		this.display(" ------------------------------ 확인(press any key) : ");
		this.menuInput();
	}
	/*금월매출현황*/
	private String getDate(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date d =new Date();
		return sdf.format(d);
	}
	/*판매화면 제어 메서드*/
	private String posDisplay(String title, String[][] orders, boolean check,String message/*, String message2*/) {
		String userData = "Y";
		int countOrder = this.countRecord(orders);
		String qty=null;
		while(true) {
			if(check) {
				// 프로그램 타이틀 출력
				this.display(title );
				//display(message2);
				// orders[0][0] != null >> message 출력 >> 장바구니(1)
				if(message.length()==0) {
					if(orders[0] !=null) {
						this.display("장바구니("+countOrder + ")");
					}
				}else {
					this.display("  "+ message + "\n");

				}
				if(orders[0] !=null) {
					this.display("장바구니("+countOrder + ")");
				}
				if(countOrder>0) {

					this.display(" --------------------------------------------------\n  순번     코드          상품        가격   수량   할인율\n --------------------------------------------------\n");

					for(int recordIndex=0; recordIndex<countOrder; recordIndex++) {
						this.display("   " + (recordIndex+1) + "\t" + orders[recordIndex][0] + "\t " + orders[recordIndex][1] + ((orders[recordIndex][1].length() >= 6)? "\t  " : "\t\t  ") 
								+ orders[recordIndex][2] + "\t" + orders[recordIndex][3] + "\t" + orders[recordIndex][4] + "%\n");	
					}	
					this.display("\n---------------------------------------------------\n");
				}
				if(userData.equals("Y")) {

					this.display("[ GOODS SEARCH ] :");
				}else {
					this.display(" ---------------------------------- Sure?(y/n) : ");
				}
				userData=this.menuInput().toUpperCase();
				break;
			}else {
				/*orders[countOrder-1]
				 *        [0]          [1]        [2]           [4]
				 *     10000001  (HOT)아메리키노    2500           0%*/
				this.display("         "+orders[countOrder-1][0]+"\t"+orders[countOrder-1][1]+"\t"+orders[countOrder-1][2]+"\t"+orders[countOrder-1][4]+"%\n");
				while(true) {
					this.display(" ____________________________________ QUANTITY : ");
					qty = this.menuInput();
					if(this.toInt(qty)) {
						orders[countOrder-1][3] = qty;
						break;
					}
				}
				this.display(" ____________________________________ ADD?(Y/N): ");
				userData = this.menuInput().toUpperCase();
				check=true;
			}
		}
		// orders != null >> 상품정보타이틀 출력
		// 상품코드 사용자 입력  >> return
		// ctlView >> orders

		return userData;
	}
	private int countRecord(String[][] orders) {
		int recordIndex;
		for(recordIndex=0;recordIndex<orders.length;recordIndex++) {

			if(orders[recordIndex]==null){break;}
		}
		return recordIndex;
	}
	private String[]ctlModMenu(String title, String menuList){
		String[] modeMenu=null;
		String[] item = {"코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 "};


		this.display(title + "\n");
		this.display(this.makeMenuTitle("메뉴 리스트"));

		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + menuList);
		this.display(" ---------------------------------------------------\n\n");

		this.display("메뉴항목을 수정하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			modeMenu = new String[3];
			this.display("메뉴코드\t:");
			modeMenu[0] = this.menuInput();
			this.display("판매가격\t:");
			modeMenu[1] = this.menuInput();
			this.display("할인율\t:");
			modeMenu[2] = this.menuInput();
		}


		return modeMenu;
	}
	private String[]ctlDelMenu(String title, String menuList){
		String[] delMenu=null;
		String[] item = {"코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 "};
		String[] regMember = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("메뉴 리스트"));

		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + menuList);
		this.display(" ---------------------------------------------------\n\n");

		this.display("메뉴항목을 삭제하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			delMenu = new String[1];
			this.display("메뉴코드\t:");
			delMenu[0] = this.menuInput();

		}
		return delMenu;
	}
	private String[]ctlModMember(String title, String  memberList){
		String[] modmember=null;
		String[] item = {"코드 ", "이름 ", "전번 "};
		String[] regMember = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display("회원정보를 수정하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			modmember = new String[3];
			this.display("회원코드\t:");
			modmember[0] = this.menuInput();
			this.display("전화번호\t:");
			modmember[1] = this.menuInput();

		}
		return modmember;
	}
	private String[]ctlDelMember(String title, String  memberList){
		String[] delmember=null;
		String[] item = {"코드 ", "이름 ", "전번 "};
		String[] regMember = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display("회원정보를 삭제하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			delmember = new String[1];
			this.display("회원코드\t:");
			delmember[0] = this.menuInput();
		}
		return delmember;
	}

	private String[] ctlRegMember(String title, String memberList) {
		String[] item = {"코드 ", "이름 ", "전번 "};
		String[] regMember = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 회원등록을 하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			while(true) {
				this.display(this.makeMenuTitle("등록할 회원"));

				for(int index=0; index<item.length;index++) {
					this.display(item[index] + ": ");
					regMember[index] = this.menuInput();
				}

				this.display("________________________________ CONFIRM?(Y/N) : ");
				if(this.menuInput().toUpperCase().equals("Y")){	break;}
			}
		}else {
			regMember = null;
		}

		return regMember;
	}

	private String[] regMenu(String title, String message) {
		String[] item = {"코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 "};
		String[] regMenu = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("등록된 메뉴"));
		for(int itemIndex=0; itemIndex<item.length; itemIndex++) {
			if(itemIndex == 0) {this.display(" ");}
			this.display(item[itemIndex] + "\t");
			if(itemIndex == 1) {this.display("\t");}
		}
		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + message);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 메뉴등록을 하시겠습니까?(y/n) : ");
		if(this.menuInput().toUpperCase().equals("Y")) {
			while(true) {
				this.display(this.makeMenuTitle("등록할 메뉴"));

				for(int index=0; index<item.length;index++) {
					this.display(item[index] + ": ");
					regMenu[index] = this.menuInput();
				}

				this.display("________________________________ CONFIRM?(Y/N) : ");
				if(this.menuInput().toUpperCase().equals("Y")){	break;}
			}
		}else {
			regMenu = null;
		}

		return regMenu;
	}

	/* 메인화면 제어 및 사용자 데이터 수집*/
	private int ctlMain(String title, String message, String[][] menu) {
		int menuCode;
		while(true) {
			this.display(title);
			this.display(message + "\n\n");
			this.display(this.makeMenuTitle("MAIN"));
			this.display(this.makeMenu(menu));
			this.display(" _____________________________________ SELECT : ");
			menuCode = this.userInput();
			if(menuCode >= 0 && menuCode <= menu.length) {
				break;
			}else {
				message = "메뉴는 0 ~ " + menu.length + "범위이어야 합니다.";
			}
		}
		return menuCode;
	}

	/* 서브메뉴화면 제어 및 사용자 데이터 수집 */
	private int ctlSub(String title, String message, String[] subMenu) {
		int subCode;
		while(true) {
			this.display(title);
			this.display(message + "\n\n");

			this.display(this.makeMenuTitle(subMenu[0]));
			this.display(this.makeMenu(subMenu));
			this.display(" _____________________________________ SELECT : ");

			subCode = this.userInput();
			if(subCode >= 0 && subCode < subMenu.length) {
				break;
			}else {
				message = "메뉴는 0 ~ " + (subMenu.length-1) + "범위이어야 합니다.";
			}
		}
		return subCode;
	}

	private String makeMenuTitle(String text) {
		StringBuffer menuTitle = new StringBuffer();
		int startSpace = (21-text.length())/2;
		int endSpace = ((21-text.length())%2 == 1)? startSpace+1: startSpace;
		menuTitle.append(" [");
		for(int space=0; space<startSpace; space++) {
			menuTitle.append(" ");
		}
		menuTitle.append(text);
		for(int space=0; space<endSpace; space++) {
			menuTitle.append(" ");
		}
		menuTitle.append("]");
		menuTitle.append("__________________________\n\n");

		return menuTitle.toString();
	}

	private String makeMenu(String[] subMenu) {
		StringBuffer buffer = new StringBuffer();

		for(int colIndex=1; colIndex<subMenu.length; colIndex++) {
			if(colIndex==1) {buffer.append("  ");}
			buffer.append(colIndex + ". " + subMenu[colIndex]);
			if(colIndex == subMenu.length-1) {
				buffer.append("\n\n");
			}else {buffer.append("   ");}
		}

		return buffer.toString();
	}

	private String makeMenu(String[][] menu) {
		StringBuffer buffer = new StringBuffer();

		for(int rowIndex=0; rowIndex<menu.length;rowIndex++) {
			if(rowIndex == 0) {buffer.append("  ");}
			buffer.append(rowIndex+1 + ". " + menu[rowIndex][0]);
			if(rowIndex == menu.length-1) {
				buffer.append("\n\n");
			}else {buffer.append("   ");}
		}

		return buffer.toString();
	}

	private String[][] saveMenu(){
		String[][] menu = {{"매장관리", "매장오픈", "매장클로즈", "금일매출현황", "매출통계"},
				{"상품관리", "메뉴등록", "메뉴수정", "메뉴삭제", "굿즈등록", "굿즈수정", "굿즈삭제"}, 
				{"회원관리", "회원등록", "회원수정", "회원삭제"},{"판매관리"}};
		return menu; 
	}

	private String makeTitle() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		buffer.append("           Point Of Sales SYSTEM v1.0\n\n");
		buffer.append("                              Designed by HoonZzang\n");       
		buffer.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		return buffer.toString();
	}

	private void display(String text) {
		System.out.print(text);
	}

	private int userInput() {
		Scanner sc = new Scanner(System.in);
		int menuCode;
		while(true) {
			String data = sc.next();
			if(this.toInt(data)) {
				menuCode = Integer.parseInt(data);
				break;
			}
		}
		return menuCode;
	}

	private String menuInput() {
		return (new Scanner(System.in)).next();
	}

	private boolean toInt(String data) {
		boolean isDigit = true;
		try {
			Integer.parseInt(data);
		}catch(Exception e) {
			isDigit = false;
		}
		return isDigit;
	}
}

/* Access Modifier : 접근제한자 
 *  public  : 모든 클래스(메서드)의 접근을 허용
 *  default : 접근제한자를 생략 :: 같은 패키지 안에 있는 클래스사이에서는 public 
 *  		  다른 패키지에 있는 클래스사이에서는 private 을 적용
 *  protected : 자식 클래스만 접근을 허용 
 *  private : 모든 클래스(메서드)의 접근을 제한
 * */
/*Java Beans
 * 	- Variable --> Array --> Collection : bean + list
 * 	- Bean : 자바프레임워크에서 여러 타입의 데이터를 저장할 수 있는 클래스
 * 			<<<:하나의 빈 갤체와 데이터베이스로 부터 추출되는 하나의 레코드를 담는 역할(이줄이 핵심)>>>
 * 			- 필드 변수(인스턴스벼수)의 접근제한은 반드시 private로 처리
 * 			- 필드를 구성하는 변수의 변수명은 소문자로 시작ㅎ나다. 단 복합명사인 
 * 			  경우 두번째 명사부터는 병사의 첫글자는 대문자로 지정한다.
 * 			- 생성자는 만들지 않는다.
 * 			- 필드변수에 접근하는 방법은 메서드화한다.
 * 				:데이터를 저장할 경우 메서드 이름은 set+필드명으로 한다.
 * 				:데이터를 추출할 경우 메서드 이름은 get+필드명으로 한다.
 * */
