<?php
require_once 'mate_functions.php';

$task_count = "count";

$task = $_POST["task"];

if($task == $task_count) {
    $username = $_POST["username"];
    count_mate($username);
    
    echo get_mate_couter_tablerow($username);
}

?>
