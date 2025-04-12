

// 버튼 상태 업데이트를 위한 함수
$(document).ready(function () {
	$('#userPw').on('input', function () {
		const inputVal = $(this).val();

		// 비밀번호 입력 칸이 비어있다면
		if (inputVal.trim() !== "") {
			$('#pwChkBtn').removeClass('btn-disabled').addClass('btn-active').removeAttr('disabled');
		} else {
			$('#pwChkBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);
		}

	})
})


$('#pwChkForm').on('submit', async function (e) {
	e.preventDefault(); // 기본 폼 제출 방지

	const userpw = {
		userpw: $('#userPw').val()
	}

	// 토큰 유효성 체크 및 갱신
	const token = await checkAndRefreshToken();
	if (!token) return; // 토큰 갱신 실패 시 종료

	$.ajax({
		url: "/checkpw",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify(userpw),
		headers: {
			'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
		},

		success: function (response) {

			alert('인증 성공 !');
			location.href = '/updateInfo';
		},
		error: function (xhr) {
			console.log(xhr)
			const errorResponse = xhr.responseJSON;
			console.log(errorResponse);
        
			// errorResponse가 정의되어 있으면 사용자에게 오류 메시지를 보여줍니다.
			if (errorResponse) {
				alert(errorResponse.errorMessage);
			} else {
				alert('알 수 없는 오류가 발생했습니다.');
			}
		}

	})

})