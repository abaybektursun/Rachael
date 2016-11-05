package chat.server;

// MongoDB
import org.bson.Document;
import java.net.UnknownHostException;
import java.util.Date;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.net.DatagramPacket;
import java.net.InetAddress;

import chat.UDPConnector;

public class Server extends UDPConnector {
	private static int port = 4435;
	
	private int portRespond = 0;
	private InetAddress addrRespond;
	private volatile String msgRespond;
	
	public Server() {
		super(port);
	}

	@Override
	public synchronized void handleError(Exception e) {
		System.out.println(e.getMessage());
	}

	@Override
	public synchronized void handlePacketReceived(DatagramPacket packet) {
		String msg = new String(packet.getData());
		
		System.out.println("From client: " + msg);
		
		portRespond = packet.getPort();
		addrRespond = packet.getAddress();
		
		if (msg.equalsIgnoreCase("ping")) {			
			msgRespond = "pong";
		} else {
			msgRespond = null;
		}
	}

	@Override
	public synchronized DatagramPacket createPacketToSend() {
		if (msgRespond == null) {
			return null;
		}
		
		byte[] data = msgRespond.getBytes();
		
		return new DatagramPacket(data, data.length, addrRespond, portRespond);
	}
	//MongoDB *****************************************************
    MongoClient mongoClient = new MongoClient();
	//getDatabase() doesn't creates new database
	MongoDatabase database = mongoClient.getDatabase("mydb");
	MongoCollection<Document> collection = database.getCollection("test");
    //MongoDB *****************************************************
    
	public static void main(String[] args) {
		Server server = new Server();
		server.start("localhost");
        
        // MongoDB *****************************************************
	    try {
	    	/**** Connect to MongoDB ****/
			// Since 2.10.0, uses MongoClient
			MongoClient mongo = new MongoClient("16.86.150.166", 27017);
	
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("testdb");
	
			/**** Get collection / table from 'testdb' ****/
			// if collection doesn't exists, MongoDB will create it for you
			DBCollection table = db.getCollection("user");
	
			/**** Insert ****/
			// create a document to store key and value
			BasicDBObject document = new BasicDBObject();
			document.put("name", "Alua");
			document.put("age", 19);
			document.put("createdDate", new Date());
			table.insert(document);
	
			/**** Find and display ****/
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "Alua");
	
			DBCursor cursor = table.find(searchQuery);
	
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
	
			/**** Update ****/
			// search document where name="Alua" and update it with new values
			BasicDBObject query = new BasicDBObject();
			query.put("name", "Alua");
	
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("name", "Alua-updated");
	
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
	
			table.update(query, updateObj);
	
			/**** Find and display ****/
			BasicDBObject searchQuery2
			    = new BasicDBObject().append("name", "Alua-updated");
	
			DBCursor cursor2 = table.find(searchQuery2);
	
			while (cursor2.hasNext()) {
				System.out.println(cursor2.next());
			}
	
			/**** Done ****/
			System.out.println("Done");
	
//		    } catch (UnknownHostException e) {
//		    	e.printStackTrace();
		    } 
            catch (MongoException e) {
		    	e.printStackTrace();
		    }
	}
}
