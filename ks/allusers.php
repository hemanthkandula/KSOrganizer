<?php


$EventNo= $_POST["EventNo"];
//$EventNo = 11;
//echo $Cluster;


    include_once 'db_functions.php';
    $db = new DB_Functions();
    $users = $db->getUnSyncRow($EventNo);


    $a = array();
    $b = array();
    if ($users != false){
        while ($row = mysqli_fetch_array($users)) {      

           //echo "i came in";
            //$b["userId"] = $row["Id"];
            $b["userName"] = $row["Name"];
            $b["EventNo"] = $row["EventNo"];
            $b["GroupName"] = $row["GroupName"];
            $b["Rank"] = $row["Rank"];
            $b["Position"] = $row["Position"];
            $b["RegiNo"]=$row["RegiNo"];
            array_push($a,$b);
        }
        echo json_encode($a);
    }
?>	