<?php

function pretty_print($number_of_bottles) {
    $img_height = "40px";
    $output = "";
    $boxes_per_delivery = 8;
    $bottles_per_box = 20;
    $separate_after_n = 5;
    $flasche_bild = "<img src=\"mate.png\" height=\"$img_height\">";
    $kasten_bild = "<img src=\"mate_kasten.jpeg\" height=\"$img_height\">";
    $lkw_bild = "<img src=\"mate_lkw.jpg\" height=\"$img_height\">";
    $single_count = $flasche_bild;
    $block_separator = " ";

    $rest_bottles = $number_of_bottles % $bottles_per_box;
    $number_of_boxes = floor($number_of_bottles / $bottles_per_box);
    $rest_boxes = $number_of_boxes % $boxes_per_delivery;
    $num_trucks = floor($number_of_boxes / $boxes_per_delivery);

    for ($i = 0; $i < $num_trucks; $i++) {
        $output .= $lkw_bild;
        $output .= $block_separator;
    }
    for ($i = 0; $i < $rest_boxes; $i++) {
        $output .= $kasten_bild;
        $output .= $block_separator;
    }
    for ($i = 0; $i < $rest_bottles; $i++) {
        if ($i % $separate_after_n == 0)
            $output .= $block_separator;
        $output .= $single_count;
    }

    return $output;
}

function mate_db_query($query) {
    //$db = new SQLite3('E:\\Projects\\Mate\\db\\mate.sqlite');
    $db = new SQLite3('/var/mate/mate.sqlite');
    $result = $db->query($query);

    return $result;
}

function add_user($username) {
    mate_db_query("insert into user (user_name,bottle_count,bottle_remain) values ('$username',0,0)");
}

function user_exists($username) {
    $result = mate_db_query("select count(user_name) as exist from user where user_name ='$username'");
    $row = $result->fetchArray(SQLITE3_ASSOC);
    if ($row['exist'] == 0)
        return false;
    return true;
}

function count_mate($username) {
    mate_db_query("update user set bottle_count = bottle_count + 1, bottle_remain = bottle_remain - 1 where user_name = '$username'");
    mate_db_query("insert into insert_log (user_id) values ( (select ROWID from user where user_name = '$username') )");
}

function add_money($username, $value, $bottle_price) {
    $num_bottles_to_add = floor($value / $bottle_price);
    mate_db_query("update user set bottle_remain = bottle_remain + $num_bottles_to_add where user_name = '$username'");
}

function get_num_consumed_bottles($username) {
    $result = mate_db_query("select bottle_count from user where user_name = '$username'");
    if ($result == null)
        return null;
    $row = $result->fetchArray(SQLITE3_ASSOC);

    return $row['bottle_count'];
}

function get_total_consumed_bottles() {
    $result = mate_db_query("select sum(bottle_count) as bottle_count from user");
    if ($result == null)
        return null;
    $row = $result->fetchArray(SQLITE3_ASSOC);

    return $row['bottle_count'];
}

function get_remaining_bottles($username) {
    $result = mate_db_query("select bottle_remain from user where user_name = '$username'");
    if ($result == null)
        return null;
    $row = $result->fetchArray(SQLITE3_ASSOC);

    return $row['bottle_remain'];
}

function get_userlist() {
    $result = mate_db_query("select user_name from user order by bottle_count desc");
    $counter = 0;
    $userlist = array();
    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        $userlist[$counter] = $row['user_name'];
        $counter++;
    }
    return $userlist;
}

function get_mate_couter_tablerow($username) {

    $mate_zaehlen_knopf = "Mate!";
    $num_bottles = get_num_consumed_bottles($username);
    $remaining_bottles = get_remaining_bottles($username);

    ob_start();
    ?>
                            <td class = "user_name"><?php echo $username ?></td>
                            <td class="count_button"><a class="mate"  onclick="countMate('<?php echo $username ?>')"><?php echo $mate_zaehlen_knopf ?></a></td>
                            <td class="pictures" width="*"><?php echo pretty_print($num_bottles) ?></td>
                            <td class="counter"><?php echo $num_bottles ?></td>
                            <td class="remain"><?php echo $remaining_bottles ?></td>
    <?php
    
    $row = ob_get_contents();
    ob_end_clean();
    return $row;
}
?>
