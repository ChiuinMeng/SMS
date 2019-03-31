package sms;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
	public static int crateFile(String pathname,int type) {
		return 0;
	}
	/**
	 * ��ָ��λ�ô����ļ�����д��String���͵����ݡ�
	 * @param pathname �ļ�λ�ã����ļ��������磺C:\Users\Chiuin\Downloads\demo.txt��
	 * ע�⣺���ʱӦ��ȡת���ַ����滻\���ţ��磺C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @param content д�������
	 * @return 1�������ɹ���-1������ʧ�ܣ�-2������ͬ���ļ�
	 */
	public static int crateFile(String pathname,String content) {
		File file = new File(pathname);
		int r = 0;
		if(!file.exists()) {//���ļ�������
			FileOutputStream fos = null;
			PrintWriter pw = null;
			File pf = file.getParentFile();
			if(!pf.exists()) pf.mkdir();//��Ŀ¼�����ڣ�����Ŀ¼
			try {
				file.createNewFile();
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
	public static int crateFile(String pathname,int type,String content) {
		return 0;
	}
	
	/**
	 * ��ȡ�ļ�
	 * @param pathname �ļ�λ�ã����ļ��������磺C:\Users\Chiuin\Downloads\demo.txt��
	 * ע�⣺���ʱӦ��ȡת���ַ����滻\���ţ��磺C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @return �ļ����ݣ��س����з���'\r'��'\n'�ַ���ʾ��
	 */
	public static String readFile(String pathname) {
		File file = new File(pathname);
		if(!file.exists()) return null; //���ļ������ڣ�����null
		String r = "";String temp;
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
	        br = new BufferedReader(isr);
	        for(int i=0;(temp=br.readLine())!=null;i++) {
	        	if(i==0) r = r+temp;
	        	else r = r+'\r'+'\n'+temp;//��Unix��ֻ�л��У�û�лس���ô�졣
	        }
	        if(fis!=null) fis.close();
	        if(isr!=null) fis.close();
	        if(br!=null) br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return r;
	}
}
