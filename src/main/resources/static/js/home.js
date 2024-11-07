// 페이지 로드 시 오프캔버스 바로 표시
$(document).ready(function () {
    $('#offcanvasScrolling').addClass('show');
    $('#offcanvasScrolling').css('visibility', 'visible');
});

// 오프캔버스 닫기 
$('#offcanvasOffBtn').on('click', function () {
    $('#offcanvasScrolling').css('visibility', 'hidden');

})

// 오프캔버스의 버튼을 눌러서 메뉴 스왑
let infoSearchBtn = $('#infoSearchBtn');
let infoDirectionBtn = $('#infoDirectionBtn');
let infoMyBtn = $('#infoMyBtn');

// 검색 버튼
infoSearchBtn.on('click', function () {
    $('#infoSearch').addClass('ACTIVE').removeClass('HIDDEN');
    $('#infoDirection').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoMy').addClass('HIDDEN').removeClass('ACTIVE');
})


// 길찾기 버튼
infoDirectionBtn.on('click', function () {
    $('#infoSearch').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoDirection').addClass('ACTIVE').removeClass('HIDDEN');
    $('#infoMy').addClass('HIDDEN').removeClass('ACTIVE');
})

// 나의 메뉴 버튼
infoMyBtn.on('click', function () {
    $('#infoSearch').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoDirection').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoMy').addClass('ACTIVE').removeClass('HIDDEN');
})



