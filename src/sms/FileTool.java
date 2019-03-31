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
 * 该类用于封装一些对文件的操作
 * @author Chiuin
 *
 */
public class FileTool {
	/**
	 * 在指定位置创建文件，若该位置已有同名文件，创建失败
	 * @param pathname 文件位置（含文件名），如：C:\Users\Chiuin\Downloads\demo.txt。
	 * 注意：编程时应采取转义字符来替换\符号，如：C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @return 1：创建成功，-1：创建失败，-2：已有同名文件
	 */
	public static int crateFile(String pathname) {
		File file = new File(pathname);
		if(!file.exists()) {//若文件不存在
			try {
				file.createNewFile();
				return 1;
			} catch (IOException e) {
				System.out.println("创建文件失败，io异常。");
				e.printStackTrace();
				return -1;
			}
		}else {
			System.out.println("创建文件失败，文件已存在。");
			return -2;
		}
	}
	public static int crateFile(String pathname,int type) {
		return 0;
	}
	/**
	 * 在指定位置创建文件，并写入String类型的数据。
	 * @param pathname 文件位置（含文件名），如：C:\Users\Chiuin\Downloads\demo.txt。
	 * 注意：编程时应采取转义字符来替换\符号，如：C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @param content 写入的内容
	 * @return 1：创建成功，-1：创建失败，-2：已有同名文件
	 */
	public static int crateFile(String pathname,String content) {
		File file = new File(pathname);
		int r = 0;
		if(!file.exists()) {//若文件不存在
			FileOutputStream fos = null;
			PrintWriter pw = null;
			File pf = file.getParentFile();
			if(!pf.exists()) pf.mkdir();//父目录不存在，创建目录
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
				System.out.println("创建文件失败，io异常。");
				e.printStackTrace();
				r = -1;
			}
		}else {
			System.out.println("创建文件失败，文件已存在。");
			r = -2;
		}
		return r;
	}
	public static int crateFile(String pathname,int type,String content) {
		return 0;
	}
	
	/**
	 * 读取文件
	 * @param pathname 文件位置（含文件名），如：C:\Users\Chiuin\Downloads\demo.txt。
	 * 注意：编程时应采取转义字符来替换\符号，如：C:\\Users\\Chiuin\\Downloads\\demo.txt
	 * @return 文件内容，回车换行符以'\r'和'\n'字符表示。
	 */
	public static String readFile(String pathname) {
		File file = new File(pathname);
		if(!file.exists()) return null; //若文件不存在，返回null
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
	        	else r = r+'\r'+'\n'+temp;//在Unix中只有换行，没有回车怎么办。
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
