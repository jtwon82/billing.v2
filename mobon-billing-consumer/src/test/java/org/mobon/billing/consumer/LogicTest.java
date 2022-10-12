package org.mobon.billing.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.adgather.util.old.DateUtils;

public class LogicTest {
	public Map<String, Item> mapData = (new ConcurrentHashMap<String, Item>());
	public static LogicTest ins;

	public static LogicTest instance() {
		if (ins == null) {
			ins = new LogicTest();
		}
		return ins;
	}

	public synchronized void appendData(Item record) {
			Item sum = mapData.get(record.generateKey());
			if (sum == null) {
				mapData.put(record.generateKey(), record);
			} else {
				sum.sumGethering(record);
			}
	}
	public synchronized Map<String, Item> remove(){
		Map<String, Item> result = null;
			result = this.mapData;
			mapData = (new ConcurrentHashMap<String, Item>());
		return result;
	}

	public static void main(String[] ar) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date() );
		cal.add(Calendar.MINUTE, -20);
		
		String zipFileName =  DateUtils.getDate("yyyy-MM-dd_HH", new Date(cal.getTimeInMillis())) ;
		
		System.out.println(zipFileName);
				
	}
	public void test3() {
		ThreadPoolExecutor threadPool1 = new ThreadPoolExecutor(63, 63, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		for (int i = 0; i < 63; i++)
			threadPool1.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						int value = new Random().nextInt(2)+1;
						int userid = new Random().nextInt(3000);
						int sitecode = new Random().nextInt(10);
						Item item = new Item("userid" + userid, "sitecode" + sitecode, value);
						LogicTest.instance().appendData(item);
					}
				}
			});

		String [][] group_key = new String[][] {{"sitecode1"},{"sitecode2"},{"sitecode3"},{"sitecode4"},{"sitecode5"}
			,{"sitecode6"},{"sitecode7"},{"sitecode8"},{"sitecode9"},{"sitecode0"}};
		
		ThreadPoolExecutor threadPool2 = new ThreadPoolExecutor(63, 63, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		for (int i = 0; i < 63; i++) {
			threadPool2.execute(new Runnable() {
				public void run() {
					while(true) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
						
						Map<String, Item> map = LogicTest.instance().remove();
						ArrayList<Item> listResult = new ArrayList();
						if(map!=null && map.entrySet()!=null) {
							System.out.println( String.format("%s %s %s", Thread.currentThread().getName(), "map.size", map.entrySet().size()) );
							Iterator<Entry<String, Item>> it = map.entrySet().iterator();
							while(it.hasNext()){
								try {
									Entry<String, Item> Titem = it.next();
									if( Titem!=null ) {
										Item item = Titem.getValue();
										if( item!=null ) {
											listResult.add(item);
										}
									}
								}catch(Exception e) {
									System.out.println( String.format("%s %s %s", Thread.currentThread().getName(), "end map.size", map.entrySet().size()) );
									e.printStackTrace();
									System.exit(0);
								}
							}

							int sumFilteringSize=0;
							for (String[] group : group_key) {
								String _id = Arrays.asList(group).toString();
								List<Item> filtering = (List<Item>) listResult.stream()
									.filter(row -> _id.equals( row.grouping() ) )
									.collect(Collectors.toList());
								
								sumFilteringSize +=filtering.size();
							}
							System.out.println(String.format("%s, %s", Thread.currentThread().getName(), sumFilteringSize));
						}
					}
				}
			});
		}
	}
}

class Item {
	private String userid;
	private String sitecode;
	private int qty;

	@Override
	public String toString() {
		return "Item [userid=" + userid + ", sitecode=" + sitecode + ", qty=" + qty + "]";
	}

	public String generateKey() {
		return String.format("[%s, %s]", sitecode, userid);
	}
	public String grouping() {
		return String.format("[%s]", sitecode);
	}

	public void sumGethering(Item from) {
		this.setQty( this.getQty() + from.getQty() );
	}

	public Item(String _userid, String _sitecode, int _qty) {
		setUserid(_userid);
		setSitecode(_sitecode);
		qty = _qty;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getSitecode() {
		return sitecode;
	}

	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
