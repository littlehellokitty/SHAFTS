package com.shafts.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class CheckUserStatus {
    private Socket socket = null;
	private String ip = "localhost";// 设置成服务器IP
	private String userphone = null;
	private int port = 8821;  //8821
	private int flag; //0代表处于离线状态
	private int leftdays;  //可使用剩余天数
	private boolean isconnect;
	private DataOutputStream out = null;
	private DataInputStream getMessageStream = null;
	/**
	 * connect the server
	 * @throws Exception
	 */
	public void connection() throws Exception{
		try{
			socket = new Socket(ip,port);
		}catch(Exception e){
			if(socket != null)
				socket.close();
			System.out.println(e.getStackTrace());
			//JOptionPane.showMessageDialog( null,"连接异常！");
		}
	}
	/**
	 * chek the authorized status
	 * @throws Exception
	 */
	public void checkauthorization() throws Exception{
		try {
			connection();
			isconnect = true;
		} catch (IOException e) {
			isconnect = false;
			JOptionPane.showMessageDialog( null,"连接异常！");
		}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(1);
				out.writeUTF(mac);
				out.flush();
			} catch (IOException e) {
				if (out != null)
					out.close();
			}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				flag = getMessageStream.readInt();				
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	/**
	 * get the authorized status of the user
	 * @return
	 */
	public int getuserstatus(){
		try {
			checkauthorization();
		} catch (Exception e) {
				e.printStackTrace();
				flag = 5;
			}
		return flag;
	}
	/**
	 * verify the user
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String verify(String key) throws Exception{
		String hasverified = null;
		try{
			connection();
			isconnect = true;
			} catch (IOException e) {
						isconnect = false;
						JOptionPane.showMessageDialog( null,"连接异常！");
						}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(4);
				out.writeUTF(mac);
				out.writeUTF(key);
				out.flush();
				} catch (IOException e) {
					if (out != null)
						out.close();
					}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				hasverified = getMessageStream.readUTF();
				}catch(Exception e){
						e.printStackTrace();
					}
		}
		return hasverified;
	}
	/**
	 * renew for extend the term
	 * @return
	 * @throws Exception 
	 */
	public String renew(int money,int days) throws Exception{
		String renewsuc = null;
		try{
			connection();
			isconnect = true;
			} catch (IOException e) {
						isconnect = false;
						JOptionPane.showMessageDialog( null,"连接异常！");
						}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(5);
				out.writeUTF(mac);
				out.writeInt(days);
				out.writeInt(money);
				out.flush();
				} catch (IOException e) {
					if (out != null)
						out.close();
					}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				renewsuc = getMessageStream.readUTF();
				}catch(Exception e){
						e.printStackTrace();
					}
		}
		return renewsuc;
	}
	/**
	 * check user left days
	 * @throws Exception
	 */
	public void getleftdays() throws Exception{
		try {
			connection();
			isconnect = true;
		} catch (IOException e) {
			isconnect = false;
			JOptionPane.showMessageDialog( null,"连接异常！");
		}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(2);
				out.writeUTF(mac);
				out.flush();
			} catch (IOException e) {
				if (out != null)
					out.close();
			}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				leftdays = getMessageStream.readInt();
			}catch(Exception e){
			}
		}
	}
	/**
	 *get the left days
	 * @return
	 */
	public int getdays(){
		try {
			getleftdays();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leftdays;
		
	}
	/**
	 *get the user phone number
	 * @return
	 */
	public String getuserphone(){
		try {
			getusertel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userphone;
		
	}
	private void getusertel() throws Exception {
		try {
			connection();
			isconnect = true;
		} catch (IOException e) {
			isconnect = false;
			JOptionPane.showMessageDialog( null,"连接异常！");
		}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(6);
				out.writeUTF(mac);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				if (out != null)
					out.close();
			}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				userphone = getMessageStream.readUTF();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	/**
	 *  pay for use the system
	 * @param days
	 * @param money
	 * @return
	 * @throws Exception
	 */
	public String buypro(int days, int money, String phonenumber) throws Exception{
		String publickey = null;
		try {
			connection();
			isconnect = true;
		} catch (IOException e) {
			isconnect = false;
			JOptionPane.showMessageDialog( null,"连接异常！");
		}
		if(isconnect){
			try {
				out = new DataOutputStream(socket.getOutputStream());
				getWindowsMACAddress getmac = new getWindowsMACAddress();
				String mac = getmac.getAddress();
				out.writeInt(3);
				out.writeUTF(mac);
				out.writeInt(days);
				out.writeInt(money);
				out.writeUTF(phonenumber);
				out.flush();
			} catch (IOException e) {
				if (out != null)
					out.close();
			}
			try{
				getMessageStream = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				publickey = getMessageStream.readUTF();				
			}catch(Exception e){
				
			}
		}
		return publickey;
	}
}
