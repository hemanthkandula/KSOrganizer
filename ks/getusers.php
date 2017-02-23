<?php

$Cluster = $_POST["Cluster"];

//echo $Cluster;


    include_once 'db_functions.php';
    $db = new DB_Functions();
    $users = $db->getUnSyncRowCount($Cluster);


    $a = array();
    $b = array();
    if ($users != false){
        while ($row = mysqli_fetch_array($users)) {      
            $b["EVENTNO"] = $row["EVENTNO"];
            $b["EVENTNAME"] = $row["EVENTNAME"];
            $b["Gflag"] = $row["Gflag"];
            array_push($a,$b);
        }
        echo json_encode($a);
    }
?>	