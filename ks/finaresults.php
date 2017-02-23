<?php

//$Cluster = $_POST["Cluster"];

//echo $Cluster;


    include_once 'db_functions.php';
    $db = new DB_Functions();
    $u= $db->getAllUsers();

$db->deleteEmpty();
if ($u!= false){
        while ($row = mysqli_fetch_array($u)) {

     $Name= $db->getEventname($b["EventNo"]);
 }}

    $users = $db->getAllUsers();


    $a = array();
    $b = array();
    if ($users != false){
        while ($row = mysqli_fetch_array($users)) { 

            $b["EventNo"] = $row["EventNo"];


            $b["GroupName"] = $row["GroupName"];

            $b["Rank"] = $row["Rank"];

            $b["Position"] = $row["Position"];
            $b["RegiNo"] = $row["RegiNo"];

            $b["Name"] = $row["Name"];

            
           $b["EventName"] = $row["EventName"];
           $b["ClusterName"] = $row["ClusterName"];


            $b["Gflag"] = $row["Gflag"];
            array_push($a,$b);
        }
        echo json_encode($a);
    }
?>



