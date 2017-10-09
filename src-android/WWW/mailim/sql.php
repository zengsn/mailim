<?php
class MailIMSQL{
	const MYSQL_HOST = "127.0.0.1";
	const MYSQL_USER = "username";
	const MYSQL_PASS = "password";
	private $connect;
	private $database;
	private $selected;
	private $table;
	
	public function __construct($db='mailim'){
		$this->connect = mysql_pconnect(self::MYSQL_HOST, self::MYSQL_USER, self::MYSQL_PASS) or trigger_error(mysql_error(),E_USER_ERROR); 	
		$this->database = $db;
		$this->selected = mysql_select_db($this->database, $this->connect);
		mysql_query("SET NAMES 'utf8'");
	}
	
	public function query($q){
		return mysql_query($q);
	}
	
	public function setDatabase($db){
		$this->database = $db;
		$this->selected = mysql_select_db($this->database, $this->connect);
		mysql_query("SET NAMES 'utf8'");
	}
	
	public function checkUser($username,$password){
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row)return false;
		else{
			if($password == $row['password'])return true;
			else return false;
		}
	}
	
	public function checkUsername($username){
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row)return false;
		else return true;
	}
	
	public function getUser($username){
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row){
			$result = mysql_query("SELECT * FROM user WHERE email='$username'");
			$row = mysql_fetch_array($result);
			if(!$row)return false;
			else return $row;
		}
		else return $row;
	}
	
	public function insertUser($username,$password){
		if($this->checkUsername($username))return false;
		$time = date('Y-m-d H:i:s');
		mysql_query("INSERT INTO user(username,password,time) VALUES('$username','$password','$time')");
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row)return false;
		else return true;
	}
	
	public function updateUser($username,$newUsername,$password,$sex,$email,
	$emailpwd,$qianming,$flag = true){
		$user = $this->getUser($username);
		if($user)$id = $user['id'];
		else return false;
		$sql = "UPDATE user SET"
			." username = '$newUsername'"
			.",password = '$password'"
			.",sex = '$sex'";
		if($flag){
			$sql .=",email = '$email'"
			.",emailpwd = '$emailpwd'";
		}
		$sql .=",qianming = '$qianming'"
			." WHERE id = '$id'";

		return mysql_query($sql);
	}

	public function login($username,$password){
		return $this->checkUser($username,$password);
	}
	
	public function updataOnline($username,$password){
		if(!$this->checkUser($username,$password))return false;
		$value = strval(time()+10);
		return mysql_query("UPDATE user SET online = '$value' WHERE username = '$username'");
	}

	public function checkOnline($username){
		if(!$this->checkUsername($username))return false;
		$row = $this->getUser($username);
		if(intval($row['online']) > time())return true;
		else return false;
	}

	public function setEmail($username,$value){
		if(!$this->checkUsername($username))return false;
		return mysql_query("UPDATE user SET email = '$value' WHERE username = '$username'");
	}
	
	public function getEmail($username){
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row)return 'false';
		else return $row['email'];
	}

	public function setPassword($username,$value){
		if(!$this->checkUsername($username))return false;
		return mysql_query("UPDATE user SET password = '$value' WHERE username = '$username'");
	}

	public function addFriend($username,$friendname){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($friendname))return false;
		return mysql_query("INSERT INTO friend(username,friendname) VALUES('$username','$friendname')");		
	}
	
	public function getFriend($username){
		return mysql_query("SELECT * FROM friend WHERE username='$username' AND flag=1 OR friendname='$username' AND flag=1");
	}
	
	public function findNewFriend($username){
		return mysql_query("SELECT * FROM friend WHERE friendname='$username' AND flag=0");
	}
	
	public function updateFriend($username,$friendname,$flag){
		return mysql_query("UPDATE friend SET flag='$flag' WHERE username='$username' AND friendname='$friendname'");
	}
		
	public function addMessage($username,$to,$text){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($to))return false;
		$time = time();
		return mysql_query("INSERT INTO message(username,friendname,text,time) VALUES('$to','$username','$text','$time')");		
	}
	
	public function getMessage($username,$friendname){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($friendname))return false;
		return mysql_query("SELECT * FROM message WHERE username='$username' AND friendname='$friendname'");
	}
	
	public function getNewMessage($username){
		if(!$this->checkUsername($username))return false;
		return mysql_query("SELECT * FROM message WHERE username='$username' AND flag=1");
	}
	
	public function flagMessage($id){
		return mysql_query("UPDATE message SET flag = false WHERE id = '$id'");
	}
}
?>