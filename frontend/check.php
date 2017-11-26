<?php
session_start();

include 'commons.php';

if (isset($_GET['kind']) && $_GET['kind'] == 'all') {
    $kind = 'all';
} else {
    $kind = 'unfinished';
}

$submissions = fetchSubmissions($_SESSION[$kind]);
if (! empty($submissions)) {
    printSubmissions($submissions);
}