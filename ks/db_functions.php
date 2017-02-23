<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user            "GroupName":"G","userName":"118004076","Rank":"0","EventNo":"13","userId":"1","Position":"P"
     * returns user details
     */
      public function storeUser($GroupName,$Rank,$EventNo,$Id,$Position,$RegiNo) {
        // Insert user into database
        $result = mysqli_query($this->db->connect(),"INSERT INTO users (EventNo, RegiNo,GroupName,Position,Rank) VALUES('$EventNo','$RegiNo','$GroupName','$Position','$Rank')"); //  '$Position','$Place',
		       
 if ($result) {


			return true;
        } else {
			if( mysqli_connect_errno()== 1062) {

				return true;
			} else {
				// For other errors
				return false;
			}            
        }
    }



    public function updateUser($GroupName,$RegiNo,$Rank,$EventNo,$Id,$Position) {
        // Insert user into database
        $result = mysqli_query($this->db->connect(),"UPDATE `users` SET `Rank`= '$Rank',`Position`= '$Position' WHERE EventNo =$EventNo and RegiNo= $RegiNo"); //  '$Position','$Place',
		       //"GroupName":"G","RegNo":"118004076","Rank":"2","EventNo":"13","userId":"1","Position":"P"}
 if ($result) {


			return true;
        } else {
			if( mysqli_connect_errno()== 1062) {

				return true;
			} else {
				// For other errors
				return false;
			}            
        }
    }

public function setEventName($EventNo){

        $result = mysqli_query($this->db->connect(),"SELECT Name,Gflag FROM user WHERE EventName=$EventNo " );








}







	 /**
     * Getting all users
     */
    public function getAllUsers() {
        $result = mysqli_query($this->db->connect(),"select * FROM users");
        return $result;
    }

public function getUnSyncRowCount($Cluster) {
        $result = mysqli_query($this->db->connect(),"SELECT Name,Id,Gflag FROM user WHERE syncsts = FALSE AND Cluster = $Cluster " );

        //echo $result;
        return $result;
    }





public function getUnSyncRow($EventNo) {
        $result = mysqli_query($this->db->connect(),"SELECT * FROM users WHERE EventNo= $EventNo" );

        //echo $result;
        return $result;
    }
    public function updateSyncSts($id, $sts){
        $result = mysqli_query($this->db->connect(),"UPDATE user SET syncsts = $sts WHERE Id = $id");
        return $result;
    }


    public function storeUser2($User,$Email,$regino,$phone) {
        // Insert user into database
        $result = mysqli_query($this->db->connect(),"INSERT INTO stud (Name,Email,Regno,Phone) VALUES('$User','$Email','$regino','$phone')");
 
        if ($result) {
            return true;
        } else {            
                // For other errors
                return false;
        }
    }

    public function storeUser3($Name,$Password,$RegNo,$Mobile,$Cluster,$ClusterName) {
        // Insert user into database
        $result = mysqli_query($this->db->connect(),"INSERT INTO Login (Name,Password,RegNo,Mobile,Cluster,ClusterName) VALUES('$Name','$Password','$RegNo','$Mobile','$Cluster','$ClusterName')");
 
        if ($result) {
            return true;
        } else {            
                // For other errors
                return false;
        }
    }




public function getname($RegiNo){
		//$sql = "SELECT Name FROM `stud` WHERE RegNo =";

        $result = mysqli_query($this->db->connect(),"SELECT Name FROM `stud` WHERE RegNo = $RegiNo " );

        //echo $result;
		// $result = mysqli_query($this->db->connect(),);
        return $result;
    }

public function getnametousers($Name,$RegiNo){
		//$sql = "SELECT Name FROM `stud` WHERE RegNo =";

        $result = mysqli_query($this->db->connect(),"update users set Name = '$Name' WHERE RegiNo = $RegiNo " );

        //echo $result;
		// $result = mysqli_query($this->db->connect(),);
        return $result;
    }
public function deleteEmpty(){
		//$sql = "SELECT Name FROM `stud` WHERE RegNo =";

        $result = mysqli_query($this->db->connect(),"DELETE FROM users WHERE Name = '' "  );

        //echo $result;
		// $result = mysqli_query($this->db->connect(),);
       // return $result;
    }

public function getEventname($EventNo){
		//$sql = "SELECT Name FROM `stud` WHERE RegNo =";

        $res = mysqli_query($this->db->connect(),"SELECT Name,Cluster,Gflag FROM `user` WHERE Id= $EventNo" );
       

if ($res!= 0)
{$row = mysqli_fetch_array($res);
              $EventName = $row["Name"];
               $Cluster =$row["Cluster"];
$Gflag=$row["Gflag"];
//echo $EventName;
}

 $res2 = mysqli_query($this->db->connect(),"SELECT ClusterName FROM `Login` WHERE Cluster= $Cluster" );
if ($res2 != 0)
{$row = mysqli_fetch_array($res2);
              
               $ClusterName =$row["ClusterName"];
//echo $ClusterName;
}


$result = mysqli_query($this->db->connect(),"update users set EventName = '$EventName' ,Gflag= '$Gflag' , ClusterName= '$ClusterName' WHERE EventNo= $EventNo" );



if(!$result){//echo "not Updated";}
}

        //echo $result;
		// $result = mysqli_query($this->db->connect(),);
        return $result;
    }



  
















public function SpotUser($EmailId,$Phone,$userName,$Regino,$userId) {  //[{"EmailId":"a@","Phone":"85","userName":"a","Regino":"8","userId":"1"}]
        // Insert user into database
        $result = mysqli_query($this->db->connect(),"INSERT INTO stud(Name,RegNo,Email,Phone) VALUES('$userName','$Regino','$EmailId','$Phone')");
 if ($result) {


			return true;
        } else {
			if( mysqli_connect_errno()== 1062) {






				return true;
			} else {
				// For other errors
				return false;
			}            
        }
        
    }








}














?>						