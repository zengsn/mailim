<?php
// $username = isset( $_GET['username'] ) ? $_GET['username'] : '';
// $password = isset( $_POST['password'] ) ? $_POST['password'] : '';
// $type = isset( $_POST['type'] ) ? $_POST['type'] : '';
$json = isset( $_POST['json'] ) ? $_POST['json'] : '';

require_once('des.php');
require_once('sql.php');
$key = 'zzhzzhzzhzzhzzh';
$des = new Des($key);
$json = $des->Decrypt($json,$key);
$data = json_decode($json,true);

$sql = new MailIMSQL();

$type = $data['type'];
$username = $data['username'];
$password = $data['password'];
switch($type){
	case 'check':
		$result = $sql->getMessage($username,'zzz');
		$res = array();
		while($row = mysql_fetch_array($result))
		{
			$res[strval($row['id'])] = $row;
		}
		echo json_encode($res);
		break;
	case 'login':
		if($sql->login($username,$password))echo 'true';
		else echo 'false';
		break;
	case 'sned':
		$message = $data['message'];
		$to = $message['to'];
		$text = $message['text'];
		if($sql-addMessage($username,$to,$text))echo 'true';
		else echo 'false';
		break;
	case 'pulse':
		if($sql->updataOnline($username,$password))echo 'true';
		else echo 'false';
		break;
}
$sql->updataOnline($username,$password);
?>