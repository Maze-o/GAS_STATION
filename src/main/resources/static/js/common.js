
// 모든 페이지 로딩 시 체크
$(document).ready(function() {

	const refreshToken = localStorage.getItem('refreshToken');
	const accessToken = localStorage.getItem('accessToken');
	
	$('#updateInfoBtn').hide();

	// refreshToken이 존재하는지 확인
	if (refreshToken) {
		const decodeToken = parseJwt(refreshToken);

		// 현재 시간
		const currentTime = Math.floor(Date.now() / 1000); // 초 단위로 현재 시간

		// refreshToken 만료 확인
		if (decodeToken.exp < currentTime) {
			// refreshToken이 만료된 경우	
			alert('로그인 정보가 만료되었습니다. 로그아웃 되었습니다.');
			// 로컬 스토리지에서 토큰 삭제
			localStorage.removeItem('accessToken');
			localStorage.removeItem('refreshToken');

			$('#statusMessage').text('로그인 해주세요'); // 로그인이 필요하다는 메시지 표시
			$('#updateInfoBtn').hide(); // 버튼 숨기기
			updateUIBasedOnToken();
			location.reload();
			return; // 더 이상 진행하지 않음
		}

		// 토큰 정보가 있다면 닉네임 표시, 회원정보 수정 버튼 표시
		$('#statusMessage').text(`안녕하세요 ${decodeToken.username}님!`);
		// 회원정보수정 버튼 표시
		$('#inputNameValue').val(decodeToken.username);
		console.log('decodetoken : ', decodeToken);
		// 일반 로그인이면 회원정보수정 버튼 표시
		if (decodeToken.isLogined) {
			$('#updateInfoBtn').show();
		}

	// 토큰 정보 없을 때
	} else {
		$('#statusMessage').text('로그인 해주세요');
		$('#updateInfoBtn').hide();
	}
	// 토큰 디코드
	function parseJwt(token) {
		try {
			const base64Url = token.split('.')[1];
			const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
			const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
				return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
			}).join(''));
			return JSON.parse(jsonPayload);
		} catch (e) {
			alert('에러!');
			return null; // 토큰이 유효하지 않으면 null 반환
		}
	}
})