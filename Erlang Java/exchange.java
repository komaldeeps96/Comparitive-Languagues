import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class exchange extends Thread{
	boolean isNotified = false;
	public static HashMap<String, Person> personMap = new HashMap<String,Person>();
	public static exchange masterThread;
	@Override
	public void run() {
		for(Map.Entry<String, Person> childThread : personMap.entrySet()) {
			childThread.getValue().start();
		}
		synchronized (this) {
			while(true) {
				try {
					this.wait(1500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.isNotified)
					this.isNotified = false;
				else
					break;
			}
		}
		for(Map.Entry<String, Person> childThread : personMap.entrySet()) {
			try {
				childThread.getValue().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("\nMaster has received no replies for 1.5 seconds, ending...");
	}
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner readFile = new Scanner(new File("calls.txt"));
		StringBuffer input = new StringBuffer();
		while(readFile.hasNext())
			input.append(readFile.nextLine());
		String inputData = input.toString().replace("{", "");
		inputData = inputData.replace("}", "");
		String[] inputDataSplit = inputData.split("\\.");
		System.out.println("**Calls to be made**");
		for(String record : inputDataSplit) {
			record = record.replace("[", "");
			record = record.replace("]", "");
			String[] recordSplit = record.split(",");
			String personName = new String(recordSplit[0].trim());
			ArrayList<String> receiverList = new ArrayList<String>();
			for(int i=1;i<recordSplit.length;++i) {
				receiverList.add(recordSplit[i].trim());
			}
			System.out.println(personName + ": " + receiverList);
			Person person = new Person();
			person.setPersonName(personName);
			person.setReceiverList(receiverList);
			personMap.put(personName, person);
		}
		System.out.println();
		masterThread = new exchange();
		masterThread.start();
	}
	
	public static void printMessage(String message) {
		System.out.println(message);
	}
}


class Person extends Thread{
	public static Random randomNumber = new Random();
	String personName;
	boolean isWaitStarted = false;
	boolean isNotified = false;
	ArrayList<String> receiverList;
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public ArrayList<String> getReceiverList() {
		return receiverList;
	}
	public void setReceiverList(ArrayList<String> receiverList) {
		this.receiverList = receiverList;
	}
	
	@Override
	public void run() {
		System.out.println("here : " + this.getPersonName());
		for(String receiver : receiverList) {
			try {
				Thread.sleep((long)randomNumber.nextInt(100));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.sendMessage(receiver);
		}
		this.isWaitStarted = true;
		synchronized (this) {
			while(true) {
				try {
					this.wait(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.isNotified)
					this.isNotified = false;
				else
					break;
			}
		}
		System.out.println("\nProcess " +this.getPersonName()+" has received no calls for 1 second, ending...");
	}
	
    public void sendMessage(String receiver) {
		Person receiverObject = exchange.personMap.get(receiver);
		synchronized (receiverObject) {
			receiverObject.isNotified = true;
			receiverObject.notify();
		}
		receiverObject.receiveMessage(receiver, this.getPersonName(), "intro", Long.toString(System.currentTimeMillis()));
	}
	public void receiveMessage(String receiver,String sender,String type, String timestamp) {
		synchronized (exchange.masterThread) {
			exchange.masterThread.isNotified = true;
			exchange.masterThread.notify();
		}
		exchange.masterThread.printMessage(String.format("%s received %s message from %s [%s]", receiver,type,sender,timestamp));
		if(type.equalsIgnoreCase("intro")) {
			try {
				Thread.sleep((long)randomNumber.nextInt(100));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Person receiverObject = exchange.personMap.get(sender);
			synchronized (receiverObject) {
				receiverObject.isNotified = true;
				receiverObject.notify();
			}
			receiverObject.receiveMessage(sender, receiver, "reply", timestamp);
		}
	}
	
}
