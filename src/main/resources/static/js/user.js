
// 함수를 사용해 로직을 보기좋게 만듬
$(document).ready(function() {
	console.log('현재 URL 경로:', window.location.pathname);
	// 페이지 로드 시 토큰 확인
	updateUIBasedOnToken();
	// 로그인 처리
	$('#loginForm').on('submit', function(event) {
		event.preventDefault(); // 기본 폼 제출 방지
		handleLogin($(this).serialize()); // 폼 데이터 직렬화 후 로그인 처리
	});

	// 로그아웃 처리
	$('#logOutBtn').on('click', function() {
		handleLogout(); // 로그아웃 처리
	});

	// OAuth2 로그인 처리
	$('#googleLoginBtn').on('click', function() {
		// 로컬 스토리지에서 토큰 제거
		localStorage.removeItem('refreshToken');
		localStorage.removeItem('accessToken');

		window.location.href = '/oauth2/authorization/google';
	});

	$('#naverLoginBtn').on('click', function() {
		// 로컬 스토리지에서 토큰 제거
		localStorage.removeItem('refreshToken');
		localStorage.removeItem('accessToken');

		window.location.href = '/oauth2/authorization/naver';
	});

	$('#loginEntryPageBtn').on('click', function() {
		//로그인 페이지로 이동
		window.location.href = '/login?step=PASSWORD_ENTRY';
	})
	// 페이지 로드 시 OAuth2 로그인 성공 처리 (리다이렉트된 경우)
	const urlParams = new URLSearchParams(window.location.search); // 쿼리 스트링을 가져옴
	const oauthSuccess = urlParams.get('oauth_success'); // 쿼리 스트링에서 oauth_success 파라미터 확인

	if (oauthSuccess) { // oauth_success가 있을 때만 호출
		const username = getCookie('username');

		const accessToken = getCookie('accessToken'); // JWT 쿠키 가져오기
		const refreshToken = getCookie('refreshToken'); // JWT 쿠키 가져오기
		if (accessToken) {
			localStorage.setItem('accessToken', accessToken); // 로컬 스토리지에 토큰 저장
			localStorage.setItem('refreshToken', refreshToken);
			// 쿠키 삭제
			document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
			document.cookie = "refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
			document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제

			alert(`소셜로그인 성공 ${username}님 환영합니다!`); // 로그인 성공 알림
			window.location.href = window.location.pathname; // 현재 경로로 이동 (쿼리 파라미터 없이)

		} else {
			alert('비정상적인 접근입니다!!');
			window.location.href = 'error';
		}
	}
	// 쿠키를 가져오는 함수
	function getCookie(name) {
		const value = `; ${document.cookie}`;
		const parts = value.split(`; ${name}=`);

		if (parts.length === 2) {
			return parts.pop().split(';').shift();
		}
	}


	// oauth2 로그인 실패 시
	const error = urlParams.get('error'); // 따온 쿼리스트링에 error값이 있으면

	if (error) { // true로 if문 실행
		alert('소셜 로그인 실패: ' + error); // 실패 메시지 표시
		location.href = "/login"; // 로그인 페이지로 리다이렉트
	}


});

// 토큰에 따라 UI 업데이트
function updateUIBasedOnToken() {
	const hasAccessToken = !!localStorage.getItem('accessToken');
	$('#loginBtn, #signinBtn').toggle(!hasAccessToken); // 로그인 버튼 보이기/숨기기
	$('#logOutBtn').toggle(hasAccessToken); // 로그아웃 버튼 보이기/숨기기

	const hasRefreshToken = !!localStorage.getItem('refreshToken');
	$('#loginBtn, #signinBtn').toggle(!hasRefreshToken); // 로그인 버튼 보이기/숨기기
	$('#logOutBtn').toggle(hasRefreshToken); // 로그아웃 버튼 보이기/숨기기
}

// 로그인 처리 함수
function handleLogin(data) {
	// 체크박스 상태 확인
	const rememberMe = $('#rememberMe').is(':checked');

	// data에 rememberMe 추가
	data += `&rememberMe=${rememberMe}`; // 직렬화된 데이터에 rememberMe 추가

	$.ajax({
		url: '/login/authenticate', // 로그인 요청 URL
		type: 'POST', // HTTP 메서드
		data: data, // 폼 데이터
		dataType: 'json', // 응답 데이터 타입 명시
		success: function(response) {
			localStorage.setItem('accessToken', response.accessToken); // 로컬스토리지에 토큰 저장
			localStorage.setItem('refreshToken', response.refreshToken);

			alert(response.username + '님 환영합니다!');
			location.href = "/"; // 메인 페이지로 리다이렉트
		},
		error: function(xhr) {
			alert(xhr.responseText); // 에러 처리
		}
	});
}


// 로그아웃 처리 함수
function handleLogout() {
	// 로컬 스토리지에서 토큰 제거
	localStorage.removeItem('accessToken');
	localStorage.removeItem('refreshToken');
	// 쿠키 삭제
	document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
	document.cookie = "refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
	document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
	updateUIBasedOnToken(); // UI 업데이트
	location.href = "/"; // 로그아웃 후 메인 페이지로 리다이렉트
}