
// $('#updateInfoForm').on('submit', function(event) {
// 	event.preventDefault(); // 기본 폼 제출 방지

// 	const username = $('#inputUsername').val();
// 	const userpw = $('#inputPassword').val();
// 	const pwchk = $('#inputPasswordChk').val();
// 	if (username) {
// 		if (username.trim().length < 3 || username.trim().length > 10) {
// 			alert('닉네임은 3자리 ~ 10자리 이내로 입력해 주세요')
// 			return;
// 		}

// 		const invalidCharacters = /[^a-zA-Z0-9가-힣]/; // 한글, 영문자, 숫자가 아닌 문자
// 		if (invalidCharacters.test(username.trim())) {
// 			alert('닉네임은 한글, 영문자, 숫자만 사용할 수 있습니다.');
// 			return;
// 		}
// 	}

// 	// 비밀번호 유효성 검사
// 	if (userpw) {
// 		// 비밀번호 길이 검사
// 		if (userpw.trim().length < 6 || userpw.trim().length > 20) {
// 			alert('비밀번호는 6자리 ~ 20자리 이내로 입력해주세요');
// 			return; // 유효성 검사 실패 시 AJAX 요청 중단
// 		}

// 		// 비밀번호에 영문자와 숫자가 모두 포함되어 있는지 검사
// 		const hasLowerCase = /[a-z]/.test(userpw);
// 		const hasDigits = /\d/.test(userpw);

// 		if (!(hasLowerCase.trim() && hasDigits.trim())) {
// 			alert('비밀번호는 영문, 숫자를 혼합하여 입력해주세요.');
// 			return; // 유효성 검사 실패 시 AJAX 요청 중단
// 		}

// 		if (pwchk != userpw) {
// 			alert('비밀번호를 확인해 주세요.');
// 			return;
// 		}
// 	}

// 	const data = {
// 		username: username ? username : null,
// 		userpw: userpw ? userpw : null
// 	};

// 	$.ajax({

// 		type: 'PATCH',
// 		url: '/updateInfo',
// 		contentType: 'application/json',
// 		data: JSON.stringify(data),
// 		headers: getAuthHeader(), // 인증 헤더 가져오기

// 		success: function(ee) {
// 			alert('회원 정보 수정 성공!');
// 			location.href = '/';
// 		},

// 		error: function(xhr) {
// 			alert(xhr.responseJSON.errorMessage);

// 		}

// 	})
// })








$(document).ready(function () {
	  $('#updateInfoBtn').css('display', 'block');
    // 회원 정보 수정 처리
    $('#updateInfoForm').on('submit', function (event) {
        event.preventDefault(); // 기본 폼 제출 방지
        handleUpdateInfo(); // 폼 데이터 직렬화 후 회원 정보 수정 처리
    });

    // 실시간 유효성 검사
    $('#inputUsername, #inputPassword, #inputPasswordChk').on('input', function () {
        validateField($(this)); // 필드 유효성 검사
        updateUpdateButtonState(); // 버튼 상태 업데이트
    });
});

function getAuthHeader() {
    const accessToken = localStorage.getItem('accessToken'); // access token만 뽑아옴
    if (accessToken) {
        return { 'Authorization': 'Bearer ' + accessToken };
    }
    return {};
}

// 회원 정보 수정 처리 함수
function handleUpdateInfo() {
    const username = $('#inputUsername').val();
    const userpw = $('#inputPassword').val();

    const data = {
        username: username ? username : null,
        userpw: userpw ? userpw : null
    };

    $.ajax({
        url: '/updateInfo', // 회원 정보 수정 요청 URL
        type: 'PATCH', // HTTP 메서드
        contentType: 'application/json',
        data: JSON.stringify(data), // JSON 데이터 전송
        headers: getAuthHeader(),
        success: function () {
            alert('회원 정보 수정 성공!');
            location.href = "/"; // 메인 페이지로 리다이렉트
        },
        
        error: function (xhr) {
            alert('회원 정보 수정 실패: ' + xhr.responseText); // 에러 처리
        }
    });
}

// 버튼 상태 업데이트 함수
function updateUpdateButtonState() {
    const usernameVal = $('#inputUsername').val();
    const passwordVal = $('#inputPassword').val();
    const passwordChkVal = $('#inputPasswordChk').val();

    const isUsernameValid = usernameVal ? validateField($('#inputUsername')) : false;
    const isPasswordValid = passwordVal ? validateField($('#inputPassword')) : false;
    const isPasswordChkValid = passwordChkVal ? validateField($('#inputPasswordChk')) : false;

    let canEnable = false;

    // 조건 1: 닉네임만 유효하면 OK
    if (usernameVal && isUsernameValid && !passwordVal && !passwordChkVal) {
        canEnable = true;
    }

    // 조건 2: 비밀번호 & 확인만 유효하면 OK
    else if (!usernameVal && passwordVal && passwordChkVal && isPasswordValid && isPasswordChkValid) {
        canEnable = true;
    }

    // 조건 3: 전부 입력했고 유효하면 OK
    else if (usernameVal && isUsernameValid && passwordVal && passwordChkVal && isPasswordValid && isPasswordChkValid) {
        canEnable = true;
    }

    // 버튼 상태 설정
    if (canEnable) {
        $('#updateInfoBtn').removeClass('btn-disabled').addClass('btn-active').removeAttr('disabled');
    } else {
        $('#updateInfoBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);
    }
}


// 유효성 검사 및 에러 메시지 추가 함수
function validateField($field) { 

    const value = $field.val().trim();	
    let isValid = true;
    clearErrorMessages($field);

    const invalidCharacters = /[^a-zA-Z0-9가-힣]/; // 한글, 영문자, 숫자가 아닌 문자

    const rules = {
        inputUsername: {
            messages: {
                length: '닉네임은 3자리 ~ 10자리 이내로 입력해 주세요.',
                format: '닉네임은 한글, 영문자, 숫자만 사용할 수 있습니다.',
            },
            test: () => {
                if (value.length < 3 || value.length > 10) {
                    addErrorMessage($field, rules.inputUsername.messages.length);
                    isValid = false;
                }
                if (invalidCharacters.test(value)) {
                    addErrorMessage($field, rules.inputUsername.messages.format);
                    isValid = false;
                }
                return isValid; // 모든 검사 통과 여부 반환
            }
        },
        inputPassword: {
            messages: {
                length: '비밀번호는 6자리 ~ 20자리 이내로 입력해주세요.',
                format: '비밀번호는 영문, 숫자를 혼합하여 입력해주세요.',
            },
            test: () => {
                if (value.length < 6 || value.length > 20) {
                    addErrorMessage($field, rules.inputPassword.messages.length);
                    isValid = false;
                }
                if (!/[0-9]/.test(value) || !/[a-zA-Z]/.test(value)) {
                    addErrorMessage($field, rules.inputPassword.messages.format);
                    isValid = false;
                }
                return isValid;
            }
        },
        inputPasswordChk: {
            message: '비밀번호를 확인해 주세요.',
            test: () => {
                if (value !== $('#inputPassword').val()) {
                    addErrorMessage($field, rules.inputPasswordChk.message);
                    return false;
                }
                return true;
            }
        }
    };

    const fieldName = $field.attr('id');
    if (rules[fieldName]) {
        if (value === "") {
            return true; // 빈칸은 유효성 검사 통과로 처리
        } else if (!rules[fieldName].test()) {
            isValid = false; // 유효성 검사 실패
        }
    }

    return isValid; // 유효성 검사 결과 반환
}

// 에러 메시지 추가 함수
function addErrorMessage($field, message) {
    clearErrorMessages($field); // 기존 에러 메시지 제거
    $field.after(`<p class="error-message">${message}</p>`); // 에러 메시지 추가
}

// 에러 메시지 제거 함수
function clearErrorMessages($field) {
    $field.next('.error-message').remove(); // 해당 입력 필드 아래의 모든 에러 메시지 제거
}
