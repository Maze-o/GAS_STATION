
// 함수를 사용해 로직을 보기좋게 만듬
$(document).ready(function() {
    // 페이지 로드 시 토큰 확인
    updateUIBasedOnToken();

    // 로그인 처리
    $('#loginForm').on('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지
        handleLogin($(this).serialize()); // 폼 데이터 직렬화 후 로그인 처리
    });

    // 회원가입 처리
    $('#signupForm').on('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지
        handleSignup($(this).serialize()); // 폼 데이터 직렬화 후 회원가입 처리
    });

    // 로그아웃 처리
    $('#logOutBtn').on('click', function() {
        handleLogout(); // 로그아웃 처리
    });
});

// 토큰에 따라 UI 업데이트
function updateUIBasedOnToken() {
    if (localStorage.getItem('token')) {
        $('#loginBtn').hide(); // 토큰이 있으면 로그인 버튼 숨김
        $('#signinBtn').hide();
        $('#logOutBtn').show(); // 로그아웃 버튼 활성화
    } else {
        $('#loginBtn').show(); // 토큰이 없으면 로그인 버튼 보임
        $('#signinBtn').show();
        $('#logOutBtn').hide(); // 로그아웃 버튼 숨김
    }
}

// 로그인 처리 함수
function handleLogin(data) {
    $.ajax({
        url: '/signin', // 로그인 요청 URL
        type: 'POST', // HTTP 메서드
        data: data, // 폼 데이터
        dataType: 'json', // 응답 데이터 타입 명시
        success: function(response) {
            localStorage.setItem('token', response.token); // 로컬스토리지에 토큰 저장
			
            alert(response.username + '님 환영합니다!');
            location.href = "/"; // 메인 페이지로 리다이렉트
        },
        error: function(xhr) {
            alert(xhr.responseText); // 에러 처리
        }
    });
}

// 회원가입 처리 함수
function handleSignup(data) {
    $.ajax({
        url: '/signup', // 회원가입 요청 URL
        type: 'POST', // HTTP 메서드
        data: data, // 폼 데이터
        success: function(response) {
            alert('회원가입 성공!');
            location.href = "/"; // 메인 페이지로 리다이렉트
        },
        error: function(xhr) {
            alert('회원가입 실패: ' + xhr.responseText); // 에러 처리
        }
    });
}

// 로그아웃 처리 함수
function handleLogout() {
    // 로컬 스토리지에서 토큰 제거
    localStorage.removeItem('token');
    updateUIBasedOnToken(); // UI 업데이트
    location.href = "/"; // 로그아웃 후 로그인 페이지로 리다이렉트
}