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
		if(!$row)return false;
		else return $row;
	}
	
	public function insertUser($username,$password){
		if(checkUsername($username))return false;
		$time = date('Y-m-d H:i:s');
		mysql_query("INSERT INTO user(username,password,time) VALUES('$username','$password','$time')");
		$result = mysql_query("SELECT * FROM user WHERE username='$username'");
		$row = mysql_fetch_array($result);
		if(!$row)return false;
		else return true;
	}
	
	public function login($username,$password){
		return $this->checkUser($username,$password);
	}
	
	public function updataOnline($username,$password){
		if(!$this->checkUser($username,$password))return false;
		$value = strval(time()+30);
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

	public function setPassword($username,$value){
		if(!$this->checkUsername($username))return false;
		return mysql_query("UPDATE user SET password = '$value' WHERE username = '$username'");
	}

	public function addFriend($username,$friendname){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($friendname))return false;
		return mysql_query("INSERT INTO friend(username,friendname) VALUES('$username','$friendname'");		
	}
	
	public function addMessage($username,$friendname,$text){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($friendname))return false;
		$time = date('Y-m-d H:i:s');
		return mysql_query("INSERT INTO message(username,friendname,text,time) VALUES('$username','$friendname','$text','$time'");		
	}
	
	public function getMessage($username,$friendname){
		if(!$this->checkUsername($username))return false;
		if(!$this->checkUsername($friendname))return false;
		return mysql_query("SELECT * FROM message WHERE username='$username' AND friendname='$friendname'");
	}
	
	public function getNewMessage($username){
		if(!$this->checkUsername($username))return false;
		return mysql_query("SELECT * FROM message WHERE username='$username' AND flag=ture");
	}
	
	public function flagMessage($id){
		return mysql_query("UPDATE message SET flag = false WHERE id = '$id'");
	}
}
?>