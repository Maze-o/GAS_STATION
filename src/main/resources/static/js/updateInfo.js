
$('#updateInfoForm').on('submit', function(event) {
	//event.preventDefault(); // 기본 폼 제출 방지
	//handleUpdateInfo($(this).serialize()); // 폼데이터 직렬화 후 처리

	console.log('버튼 호출')
	const username = $('#inputUsername').val();
	const userpw = $('inputPassword').val();
	const token = localStorage.getItem('token'); // JWT 토큰 가져오기

	// 회원정보 수정 ajax

	const data = {
		username: username,
		userpw: userpw ? userpw : null
	};
	console.log(JSON.stringify(data));

	$.ajax({

		type: 'PATCH',
		url: '/updateInfo',
		contentType: 'application/json',
		data: JSON.stringify(data),
		headers: {
			'Authorization': 'Bearer ' + localStorage.getItem('token')
		},


		success: function(ee) {
			alert('회원 정보 수정 성공!', ee);
			location.href = '/';
		},

		error: function(xhr) {
			alert('실패!', xhr)
			console.log(xhr);
		}

	})
})
