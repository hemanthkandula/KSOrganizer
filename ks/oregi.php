<?php 
/**
 * Insert User into DB
 */ ?>
<style>
*, *:before, *:after {
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}

body {
  font-family: 'Nunito', sans-serif;
  color: #384047;
}

form {
  max-width: 300px;
  margin: 10px auto;
  padding: 10px 20px;
  background: #f4f7f8;
  border-radius: 8px;
}

h1 {
  margin: 0 0 30px 0;
  text-align: center;
}

input[type="text"],
input[type="password"],
input[type="date"],
input[type="datetime"],
input[type="email"],
input[type="number"],
input[type="search"],
input[type="tel"],
input[type="time"],
input[type="url"],
textarea,
select {
  background: rgba(255,255,255,0.1);
  border: none;
  font-size: 16px;
  height: auto;
  margin: 0;
  outline: 0;
  padding: 15px;
  width: 100%;
  background-color: #e8eeef;
  color: #8a97a0;
  box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
  margin-bottom: 30px;
}

input[type="radio"],
input[type="checkbox"] {
  margin: 0 4px 8px 0;
}

select {
  padding: 6px;
  height: 32px;
  border-radius: 2px;
}

button {
  padding: 19px 39px 18px 39px;
  color: #FFF;
  background-color: #4bc970;
  font-size: 18px;
  text-align: center;
  font-style: normal;
  border-radius: 5px;
  width: 100%;
  border: 1px solid #3ac162;
  border-width: 1px 1px 3px;
  box-shadow: 0 -1px 0 rgba(255,255,255,0.1) inset;
  margin-bottom: 10px;
}

fieldset {
  margin-bottom: 30px;
  border: none;
}

legend {
  font-size: 1.4em;
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 8px;
}

label.light {
  font-weight: 300;
  display: inline;
}

.number {
  background-color: #5fcf80;
  color: #fff;
  height: 30px;
  width: 30px;
  display: inline-block;
  font-size: 0.8em;
  margin-right: 4px;
  line-height: 30px;
  text-align: center;
  text-shadow: 0 1px 0 rgba(255,255,255,0.2);
  border-radius: 100%;
}

@media screen and (min-width: 480px) {

  form {
    max-width: 480px;
  }

}</style>
<center>
<div class="header"> 
</div>
</center>
<form method="POST">
<h1>Registration</h1>
<fieldset>
          <!-- <legend><span class="number">1</span>Your basic info</legend> -->
          <label for="name">Name:</label>
          <input type="text" id="name" name="username">
          
          <label for="password">Set Password:</label>
          <input type="text" id="pass" name="password">

          <label for="regino">Register Number:</label>
          <input type="number" id="reg" name="regino">         
          
          <label for="mail">Email:</label>
          <input type="email" id="mail" name="Email">

          <label for="mobile">Mobile:</label>
          <input type="number" id="mob" name="phone">

          <label for="cluster">Cluster:</label>
          <select name="dropdown">
              <option value="1" name="Arts" selected>Arts</option>
              <option value="2" name="Design" >Design</option>
              <option value="3" name="Dramatics">Dramatics</option>
              <option value="4" name="EDance">Eastern Dance</option>
              <option value="5" name="ELits">English Lits</option>
              <option value="6" name="HLits">Hindi Lits</option>
              <option value="7" name="Music">Music</option>
              <option value="8" name="Sfh">SFH</option>
              <option value="9" name="TmlLits">Tamil Lits</option>
              <option value="10" name="TlguLits">Telugu Lits</option>
              <option value="11" name="WDance">Western Dance</option>
              <option value="12" name="pr">PR</option>
          </select>

        </fieldset>
        
        <button type="submit" value="Add User">Submit</button>
<h3><a href="https://drive.google.com/file/d/0BxYXKU1HKC-uVzIyVGxHUzdkeW8/view?usp=sharing" target="_blank">Download App</a></h3>
</form>


<?php
include_once './db_functions.php';
//Create Object for DB_Functions clas
if(isset($_POST["username"],$_POST["password"] ,$_POST["Email"] ,$_POST["regino"],$_POST["phone"],$_POST["dropdown"])&& !empty($_POST["username"])){
$db = new DB_Functions(); 
//Store User into MySQL DB
$cul =array("Arts","Design","Dramatics","Eastern Dance","English Lits","Hindi Lits","Music","SFH","Tamil Lits","Telugu Lits","Western Dance","PR");
$uname = $_POST["username"];
$password = $_POST["password"];
$Email = $_POST["Email"];
$regino = $_POST["regino"];
$phone = $_POST["phone"];
$dropdown = $_POST["dropdown"];
$res = $db->storeUser3($uname,$password,$regino, $phone, $dropdown,$cul[$dropdown-1]);
    //Based on inserttion, create JSON response
    if($res){ ?>
         <div id="msg">Insertion successful</div>
    <?php }else{ ?>
         <div id="msg">Insertion failed</div>
    <?php }
} else{ ?>
 <div id="msg">Please enter all details and submit</div>
<?php }
?>			