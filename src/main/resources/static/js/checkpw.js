
$('#pwChkForm').on('submit', function(e) {
	e.preventDefault(); // 기본 폼 제출 방지

	const userpw = {
		userpw: $('#userPw').val()
	}

	$.ajax({
		url: "/checkpw",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify(userpw),
		headers: {
			'Authorization': 'Bearer ' + localStorage.getItem('token')
		},

		success: function(response) {
			if (response.success) {
				alert('인증 성공 !');
				location.href = '/updateInfo';
			} else {
				alert('비밀번호가 틀렸습니다 다시 시도해 주세요');
			}
		},
		error: function(xhr) {
			alert('에러!', xhr);
		}

	})

})