<?php
include_once './db_functions.php';

$db = new DB_Functions(); 

//$db->storeUser(20,'im');


$json = $_POST["usersJSON"];


//$json_string = 'http://carpe16.esy.es/carpe16/sync/post.json';
//$json = file_get_contents($json_string);






if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}

$data = json_decode($json);

$a=array();
$b=array();

for($i=0; $i<count($data) ; $i++)
{   //echo $data[$i];

$res = $db->updateUser($data[$i]->GroupName,$data[$i]->RegNo,$data[$i]->Rank,$data[$i]->EventNo,$data[$i]->userId,$data[$i]->Position);//    $data[$i]->Position,$data[$i]->Place,
//  "GroupName":"G","RegNo":"118004076","Rank":"2","EventNo":"13","userId":"1","Position":"P"}
if($res){
		$b["id"] = $data[$i]->userId;
          

         //echo $Name;
        




                 
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