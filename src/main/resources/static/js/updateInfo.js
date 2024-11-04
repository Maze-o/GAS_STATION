
$('#updateInfoForm').on('submit', function (event) {
	event.preventDefault(); // 기본 폼 제출 방지

	console.log('버튼 호출')
	const username = $('#inputUsername').val();
	const userpw = $('#inputPassword').val();

	// 비밀번호 유효성 검사
	if (userpw) {
		// 비밀번호 길이 검사
		if (userpw.length < 6 || userpw.length > 20) {
			alert('비밀번호는 6자리 ~ 20자리 이내로 입력해주세요');
			return; // 유효성 검사 실패 시 AJAX 요청 중단
		}

		// 비밀번호에 영문자와 숫자가 모두 포함되어 있는지 검사
		const hasLowerCase = /[a-z]/.test(userpw);
		const hasDigits = /\d/.test(userpw);

		if (!(hasLowerCase && hasDigits)) {
			alert('비밀번호는 영문, 숫자를 혼합하여 입력해주세요.');
			return; // 유효성 검사 실패 시 AJAX 요청 중단
		}
	}

	const data = {
		username: username,
		userpw: userpw ? userpw : null
	};

	$.ajax({

		type: 'PATCH',
		url: '/updateInfo',
		contentType: 'application/json',
		data: JSON.stringify(data),
		headers: getAuthHeader(), // 인증 헤더 가져오기

		success: function (ee) {
			alert('회원 정보 수정 성공!');
			location.href = '/';
		},

		error: function (xhr) {
			alert(xhr.responseJSON.errorMessage);

		}

	})
})
