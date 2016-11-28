package work;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import sun.misc.BASE64Encoder;
import static com.mongodb.client.model.Filters.eq;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class user {
	    private final DBCollection usersCollection;
	    private Random random = new SecureRandom();

	    public user(final DBCollection chatDB) {
	        usersCollection = chatDB.getCollection("users");
	    }

	    // validates that username is unique and insert into db
	    public boolean addUser(Integer id, String password, String fname, String lname) {
	    	BasicDBObject document = new BasicDBObject();
	        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

	        document.put("_id", id);
	        document.put("password", passwordHash);

	        if (fname != null && !fname.equals("")) {
	            // XXX WORK HERE
	            // if there is an email address specified, add it to the document too.
	        	document.put("fname", fname);
	        }
			if (lname != null && !lname.equals("")) {
	            // XXX WORK HERE
	            // if there is an email address specified, add it to the document too.
	        	document.put("lname", lname);
	        }

	        try {
	            // XXX WORK HERE
	            // insert the document into the user collection here
	        	usersCollection.insert(document);;
	            return true;
	        } catch (MongoWriteException e) {
	            if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
	                System.out.println("Username already in use: " + username);
	                return false;
	            }
	            throw e;
	        }
	    }

	    public DBObject validateLogin(String fname, String lname, String password) {
	    	
	    	DBObject user = usersCollection.findOne(new BasicDBObject("fname",fname,"lname",lname));

	        // XXX look in the user collection for a user that has this username
	        // assign the result to the user variable.
	        if (user == null) {
	            System.out.println("User not in database");
	            return null;
	        }

	        String hashedAndSalted = user.get("password").toString();

	        String salt = hashedAndSalted.split(",")[1];

	        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
	            System.out.println("Submitted password is not a match");
	            return null;
	        }

	        return user;
	    }


	    private String makePasswordHash(String password, String salt) {
	        try {
	            String saltedAndHashed = password + "," + salt;
	            MessageDigest digest = MessageDigest.getInstance("MD5");
	            digest.update(saltedAndHashed.getBytes());
	            BASE64Encoder encoder = new BASE64Encoder();
	            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
	            return encoder.encode(hashedBytes) + "," + salt;
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("MD5 is not available", e);
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
	        }
	    }
	}


