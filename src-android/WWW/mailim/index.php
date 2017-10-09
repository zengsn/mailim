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
if(array_key_exists('username',$data))$username = $data['username'];
else $username = '';
if(array_key_exists('password',$data))$password = $data['password'];
else $password = '';
if($type == 'register'){
	if($sql->insertUser($username,$password)){
		copy('head/head.png','head/'.$username.'.png');
		// $sql->addFriend($username,$username);
		$type = 'login';
	}
	else {
		echo 'false';
		exit();
	}
}
if(!$sql->checkUser($username,$password)){
	echo 'false';
	exit();
}
$sql->updataOnline($username,$password);
switch($type){
	case 'updateUser':
		$newUsername = $data['newUsername'];
		$newPassword = $data['newPassword'];
		$sex = $data['sex'];
		$flag = $data['updateemail'];
		$email = array_key_exists('email',$data)?$data['email']:'';
		$emailpwd = array_key_exists('emailpwd',$data)?$data['emailpwd']:'';			
		$qianming = $data['qianming'];
		if($sql->updateUser($username,$newUsername,$newPassword,
		$sex,$email,$emailpwd,$qianming,$flag)){
			echo 'true';
		}
		else echo 'false';
		break;
	case 'getUser':
		$res = $sql->getUser($data['name']);
		if($res){
			$res['type'] = 'user';
			echo json_encode($res);
		}
		else echo '[]';
		break;
	case 'online':
		$name = $data['name'];
		if($sql->checkOnline($name))echo 'true';
		else echo 'false';
		break;
	case 'setEmail':
		$email = $data['email'];
		if($sql->setEmail($username,$email))echo 'true';
		else echo 'false';
		break;
	case 'getEmail':
		$name = $data['name'];
		echo $sql->getEmail($name);
		break;
	case 'getChat':
		$result = $sql->getNewMessage($username);
		$res = array();
		$flag = false;
		$res['json'] = true;
		$res_data = array();
		while($row = mysql_fetch_array($result))
		{
			$row['text'] = userTextDecode($row['text']);
			$res_data[strval($row['id'])] = $row;
			$sql->flagMessage($row['id']);
			$flag = true;
		}
		if($flag){
			$res['data'] = $res_data;
			echo json_encode($res);
		}
		else echo 'null';
		break;
	case 'addFriend':
		if($sql->addFriend($username,$data['name']))echo 'true';
		else echo 'false';
		break;
	case 'newFriend':
		$result = $sql->findNewFriend($username);
		$res = array();
		$flag = false;
		$res['json'] = true;
		$res_data = array();
		while($row = mysql_fetch_array($result))
		{
			$res_data[strval($row['id'])] = $row;
			//$sql->updateFriend($row['id'],1);
			$flag = true;
		}
		if($flag){
			$res['data'] = $res_data;
			echo json_encode($res);
		}
		else echo 'null';
		break;
	case 'agree':
		$friendname = $data['friendname'];
		if($sql->updateFriend($friendname,$username,1))echo 'true';
		else echo 'false';
		break;
	case 'disagree':
		$friendname = $data['friendname'];
		if($sql->updateFriend($friendname,$username,2))echo 'true';
		else echo 'false';
		break;
	case 'login':
		if($sql->login($username,$password)){
			$res = $sql->getUser($username);
			if($res){
				$res['type'] = 'user';
				echo json_encode($res);
			}
			else echo '[]';	
		}
		else 'false';
		break;
	case 'chat':
		$to = $data['to'];
		$text = userTextEncode($data['text']);
		if($sql->addMessage($username,$to,$text))echo 'true';
		else echo 'false';
		break;
	case 'friend':
		$result = $sql->getFriend($username);
		$res = array();
		$res['json'] = true;
		$res_data = array();
		while($row = mysql_fetch_array($result))
		{
			if($username == $row['username'])$friendname = $row['friendname'];
			else $friendname = $row['username'];
			if($online = $sql->checkOnline($friendname))
				$row['online'] = 'true';
			else $row['online'] = 'false';
			$res_data[strval($row['id'])] = $row;
		}
		$res['data'] = $res_data;
		echo json_encode($res);
		break;
	case 'pulse':
		if($sql->updataOnline($username,$password))echo 'true';
		else echo 'false';
		break;
	case 'head':
		$base_path = "./upload/"; // 接收文件目录  
		$target_path = $base_path .$username.'/'. basename ( $_FILES ['uploadfile'] ['name'] );  
		$dir = dirname($target_path);
		if(!is_dir($dir)){
			mkdir($dir,0777,true);
		}
		if (move_uploaded_file ( $_FILES ['uploadfile'] ['tmp_name'], $target_path )) {  
			my_image_resize($target_path,'head/'.$username,'100','100');
			echo 'true';
		} else {  
			echo 'false';
		}  
		break;
	default:
		echo $type;
}
// 获得任意大小图像，不足地方拉伸，不产生变形，不留下空白
function my_image_resize($src_file, $dst_file , $new_width , $new_height) {
	$new_width= intval($new_width);
	$new_height=intval($new_width);
	if($new_width <1 || $new_height <1) {
		echo "params width or height error !";
		exit();
	}
	if(!file_exists($src_file)) {
		echo $src_file . " is not exists !";
		exit();
	}
	// 图像类型
	$type=exif_imagetype($src_file);
	$support_type=array(IMAGETYPE_JPEG , IMAGETYPE_PNG , IMAGETYPE_GIF);
	if(!in_array($type, $support_type,true)) {
		echo "this type of image does not support! only support jpg , gif or png";
		exit();
	}
	//Load image
	switch($type) {
		case IMAGETYPE_JPEG :
			$src_img=imagecreatefromjpeg($src_file);
			break;
		case IMAGETYPE_PNG :
			$src_img=imagecreatefrompng($src_file);
			break;
		case IMAGETYPE_GIF :
			$src_img=imagecreatefromgif($src_file);
			break;
		default:
			echo "Load image error!";
			exit();
	}
	$w=imagesx($src_img);
	$h=imagesy($src_img);
	$ratio_w=1.0 * $new_width / $w;
	$ratio_h=1.0 * $new_height / $h;
	$ratio=1.0;
	// 生成的图像的高宽比原来的都小，或都大 ，原则是 取大比例放大，取大比例缩小（缩小的比例就比较小了）
	if( ($ratio_w < 1 && $ratio_h < 1) || ($ratio_w > 1 && $ratio_h > 1)) {
		if($ratio_w < $ratio_h) {
			$ratio = $ratio_h ; // 情况一，宽度的比例比高度方向的小，按照高度的比例标准来裁剪或放大
		}else {
			$ratio = $ratio_w ;
		}
		// 定义一个中间的临时图像，该图像的宽高比 正好满足目标要求
		$inter_w=(int)($new_width / $ratio);
		$inter_h=(int) ($new_height / $ratio);
		$inter_img=imagecreatetruecolor($inter_w , $inter_h);
		//var_dump($inter_img);
		imagecopy($inter_img, $src_img, 0,0,0,0,$inter_w,$inter_h);
		// 生成一个以最大边长度为大小的是目标图像$ratio比例的临时图像
		// 定义一个新的图像
		$new_img=imagecreatetruecolor($new_width,$new_height);
		//var_dump($new_img);exit();
		imagecopyresampled($new_img,$inter_img,0,0,0,0,$new_width,$new_height,$inter_w,$inter_h);
		switch($type) {
			case IMAGETYPE_JPEG :
				imagejpeg($new_img, $dst_file,100); // 存储图像
				break;
			case IMAGETYPE_PNG :
				imagepng($new_img,$dst_file,100);
				break;
			case IMAGETYPE_GIF :
				imagegif($new_img,$dst_file,100);
				break;
			default:
				break;
		}
	} // end if 1
	// 2 目标图像 的一个边大于原图，一个边小于原图 ，先放大平普图像，然后裁剪
	// =if( ($ratio_w < 1 && $ratio_h > 1) || ($ratio_w >1 && $ratio_h <1) )
	else{
		$ratio=$ratio_h>$ratio_w? $ratio_h : $ratio_w; //取比例大的那个值
		// 定义一个中间的大图像，该图像的高或宽和目标图像相等，然后对原图放大
		$inter_w=(int)($w * $ratio);
		$inter_h=(int) ($h * $ratio);
		$inter_img=imagecreatetruecolor($inter_w , $inter_h);
		//将原图缩放比例后裁剪
		imagecopyresampled($inter_img,$src_img,0,0,0,0,$inter_w,$inter_h,$w,$h);
		// 定义一个新的图像
		$new_img=imagecreatetruecolor($new_width,$new_height);
		imagecopy($new_img, $inter_img, 0,0,0,0,$new_width,$new_height);
		switch($type) {
			case IMAGETYPE_JPEG :
				imagejpeg($new_img, $dst_file,100); // 存储图像
				break;
			case IMAGETYPE_PNG :
				imagepng($new_img,$dst_file,100);
				break;
			case IMAGETYPE_GIF :
				imagegif($new_img,$dst_file,100);
				break;
			default:
				break;
		}
	}// if3
}// end function
function userTextEncode($str){
    if(!is_string($str))return $str;
    if(!$str || $str=='undefined')return '';

    $text = json_encode($str); //暴露出unicode
    $text = preg_replace_callback("/(\\\u[ed][0-9a-f]{3})/i",function($str){
        return addslashes('\\'.$str[0]);
    },$text); //将emoji的unicode留下，其他不动，这里的正则比原答案增加了d，因为我发现我很多emoji实际上是\ud开头的，反而暂时没发现有\ue开头。
    return json_decode($text);
}
/**
  解码上面的转义
 */
function userTextDecode($str){
    $text = json_encode($str); //暴露出unicode
    $text = preg_replace_callback('/\\\\\\\\/i',function($str){
        return '\\';
    },$text); //将两条斜杠变成一条，其他不动
    return json_decode($text);
}
?>