$(document).ready(function() {
	var emailElement = $('#email');
	var submissionsElement = $('#submissions');
	
	function updateSubmissions(data) {
		var updatedSubmissions = $($(data).get().reverse());
		if (updatedSubmissions.length) {
			submissionsElement.find('.empty').remove();
		}
		updatedSubmissions.each(function() {
			var submission = $(this);
			var existing = submissionsElement.find('#' + submission.attr('id'));
			if (existing.length) {
				existing.attr('class', submission.attr('class'));
				existing.find('.card-header a').replaceWith(submission.find('.card-header a'));
				existing.find('.card-body').replaceWith(submission.find('.card-body'));
			} else {
				submissionsElement.prepend(submission);
			}
		});
	}
	
	var lastCheck = 0;
	function periodicUpdate() {
		var unfinished = submissionsElement.find('.unknown').length;
		var now = +new Date();
		if (unfinished || now > lastCheck + 60000) {
			$.ajax({
				url : '/check.php?kind=' + (unfinished ? 'unfinished' : 'all'),
				dataType : 'html',
				type : 'GET',
				success : updateSubmissions
			});
			lastCheck = now;
		}
	}
	window.setInterval(periodicUpdate, 3000);

	$('.task').each(function() {
		var taskElement = $(this);
		var taskId = taskElement.data('task-id');

		var languageElement = taskElement.find('.task-language select');
		var editorElement = taskElement.find('.editor');
		var editor = ace.edit(editorElement.attr('id'));
		editor.setOptions({
			fontSize: 16,
			minLines: 5,
			maxLines: 20
		});
		
		var countElement = taskElement.find('.task-count');
		editor.getSession().on('change', function(){
			var normalized = editor.session.getValue().trim().replace('\r\n', '\n');
			countElement.text(normalized.length);
		});

		function languageFunction() {
			var lang = languageElement.val();
			editor.getSession().setMode("ace/mode/" + lang);
		}
		languageElement.change(languageFunction);
		languageFunction();

		var formElement = taskElement.find('form');
		formElement.submit(function(event) {
			event.preventDefault();

			var email = emailElement.val();
			if (/^\s*$/.test(email)) {
				$('#collapseIntro').collapse('show');
				ezBSAlert({
					messageText: "Please fill the email before submitting.",
					alertType: "danger"
				}).done(function() {
					$('#email').focus();
				});
				return;
			}

			if (!/^\S+@\S+\.\S+$/.test(email)) {
				$('#collapseIntro').collapse('show');
				ezBSAlert({
					messageText: "The provided email '" + email + "' is invalid.",
					alertType: "danger"
				}).done(function() {
					$('#email').focus();
				});
				return;
			}

			var code = editor.getSession().getValue();
			if (/^\s*$/.test(code)) {
				ezBSAlert({
					messageText: "Please write your solution.",
					alertType: "danger"
				});
				return;
			}

			$.ajax({
				url : '/submit.php',
				contentType : 'application/json; charset=UTF-8',
				data : JSON.stringify({
					email : email,
					task : taskId,
					language : languageElement.val(),
					code : code
				}),
				dataType : 'html',
				type : 'POST',
				success : function(data) {
					ezBSAlert({
						headerText: "Submission",
						messageText: "Submission was successful. Please watch the submissions section for processing results.",
						alertType: "info"
					});
					updateSubmissions(data);
				}
			});
		});
	});
});
