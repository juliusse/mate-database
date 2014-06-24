<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
    <?php
    require('mate_functions.php');

    $mate_flaschenpreis = "0.75";

    $mate_zaehlen_knopf = "Mate!";
    $einzahlen_knopf = "Einzahlen!";
    $nutzer_anlegen_knopf = "anlegen!";
    $betrag_einzahlen_task = "Betrag einzahlen";
    $neuer_nutzer_task = "Neuer Nutzer";

    ///////////////////////////////////////////////////////////
    // Initial tasks
    // Create database/table if not exists	
    mate_db_query("PRAGMA foreign_keys = ON");
    mate_db_query("create table if not exists user (user_name TEXT, bottle_count NUMERIC, bottle_remain NUMERIC)");
    mate_db_query("create table if not exists insert_log (user_id INTEGER, timestamp INTEGER default (date('now')), foreign key(user_id) references user(ROWID) on update cascade on delete restrict)");

    ///////////////////////////////////////////////////////////
    ?>
<html>
    <head>
        <title>Matenbank</title>
        <link rel="stylesheet" type="text/css" href="mate.css">
        <script type="text/javascript" src="mate.js"></script>
    </head>
    <body>
        <?php
        if (isset($_POST["task"])) {
            
            $task = $_POST["task"];
            $user_name = $_POST["user_name"];
            if ($task == $betrag_einzahlen_task) {
                // Komma in Werten wird durch Punkt ersetzt
                $bottle_price = str_replace(",", ".", $_POST["flaschenpreis_bei_einzahlung"]);
                $value = str_replace(",", ".", $_POST["betrag"]);
                add_money($user_name, $value, $bottle_price);
            }
            if ($task == $neuer_nutzer_task) {
                if (!user_exists($user_name))
                    add_user($user_name);
            }
        }

        $user = get_userlist();

        $mate_zaehlen_task = "Mate zaehlen";
        $betrag_einzahlen_task = "Betrag einzahlen";
        ?>
        <div class="main">
            <h3>Matenbank</h3>
            <table class="mate_counter" width="100%">
                <tr>
                    <th class="user_name">Wer?</th>
                    <th class="count_button">Durst!</th>
                    <th class="pictures">Schon so viel?</th>
                    <th class="counter">gez&auml;hlt</th>
                    <th class="remain">&uuml;brig</th>
                </tr>
                <?php
                foreach ($user as $user_name) :
                    ?>
                    <tr id="row-<?php echo $user_name ?>">
                        <?php echo get_mate_couter_tablerow($user_name); ?>
                    </tr>
                    <?php
                endforeach;

                if (sizeOf($user) == 0) {
                    ?>
                    <tr><td class="user_name"></td><td class="count_button"></td><td class="error_message" width="*">Es wurde noch kein Nutzer angelegt!</td><td class="counter"></td><td class="remain"></td></tr>
                    <?php
                }
                ?>
                <tr><th class="user_name">Team</th><th></th><th></th><th class="counter"><?php echo get_total_consumed_bottles() ?></th><th></th></tr>

            </table>
        </div>

        <div class="footer">
            <div class="options">
                <h3>Einzahlen</h3>
                <form action="<?php echo $_SERVER['PHP_SELF'] ?>" method="post">
                    <input type="hidden" name="task" value="<?php echo $betrag_einzahlen_task?>">Nutzer <select class="user_wahl" name="user_name">
                        <?php
                        foreach ($user as $user_name) {
                            ?>
                            <option><?php echo $user_name ?></option>
                            <?php
                        }
                        ?>
                    </select>
                    Betrag
                    <input type="text" class="money" name="betrag" value="10">
                    Euro Flaschenpreis
                    <input type="text" class="price" name="flaschenpreis_bei_einzahlung" value="<?php echo $mate_flaschenpreis ?>">
                    Euro
                    <input type="submit" value="<?php echo $einzahlen_knopf ?>">
                </form>
            </div>

            <div class="options">
                <h3>Neuer Nutzer</h3>
                <form action="<?php echo $_SERVER['PHP_SELF'] ?>" method="post">
                        <input type="hidden" name="task" value="<?php echo $neuer_nutzer_task?>">
                        <input type="text" class="user_name" name="user_name" placeholder="Neuer Nutzer">
                        <input type="submit" value="<?php echo $nutzer_anlegen_knopf ?>">
                </form>
            </div>
        </div>

    </body>
</html>
