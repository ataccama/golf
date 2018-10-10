<?php
date_default_timezone_set('Europe/Prague');

require './Carbon.php';
use Carbon\Carbon;

// uncomment and fill the correct IP address
define("BACKEND", "http://gateway:8080");

if (empty($_SESSION['all'])) {
    $_SESSION['all'] = [];
}
if (empty($_SESSION['unfinished'])) {
    $_SESSION['unfinished'] = [];
}
if (empty($_SESSION['email'])) {
    $_SESSION['email'] = '';
}

function send($method, $endpoint, $body = NULL)
{
    if (!defined("BACKEND")) {
        return NULL;
    }
    $ch = curl_init(BACKEND . $endpoint);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
    if (! empty($body)) {
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($body)
        ));
    }

    $ret = curl_exec($ch);
    return json_decode($ret, false);
}

function fetchSubmissions($ids)
{
    $submissions = [];
    if (! empty($ids)) {
        $submissions = send('GET', '/check?ids=' . implode(',', $_SESSION['all']));
        
        $remove = [];
        foreach ($submissions as $submission) {
            if ($submission->finished) {
                $remove[] = $submission->id;
            }
        }
        $_SESSION['unfinished'] = array_diff($_SESSION['unfinished'], $remove);
    }
    
    return $submissions;
}

function submissionState($submission)
{
    if (! $submission->finished) {
        return "unknown";
    } else if ($submission->processingResult != 'OK') {
        return "failed";
    } else if ($submission->gradingResult != 'OK') {
        return "wrong";
    } else {
        return "correct";
    }
}

function parseInstant($instant)
{
    $dt = Carbon::createFromFormat("Y-m-d\TH:i:s.u+", $instant, "UTC");
    $dt->setTimezone('Europe/Prague');
    return $dt;
}

function printInstant($instant, $detail = false)
{
    $dt = parseInstant($instant);
    $output = '';
    if (! $dt->isToday()) {
        $output .= "on " . $dt->format('M j');
        if (! $dt->isCurrentYear()) {
            $output .= ", " . $dt->format('Y');
        }
        $output .= " ";
    }
    $output .= "at " . $dt->format('G:i');
    if ($detail) {
        $output .= sprintf(":%02d.%03d", $dt->second, (int) ($dt->micro / 1000));
    }
    return $output;
}

function printSubmissions($submissions)
{
    foreach ($submissions as $submission) {
        ?>
<div class="card submission <?= submissionState($submission) ?>"
	id="submission-<?= $submission->id ?>">
	<div class="card-header" role="tab"
		id="submission-heading-<?= $submission->id ?>">
		<h5 class="mb-0">
			<a class="collapsed" data-toggle="collapse"
				href="#submission-body-<?= $submission->id ?>" aria-expanded="false"
				aria-controls="submission-body-<?= $submission->id ?>">
			<?= $submission->task ?> &raquo;
			<?= $submission->language ?> &raquo;
			<?php
        echo submissionState($submission);
        if ($submission->gradingScore > 0) {
            echo " (" . $submission->gradingScore . ")";
        }
        echo " <small>" . printInstant($submission->created) . "</small>";
        ?>
				</a>
		</h5>
	</div>
	<div id="submission-body-<?= $submission->id ?>" class="collapse"
		role="tabpanel"
		aria-labelledby="submission-heading-<?= $submission->id ?>">
		<div class="card-body">
			<dl class="row">
				<dt class="col-sm-4">Id</dt>
				<dd class="col-sm-8"><?= $submission->id ?></dd>
				<dt class="col-sm-4">Task</dt>
				<dd class="col-sm-8"><?= $submission->task ?></dd>
				<dt class="col-sm-4">Language</dt>
				<dd class="col-sm-8"><?= $submission->language ?></dd>
				<dt class="col-sm-4">Code</dt>
				<dd class="col-sm-8"><?php ?><pre><?php ?><code><?= $submission->code ?></code><?php ?></pre><?php ?></dd>
				<dt class="col-sm-4">Created</dt>
				<dd class="col-sm-8"><?= printInstant($submission->created, true) ?></dd>
				<dt class="col-sm-4">Updated</dt>
				<dd class="col-sm-8"><?= printInstant($submission->updated, true) ?></dd>
				<dt class="col-sm-4">Processing</dt>
				<dd class="col-sm-8"><?= $submission->processingResult ?></dd>
<?php
        if ($submission->processingResult != 'UNKNOWN') {
            ?>
				<dt class="col-sm-4">P Start</dt>
				<dd class="col-sm-8"><?= printInstant($submission->processingStarted, true) ?></dd>
				<dt class="col-sm-4">P End</dt>
				<dd class="col-sm-8"><?= printInstant($submission->processingEnded, true) ?></dd>
<?php
            if (! empty($submission->processingMessage)) {
                ?>
				<dt class="col-sm-4">P Message</dt>
				<dd class="col-sm-8"><?php ?><pre><?php ?><code><?= $submission->processingMessage ?></code><?php ?></pre><?php ?></dd>
				<?php
            }
        }
        ?>
				<dt class="col-sm-4">Grading</dt>
				<dd class="col-sm-8"><?= $submission->gradingResult ?></dd>
<?php
        if ($submission->gradingResult != 'UNKNOWN') {
            ?>
				<dt class="col-sm-4">G Start</dt>
				<dd class="col-sm-8"><?= printInstant($submission->gradingStarted, true) ?></dd>
				<dt class="col-sm-4">G End</dt>
				<dd class="col-sm-8"><?= printInstant($submission->gradingEnded, true) ?></dd>
<?php
            if (! empty($submission->gradingMessage)) {
                ?>
				<dt class="col-sm-4">G Message</dt>
				<dd class="col-sm-8"><?php ?><pre><?php ?><code><?= $submission->gradingMessage ?></code><?php ?></pre><?php ?></dd>
				<?php
            }
            if (! empty($submission->gradingScore)) {
                ?>
				<dt class="col-sm-4">G Score</dt>
				<dd class="col-sm-8"><?= $submission->gradingScore ?></dd>
				<?php
            }
        }
        ?>
			</dl>
		</div>
	</div>
</div>
<?php
    }
}
