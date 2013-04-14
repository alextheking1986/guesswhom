/**
 * 
 */
package com.sameer.gupshap;

/**
 * @author Syed Sameer
 *
 */
public class User {
	//private variables
		String id;
		String name;
		String chat;
		String msg_type;
		
		public String getMsg_type() {
			return msg_type;
		}
		public void setMsg_type(String msg_type) {
			this.msg_type = msg_type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getChat() {
			return chat;
		}
		public void setChat(String chat) {
			this.chat = chat;
		}
		
		public User()
		{
			
		}
		
		
		public User(String id, String name, String chat,String typ) {
			super();
			this.id = id;
			this.name = name;
			this.chat = chat;
			this.msg_type = typ;
		}
		
		
	
		
		

}
