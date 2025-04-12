
// 버튼 상태 업데이트를 위한 함수
$(document).ready(function () {
	$('#userId, #userName').on('input', function () {
		const inputIdVal = $('#userId').val();
        const inputNameVal = $('#userName').val();

		// 입력 칸이 비어있다면
		if (inputIdVal.trim() !== "" && inputNameVal.trim() !== "") {
			$('#findPwBtn').removeClass('btn-disabled').addClass('btn-active').removeAttr('disabled');
		} else {
			$('#findPwBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);
		}

	})
})

$('#findpwForm').on('submit', function(event) {
	console.log('js submit 발동');
	console.log('event : ', event);
	event.preventDefault(); // 기본 폼 제출 방지
	alert('비밀번호 찾기 버튼 누름');
	findPw(); // 비밀번호 찾기 처리
});


// 비밀번호 찾기 함수
function findPw() {
	const userId = $('#userId').val();
	const userName = $('#userName').val();

	const data = {
		userid: userId,
		username: userName,
	};


	$.ajax({
		url: '/findpw',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(data),
		
		success: function(response) {
			alert(`임시 비밀번호는 ${response.userpw} 입니다 비밀번호를 변경하여 사용하세요`);
			location.href='/login?step=PASSWORD_ENTRY';
		},
		error: function(xhr) {
			alert('사용자 정보가 일치하지 않습니다!!');
		}
	});
}