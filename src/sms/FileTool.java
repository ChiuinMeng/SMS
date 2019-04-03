package sms;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

/**
 * �������ڷ�װһЩ���ļ��Ĳ���
 * @author Chiuin
 *
 */
public class FileTool {
	/**
	 * ��ָ��λ�ô����ļ�������λ������ͬ���ļ�������ʧ��
	 * @param pathname �ļ�λ�ã����ļ��������磺C:\Users\Chiuin\Downloads\demo.txt��
	 * ע�⣺���ʱӦ��ȡת���ַ����滻\���ţ��磺C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @return 1�������ɹ���-1������ʧ�ܣ�-2������ͬ���ļ�
	 */
	public static int crateFile(String pathname) {
		if(System.getProperty("os.name").contains("inux")) {
			pathname = pathname.replaceAll("\\\\","/");
			System.out.println("��Ϊ��Linuxϵͳ���ļ������޸�Ϊ��"+pathname);
		}
		File file = new File(pathname);
		if(!file.exists()) {//���ļ�������
			try {
				file.createNewFile();
				return 1;
			} catch (IOException e) {
				System.out.println("�����ļ�ʧ�ܣ�io�쳣��");
				e.printStackTrace();
				return -1;
			}
		}else {
			System.out.println("�����ļ�ʧ�ܣ��ļ��Ѵ��ڡ�");
			return -2;
		}
	}
	/**
	 * ��ָ��λ�ô����ļ�����д��String���͵����ݡ�
	 * @param pathname �ļ�λ�ã����ļ��������磺C:\Users\Chiuin\Downloads\demo.txt��
	 * ע�⣺���ʱӦ��ȡת���ַ����滻\���ţ��磺C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @param content д�������
	 * @return 1�������ɹ���-1������ʧ�ܣ�-2������ͬ���ļ�
	 */
	public static int crateFile(String pathname,String content) {
		if(System.getProperty("os.name").contains("inux")) {
			pathname = pathname.replaceAll("\\\\","/");
			System.out.println("��Ϊ��Linuxϵͳ���ļ������޸�Ϊ��"+pathname);
		}
		File file = new File(pathname);
		int r = 0;
		if(!file.exists()) {//���ļ�������
			FileOutputStream fos = null;
			PrintWriter pw = null;
			File pf = file.getParentFile();
			if(!pf.exists()) pf.mkdir();//��Ŀ¼�����ڣ�����Ŀ¼
			try {
				file.createNewFile();//Linuxִ�п��ܻᱨ��,��sudo����ִ�С�
				fos = new FileOutputStream(file);
				pw = new PrintWriter(fos);
				pw.write(content);
				pw.flush();
				r = 1;
				if(fos!=null) fos.close();
				if(pw!=null) pw.close();
			} catch (IOException e) {
				System.out.println("�����ļ�ʧ�ܣ�io�쳣��");
				e.printStackTrace();
				r = -1;
			}
		}else {
			System.out.println("�����ļ�ʧ�ܣ��ļ��Ѵ��ڡ�");
			r = -2;
		}
		return r;
	}
	
	/**
	 * ��ȡ�ļ�
	 * @param pathname �ļ�λ�ã����ļ��������磺C:\Users\Chiuin\Downloads\demo.txt��
	 * ע�⣺���ʱӦ��ȡת���ַ����滻\���ţ��磺C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @return �ļ����ݣ��س����з���'\r'��'\n'�ַ���ʾ��
	 */
	public static String readFile(String pathname) {
		if(System.getProperty("os.name").contains("inux")) {
			pathname = pathname.replaceAll("\\\\","/");
			System.out.println("��Ϊ��Linuxϵͳ���ļ������޸�Ϊ��"+pathname);
		}
		File file = new File(pathname);
		if(!file.exists()) return null; //���ļ������ڣ�����null
		
		String r = "";
		String temp;
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis,"UTF-8");
	        br = new BufferedReader(isr);
	        for(int i=0;(temp=br.readLine())!=null;i++) {//readLine()���ܻ����룡
	        	if(i==0) r = r+temp;
	        	else r = r+'\r'+'\n'+temp;//��Unix��ֻ�л��У�û�лس���ô�졣
	        }
	        if(fis!=null) fis.close();
	        if(isr!=null) fis.close();
	        if(br!=null) br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return r;
	}
	
	/**
	 * ʹ��common-io����ȡ���ļ���Ϣ��
	 * @param path
	 * @return
	 */
	public static String readFileToString(String path) {
		try {
			return FileUtils.readFileToString(new File(path), "UTF-8");
		} catch (Exception e) {
			System.out.println("��ȡ�ļ�����");
			e.printStackTrace();
		}
		return null;
	}
}
