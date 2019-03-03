package system.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

public class ZooMethods {
	private static  ZooKeeper zoo;
	final CountDownLatch con = new CountDownLatch(1);

	public ZooKeeper connect(String host) throws IOException,InterruptedException {
	      zoo = new ZooKeeper(host,5000,new Watcher() {	
	         public void process(WatchedEvent we) {
	            if (we.getState() == KeeperState.SyncConnected) {
	               con.countDown();
	            }
	         }
	      });
	      con.await();
	      return zoo;
	    }

	public static void close() throws InterruptedException {
		zoo.close();
		}
	
	public static Stat exists(String path) throws KeeperException,InterruptedException {
		return zoo.exists("/"+path, true);
		}
	
	public static void create(String path, byte[] data) throws KeeperException,InterruptedException {
		zoo.create("/"+path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		}
	
	public static String read(String path) throws KeeperException,InterruptedException{
		byte[] b = zoo.getData("/"+path, true, zoo.exists("/"+path, true));
    	String data = new String(b, StandardCharsets.UTF_8);
		return 	data;
		}
	
	public static void append(String path, byte[] data) throws KeeperException,InterruptedException { 
		byte[] bn = zoo.getData("/"+path,false, null);
        String prev = new String(bn, StandardCharsets.UTF_8) + new String(data, StandardCharsets.UTF_8);
		zoo.setData("/"+path, prev.getBytes(), zoo.exists("/"+path,true).getVersion());
		}
	public static void delete(String path) throws KeeperException,InterruptedException {
	      zoo.delete("/"+path,zoo.exists("/"+path,true).getVersion());
	   }
	public static String getFiles() throws  KeeperException,InterruptedException {  //additional
		List <String> children = zoo.getChildren("/", false);
		String files="";
        for(int i = 0; i < children.size(); i++) {
        	files=files+children.get(i)+" ";
	}
        return files;
	}
	public static void update(String path, byte[] data) throws KeeperException,InterruptedException { //additional
		zoo.setData("/"+path, data, zoo.exists("/"+path,true).getVersion());
		}
}
