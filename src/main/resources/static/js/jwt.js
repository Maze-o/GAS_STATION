// 토큰 확인 및 갱신 함수
async function checkAndRefreshToken() {
    const token = localStorage.getItem('accessToken');
    if (!token || isTokenExpired(token)) {
        // 토큰 갱신 요청
        const newToken = await refreshToken();
        if (newToken) {
            console.log('새로운 토큰 저장 완료')
            localStorage.setItem('accessToken', newToken); // 새로운 토큰 저장
            return newToken; // 새로운 토큰 반환
        } else {
            alert('사용자 정보가 만료되었습니다. 다시 로그인 해주세요.');
			localStorage.removeItem('accessToken');
			localStorage.removeItem('refreshToken');
            location.href = '/login'; // 로그인 페이지로 리디렉션
            return null; // 갱신 실패 시 null 반환
        }
    }
    return token; // 유효한 토큰 반환
}

// 인증 헤더 생성 함수
function getAuthHeader() {
    const token = localStorage.getItem('refreshToken');
    return {
        'Authorization': 'Bearer ' + token // Bearer 토큰 형태로 헤더 생성
    };
}

// 유효성 체크 함수
function isTokenExpired(token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp * 1000; // 만료 시간
    return Date.now() > exp; // 현재 시간과 비교
}

// 토큰 유효성 체크 및 갱신 요청 함수
async function refreshToken() {
    console.log('refreshToken함수 호출')
    try {
        console.log('refreshToken ajax 함수 호출')
        const response = await $.ajax({
            url: '/auth/refresh', // 토큰 갱신 엔드포인트
            type: 'POST',
            headers: getAuthHeader(),
        });
        console.log('repsonse : ', response)
        return response.accessToken; // 새로운 토큰 반환
    } catch (error) {
        console.error('토큰 갱신 실패:', error);
        return null; // 갱신 실패 시 null 반환
    }
}
