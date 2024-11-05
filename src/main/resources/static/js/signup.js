
$(document).ready(function () {
    // 회원가입 처리
    $('#signupForm').on('submit', function (event) {
        event.preventDefault(); // 기본 폼 제출 방지
        handleSignup($(this).serialize()); // 폼 데이터 직렬화 후 회원가입 처리
    });

    // 실시간 유효성 검사
    $('#userId, #userPw, #confirmPw, #userName').on('input', function () {
        validateField($(this));
        updateSignupButtonState(); // 필드 검증 후 버튼 상태 업데이트


    });

});

// 회원가입 처리 함수
function handleSignup(data) {
    $.ajax({
        url: '/signup', // 회원가입 요청 URL
        type: 'POST', // HTTP 메서드
        data: data, // 폼 데이터
        success: function () {
            alert('회원가입 성공!');
            location.href = "/"; // 메인 페이지로 리다이렉트
        },
        error: function (xhr) {
            alert('회원가입 실패: ' + xhr.responseText); // 에러 처리
        }
    });
}


// 버튼 상태 업데이트 함수
function updateSignupButtonState() {
    const isUserIdValid = validateField($('#userId'));
    const isPasswordValid = validateField($('#userPw'));
    const isConfirmPasswordValid = validateField($('#confirmPw'));
    const isUserNameValid = validateField($('#userName'));

    
    // 모든 유효성이 통과했을 때 버튼 활성화
    if (isUserIdValid && isPasswordValid && isConfirmPasswordValid && isUserNameValid) {
        $('#signUpBtn').removeClass('btn-disabled').addClass('btn-active').removeAttr('disabled');
    } else {
        $('#signUpBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);        
    }

    // 각 필드가 비어 있는지 체크하여 버튼 비활성화
    if (!$('#userId').val() || !$('#userPw').val() || !$('#confirmPw').val() || !$('#userName').val()) {
        $('#signUpBtn').addClass('btn-disabled').removeClass('btn-active').attr('disabled', true);
    }
}


// 정규식 정의
const kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/; // 한글 포함 여부 체크
const num = /[0-9]/; // 숫자 포함 여부 체크
const eng = /[a-zA-Z]/; // 영문 포함 여부 체크
const invalidCharacters = /[^a-zA-Z0-9가-힣]/; // 한글, 영문자, 숫자가 아닌 문자

function validateField($field) { 
    const value = $field.val().trim();
    let isValid = true;
    clearErrorMessages($field);

    const rules = {
        userId: {
            messages: {
                length: '아이디는 4자리 ~ 12자리 이내로 입력해주세요.',
                format: '아이디는 영문과 숫자를 혼합하여 입력해주세요.',
            },
            test: () => {
                if (value.length < 4 || value.length > 12) {
                    addErrorMessage($field, rules.userId.messages.length);
                    isValid = false;
                }
                if (kor.test(value)) {
                    addErrorMessage($field, '아이디에 한글을 포함할 수 없습니다.');
                    isValid = false;
                }
                if (!num.test(value) || !eng.test(value)) {
                    addErrorMessage($field, rules.userId.messages.format);
                    isValid = false;
                }
                return isValid; // 모든 검사 통과 여부 반환
            }
        },
        userPw: {
            messages: {
                length: '비밀번호는 6자리 ~ 20자리 이내로 입력해주세요.',
                format: '비밀번호는 영문, 숫자를 혼합하여 입력해주세요.',
            },
            test: () => {
                if (value.length < 6 || value.length > 20) {
                    addErrorMessage($field, rules.userPw.messages.length);
                    isValid = false;
                }
                if (!num.test(value) || !eng.test(value)) {
                    addErrorMessage($field, rules.userPw.messages.format);
                    isValid = false;
                }
                return isValid;
            }
        },
        confirmPw: {
            message: '비밀번호를 확인해 주세요',
            test: () => {
                if (value !== $('#userPw').val()) {
                    addErrorMessage($field, rules.confirmPw.message);
                    return false;
                }
                return true;
            }
        },
        userName: {
            message: '이름은 3자리 ~ 10자리 이내로 입력해 주세요',
            format: '이름은 한글, 영문자, 숫자만 사용할 수 있습니다.',
            test: () => {
                if (value.length < 3 || value.length > 10) {
                    addErrorMessage($field, rules.userName.message);
                    return false;
                } if (invalidCharacters.test(value)) {
                    addErrorMessage($field, rules.userName.format);
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
