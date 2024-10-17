
// 함수를 사용해 로직을 보기좋게 만듬
$(document).ready(function() {
	console.log('현재 URL 경로:', window.location.pathname);
	// 페이지 로드 시 토큰 확인
	updateUIBasedOnToken();
	// 로그인 처리
	$('#loginForm').on('submit', function(event) {
		alert('로그인버튼눌렀다.');
		event.preventDefault(); // 기본 폼 제출 방지
		handleLogin($(this).serialize()); // 폼 데이터 직렬화 후 로그인 처리
	});

	// 회원가입 처리
	$('#signupForm').on('submit', function(event) {
		alert('회원가입버튼눌렀다.')
		event.preventDefault(); // 기본 폼 제출 방지
		handleSignup($(this).serialize()); // 폼 데이터 직렬화 후 회원가입 처리
	});

	// 로그아웃 처리
	$('#logOutBtn').on('click', function() {
		handleLogout(); // 로그아웃 처리
	});

	// OAuth2 로그인 처리

	$('#googleLoginBtn').on('click', function() {
		alert('Google 버튼 눌렀다.');
		document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
		// 로컬 스토리지에서 토큰 제거
		localStorage.removeItem('token');

		window.location.href = '/oauth2/authorization/google';
	});

	$('#naverLoginBtn').on('click', function() {
		alert('naver 버튼 눌렀다.');
		document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
		// 로컬 스토리지에서 토큰 제거
		localStorage.removeItem('token');

		window.location.href = '/oauth2/authorization/naver';
	});

	$('#loginEntryPagebtn').on('click', function () {
		//로그인 페이지로 이동
		window.location.href = '/login?step=PASSWORD_ENTRY'; 
	})
	// 페이지 로드 시 OAuth2 로그인 성공 처리 (리다이렉트된 경우)
	const urlParams = new URLSearchParams(window.location.search); // 쿼리 스트링을 가져옴
	const oauthSuccess = urlParams.get('oauth_success'); // 쿼리 스트링에서 oauth_success 파라미터 확인

	if (oauthSuccess) { // oauth_success가 있을 때만 호출
		const username = getCookie('username');

		const jwtToken = getCookie('JWT'); // JWT 쿠키 가져오기
		if (jwtToken) {
			localStorage.setItem('token', jwtToken); // 로컬 스토리지에 토큰 저장
			// 추가적인 UI 업데이트 로직 호출 가능
			// 이후에는 쿠키를 삭제
			//document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // 쿠키 삭제
			//window.location.href = "/";
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


	/*	// 페이지 로드 시 OAuth2 로그인 성공 처리 (리다이렉트된 경우)
		if (window.location.pathname === '/login/oauth2/success') {
			alert('oauth2login redirect 성공');
			handleOAuth2Login(); // 이 함수를 호출하여 토큰을 받는 AJAX 요청을 보냄
		}*/

	// oauth2 로그인 실패 시
	const error = urlParams.get('error'); // 따온 쿼리스트링에 error값이 있으면

	if (error) { // true로 if문 실행
		alert('OAuth2 로그인 실패: ' + error); // 실패 메시지 표시
		location.href = "/login"; // 로그인 페이지로 리다이렉트
	}

	/*	const urlParams = new URLSearchParams(window.location.search);
		const error = urlParams.get('error'); // 따온 쿼리스트링에 error값이 있으면
	
		if (error) {
			alert('OAuth2 로그인 실패: ' + error);
			// failureHandler로 직접 리다이렉트
			window.location.href = "/signin/oauth2/failure?error=" + encodeURIComponent(error);
		}*/
});

// 토큰에 따라 UI 업데이트
function updateUIBasedOnToken() {
	const hasToken = !!localStorage.getItem('token');
	$('#loginBtn, #signinBtn').toggle(!hasToken); // 로그인 버튼 보이기/숨기기
	$('#logOutBtn').toggle(hasToken); // 로그아웃 버튼 보이기/숨기기
}

// 로그인 처리 함수
function handleLogin(data) {
	$.ajax({
		url: '/login/authenticate', // 로그인 요청 URL
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
		success: function() {
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
	document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // jwt 쿠키 삭제
	document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // username 쿠키 삭제
	updateUIBasedOnToken(); // UI 업데이트
	location.href = "/"; // 로그아웃 후 로그인 페이지로 리다이렉트
}