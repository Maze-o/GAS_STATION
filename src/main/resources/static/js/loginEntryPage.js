
const passwordToggleIcon = $('.eye-icon');
const passwordInput = $('#userPw');

// 비밀번호 표시 / 숨김 기능
passwordToggleIcon.on('click', function () {
    if (passwordInput.prop('type') === 'password') {
        passwordToggleIcon.text('visibility_off');
        passwordInput.prop('type', 'text');
    } else {
        passwordToggleIcon.text('visibility');
        passwordInput.prop('type', 'password');
    }
})


// 버튼 상태 업데이트를 위한 함수
$(document).ready(function () {
	$('#userId, #userPw').on('input', function () {
		const inputIdVal = $('#userId').val();
        const inputPwVal = $('#userPw').val();

		// 입력 칸이 비어있다면
		if (inputIdVal.trim() !== "" && inputPwVal.trim() !== "") {
			$('#loginBtn').removeClass('btn-disabled').addClass('btn-active').removeAttr('disabled');
		} else {
			$('#loginBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);
		}

	})
})
