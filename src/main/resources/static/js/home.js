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

    // 주소 검색이 된 상태면 주변 탐색 메뉴를 띄우면 안되기 때문에 추가
    if($('#infoSearch').hasClass('OFF') === true) {
        $('#infoMain').addClass('ACTIVE').removeClass('HIDDEN');
    } else if ($('#infoSearch').hasClass('ON') === true) {
        $('#infoMain').addClass('HIDDEN').removeClass('ACTIVE');
    }

})


// 길찾기 버튼
infoDirectionBtn.on('click', function () {
    $('#infoMain').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoSearch').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoDirection').addClass('ACTIVE').removeClass('HIDDEN');
    $('#infoMy').addClass('HIDDEN').removeClass('ACTIVE');
})

// 나의 메뉴 버튼
infoMyBtn.on('click', function () {
    $('#infoMain').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoSearch').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoDirection').addClass('HIDDEN').removeClass('ACTIVE');
    $('#infoMy').addClass('ACTIVE').removeClass('HIDDEN');
})




function dircetion() {

    $.ajax({
        type: 'GET',
        url: 'https://apis-navi.kakaomobility.com/v1/directions?origin=127.11015314141542,37.39472714688412&destination=127.10824367964793,37.401937080111644',
        contentType: 'application/json',
        headers: {
            "Authorization": "KakaoAK dd93827481a2d3acf5fdb3b0521d48a2"
        },
        success: function (data) {
            alert(data.routes[0].result_msg);
            console.log(data.routes);

        },
        error: function (xhr) {
            alert('실패 !' + xhr)
            console.log(xhr);
        }

    })




    
}