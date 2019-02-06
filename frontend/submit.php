<?php
session_start();

include 'commons.php';

$input = file_get_contents('php://input');

$submission = send('POST', '/submit', $input);

array_unshift($_SESSION['all'], $submission->id);
array_unshift($_SESSION['unfinished'], $submission->id);

$_SESSION['email'] = json_decode($input)->email;

printSubmissions([
    $submission
]);
