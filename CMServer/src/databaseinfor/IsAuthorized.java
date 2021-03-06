package databaseinfor;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

import javax.swing.JOptionPane;

import sendinfor.RandomString;

public class IsAuthorized {
	private int flag = 0;
	private int days = 0;
	private int authorize;
	private int day;
	private boolean hasmarked = false;

	/**
	 * check the user information
	 * @param mac
	 * @return
	 */
    public int isauthorized(String mac){
    	Connection con = DBConnection();
    	try{
    		String date1 = null;
    		String date3;
    		Date date2;
    		Statement st = con.createStatement();
    		String sql = "select macid from userconf ";
    		String sql4 = "select status from userconf where macid = '"+ mac +"'";
    		ResultSet rs = st.executeQuery(sql);
    		while(rs.next()){
    			if(rs.getString(1).equals(mac)){
    				hasmarked = true;
    				break;
    				}
   				}
    		//System.out.println(hasmarked);
    		if(!hasmarked){
    			String sql1 = "insert into userconf(macid) values('"+ mac +"')";
        		st.executeUpdate(sql1);
        		JOptionPane.showMessageDialog( null,"Mac地址已记录！");
        		flag = 0;
    			}
    		else {   
    			ResultSet rs1 = st.executeQuery(sql4);
        		while(rs1.next())
        			authorize = rs1.getInt(1);
    			if(authorize == 0)
    				flag = 0;
    			else{
    				flag =1;
    				String sql2 = "select excutedate,days from userconf where macid = '"+ mac +"'";
    				ResultSet rs2 = st.executeQuery(sql2);
    				while(rs2.next()){
    					date1 = rs2.getString(1);
    					day = rs2.getInt(2);
    				}
    				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    				date2 = new Date();
    				date3 = df.format(date2);
    				days = day - nDaysBetweenTwoDate(date1,date3);
    				if(days<1){      //购买已到期
    					flag = 2;    //  返回用户提示续费
    					String sql3 = "update userconf set days = 0,status = 0,excutedate = null,costnum = 0,reset_flag = 0 where macid = '"+ mac +"'";
    					st.executeUpdate(sql3);
        				}       			    			
    				}
    			}
    			
    		}catch(Exception e){
    			e.printStackTrace();
    			flag = 3;
    		}
    		return flag;
    	}
    /**
     * 购买
     * @param money
     * @param days
     * @param mac
     * @param phonenumber
     * @return
     */
    public String buypro(int money,int days,String mac,String phonenumber){
    		String key = null;
    		int times = 0;
    		RandomString RS = new RandomString();
    		Connection con = DBConnection();
    		try{
    			Statement st = con.createStatement();
    			String sql = "select shop_times from userconf where macid = '"+ mac +"'";
    			ResultSet rs = st.executeQuery(sql);
    			while(rs.next())
    				times = rs.getInt(1);
    			if(times != 0){
    				key = "hasbought";
    			}
    			else{
    				key = RS.generateRandomString();
    				times = times +1;
    				String sql1 = "update userconf set days = '"+ days +"',costnum = '"+ money +"',tel = '"+ phonenumber +"',user_key = '"+key+"', shop_times = '"+times+"' where macid = '"+ mac +"'";
    				st.executeUpdate(sql1);
    				
    				}	
    			}catch(Exception e){
    				    key = "error";
    					e.printStackTrace();
    					}
    		return key;
    		}
    /**
     * 续费
     * @param money
     * @param days
     * @param mac
     * @return
     */
    public String renew(int money,int days,String mac){
    	int reset = 0;
		int shoptimes = 0;
		String renewsuc = null;
		Connection con = DBConnection();
		try{
			Statement st = con.createStatement();
			String sql = "select reset_flag,shop_times from userconf where macid = '"+mac+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				reset = rs.getInt(1);
				shoptimes = rs.getInt(2);
			}
			
			if(reset == 1){//使用已到期的续费
				shoptimes = shoptimes +1;
				String sql1 = "update userconf set days = '"+ days +"',status = 1,costnum = '"+ money +"',reset_flag = 0,shop_times = '"+shoptimes+"' where macid = '"+ mac +"'";
				st.executeUpdate(sql1);
				renewsuc = "success";
			}else if(shoptimes != 0){ //使用未到期的续费
				shoptimes = shoptimes +1;
				String sql1 = "update userconf set days = days + '"+ days +"',costnum = '"+ money +"',shop_times = '"+shoptimes+"' where macid = '"+ mac +"'";
				st.executeUpdate(sql1);
				renewsuc = "success";
			}
			else renewsuc = "newuser";
		}catch(Exception e){
			e.printStackTrace();
			}
		return renewsuc;
    }
    /**
     * verify the user permission
     * @param key
     * @param mac
     * @return
     */
    public String verify(String key,String mac){
    		String verify = null;
    		String privacykey = null;
    		Connection con = DBConnection();
    		Date date3;
			String date4;
			int flag = 0;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			date3 = new Date();  //获取当前系统日期
			date4 = df.format(date3);
    		try{
    			String sql3 = "select user_key from userconf where macid = '"+ mac +"'";
    			String sql4 = "select status from userconf where macid = '"+ mac +"'";
    			Statement st = con.createStatement();
    			ResultSet rs4 = st.executeQuery(sql4);
    			while(rs4.next())
    				flag = rs4.getInt(1);
    			if(flag ==1)
    				verify = "hasverified";
    			else{
    			ResultSet rs3 = st.executeQuery(sql3);
    			while(rs3.next())
    				privacykey = rs3.getString(1);
    				if(key.equals(privacykey)){
    					verify = "verifiedsuccess";
    					String sql5 = "update userconf set status = 1,excutedate = '"+ date4 +"' where macid = '"+ mac +"'";
        				st.executeUpdate(sql5);
    					}
    				else
    					verify = "notmatch";
    				}
    			}catch(Exception e){
						e.printStackTrace();
						}
    		return verify;
    		}
    /**
     * get left days
     * @return
     */
    public int getleftdays(String mac){
    	Connection con = DBConnection();
    	try{
    		Date date2;
    		int authorizeday = 0;
    		String date1 = null;
    		String time;
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Statement st = con.createStatement();
    		String sql = "select excutedate,days,tel from userconf where macid = '"+ mac +"'";
    		ResultSet rs = st.executeQuery(sql);
    		while(rs.next()){
    			date1 = rs.getString(1);
    			authorizeday = rs.getInt(2);
    		}   		
    		date2 = new Date();
    		time = df.format(date2);
    		days = authorizeday - nDaysBetweenTwoDate(date1 ,time);
    		
    	}catch(Exception e){
    		days = -1;
    		e.printStackTrace();
    	}
    	return days;
    }
    /**
     * get the user phone number
     * @param mac
     * @return
     */
    public String getuserphone(String mac){
    	Connection con = DBConnection();
    	String phone = null;
    	try{
    		Statement st = con.createStatement();
    		String sql = "select tel from userconf where macid = '"+ mac +"'";
    		ResultSet rs = st.executeQuery(sql);
    		while(rs.next()){
    			phone = rs.getString(1);
    		}   		   		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return phone;
    }
    /**
     * connect database
     * @return
     */
	private Connection DBConnection() {
		Connection con = null;
		//String url = "jdbc:odbc:userconf";
		String url = "jdbc:mysql://localhost:3306/chemmapper";
		String username = "root";
		String password = "1133557799";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
		}catch(SQLException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException ex){ex.printStackTrace();}
		return con;
	}
	/**
	 * Calculate the left days
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public   int   nDaysBetweenTwoDate(String   firstString,String   secondString)   {  
        SimpleDateFormat   df   =   new   SimpleDateFormat("yyyy-MM-dd");  
        Date   firstDate=null;  
        Date   secondDate=null;  
        try   {  
            firstDate   =   df.parse(firstString);  
            secondDate=df.parse(secondString);  
        }  
        catch(Exception   e)   {  
            //   日期型字符串格式错误  
        }  
 
        int nDay=(int)((secondDate.getTime()-firstDate.getTime())/(24*60*60*1000));  
        return   nDay;  
    }
	public static void main(String args[]){
		String mac = "78-AC-C0-A0-6A-93";
		int a = new IsAuthorized().getleftdays(mac);
		//for(int i = 0;i<a.size();i++)
		//{
		System.out.println(a);
	}
}
