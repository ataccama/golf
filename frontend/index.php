<?php
  session_start();
  
  include 'commons.php';
  
  $services = send('GET', '/services');

?>

<!doctype html>
<html class="no-js" lang="">

<head>
	<meta charset="utf-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<title>Ataccama Task</title>
	<meta name="description"
	      content="A competition by Ataccama which requires contestants to write the shortest possible codes.">
	<meta name="viewport"
	      content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="icon" type="image/png" href="./img/favicon.png" sizes="32x32">

	<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	        integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
	        crossorigin="anonymous"></script>
	<script
		 src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js"
		 integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh"
		 crossorigin="anonymous"></script>
	<link rel="stylesheet"
	      href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
	      integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb"
	      crossorigin="anonymous">
	<script
		 src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"
		 integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ"
		 crossorigin="anonymous"></script>

	<script src="./ace-builds-1.2.9/src/ace.js"></script>

	<script src="./ezbsalert.js"></script>
	<link rel="stylesheet" href="./main.css?v=2">
	<script type="text/javascript" src="./script.js?v=2"></script>
	<!-- Google Tag Manager -->
	<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
          new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
          j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
          'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
       })(window,document,'script','dataLayer','GTM-MXGLQ3Z');</script>
	<!-- End Google Tag Manager -->
</head>

<body>

<div class="container-fluid">
	<div class="row py-3 header">
		<div class="col-12">
			<div class="container">
				<div class="row">
					<div class="col-12 logo">
						<img src="./img/ata-logo-white.png"/>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="container-fluid intro py-5">
	<div class="row">
		<div class="col-12">
			<div class="container">
				<div class="row">
					<div class="col-12 motto">
						<h1 class="mb-4">Short Programming Tasks</h1>
					</div>
					<div class="col-12 col-md-6">
						<p>Your goal is to solve tasks by submitting pieces of code. You
							might have seen some of the tasks already, but we gave them a
							twist to make them more entertaining. <strong>Each task is going to
							have a winner and an award for the best submission.</strong></p>
						<p>You can submit as many solutions to each task as you desire.
							Each submission is graded independently. The list of your
							submissions is stored in session, so you might want back them
							up. Submissions are kept secret until the end of the
							competition.</p>
						<p>Good luck!</p>
					</div>
					<div class="col-12 col-md-6">
						<form>
							<div class="form-group">
								<p><span class="emailLabel">Your email address</span><br>
								The email allows us to identify the winners.</p>
								<input
									 type="email" class="form-control" id="email"
									 aria-describedby="emailHelp" placeholder="Enter email"
									 value="<?= $_SESSION['email'] ?>">
								<p><small>By submitting your email address, you agree to the Ataccama <a href="https://www.ataccama.com/legal/privacy-policy/">privacy policy</a>.</small></p>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="container py-5">
<?php
if (!empty($services->tasks)) {
?>
	<div class="row content">
		<div class="col-12 mb-4">
			<h2>Tasks</h2>
			<div id="tasks" role="tablist">
              <?php
                foreach ($services->tasks as $task) {
                  ?>
					<div class="card task" id="task-<?= $task->id ?>"
					     data-task-id="<?= $task->id ?>">
						<div class="card-header" role="tab"
						     id="task-heading-<?= $task->id ?>">
							<h5 class="mb-0">
								<a class="collapsed" data-toggle="collapse"
								   href="#task-body-<?= $task->id ?>" aria-expanded="false"
								   aria-controls="task-body-<?= $task->id ?>"><?= $task->name ?></a>
							</h5>
						</div>
						<div id="task-body-<?= $task->id ?>" class="collapse"
						     role="tabpanel" aria-labelledby="task-heading-<?= $task->id ?>"
						     data-parent="#tasks">
							<div class="card-body">
								<div class="task-description">
									<p><?= $task->description ?></p>
								</div>
								<form id="form-<?= $task->id ?>">
									<div class="form-group task-language">
										<label for="task-language-<?= $task->id ?>">Select language:</label>
										<select class="form-control"
										        id="task-language-<?= $task->id ?>">
                                          <?php foreach ($services->languages as $language) { ?>
											  <option value="<?= $language->id ?>">
                                                <?= $language->name ?>: <?= $language->description ?>
											  </option>
                                          <?php } ?>
										</select>
									</div>

									<div class="form-group code">
										<label class="textAreaLabel" for="editor-<?= $task->id ?>">Type
											your code here:</label>
										<div class="editor" id="task-editor-<?= $task->id ?>"></div>
										<p>
											Your code is using <span class="task-count">0</span>
											characters.
										</p>
									</div>
									<button type="submit" class="btn btn-primary submit"
									        id="task-submit-<?= $task->id ?>">Submit solution
									</button>
								</form>
							</div>
						</div>
					</div>
                <?php } ?>
			</div>
		</div>
		<div class="col-12">
			<h2>Submissions</h2>

			<div id="submissions" role="tablist">
              <?php
                $submissions = fetchSubmissions($_SESSION['all']);
                if (empty($submissions)) {
                  ?>
					<p class="empty">You don't have any submissions.</p>
                  <?php
                } else {
                  printSubmissions($submissions);
                }
              ?>
			</div>
		</div>
	</div>
<?php
} else {
?>
	<div class="row content">
		<div class="col-12 mb-4">
			<center><big>
				The competition is currently not running.
				Please <a href="https://www.ataccama.com/company/contact">contact Ataccama</a> if you have any questions.
			</big></center>
		</div>
	</div>
<?php
}
?>
</div>
</body>
</html>
