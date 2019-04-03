package sms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PeerManagement {
	private static PeerManagement peerManagement;
	private static Set<InetAddress> ipPool = new HashSet<InetAddress>();//IP池
	private int timeInterval = 60000;//时间间隔1分钟
	private int port;
	private int MAX_UDPMESSAGE_LENGTH = 2048;//https://www.cnblogs.com/linuxbug/p/4906000.html
	private Thread thread_maintainIpPool;
	private Thread thread_listenUDPMessage;
	private Thread thread_handleUDPMessage;
	private Queue<DatagramPacket> messageQueue = new LinkedList<DatagramPacket>();//消息队列
	private HashMap<String,Long> rip = new HashMap<String, Long>();//记录ip和最近回复时间。只记录一段时间内的
	private static DatagramSocket socket;
	
	private PeerManagement(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public static synchronized PeerManagement getInstance(int port) {
		if(peerManagement==null) peerManagement = new PeerManagement(port);
		return peerManagement;
	}
	/**
	 * 发送udp消息到指定主机端口
	 * @param host 可以为"127.0.0.1",或者"java.sun.com"
	 * @param port 端口
	 * @param info 要发送的消息
	 */
	public static void sendUDPMessage(String host,int port,String info) {
		try {
			byte buf[] = info.getBytes();
			int length = buf.length;
		    InetAddress address = InetAddress.getByName(host);
			DatagramPacket datagrampacket = new DatagramPacket(buf, length, address, port);
			socket.send(datagrampacket);
			System.out.println("已发送消息到"+host+"，消息："+info);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void broadcastUDPMessage(String info,int port) {
		if(ipPool.size()==0) {
			System.out.println("IP池为空，无法广播消息");
			return;
		}
		ipPool.forEach(x->{
			sendUDPMessage(x.getHostAddress(), port, info);
		});
		System.out.println("已向"+ipPool.size()+"个主机广播了消息");
	}
	
	/**
	 * 监听UDP端口，把消息放进消息队列
	 */
	public void listenUDPMessage() {
		if(thread_listenUDPMessage!=null && thread_listenUDPMessage.isAlive()) return;
		System.out.println("正在建立监听udp消息的线程。。。");
		thread_listenUDPMessage = new Thread() {
			@SuppressWarnings("resource")
			public void run() {
				while(true) {
					byte[] buf = new byte[MAX_UDPMESSAGE_LENGTH];
					int length = MAX_UDPMESSAGE_LENGTH;
					DatagramPacket packet = new DatagramPacket(buf,length);
					try {
						//会阻塞！
						socket.receive(packet);//会阻塞！直到收到数据报
						synchronized (messageQueue) {
							messageQueue.add(packet);//把接收的消息加入消息队列中
							messageQueue.notify();
						}
						System.out.println("监听到一个包，已加入消息队列");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread_listenUDPMessage.start();
		System.out.println("监听UDP消息的线程已启动。端口监听中。。。");
	}
	/**
	 * 创建一个线程，该线程会定时的发送消息。若没有回应，从IP池中删除该IP。
	 * todo：尚未做完删ip的操作。
	 */
	public void maintainIpPool() {
		if(thread_maintainIpPool!=null && thread_maintainIpPool.isAlive()) return;
		String info = "exist?";//todo：可能需要修改
		thread_maintainIpPool = new Thread() {
            public void run() {
            	while(true) {
					try {
						sleep(timeInterval);//每隔一段时间向所有ip发送存在消息。
						if(ipPool.size()!=0) {
							ipPool.forEach(x->{
								sendUDPMessage(x.getHostAddress(), port, info);
							});
						}
						sleep(5000);//延迟5秒，然后确认该ip是否回复了消息，若未回复，则该ip不存在。
						Iterator<InetAddress> it = ipPool.iterator();
				        for(int i=0; i<ipPool.size(); i++){//边遍历边删除？
				        	InetAddress ip = it.next();
				            if(!rip.containsKey(ip.getHostAddress())){//若一段时间内未回复，删除。
				                it.remove();//？
				                i--;//？
				            }
				        }	
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
            }
        };
        thread_maintainIpPool.start();
	}
	
	/**
	 * 处理收到的UDP消息。
	 */
	public void handleUDPMessage() {
		if(thread_handleUDPMessage!=null && thread_handleUDPMessage.isAlive()) return;
		System.out.println("udp消息处理线程启动准备中。。。");
		thread_handleUDPMessage = new Thread() {
			 public void run() {
				 while(true) {
					 DatagramPacket packet = null;
					 synchronized (messageQueue) {
						 if(messageQueue.size()!=0) {//有消息处理
							 packet = messageQueue.poll();//待处理的包
						 }else {
							 System.out.println("没有消息，等待中，有消息来时唤醒");
							 try {messageQueue.wait();} //等待，放弃该对象锁
							 catch (InterruptedException e) {e.printStackTrace();}
						 }
					}
					if(packet!=null) {
						System.out.println("准备处理来自"+packet.getAddress().getHostAddress()+"的包");
					}
				 }
			 }
		};
		thread_handleUDPMessage.start();
		System.out.println("udp消息处理线程已启动，运行中。。。");
	}
	
	/**
	 * 关闭socket资源。
	 */
	public void close() {
		if(socket!=null)socket.close();
	}
	
	public static void addHost(InetAddress host) {
		ipPool.add(host);
	}
	public static void addHost(String host) {
		try {
			ipPool.add(InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			System.out.println("输入的主机有误");
			e.printStackTrace();
		}
	}
}
