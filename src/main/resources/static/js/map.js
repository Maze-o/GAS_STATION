
// -------------------------------지도----------------------------------

// initMap 함수를 실행하여 초기 위치를 설정합니다
initMap();

let overlayOn = false; // 지도 위에 로드뷰 오버레이가 추가된 상태를 가지고 있을 변수
let container = document.getElementById('container'); // 지도와 로드뷰를 감싸고 있는 div 입니다
let mapWrapper = document.getElementById('mapWrapper'); // 지도를 감싸고 있는 div 입니다
let mapContainer = document.getElementById('map'); // 지도를 표시할 div 입니다 
let rvContainer = document.getElementById('roadview'); //로드뷰를 표시할 div 입니다


let mapOption = {
	center: new kakao.maps.LatLng(33.450701, 126.570667), // 기본 중심 좌표 (예: 제주도)
	level: 4 // 기본 확대 레벨
};


// 마커가 표시될 위치입니다 
let mapCenter = new kakao.maps.LatLng(33.450701, 126.570667);
// 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
let map = new kakao.maps.Map(mapContainer, mapOption);
let geocoder = new kakao.maps.services.Geocoder(); // 주소-좌표 변환 객체 생성



// 로드뷰 객체를 생성합니다 
let rv = new kakao.maps.Roadview(rvContainer);

// 좌표로부터 로드뷰 파노라마 ID를 가져올 로드뷰 클라이언트 객체를 생성합니다 
let rvClient = new kakao.maps.RoadviewClient();

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 정보창 생성
var infoWindow = new kakao.maps.InfoWindow({ zIndex: 1 });


// 마커 이미지를 생성합니다
let markImage = new kakao.maps.MarkerImage(
	'https://t1.daumcdn.net/localimg/localimages/07/2018/pc/roadview_minimap_wk_2018.png',
	new kakao.maps.Size(26, 46),
	{
		// 스프라이트 이미지를 사용합니다.
		// 스프라이트 이미지 전체의 크기를 지정하고
		spriteSize: new kakao.maps.Size(1666, 168),
		// 사용하고 싶은 영역의 좌상단 좌표를 입력합니다.
		// background-position으로 지정하는 값이며 부호는 반대입니다.
		spriteOrigin: new kakao.maps.Point(705, 114),
		offset: new kakao.maps.Point(13, 46)
	}
);

// 드래그가 가능한 마커를 생성합니다
let marker = new kakao.maps.Marker({
	position: mapCenter,
	// draggable: true
});

// 장소 검색 
function placeSearchPlaces() {


	var keyword = document.getElementById('keyword').value;

	if (!keyword.replace(/^\s+|\s+$/g, '')) {
		alert('키워드를 입력해주세요!');
		return false;
	}



	// 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
	ps.keywordSearch(keyword, placesSearch);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearch(data, status, pagination) {

	if (status === kakao.maps.services.Status.OK) {
		// 정상적으로 검색이 완료됐으면
		// 검색 목록과 마커를 표출합니다
		searchDisplayPlaces(data);

		// 페이지 번호를 표출합니다
		displayPagination(pagination);
		$('#infoSearch').addClass('ACTIVE').removeClass('HIDDEN');
		$('#infoDirection').addClass('HIDDEN').removeClass('ACTIVE');
		$('#infoMy').addClass('HIDDEN').removeClass('ACTIVE');

	} else if (status === kakao.maps.services.Status.ZERO_RESULT) {

		alert('검색 결과가 존재하지 않습니다.');
		return;

	} else if (status === kakao.maps.services.Status.ERROR) {

		alert('검색 결과 중 오류가 발생했습니다.');
		return;

	}


}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function searchDisplayPlaces(places) {
	// 목록을 표시 할 때 메인 내용 감추고 검색 결과만 표시
	$('#infoMain').addClass('HIDDEN').removeClass('ACTIVE');
	$('#infoSearch').addClass('ACTIVE').removeClass('HIDDEN');


	var listEl = document.getElementById('placesList'),
		menuEl = document.getElementById('menu_wrap'),
		fragment = document.createDocumentFragment(),
		bounds = new kakao.maps.LatLngBounds(),
		listStr = '';


	// 검색 결과 목록에 추가된 항목들을 제거합니다
	removeAllChildNods(listEl);

	// 지도에 표시되고 있는 마커를 제거합니다
	removeMarker();

	for (var i = 0; i < places.length; i++) {
		let xyCoordinate = [
			{name: places[i].place_name, x: places[i].x, y: places[i].y},

		]
		// 마커를 생성하고 지도에 표시합니다
		var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
			marker = addMarker(placePosition, i),
			itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

		// 검색된 장소 위치를 기준으로 지도 범위를 설정
		bounds.extend(placePosition);

		// 마커와 검색결과 항목에 mouseover 했을때
		// 해당 장소에 인포윈도우에 장소명을 표시합니다
		// mouseout 했을 때는 인포윈도우를 닫습니다
		(function (marker, title) {

			kakao.maps.event.addListener(marker, 'mouseover', function () {
				displayInfowindow(marker, title);
			});

			kakao.maps.event.addListener(marker, 'mouseout', function () {
				infoWindow.close();
			});

			itemEl.onclick = function () {
				// 클릭한 위치의 좌표 찾기
				var value = places.find(o => o.place_name === title);

				// 클릭한 위치의 좌표를 맵 중앙으로 설정
				var placeCenter = new kakao.maps.LatLng(value.y, value.x);
				map.setCenter(placeCenter);
				map.setLevel(4);
				displayInfowindow(marker, title);
			};

			itemEl.onmouseout = function () {
				infoWindow.close();
			};
		})(marker, places[i].place_name);

		fragment.appendChild(itemEl);
	}

	// 검색결과 항목들을 검색결과 목록 Element에 추가합니다
	listEl.appendChild(fragment);
	menuEl.scrollTop = 0;

	// 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
	map.setBounds(bounds);

	// 닫기 버튼 클릭 시 메인 메뉴 표시
	$('#searchCloseBtn').on('click', function() {
		removeSearchElement();
	})
}

function removeSearchElement() {
	$('#infoMain').addClass('ACTIVE').removeClass('HIDDEN');
	$('#infoSearch').addClass('HIDDEN').removeClass('ACTIVE');
	
}

function addMarker(position, idx, title) {
	var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
		imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
		imgOptions = {
			spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
			spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
			offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
		},
		markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
		marker = new kakao.maps.Marker({
			position: position, // 마커의 위치
			image: markerImage
		});

	marker.setMap(map); // 지도 위에 마커를 표출합니다
	markers.push(marker);  // 배열에 생성된 마커를 추가합니다

	return marker;
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

	var el = document.createElement('li'),
		itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
			'<div class="info">' +
			'   <h5>' + places.place_name + '</h5>';

	if (places.road_address_name) {
		itemStr += '    <span>' + places.road_address_name + '</span>' +
			'   <span class="jibun gray">' + places.address_name + '</span>';
	} else {
		itemStr += '    <span>' + places.address_name + '</span>';
	}

	itemStr += '  <span class="tel">' + places.phone + '</span>' +
		'</div>';

	el.innerHTML = itemStr;
	el.className = 'item';

	return el;
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {

	var paginationEl = document.getElementById('pagination'),
		fragment = document.createDocumentFragment(),
		i;
	
	// 상단으로 이동 버튼
	let paginationToTop = $('#paginationToTop');
	paginationToTop.addClass('ACTIVE').removeClass('HIDDEN');

	// 기존에 추가된 페이지번호를 삭제합니다
	while (paginationEl.hasChildNodes()) {
		paginationEl.removeChild(paginationEl.lastChild);
	}

	for (i = 1; i <= pagination.last; i++) {
		var el = document.createElement('a');
		el.href = "#";
		el.innerHTML = i;

		if (i === pagination.current) {
			el.className = 'on';
		} else {
			el.onclick = (function (i) {
				return function () {
					pagination.gotoPage(i);
				}
			})(i);
		}

		fragment.appendChild(el);
	}

	// 상단 이동!
	paginationToTop.on('click', function() {
		$('.offcanvas-body__info-body').scrollTop(0);
	})

	paginationEl.appendChild(fragment);

}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {

	var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

	infoWindow.setContent(content);
	infoWindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
	while (el.hasChildNodes()) {
		el.removeChild(el.lastChild);
	}
}


















// 마커 전용 아이콘
var markerIcon = new kakao.maps.MarkerImage(
	'https://i1.daumcdn.net/dmaps/apis/n_local_blit_04.png',
	new kakao.maps.Size(31, 35),
	{
		shape: 'poly',
		coords: '16,0,20,2,24,6,26,10,26,16,23,22,17,25,14,35,13,35,9,25,6,24,2,20,0,16,0,10,2,6,6,2,10,0'
	});

// 사용자 위치를 가져와 지도와 로드뷰 중심 설정하기
function initMap() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function (position) {
			let latitude = position.coords.latitude;
			let longitude = position.coords.longitude;

			let userLocation = new kakao.maps.LatLng(latitude, longitude);

			// 지도 중심 설정
			map.setCenter(userLocation);
			// 지도 줌/아웃 상태 설정
			map.setLevel(4);

			// 로드뷰의 위치를 사용자의 위치로 설정
			rvClient.getNearestPanoId(userLocation, 50, function (panoId) {
				if (panoId !== null) {
					rv.setPanoId(panoId, userLocation);
				}
			});

			// 마커 위치 설정
			marker.setPosition(userLocation);
			marker.setMap(map);

			// 위치 정보 표시
			displayLocationInfo(userLocation);

		}, function (error) {
			console.error("사용자 위치를 가져올 수 없습니다:", error);
			map.setCenter(mapCenter);
			rvClient.getNearestPanoId(mapCenter, 50, function (panoId) {
				if (panoId !== null) {
					rv.setPanoId(panoId, mapCenter);
				}
			});
			marker.setPosition(mapCenter);
		});
	} else {
		console.error("이 브라우저에서는 위치 정보를 사용할 수 없습니다.");
		map.setCenter(mapCenter);
		rvClient.getNearestPanoId(mapCenter, 50, function (panoId) {
			if (panoId !== null) {
				rv.setPanoId(panoId, mapCenter);
			}
		});
		marker.setPosition(mapCenter);
	}
}

function zoomIn() {
	// 현재 지도의 레벨을 얻어옵니다
	var level = map.getLevel();

	// 지도를 1레벨 내립니다 (지도가 확대됩니다)
	map.setLevel(level - 1);
}

function zoomOut() {
	// 현재 지도의 레벨을 얻어옵니다
	var level = map.getLevel();

	// 지도를 1레벨 올립니다 (지도가 축소됩니다)
	map.setLevel(level + 1);
}

//지도에 클릭 이벤트를 등록합니다
kakao.maps.event.addListener(map, 'click', function (mouseEvent) {

	let position = mouseEvent.latLng;
	// marker.setPosition(position);
	// displayLocationInfo(position);

	// 로드뷰가 활성화되어 있을 때만 로드뷰 위치를 업데이트합니다
	if (overlayOn) {
		toggleRoadview(position);
	}
});






// 위치 정보 표시 함수
function displayLocationInfo(position) {
	geocoder.coord2Address(position.getLng(), position.getLat(), function (result, status) {
		if (status === kakao.maps.services.Status.OK) {
			let address = result[0].address.address_name;

			// 정보창에 주소 표시
			infoWindow.setContent('<div style="width: 170px;"">' + address + '</div>');
			infoWindow.open(map, marker);
		}
	});
}


// 로드뷰에 좌표가 바뀌었을 때 발생하는 이벤트를 등록합니다 
kakao.maps.event.addListener(rv, 'position_changed', function () {

	// 현재 로드뷰의 위치 좌표를 얻어옵니다 
	let rvPosition = rv.getPosition();

	// 지도의 중심을 현재 로드뷰의 위치로 설정합니다
	map.setCenter(rvPosition);

	// 지도 위에 로드뷰 도로 오버레이가 추가된 상태이면
	if (overlayOn) {
		// 마커의 위치를 현재 로드뷰의 위치로 설정합니다
		marker.setPosition(rvPosition);
	}
});



// 마커에 dragend 이벤트를 등록합니다
kakao.maps.event.addListener(marker, 'dragend', function (mouseEvent) {

	// 현재 마커가 놓인 자리의 좌표입니다 
	let position = marker.getPosition();

	// 마커가 놓인 위치의 정보를 업데이트합니다
	displayLocationInfo(position);

	// 로드뷰가 활성화되어 있으면 로드뷰도 업데이트합니다
	if (overlayOn) {
		toggleRoadview(position);
	}
});


// 전달받은 좌표(position)에 가까운 로드뷰의 파노라마 ID를 추출하여
// 로드뷰를 설정하는 함수입니다
function toggleRoadview(position) {
	rvClient.getNearestPanoId(position, 50, function (panoId) {
		// 파노라마 ID가 null 이면 로드뷰를 숨깁니다
		if (panoId === null) {
			toggleMapWrapper(true, position);
		} else {
			toggleMapWrapper(false, position);

			// panoId로 로드뷰를 설정합니다
			rv.setPanoId(panoId, position);
		}
	});
}

// 지도를 감싸고 있는 div의 크기를 조정하는 함수입니다
function toggleMapWrapper(active, position) {
	if (active) {

		// 지도를 감싸고 있는 div의 너비가 100%가 되도록 class를 변경합니다 
		container.className = '';

		// 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
		map.relayout();

		// 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
		map.setCenter(position);
	} else {

		// 지도만 보여지고 있는 상태이면 지도의 너비가 50%가 되도록 class를 변경하여
		// 로드뷰가 함께 표시되게 합니다
		if (container.className.indexOf('view_roadview') === -1) {
			container.className = 'view_roadview';

			// 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
			map.relayout();

			// 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
			map.setCenter(position);
		}
	}
}

// 지도 위의 로드뷰 도로 오버레이를 추가,제거하는 함수입니다
function toggleOverlay(active) {
	if (active) {
		overlayOn = true;

		// 지도 위에 로드뷰 도로 오버레이를 추가합니다
		map.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

		// 마커의 이미지를 변경
		marker.setImage(markImage);


		// 마커의 위치를 지도 중심으로 설정합니다 
		marker.setPosition(map.getCenter());

		// 로드뷰의 위치를 지도 중심으로 설정합니다
		toggleRoadview(map.getCenter());
	} else {
		overlayOn = false;

		// 지도 위의 로드뷰 도로 오버레이를 제거합니다
		map.removeOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

		// 지도 위의 마커를 제거합니다
		// marker.setMap(null);
		marker.setImage(markerIcon);
	}

	// 지도 위에 마커를 표시합니다
	marker.setMap(map);
}

// 지도 위의 로드뷰 버튼을 눌렀을 때 호출되는 함수입니다
function setRoadviewRoad() {
	let control = document.getElementById('roadviewControl');

	// 버튼이 눌린 상태가 아니면
	if (control.className.indexOf('active') === -1) {
		control.className = 'active';

		// 로드뷰 도로 오버레이가 보이게 합니다
		toggleOverlay(true);
	} else {
		control.className = '';

		// 로드뷰 도로 오버레이를 제거합니다
		toggleOverlay(false);
	}
}

// 로드뷰에서 X버튼을 눌렀을 때 로드뷰를 지도 뒤로 숨기는 함수입니다
function closeRoadview() {
	let position = marker.getPosition();
	toggleMapWrapper(true, position);
}

var placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 }),
	contentNode = document.createElement('div'), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다 
	markers = [], // 마커를 담을 배열입니다
	currCategory = ''; // 현재 선택된 카테고리를 가지고 있을 변수입니다
// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places(map);


// 지도에 idle 이벤트를 등록합니다
kakao.maps.event.addListener(map, 'idle', searchPlaces);

// 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다 
contentNode.className = 'placeinfo_wrap';

// 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
// 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다 
addEventHandle(contentNode, 'mousedown', kakao.maps.event.preventMap);
addEventHandle(contentNode, 'touchstart', kakao.maps.event.preventMap);

// 커스텀 오버레이 컨텐츠를 설정합니다
placeOverlay.setContent(contentNode);

// 각 카테고리에 클릭 이벤트를 등록합니다
addCategoryClickEvent();

// 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
function addEventHandle(target, type, callback) {
	if (target.addEventListener) {
		target.addEventListener(type, callback);
	} else {
		target.attachEvent('on' + type, callback);
	}
}

// 카테고리 검색을 요청하는 함수입니다
function searchPlaces() {
	if (!currCategory) {
		return;
	}

	// 커스텀 오버레이를 숨깁니다 
	placeOverlay.setMap(null);

	// 지도에 표시되고 있는 마커를 제거합니다
	removeMarker();

	ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
	if (status === kakao.maps.services.Status.OK) {

		// 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
		displayPlaces(data);
	} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
		// 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요

	} else if (status === kakao.maps.services.Status.ERROR) {
		// 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요

	}
}

// 지도에 마커를 표출하는 함수입니다
function displayPlaces(places) {

	// 몇번째 카테고리가 선택되어 있는지 얻어옵니다
	// 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
	var order = document.getElementById(currCategory).getAttribute('data-order');



	for (var i = 0; i < places.length; i++) {

		// 마커를 생성하고 지도에 표시합니다
		var marker = addCategoryMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order);

		// 마커와 검색결과 항목을 클릭 했을 때
		// 장소정보를 표출하도록 클릭 이벤트를 등록합니다
		(function (marker, place) {
			kakao.maps.event.addListener(marker, 'click', function () {
				displayPlaceInfo(place);
			});
		})(marker, places[i]);
	}
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addCategoryMarker(position, order) {
	var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
		imageSize = new kakao.maps.Size(27, 28),  // 마커 이미지의 크기
		imgOptions = {
			spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
			spriteOrigin: new kakao.maps.Point(46, (order * 36)), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
			offset: new kakao.maps.Point(11, 28) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
		},
		markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
		marker = new kakao.maps.Marker({
			position: position, // 마커의 위치
			image: markerImage
		});

	marker.setMap(map); // 지도 위에 마커를 표출합니다
	markers.push(marker);  // 배열에 생성된 마커를 추가합니다

	return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(null);
	}
	markers = [];
}

// 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
function displayPlaceInfo(place) {
	var content = '<div class="placeinfo">' +
		'   <a class="title" href="' + place.place_url + '" target="_blank" title="' + place.place_name + '">' + place.place_name + '</a>';

	if (place.road_address_name) {
		content += '    <span title="' + place.road_address_name + '">' + place.road_address_name + '</span>' +
			'  <span class="jibun" title="' + place.address_name + '">(지번 : ' + place.address_name + ')</span>';
	} else {
		content += '    <span title="' + place.address_name + '">' + place.address_name + '</span>';
	}

	content += '    <span class="tel">' + place.phone + '</span>' +
		'</div>' +
		'<div class="after"></div>';

	contentNode.innerHTML = content;
	placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
	placeOverlay.setMap(map);
}


// 각 카테고리에 클릭 이벤트를 등록합니다
function addCategoryClickEvent() {
	var category = document.getElementById('category'),
		children = category.children;

	for (var i = 0; i < children.length; i++) {
		children[i].onclick = onClickCategory;
	}
}

// 카테고리를 클릭했을 때 호출되는 함수입니다
function onClickCategory() {
	var id = this.id,
		className = this.className;

	placeOverlay.setMap(null);

	if (className === 'on') {
		currCategory = '';
		changeCategoryClass();
		removeMarker();
	} else {
		currCategory = id;
		changeCategoryClass(this);
		searchPlaces();
	}
}

// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
function changeCategoryClass(el) {
	var category = document.getElementById('category'),
		children = category.children,
		i;

	for (i = 0; i < children.length; i++) {
		children[i].className = '';
	}

	if (el) {
		el.className = 'on';
	}
} 