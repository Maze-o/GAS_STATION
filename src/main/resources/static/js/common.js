
$(document).ready(function () {
    const token = localStorage.getItem('token');

    // 토큰 정보가 있다면 닉네임 표시, 회원정보 수정 버튼 표시
    if (token) {
        const decodeToken = parseJwt(token);
        console.log('decodeToken : ' ,decodeToken.sub);
        $('#statusMessage').text(`안녕하세요 ${decodeToken.sub}님!`);
        // 회원정보수정 버튼
        $('#updateInfoBtn').show();
        $('#inputNameValue').val(decodeToken.sub);
    // 토큰 정보 없을 때
    } else {
        $('#statusMessage').text('로그인 해주세요');
        // 회원정보수정 버튼
        $('#updateInfoBtn').hide();
    }
    // 토큰 디코드 해서 username추출
    function parseJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    }

})