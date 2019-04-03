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
	private static Set<InetAddress> ipPool = new HashSet<InetAddress>();//IP��
	private int timeInterval = 60000;//ʱ����1����
	private int port;
	private int MAX_UDPMESSAGE_LENGTH = 2048;//https://www.cnblogs.com/linuxbug/p/4906000.html
	private Thread thread_maintainIpPool;
	private Thread thread_listenUDPMessage;
	private Thread thread_handleUDPMessage;
	private Queue<DatagramPacket> messageQueue = new LinkedList<DatagramPacket>();//��Ϣ����
	private HashMap<String,Long> rip = new HashMap<String, Long>();//��¼ip������ظ�ʱ�䡣ֻ��¼һ��ʱ���ڵ�
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
	 * ����udp��Ϣ��ָ�������˿�
	 * @param host ����Ϊ"127.0.0.1",����"java.sun.com"
	 * @param port �˿�
	 * @param info Ҫ���͵���Ϣ
	 */
	public static void sendUDPMessage(String host,int port,String info) {
		try {
			byte buf[] = info.getBytes();
			int length = buf.length;
		    InetAddress address = InetAddress.getByName(host);
			DatagramPacket datagrampacket = new DatagramPacket(buf, length, address, port);
			socket.send(datagrampacket);
			System.out.println("�ѷ�����Ϣ��"+host+"����Ϣ��"+info);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void broadcastUDPMessage(String info,int port) {
		if(ipPool.size()==0) {
			System.out.println("IP��Ϊ�գ��޷��㲥��Ϣ");
			return;
		}
		ipPool.forEach(x->{
			sendUDPMessage(x.getHostAddress(), port, info);
		});
		System.out.println("����"+ipPool.size()+"�������㲥����Ϣ");
	}
	
	/**
	 * ����UDP�˿ڣ�����Ϣ�Ž���Ϣ����
	 */
	public void listenUDPMessage() {
		if(thread_listenUDPMessage!=null && thread_listenUDPMessage.isAlive()) return;
		System.out.println("���ڽ�������udp��Ϣ���̡߳�����");
		thread_listenUDPMessage = new Thread() {
			@SuppressWarnings("resource")
			public void run() {
				while(true) {
					byte[] buf = new byte[MAX_UDPMESSAGE_LENGTH];
					int length = MAX_UDPMESSAGE_LENGTH;
					DatagramPacket packet = new DatagramPacket(buf,length);
					try {
						//��������
						socket.receive(packet);//��������ֱ���յ����ݱ�
						synchronized (messageQueue) {
							messageQueue.add(packet);//�ѽ��յ���Ϣ������Ϣ������
							messageQueue.notify();
						}
						System.out.println("������һ�������Ѽ�����Ϣ����");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread_listenUDPMessage.start();
		System.out.println("����UDP��Ϣ���߳����������˿ڼ����С�����");
	}
	/**
	 * ����һ���̣߳����̻߳ᶨʱ�ķ�����Ϣ����û�л�Ӧ����IP����ɾ����IP��
	 * todo����δ����ɾip�Ĳ�����
	 */
	public void maintainIpPool() {
		if(thread_maintainIpPool!=null && thread_maintainIpPool.isAlive()) return;
		String info = "exist?";//todo��������Ҫ�޸�
		thread_maintainIpPool = new Thread() {
            public void run() {
            	while(true) {
					try {
						sleep(timeInterval);//ÿ��һ��ʱ��������ip���ʹ�����Ϣ��
						if(ipPool.size()!=0) {
							ipPool.forEach(x->{
								sendUDPMessage(x.getHostAddress(), port, info);
							});
						}
						sleep(5000);//�ӳ�5�룬Ȼ��ȷ�ϸ�ip�Ƿ�ظ�����Ϣ����δ�ظ������ip�����ڡ�
						Iterator<InetAddress> it = ipPool.iterator();
				        for(int i=0; i<ipPool.size(); i++){//�߱�����ɾ����
				        	InetAddress ip = it.next();
				            if(!rip.containsKey(ip.getHostAddress())){//��һ��ʱ����δ�ظ���ɾ����
				                it.remove();//��
				                i--;//��
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
	 * �����յ���UDP��Ϣ��
	 */
	public void handleUDPMessage() {
		if(thread_handleUDPMessage!=null && thread_handleUDPMessage.isAlive()) return;
		System.out.println("udp��Ϣ�����߳�����׼���С�����");
		thread_handleUDPMessage = new Thread() {
			 public void run() {
				 while(true) {
					 DatagramPacket packet = null;
					 synchronized (messageQueue) {
						 if(messageQueue.size()!=0) {//����Ϣ����
							 packet = messageQueue.poll();//������İ�
						 }else {
							 System.out.println("û����Ϣ���ȴ��У�����Ϣ��ʱ����");
							 try {messageQueue.wait();} //�ȴ��������ö�����
							 catch (InterruptedException e) {e.printStackTrace();}
						 }
					}
					if(packet!=null) {
						System.out.println("׼����������"+packet.getAddress().getHostAddress()+"�İ�");
					}
				 }
			 }
		};
		thread_handleUDPMessage.start();
		System.out.println("udp��Ϣ�����߳��������������С�����");
	}
	
	/**
	 * �ر�socket��Դ��
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
			System.out.println("�������������");
			e.printStackTrace();
		}
	}
}
