<?php
include_once './db_functions.php';

$db = new DB_Functions(); 

//$db->storeUser(20,'im');


$json = $_POST["usersJSON"];


//$json_string = 'http://carpe16.esy.es/carpe16/sync/spot.json';
//$json = file_get_contents($json_string);






if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}

$data = json_decode($json);

$a=array();
$b=array();

for($i=0; $i<count($data) ; $i++)





{   //echo $data[$i];

$res = $db->SpotUser($data[$i]->EmailId,$data[$i]->Phone,$data[$i]
->userName,$data[$i]->Regino,$data[$i]->userId);  
	


if($res)
		{$b["id"] = $data[$i]->userId;
		$b["status"] = 'yes';
		array_push($a,$b);
	}else{
		$b["id"] = $data[$i]->userId;
		$b["status"] = 'no';
		array_push($a,$b);
	}
}

echo json_encode($a);
?>