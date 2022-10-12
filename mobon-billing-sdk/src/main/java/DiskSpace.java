

import java.io.File;

public class DiskSpace {
	public static float space() {
//		File diskPartition = new File("C:/");
		File diskPartition = new File("/");
        long totalCapacity = diskPartition.getTotalSpace();
        long freePartitionSpace = diskPartition.getFreeSpace();
        long usablePatitionSpace = diskPartition.getUsableSpace();
        
        float total = totalCapacity / (1024*1024);
        float use = usablePatitionSpace / (1024 *1024);
        
        float percent = (use/total)*100;
        return percent;
	}
	
	public static boolean isWrite() {
		return (space() < 30) ? false : true;
	}
	
	public static void main(String args[]) {
		System.out.println(DiskSpace.space());
		System.out.println(DiskSpace.isWrite());
	}
}
